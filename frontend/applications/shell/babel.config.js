/**
 * Babel configuration file for the frontend shell application
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
    [
      // @babel/plugin-transform-runtime reduces code duplication by extracting Babel helpers
      "@babel/plugin-transform-runtime",
      { 
        useESModules: true,  // Use ES modules syntax for smaller bundle size
        regenerator: false   // Disable regenerator transform as we're not using generators
      },
    ],
  ],
  // Global presets that apply to all environments
  presets: [
    "@babel/preset-env",     // Transforms modern JavaScript features to be compatible with older environments
    "@babel/preset-typescript" // Adds TypeScript support
  ],
};
