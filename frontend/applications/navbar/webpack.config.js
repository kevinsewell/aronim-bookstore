/**
 * Webpack configuration for the navbar micro-frontend application
 */
const path = require("path");
const { merge } = require("webpack-merge");
const singleSpaDefaults = require("webpack-config-single-spa-react");

/**
 * Exports the webpack configuration function
 * @param {Object} webpackConfigEnv - Webpack environment configuration
 * @param {Object} argv - Command line arguments
 * @return {Object} Final webpack configuration
 */
module.exports = (webpackConfigEnv, argv) => {

  // Organization name used for namespacing in the micro-frontend architecture
  const orgName = "aronim";

  // Get default single-spa React configuration
  const defaultConfig = singleSpaDefaults({
    orgName,
    projectName: "application-navbar",
    webpackConfigEnv,
    argv,
  });

  // Merge default config with custom settings
  const config = merge(defaultConfig, {
    externals: ["react", "react-dom", "react-dom/client", "single-spa", "single-spa-react"], // bundle all other dependencies
    resolve: {
      extensions: [".ts", ".tsx"], // Add TypeScript file extensions support
    },
  });

  // Find the SystemJSPublicPathWebpackPlugin in the plugins array
  const publicPathPluginIndex = config.plugins.findIndex(
    (plugin) => plugin.constructor.name === "SystemJSPublicPathWebpackPlugin"
  );

  // Remove the SystemJSPublicPathWebpackPlugin if found
  // This prevents issues with public path resolution in the micro-frontend
  if (publicPathPluginIndex >= 0) {
    config.plugins.splice(publicPathPluginIndex, 1);
  }

  return config;
};
