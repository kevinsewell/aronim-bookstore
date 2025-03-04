# ADR-033: Adoption of SystemJS for Dynamic Module Loading in Frontend

## Status
Proposed

## Date
2025-03-03

## Context
The Aronim Bookstore application is implementing a micro-frontend architecture (ADR-025) with React as the default frontend framework (ADR-027). As we build our micro-frontend architecture, we need a robust module loading system that can:

- Dynamically load modules at runtime
- Support multiple module formats (ESM, CommonJS, UMD, etc.)
- Enable effective module federation across micro-frontends
- Provide fallback mechanisms for different environments
- Support lazy loading and code-splitting
- Work effectively in both development and production environments
- Handle versioning and module dependencies efficiently
- Ensure compatibility with our existing frontend architecture

In the JavaScript ecosystem, there are several module formats in use:
1. CommonJS (CJS) - The traditional Node.js module format using `require()` and `module.exports`
2. ECMAScript Modules (ESM) - The standard JavaScript module format using `import` and `export` statements
3. Universal Module Definition (UMD) - A pattern that works in multiple environments
4. AMD (Asynchronous Module Definition) - Primarily used with older tools like RequireJS

For our micro-frontend architecture, we need a universal module loader that can:
1. Handle different module formats consistently
2. Provide polyfills for older browsers when needed
3. Support dynamic loading patterns required by our micro-frontend architecture
4. Handle complex module resolution scenarios
5. Provide consistent loading behavior across different environments

SystemJS is a universal dynamic module loader that can load different module formats and provide compatibility across browsers and environments.

## Decision
We will adopt **SystemJS** as the dynamic module loader for the Aronim Bookstore frontend architecture with the following implementation approach:

### 1. Core Implementation Strategy
- Use SystemJS as the primary dynamic module loader for micro-frontends
- Configure SystemJS to support multiple module formats (ESM, CommonJS, UMD)
- Set up appropriate fallbacks for different environments and browsers
- Implement SystemJS import maps for module resolution
- Integrate SystemJS with our module federation approach
- Configure build tools to generate SystemJS-compatible outputs

### 2. Build and Development Tooling
- Configure webpack/Rollup to output SystemJS-compatible formats
- Set up SystemJS import maps for development and production
- Create tooling to generate and update import maps automatically
- Configure module federation to work with SystemJS
- Update testing tools to support SystemJS module loading
- Implement proper source map generation for SystemJS modules

### 3. Module Loading Strategy
- Implement progressive loading strategies using SystemJS
- Configure preloading for critical modules
- Set up lazy loading for non-critical components
- Implement versioning strategy for modules
- Create caching policies for loaded modules
- Define a consistent approach to module paths and resolution

### 4. Micro-Frontend Integration
- Use SystemJS as the bridge between micro-frontend applications
- Configure shared dependencies through SystemJS
- Implement versioning strategy for shared modules
- Create consistent loading patterns across micro-frontends
- Set up error handling and fallback mechanisms
- Configure runtime dependency resolution

### 5. Browser Compatibility
- Set up appropriate polyfills for older browsers
- Implement feature detection for module support
- Configure differential loading based on browser capabilities
- Ensure proper MIME types and caching headers
- Test across target browser matrix

## Implementation Approach

1. **SystemJS Configuration Setup**
   - Create base SystemJS configuration
   - Configure module resolution strategies
   - Set up import maps
   - Configure fallback mechanisms

   ```html
   <!-- Base SystemJS setup in index.html -->
   <script src="https://cdn.jsdelivr.net/npm/systemjs@6.12.1/dist/system.min.js"></script>
   <script type="systemjs-importmap">
   {
     "imports": {
       "react": "https://cdn.jsdelivr.net/npm/react@17/umd/react.production.min.js",
       "react-dom": "https://cdn.jsdelivr.net/npm/react-dom@17/umd/react-dom.production.min.js",
       "@aronim/design-system": "/shared/design-system.js",
       "@aronim/catalog": "/catalog/catalog.js",
       "@aronim/checkout": "/checkout/checkout.js"
     }
   }
   </script>
   ```

2. **Module Federation with SystemJS**
   - Configure webpack for SystemJS-compatible output
   - Set up module federation to work with SystemJS
   - Implement shared dependency management

   ```js
   // webpack.config.js for SystemJS compatibility
   module.exports = {
     output: {
       libraryTarget: 'system',
       publicPath: 'auto'
     },
     plugins: [
       new ModuleFederationPlugin({
         name: 'catalog',
         filename: 'remoteEntry.js',
         exposes: {
           './CatalogApp': './src/CatalogMicroFrontend',
         },
         shared: {
           react: { 
             singleton: true, 
             requiredVersion: deps.react
           },
           'react-dom': { 
             singleton: true, 
             requiredVersion: deps['react-dom']
           }
         }
       }),
     ]
   };
   ```

3. **Dynamic Module Loading Implementation**
   - Create utility functions for dynamic loading
   - Set up error handling and retries
   - Implement loading states and feedback

   ```js
   // dynamicImport.js - Utility for loading modules
   export const loadModule = async (moduleName) => {
     try {
       return await System.import(moduleName);
     } catch (error) {
       console.error(`Failed to load module: ${moduleName}`, error);
       // Implement retry logic or fallback strategy
       return await loadFallbackModule(moduleName);
     }
   };

   // Usage example
   const CatalogApp = await loadModule('@aronim/catalog');
   const catalogElement = document.getElementById('catalog-container');
   CatalogApp.render(catalogElement);
   ```

4. **Micro-Frontend Shell Implementation**
   - Configure the application shell to use SystemJS
   - Set up dynamic loading of micro-frontends
   - Implement routing and navigation between micro-frontends

   ```js
   // app-shell.js
   import { registerApplication, start } from 'single-spa';
   import { loadModule } from './dynamicImport';

   // Register micro-frontend applications
   registerApplication({
     name: 'catalog',
     app: () => loadModule('@aronim/catalog'),
     activeWhen: ['/catalog']
   });

   registerApplication({
     name: 'checkout',
     app: () => loadModule('@aronim/checkout'),
     activeWhen: ['/checkout']
   });

   // Start the application
   start();
   ```

5. **Development Environment Configuration**
   - Set up local development server with SystemJS support
   - Configure hot module replacement
   - Create development-specific import maps

   ```js
   // dev-server.js
   const fs = require('fs');
   const path = require('path');

   // Generate development import map
   const generateImportMap = () => {
     const importMap = {
       imports: {
         'react': 'http://localhost:3000/node_modules/react/umd/react.development.js',
         'react-dom': 'http://localhost:3000/node_modules/react-dom/umd/react-dom.development.js',
         '@aronim/design-system': 'http://localhost:3001/design-system.js',
         '@aronim/catalog': 'http://localhost:3002/catalog.js',
         '@aronim/checkout': 'http://localhost:3003/checkout.js'
       }
     };

     fs.writeFileSync(
       path.resolve(__dirname, 'import-map.dev.json'),
       JSON.stringify(importMap, null, 2)
     );
   };

   generateImportMap();
   ```

## Consequences

### Positive
- Enables dynamic loading of modules at runtime
- Provides compatibility layer for different module formats (CommonJS, UMD, ESM)
- Supports older browsers through polyfills and fallbacks
- Facilitates micro-frontend architecture with runtime integration
- Allows for progressive and lazy loading of modules
- Simplifies module resolution across different environments
- Provides consistent module loading behavior
- Enables more flexible dependency management
- Supports both development and production optimization
- Facilitates better code-splitting and performance optimization

### Negative
- Adds additional complexity to the build and runtime configuration
- Increases initial bundle size due to SystemJS loader
- Requires careful configuration to avoid performance issues
- May introduce potential points of failure in module resolution
