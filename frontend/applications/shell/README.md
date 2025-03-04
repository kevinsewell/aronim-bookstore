#

## Development Dependencies

### Workspace/Local Dependencies

- `@aronim/eslint-config`: Custom ESLint configuration shared across the Aronim project workspaces

### Babel Related

- `@babel/core`: Core Babel compiler for JavaScript transpilation
- `@babel/eslint-parser`: Enables linting of Babel-processed code
- `@babel/plugin-transform-runtime`: Reduces code duplication by extracting Babel helpers
- `@babel/preset-env`: Smart preset for transpiling JavaScript to target environments
- `@babel/preset-typescript`: Preset for TypeScript support in Babel
- `@babel/runtime`: Runtime helpers for transformed code

### TypeScript Related

- `@types/jest`: TypeScript type definitions for Jest testing
- `@types/systemjs`: TypeScript definitions for SystemJS module loader
- `@types/webpack-env`: TypeScript definitions for webpack environment
- `typescript`: The TypeScript compiler
- `ts-config-single-spa`: TypeScript configuration for single-spa applications

### Testing

- `jest`: JavaScript testing framework
- `jest-cli`: Command line interface for Jest

### Code Quality

- `eslint`: JavaScript/TypeScript linter
- `eslint-config-prettier`: Disables ESLint rules that conflict with Prettier
- `eslint-config-ts-important-stuff`: ESLint configuration for TypeScript
- `eslint-plugin-prettier`: Runs Prettier as an ESLint rule
- `prettier`: Code formatter
- `pretty-quick`: Runs Prettier on changed files

### Build Tools

- `concurrently`: Runs multiple commands simultaneously (used in build script)
- `cross-env`: Sets environment variables across platforms
- `webpack`: Module bundler
- `webpack-cli`: Command line interface for webpack
- `webpack-config-single-spa-ts`: Webpack config for TypeScript single-spa apps
- `webpack-dev-server`: Development server for webpack
- `webpack-merge`: Merges webpack configurations
- `html-webpack-plugin`: Simplifies HTML file creation for webpack bundles

### Utilities

- `serve`: Static file serving utility

This configuration supports a TypeScript-based micro-frontend shell application using the single-spa framework, with a comprehensive setup for development, testing, and building.
