/**
 * Webpack configuration for the application shell in a micro-frontend architecture
 * using single-spa framework.
 */

// Import required webpack plugins and utilities
const { merge } = require("webpack-merge");         // Utility to merge webpack configurations
const singleSpaDefaults = require("webpack-config-single-spa-ts");  // Default single-spa config for TypeScript
const HtmlWebpackPlugin = require("html-webpack-plugin");  // Plugin to generate HTML files

/**
 * Main webpack configuration function
 * @param {Object} webpackConfigEnv - Environment variables passed to webpack
 * @param {Object} argv - Command line arguments passed to webpack
 * @returns {Object} Final webpack configuration
 */
module.exports = (webpackConfigEnv, argv) => {
  // Organization name used for namespacing in the micro-frontend architecture
  const orgName = "aronim";
  
  // Get the default single-spa webpack configuration
  const defaultConfig = singleSpaDefaults({
    orgName,
    projectName: "application-shell",  // Project name for the shell application
    webpackConfigEnv,
    argv,
    disableHtmlGeneration: true,  // Disable default HTML generation as we'll use our custom template
  });

  // Merge the default config with our custom configuration
  return merge(defaultConfig, {
     // bundle all other dependencies
    externals: ["keycloak-js", "rxjs", "single-spa", "single-spa-layout"],
    // modify the webpack config however you'd like to by adding to this object
    plugins: [
      // Configure HTML generation with our custom template
      new HtmlWebpackPlugin({
        inject: false,  // Don't automatically inject assets into the template
        template: "src/index.ejs",  // Path to the template file
        templateParameters: {
          // Pass parameters to the template
          isLocal: webpackConfigEnv && webpackConfigEnv.isLocal,  // Flag to indicate local development
          orgName,  // Pass the organization name to the template
        },
      }),
    ],
  });
};
