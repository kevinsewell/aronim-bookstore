import { AuthProvider } from "@refinedev/core";
import { AxiosInstance } from "axios";
import { useKeycloak } from "@react-keycloak/web";
import {
  AuthActionResponse,
  OnErrorResponse,
} from "@refinedev/core/src/contexts/auth/types";
import {
  CheckResponse,
  IdentityResponse,
  PermissionResponse,
} from "@refinedev/core/dist/contexts/auth/types";

/**
 * Custom hook that provides authentication functionality using Keycloak
 * @param axiosInstance - The axios instance to be configured with auth headers
 * @returns Authentication provider and initialization status
 */
export function useAuthentication(axiosInstance: AxiosInstance) {
  // Get Keycloak instance and initialization status from the Keycloak hook
  const { keycloak, initialized } = useKeycloak();

  // Create an auth provider that implements the RefineJS AuthProvider interface
  const authProvider: AuthProvider = {
    /**
     * Handles user login through Keycloak
     * Extracts redirect URL from query parameters if available
     */
    login: async (): Promise<AuthActionResponse> => {
      const urlSearchParams = new URLSearchParams(window.location.search);
      const { to } = Object.fromEntries(urlSearchParams.entries());
      await keycloak.login({
        redirectUri: to ? `${window.location.origin}${to}` : undefined,
      });
      return {
        success: true,
      };
    },

    /**
     * Handles user logout through Keycloak
     * Redirects to the application's root URL after logout
     */
    logout: async (): Promise<AuthActionResponse> => {
      try {
        await keycloak.logout({
          redirectUri: window.location.origin,
        });
        return {
          success: true,
          redirectTo: "/login",
        };
      } catch (error) {
        return {
          success: false,
          error: new Error("Logout failed"),
        };
      }
    },

    /**
     * Handles errors that occur during authentication
     * @param error - The error that occurred
     */
    onError: async (error): Promise<OnErrorResponse> => {
      console.error(error);
      return { error };
    },

    /**
     * Checks if the user is authenticated
     * Sets the Authorization header if a token is available
     */
    check: async (): Promise<CheckResponse> => {
      try {
        const { token } = keycloak;
        if (token) {
          // Set the Authorization header for all axios requests
          axiosInstance.defaults.headers.common = {
            Authorization: `Bearer ${token}`,
          };
          return {
            authenticated: true,
          };
        } else {
          return {
            authenticated: false,
            logout: true,
            redirectTo: "/login",
            error: {
              message: "Check failed",
              name: "Token not found",
            },
          };
        }
      } catch (error) {
        return {
          authenticated: false,
          logout: true,
          redirectTo: "/login",
          error: {
            message: "Check failed",
            name: "Token not found",
          },
        };
      }
    },

    /**
     * Gets user permissions (not implemented)
     * @returns null since permissions are not used in this implementation
     */
    getPermissions: async (): Promise<PermissionResponse> => null,

    /**
     * Gets the identity of the authenticated user
     * @returns User identity object or null if not authenticated
     */
    getIdentity: async (): Promise<IdentityResponse> => {
      if (keycloak?.tokenParsed) {
        const {family_name: familyName, given_name: givenName} = keycloak.tokenParsed
        return {
          name: `${givenName} ${familyName}`,
        };
      }
      return null;
    },
  };

  // Return the auth provider and initialization status
  return { authProvider, initialized };
}
