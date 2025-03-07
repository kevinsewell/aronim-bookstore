"use strict";

/**
 * Import the babel-jest transformer which is used to process JavaScript/JSX files with Babel
 * before running tests with Jest
 */
const babelJest = require("babel-jest").default;

/**
 * Self-executing function to determine if the new JSX transform is available
 * This checks if we can use the new JSX Runtime introduced in React 17
 */
const hasJsxRuntime = (() => {
  // If explicitly disabled via environment variable, don't use the new transform
  if (process.env.DISABLE_NEW_JSX_TRANSFORM === "true") {
    return false;
  }

  try {
    // Try to resolve the JSX runtime package
    // If it succeeds, we can use the new transform
    require.resolve("react/jsx-runtime");
    return true;
  } catch (e) {
    // If the package is not found, fall back to classic JSX transform
    return false;
  }
})();

/**
 * Export the Babel transformer configuration for Jest
 * This tells Jest how to process JavaScript/JSX files during testing
 */
module.exports = babelJest.createTransformer({
  presets: [
    [
      // Use the react-app babel preset which includes necessary plugins for React
      require.resolve("babel-preset-react-app"),
      {
        // Use automatic JSX runtime if available, otherwise use classic
        // Automatic runtime doesn't require importing React in every file with JSX
        runtime: hasJsxRuntime ? "automatic" : "classic",
      },
    ],
  ],
  babelrc: false, // Don't use .babelrc files
  configFile: false, // Don't use babel.config.js files
});
