import Keycloak from "keycloak-js";
import {BehaviorSubject} from "rxjs";

interface KeycloakConfig {
    url: string;
    realm: string;
    clientId: string;
}

class KeycloakService {
    private keycloak: Keycloak.KeycloakInstance | null = null;
    private authenticated: boolean = false;
    private tokenSubject: BehaviorSubject<string | null> = new BehaviorSubject<string | null>(null);

    init(): Promise<boolean> {
        const keycloakConfig: KeycloakConfig = {
            url: "https://identity.aronim.local",
            realm: "bookstore",
            clientId: "aronim-bookstore-frontend-application"
        };

        this.keycloak = new Keycloak(keycloakConfig);

        return this.keycloak.init({
            onLoad: "check-sso",
            silentCheckSsoRedirectUri: window.location.origin + "/silent-check-sso.html",
            pkceMethod: "S256" // For PKCE
        })
            .then((authenticated: boolean) => {
                this.authenticated = authenticated;

                if (authenticated && this.keycloak) {
                    // Set up token refresh
                    this.setupTokenRefresh();

                    // Store the token in sessionStorage or a more secure storage
                    sessionStorage.setItem("token", this.keycloak.token as string);
                    sessionStorage.setItem("refreshToken", this.keycloak.refreshToken as string);
                    this.tokenSubject.next(this.keycloak.token);
                }

                return authenticated;
            });
    }

    setupTokenRefresh(): void {
        if (!this.keycloak) return;

        // Refresh token 30 seconds before it expires
        this.keycloak.onTokenExpired = () => {
            if (!this.keycloak) return;

            this.keycloak.updateToken(30)
                .then((refreshed: boolean) => {
                    if (refreshed && this.keycloak) {
                        sessionStorage.setItem("token", this.keycloak.token as string);
                        sessionStorage.setItem("refreshToken", this.keycloak.refreshToken as string);
                        this.tokenSubject.next(this.keycloak.token);
                    }
                })
                .catch(() => {
                    console.error("Failed to refresh token");
                    this.logout();
                });
        };
    }

    login(): Promise<void> {
        return this.keycloak?.login() || Promise.reject("Keycloak not initialized");
    }

    logout(): Promise<void> {
        sessionStorage.removeItem("token");
        sessionStorage.removeItem("refreshToken");
        return this.keycloak?.logout() || Promise.reject("Keycloak not initialized");
    }

    getToken(): string | undefined {
        return this.keycloak?.token;
    }

    isAuthenticated(): boolean {
        return this.authenticated;
    }

    getUsername(): string | undefined {
        return this.keycloak?.tokenParsed?.preferred_username;
    }

    hasRole(role: string): boolean {
        return this.keycloak?.hasRealmRole(role) || false;
    }

    // Subscribe to token changes
    onTokenChanged(callback: (token: string | null) => void): { unsubscribe: () => void } {
        return this.tokenSubject.subscribe(callback);
    }
}

const keycloakService = new KeycloakService();
export default keycloakService;
