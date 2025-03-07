"use strict";

// Required Node.js modules
const fs = require("fs"); // File system module for reading files
const path = require("path"); // Path manipulation utility
const paths = require("./paths"); // Custom paths configuration for the application
const chalk = require("react-dev-utils/chalk"); // Colored console output utility from Create React App
const resolve = require("resolve"); // Module resolution utility

/**
 * Get additional module paths based on the baseUrl of a compilerOptions object.
 * This function determines where to look for modules based on TypeScript/JavaScript configuration.
 *
 * @param {Object} options - Compiler options from tsconfig.json or jsconfig.json
 * @returns {Array|null|string} - Additional module paths or null if default paths should be used
 */
function getAdditionalModulePaths(options = {}) {
  const baseUrl = options.baseUrl;

  if (!baseUrl) {
    return ""; // Return empty string if no baseUrl is specified
  }

  const baseUrlResolved = path.resolve(paths.appPath, baseUrl);

  // We don't need to do anything if `baseUrl` is set to `node_modules`. This is
  // the default behavior.
  if (path.relative(paths.appNodeModules, baseUrlResolved) === "") {
    return null;
  }

  // Allow the user set the `baseUrl` to `appSrc`.
  if (path.relative(paths.appSrc, baseUrlResolved) === "") {
    return [paths.appSrc]; // Return src directory as an additional module path
  }

  // If the path is equal to the root directory we ignore it here.
  // We don't want to allow importing from the root directly as source files are
  // not transpiled outside of `src`. We do allow importing them with the
  // absolute path (e.g. `src/Components/Button.js`) but we set that up with
  // an alias.
  if (path.relative(paths.appPath, baseUrlResolved) === "") {
    return null;
  }

  // Otherwise, throw an error.
  throw new Error(
    chalk.red.bold(
      "Your project's `baseUrl` can only be set to `src` or `node_modules`." +
        " Create React App does not support other values at this time.",
    ),
  );
}

/**
 * Get webpack aliases based on the baseUrl of a compilerOptions object.
 * Creates webpack aliases to simplify imports in the application.
 *
 * @param {Object} options - Compiler options from tsconfig.json or jsconfig.json
 * @returns {Object} - Webpack alias configuration
 */
function getWebpackAliases(options = {}) {
  const baseUrl = options.baseUrl;

  if (!baseUrl) {
    return {}; // Return empty object if no baseUrl is specified
  }

  const baseUrlResolved = path.resolve(paths.appPath, baseUrl);

  // If baseUrl is the root directory, create an alias for 'src'
  if (path.relative(paths.appPath, baseUrlResolved) === "") {
    return {
      src: paths.appSrc, // Create an alias 'src' pointing to the src directory
    };
  }
}

/**
 * Get jest aliases based on the baseUrl of a compilerOptions object.
 * Creates Jest module name mapper aliases for testing.
 *
 * @param {Object} options - Compiler options from tsconfig.json or jsconfig.json
 * @returns {Object} - Jest module name mapper configuration
 */
function getJestAliases(options = {}) {
  const baseUrl = options.baseUrl;

  if (!baseUrl) {
    return {}; // Return empty object if no baseUrl is specified
  }

  const baseUrlResolved = path.resolve(paths.appPath, baseUrl);

  // If baseUrl is the root directory, create a regex-based alias for src paths
  if (path.relative(paths.appPath, baseUrlResolved) === "") {
    return {
      "^src/(.*)$": "<rootDir>/src/$1", // Maps imports starting with 'src/' to the actual src directory
    };
  }
}

/**
 * Main function that collects all module configurations.
 * Determines project type (TypeScript or JavaScript) and sets up appropriate module paths and aliases.
 *
 * @returns {Object} - Object containing module paths and alias configurations
 */
function getModules() {
  // Check if TypeScript is setup
  const hasTsConfig = fs.existsSync(paths.appTsConfig);
  const hasJsConfig = fs.existsSync(paths.appJsConfig);

  // Ensure we don't have both TypeScript and JavaScript configs
  if (hasTsConfig && hasJsConfig) {
    throw new Error(
      "You have both a tsconfig.json and a jsconfig.json. If you are using TypeScript please remove your jsconfig.json file.",
    );
  }

  let config;

  // If there's a tsconfig.json we assume it's a
  // TypeScript project and set up the config
  // based on tsconfig.json
  if (hasTsConfig) {
    // Dynamically require TypeScript from node_modules
    const ts = require(
      resolve.sync("typescript", {
        basedir: paths.appNodeModules,
      }),
    );
    config = ts.readConfigFile(paths.appTsConfig, ts.sys.readFile).config;
    // Otherwise we'll check if there is jsconfig.json
    // for non TS projects.
  } else if (hasJsConfig) {
    config = require(paths.appJsConfig);
  }

  // Ensure config exists even if no config files were found
  config = config || {};
  const options = config.compilerOptions || {};

  const additionalModulePaths = getAdditionalModulePaths(options);

  // Return all configuration for module resolution
  return {
    additionalModulePaths: additionalModulePaths, // Additional paths to look for modules
    webpackAliases: getWebpackAliases(options),   // Webpack alias configuration
    jestAliases: getJestAliases(options),         // Jest alias configuration
    hasTsConfig,                                  // Flag indicating if project uses TypeScript
  };
}

// Export the result of getModules to be used by the build system
module.exports = getModules();
