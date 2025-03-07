"use strict";

/**
 * Paths Module
 *
 * This module manages file paths for the React application.
 * It provides a consistent way to reference paths throughout the application
 * regardless of where the code is executed from.
 */

// Import required Node.js modules
const path = require("path");
const fs = require("fs");
const getPublicUrlOrPath = require("react-dev-utils/getPublicUrlOrPath");

// Make sure any symlinks in the project folder are resolved:
// https://github.com/facebook/create-react-app/issues/637
const appDirectory = fs.realpathSync(process.cwd());

/**
 * Helper function to resolve app paths relative to the project root
 * @param {string} relativePath - Path relative to the project root
 * @returns {string} Absolute path
 */
const resolveApp = (relativePath) => path.resolve(appDirectory, relativePath);

/**
 * Determines the public URL path for asset references
 * Uses environment variables or package.json homepage field
 *
 * We use `PUBLIC_URL` environment variable or "homepage" field to infer
 * "public path" at which the app is served.
 *
 * webpack needs to know it to put the right <script> hrefs into HTML even in
 * single-page apps that may serve index.html for nested URLs like /todos/42.
 * We can't use a relative path in HTML because we don't want to load something
 * like /todos/42/static/js/bundle.7289d.js. We have to know the root.
 */
const publicUrlOrPath = getPublicUrlOrPath(
  process.env.NODE_ENV === "development",
  require(resolveApp("package.json")).homepage,
  process.env.PUBLIC_URL,
);

/**
 * Build directory path - can be customized via BUILD_PATH env variable
 */
const buildPath = process.env.BUILD_PATH || "build";

/**
 * List of supported file extensions in order of preference
 * Used for module resolution
 */
const moduleFileExtensions = [
  "web.mjs",
  "mjs",
  "web.js",
  "js",
  "web.ts",
  "ts",
  "web.tsx",
  "tsx",
  "json",
  "web.jsx",
  "jsx",
];

/**
 * Resolves a module path by trying different file extensions
 * @param {Function} resolveFn - Function to resolve the path
 * @param {string} filePath - Base file path without extension
 * @returns {string} Resolved path with appropriate extension
 */
const resolveModule = (resolveFn, filePath) => {
  const extension = moduleFileExtensions.find((extension) =>
    fs.existsSync(resolveFn(`${filePath}.${extension}`)),
  );

  if (extension) {
    return resolveFn(`${filePath}.${extension}`);
  }

  // Default to .js if no matching extension is found
  return resolveFn(`${filePath}.js`);
};

/**
 * Exported path configuration
 * Contains all important paths used throughout the application
 */
module.exports = {
  dotenv: resolveApp(".env"), // Environment variables file
  appPath: resolveApp("."), // Root application directory
  appBuild: resolveApp(buildPath), // Build output directory
  appPublic: resolveApp("public"), // Public assets directory
  appHtml: resolveApp("public/index.html"), // Main HTML template
  appIndexJs: resolveModule(resolveApp, "src/index"), // Application entry point
  appPackageJson: resolveApp("package.json"), // Package configuration
  appSrc: resolveApp("src"), // Source code directory
  appTsConfig: resolveApp("tsconfig.json"), // TypeScript configuration
  appJsConfig: resolveApp("jsconfig.json"), // JavaScript configuration
  yarnLockFile: resolveApp("yarn.lock"), // Yarn lockfile
  testsSetup: resolveModule(resolveApp, "src/setupTests"), // Test setup file
  proxySetup: resolveApp("src/setupProxy.js"), // Proxy configuration for development
  appNodeModules: resolveApp("node_modules"), // Node modules directory
  appWebpackCache: resolveApp("node_modules/.cache"), // Webpack cache directory
  appTsBuildInfoFile: resolveApp("node_modules/.cache/tsconfig.tsbuildinfo"), // TypeScript build info
  swSrc: resolveModule(resolveApp, "src/service-worker"), // Service worker source
  publicUrlOrPath, // Public URL for assets
};

/**
 * Export supported file extensions for use in other configurations
 */
module.exports.moduleFileExtensions = moduleFileExtensions;
