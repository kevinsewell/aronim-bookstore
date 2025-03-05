/**
 * Main entry point for the Aronim Application Shell.
 * This file sets up the micro-frontend architecture using single-spa framework.
 */

import {
  registerApplication,
  RegisterApplicationConfig,
  start,
} from "single-spa";
import {
  constructApplications,
  constructLayoutEngine,
  constructRoutes,
} from "single-spa-layout";
import keycloakService from "./services/keycloak-service";
import microfrontendLayout from "./microfrontend-layout.html";

/**
 * Application initialization flow:
 * 1. Initialize Keycloak authentication
 * 2. Parse the layout to define routes
 * 3. Construct applications based on routes
 * 4. Register each application with authentication checks
 * 5. Create and activate the layout engine
 * 6. Start the single-spa framework
 */

// Initialize Keycloak before setting up the layout
keycloakService
  .init()
  .then(() => {
    // Parse the layout from HTML template to create route definitions
    const routes = constructRoutes(microfrontendLayout);

    /**
     * Construct application configurations from routes.
     * The loadApp function dynamically imports each micro-frontend application
     * using the application name as the module path.
     */
    const applications = constructApplications({
      routes,
      loadApp({ name }) {
        return import(/* webpackIgnore: true */ name);
      },
    });

    // Register each application with single-spa
    applications.forEach((application: RegisterApplicationConfig) => {
      registerApplication({
        name: application.name,
        app: application.app,
        // Authentication guard - determines when an application should be active.
        // If user is not authenticated, redirect to login page and prevent activation.
        activeWhen: () => {
          if (!keycloakService.isAuthenticated()) {
            keycloakService.login();
            return false;
          }
          return true;
        },
        // Provides props to each micro-frontend application.
        // Currently, passes the authentication token for API calls.
        customProps: () => {
          // Provide the auth token to each application
          return {
            authToken: keycloakService.getToken(),
          };
        },
      });
    });

    // Create the layout engine to manage application mounting/unmounting
    const layoutEngine = constructLayoutEngine({
      routes,
      applications,
    });

    // Activate the layout engine to start handling route changes
    layoutEngine.activate();

    // Start the single-spa framework to begin mounting applications
    start();
  })
  .catch((error: Error) => {
    console.error("Failed to initialize Keycloak", error);
  });
