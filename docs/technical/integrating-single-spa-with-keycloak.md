# Integrating Single-Spa with Keycloak using single-spa-layout (TypeScript Version)

## 1. Set Up Keycloak Client

First, you need to configure a client in Keycloak:

1. Log in to the Keycloak admin console
2. Select your realm
3. Go to "Clients" > "Create"
4. Set up a new client with:
   - Client ID: `single-spa-app`
   - Client Protocol: `openid-connect`
   - Root URL: Your single-spa root application URL (e.g., `http://localhost:9000`)
5. In the client settings:
   - Set Access Type to `public`
   - Enable "Standard Flow"
   - Add valid redirect URIs (your single-spa app URL, e.g., `http://localhost:9000/*`)
   - Add allowed web origins for CORS (e.g., `http://localhost:9000`)

## 2. Install Required Packages

In your single-spa root application:

```bash
npm install --save keycloak-js single-spa-layout
npm install --save-dev @types/keycloak-js
```

## 3. Create a Keycloak Service

Create a service to handle Keycloak authentication:

```typescript
// src/services/keycloak-service.ts
import Keycloak from 'keycloak-js';
import { BehaviorSubject } from 'rxjs';

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
      url: 'http://localhost:8080/auth', // Your Keycloak server URL
      realm: 'your-realm',
      clientId: 'single-spa-app'
    };

    this.keycloak = new Keycloak(keycloakConfig);

    return this.keycloak.init({
      onLoad: 'check-sso',
      silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html',
      pkceMethod: 'S256' // For PKCE
    })
      .then((authenticated: boolean) => {
        this.authenticated = authenticated;
        
        if (authenticated && this.keycloak) {
          // Set up token refresh
          this.setupTokenRefresh();
          
          // Store the token in sessionStorage or a more secure storage
          sessionStorage.setItem('token', this.keycloak.token as string);
          sessionStorage.setItem('refreshToken', this.keycloak.refreshToken as string);
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
            sessionStorage.setItem('token', this.keycloak.token as string);
            sessionStorage.setItem('refreshToken', this.keycloak.refreshToken as string);
            this.tokenSubject.next(this.keycloak.token);
          }
        })
        .catch(() => {
          console.error('Failed to refresh token');
          this.logout();
        });
    };
  }

  login(): Promise<void> {
    return this.keycloak?.login() || Promise.reject('Keycloak not initialized');
  }

  logout(): Promise<void> {
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('refreshToken');
    return this.keycloak?.logout() || Promise.reject('Keycloak not initialized');
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
```

## 4. Create a Silent Check SSO HTML File

Create a file called `silent-check-sso.html` in your public folder:

```html
<!DOCTYPE html>
<html>
<head>
  <script>
    parent.postMessage(location.href, location.origin);
  </script>
</head>
<body>
</body>
</html>
```

## 5. Create Layout HTML File

Create a file called `microfrontend-layout.html` in your root config:

```html
<!-- src/microfrontend-layout.html -->
<single-spa-router>
  <!-- Common content that will be displayed on all routes -->
  <nav class="topnav">
    <application name="@org/navbar"></application>
  </nav>

  <!-- Different routes for different applications -->
  <main>
    <route path="public">
      <application name="@org/public-app"></application>
    </route>

    <route path="protected">
      <!-- This will be handled by auth guard -->
      <application name="@org/protected-app"></application>
    </route>

    <route path="admin">
      <!-- This will be handled by role-based auth guard -->
      <application name="@org/admin-app"></application>
    </route>

    <route path="unauthorized">
      <application name="@org/unauthorized"></application>
    </route>

    <route default>
      <application name="@org/home"></application>
    </route>
  </main>
</single-spa-router>
```

## 6. Create Auth Guards for Protected Routes

Create an authentication guard:

```typescript
// src/services/auth-guard.ts
import keycloakService from './keycloak-service';

export function requireAuth(location: Location): boolean {
  if (!keycloakService.isAuthenticated()) {
    keycloakService.login();
    return false;
  }
  return true;
}

export function requireRole(role: string): (location: Location) => boolean {
  return (location: Location) => {
    if (!keycloakService.isAuthenticated()) {
      keycloakService.login();
      return false;
    }
    
    if (!keycloakService.hasRole(role)) {
      // Redirect to unauthorized page or show error
      window.location.href = '/unauthorized';
      return false;
    }
    
    return true;
  };
}
```

## 7. Initialize Keycloak in Root Application with single-spa-layout

```typescript
// src/root-config.ts
import { registerApplication, start } from 'single-spa';
import { constructApplications, constructRoutes, constructLayoutEngine } from 'single-spa-layout';
import keycloakService from './services/keycloak-service';
import { requireAuth, requireRole } from './services/auth-guard';

// Import the layout definition
import microfrontendLayout from './microfrontend-layout.html';

// Initialize Keycloak before setting up the layout
keycloakService.init().then(() => {
  // Parse the layout
  const routes = constructRoutes(microfrontendLayout);

  // Define the applications with auth checks
  const applications = constructApplications({
    routes,
    loadApp({ name }) {
      // Load the appropriate application based on name
      switch (name) {
        case '@org/navbar':
          return import('./navbar/navbar');
        case '@org/public-app':
          return import('./public-app/public-app');
        case '@org/protected-app':
          // Check authentication before loading the app
          if (requireAuth(window.location)) {
            return import('./protected-app/protected-app');
          } else {
            return Promise.resolve(null);
          }
        case '@org/admin-app':
          // Check admin role before loading the app
          if (requireRole('admin')(window.location)) {
            return import('./admin-app/admin-app');
          } else {
            return Promise.resolve(null);
          }
        case '@org/unauthorized':
          return import('./unauthorized/unauthorized');
        case '@org/home':
          return import('./home/home');
        default:
          return Promise.reject(`Unknown application: ${name}`);
      }
    },
    customProps: (name) => {
      // Provide the auth token to each application
      return {
        authToken: keycloakService.getToken()
      };
    },
  });

  // Create the layout engine
  const layoutEngine = constructLayoutEngine({
    routes,
    applications,
  });

  // Register each application with single-spa
  applications.forEach(registerApplication);

  // Start the single-spa framework
  start();
}).catch((error: Error) => {
  console.error('Failed to initialize Keycloak', error);
});
```

## 8. Create Activity Functions with Auth Guards

For more fine-grained control over when applications should be active:

```typescript
// src/activity-functions.ts
import { ActivityFn } from 'single-spa';
import keycloakService from './services/keycloak-service';

export const protectedActivityFn: ActivityFn = (location) => {
  // Only load protected app if user is authenticated
  if (location.pathname.startsWith('/protected')) {
    if (!keycloakService.isAuthenticated()) {
      keycloakService.login();
      return false;
    }
    return true;
  }
  return false;
};

export const adminActivityFn: ActivityFn = (location) => {
  // Only load admin app if user is authenticated and has admin role
  if (location.pathname.startsWith('/admin')) {
    if (!keycloakService.isAuthenticated()) {
      keycloakService.login();
      return false;
    }
    
    if (!keycloakService.hasRole('admin')) {
      window.location.href = '/unauthorized';
      return false;
    }
    
    return true;
  }
  return false;
};
```

Then use these activity functions in your root config:

```typescript
// Alternative approach using activity functions in root-config.ts
import { protectedActivityFn, adminActivityFn } from './activity-functions';

// ...

const applications = constructApplications({
  routes,
  loadApp({ name }) {
    // Load the appropriate application based on name
    switch (name) {
      case '@org/protected-app':
        return import('./protected-app/protected-app');
      case '@org/admin-app':
        return import('./admin-app/admin-app');
      // other apps...
    }
  },
  activeWhen: {
    '@org/protected-app': protectedActivityFn,
    '@org/admin-app': adminActivityFn,
    // Default behavior for other apps
  },
  customProps: (name) => {
    return {
      authToken: keycloakService.getToken()
    };
  },
});
```

## 9. Share Authentication State with Micro-Frontends

For each micro-frontend to access the authentication token:

```typescript
// In each micro-frontend's entry file
import axios, { AxiosRequestConfig } from 'axios';

interface MountProps {
  authToken?: string;
  [key: string]: any;
}

export function mount(props: MountProps): void {
  const { authToken } = props;
  
  // Set up API calls with the auth token
  setupAxiosInterceptors(authToken);
  
  // Rest of your mount logic...
}

function setupAxiosInterceptors(token?: string): void {
  axios.interceptors.request.use((config: AxiosRequestConfig) => {
    if (token) {
      config.headers = config.headers || {};
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  });
}
```

## 10. Create a Navbar Component with Login/Logout Functionality

```typescript
// src/navbar/navbar.component.tsx
import React from 'react';
import keycloakService from '../services/keycloak-service';

const Navbar: React.FC = () => {
  const [isAuthenticated, setIsAuthenticated] = React.useState<boolean>(keycloakService.isAuthenticated());
  const [username, setUsername] = React.useState<string | undefined>(keycloakService.getUsername());

  const handleLogin = (): void => {
    keycloakService.login();
  };

  const handleLogout = (): void => {
    keycloakService.logout();
  };

  // Listen for token changes to update the UI
  React.useEffect(() => {
    const subscription = keycloakService.onTokenChanged((token) => {
      setIsAuthenticated(!!token);
      setUsername(keycloakService.getUsername());
    });

    return () => {
      subscription.unsubscribe();
    };
  }, []);

  return (
    <nav className="navbar">
      <div className="navbar-brand">
        <a href="/">Single-Spa Bookstore</a>
      </div>
      <div className="navbar-menu">
        <a href="/public">Public</a>
        <a href="/protected">Protected</a>
        {keycloakService.hasRole('admin') && <a href="/admin">Admin</a>}
      </div>
      <div className="navbar-end">
        {isAuthenticated ? (
          <>
            <span>Welcome, {username}</span>
            <button onClick={handleLogout}>Logout</button>
          </>
        ) : (
          <button onClick={handleLogin}>Login</button>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
```

## 11. Handle Token Refresh for Long-Running Sessions

The token refresh is already handled in the KeycloakService with the `onTokenChanged` method. Here's how to use it in a micro-frontend:

```typescript
// In a micro-frontend component
import React, { useEffect, useState } from 'react';
import keycloakService from './services/keycloak-service';

interface Props {
  authToken?: string;
}

const ProtectedComponent: React.FC<Props> = ({ authToken: initialAuthToken }) => {
  const [authToken, setAuthToken] = useState<string | null>(initialAuthToken || null);
  
  useEffect(() => {
    // Subscribe to token changes
    const subscription = keycloakService.onTokenChanged((newToken) => {
      setAuthToken(newToken);
      // Update your API client with the new token
    });
    
    // Cleanup subscription
    return () => {
      subscription.unsubscribe();
    };
  }, []);
  
  // Rest of component...
};
```

## 12. Loading States and Error Handling

Enhance your layout to handle loading states and errors:

```html
<!-- Updated microfrontend-layout.html -->
<single-spa-router>
  <nav class="topnav">
    <application name="@org/navbar"></application>
  </nav>

  <main>
    <route path="public">
      <application name="@org/public-app">
        <template name="loading">Loading public content...</template>
        <template name="error">Error loading public content</template>
      </application>
    </route>

    <route path="protected">
      <application name="@org/protected-app">
        <template name="loading">Loading protected content...</template>
        <template name="error">Error loading protected content</template>
      </application>
    </route>

    <route path="admin">
      <application name="@org/admin-app">
        <template name="loading">Loading admin content...</template>
        <template name="error">Error loading admin content</template>
      </application>
    </route>

    <route path="unauthorized">
      <h2>You are not authorized to view this page</h2>
      <a href="/">Return to home</a>
    </route>

    <route default>
      <application name="@org/home"></application>
    </route>
  </main>
</single-spa-router>
```
