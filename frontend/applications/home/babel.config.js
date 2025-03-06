/**
 * Babel configuration for the navbar application
 * This configuration specifies how JavaScript/TypeScript code should be transformed
 */
module.exports = {
  // Environment-specific settings
  env: {
    test: {
      // Special configuration for test environment
      // Uses @babel/preset-env targeting the current Node.js version
      presets: [["@babel/preset-env", { targets: "current node" }]],
    },
  },
  // Transform plugins to apply
  plugins: [
    // Enables the re-use of Babel's injected helper code to save on codesize
    "@babel/plugin-transform-runtime",
    // Transform TypeScript code to JavaScript while preserving namespace syntax
    ["@babel/plugin-transform-typescript", { allowNamespaces: true }],
  ],
  // Preset configurations that include multiple plugins
  presets: [
    [
      // Core preset for transpiling ES2015+ syntax
      "@babel/preset-env",
      {
        // Browser compatibility targets
        targets: {
          browsers: [
            ">0.2%", // Browsers with more than 0.2% global usage
            "not dead", // Browsers that are still maintained
            "not op_mini all", // Exclude Opera Mini
          ],
        },
        // Only include polyfills for features used in the code
        useBuiltIns: "usage",
        // Version of core-js to use for polyfills
        corejs: 3,
      },
    ],
    // Preset for React JSX transformation
    ["@babel/preset-react", { runtime: "automatic" }],
    ["@babel/preset-typescript", { isTSX: true, allExtensions: true }],
  ],
};
