import {registerApplication, start} from "single-spa";
import {constructApplications, constructLayoutEngine, constructRoutes,} from "single-spa-layout";
import keycloakService from "./services/keycloak-service";
import microfrontendLayout from "./microfrontend-layout.html";

// Initialize Keycloak before setting up the layout
keycloakService
    .init()
    .then(() => {

        // Parse the layout
        const routes = constructRoutes(microfrontendLayout);

        const applications = constructApplications({
            routes,
            loadApp({name}) {
                return import(/* webpackIgnore: true */ name);
            },
        });

        // Register each application with single-spa
        applications
            .forEach((application) => {
                registerApplication({
                    name: application.name,
                    app: application.app,
                    activeWhen: (location: Location) => {
                        if (!keycloakService.isAuthenticated()) {
                            keycloakService.login();
                            return false;
                        }
                        return true;
                    },
                    customProps: (name) => {
                        // Provide the auth token to each application
                        return {
                            authToken: keycloakService.getToken()
                        };
                    }
                })
            });

        // Create the layout engine
        const layoutEngine = constructLayoutEngine({
            routes,
            applications,
        });

        layoutEngine.activate();

        // Start the single-spa framework
        start();
    }).catch((error: Error) => {
    console.error("Failed to initialize Keycloak", error);
});
