"use strict";

// Import the Node.js path module for file path operations
const path = require("path");

// Import the camelcase library to convert strings to camelCase or PascalCase
const camelcase = require("camelcase");

// This is a custom Jest transformer turning file imports into filenames.
// http://facebook.github.io/jest/docs/en/webpack.html

module.exports = {
  /**
   * Process function that transforms file imports during Jest tests
   * @param {string} src - The source content of the file
   * @param {string} filename - The path to the file being processed
   * @returns {Object} Object containing the transformed code
   */
  process(src, filename) {
    // Extract and stringify the base filename
    const assetFilename = JSON.stringify(path.basename(filename));

    // Special handling for SVG files
    if (filename.match(/\.svg$/)) {
      // Based on how SVGR generates a component name:
      // https://github.com/smooth-code/svgr/blob/01b194cf967347d43d4cbe6b434404731b87cf27/packages/core/src/state.js#L6

      // Convert the filename to PascalCase for React component naming conventions
      const pascalCaseFilename = camelcase(path.parse(filename).name, {
        pascalCase: true,
      });

      // Create a component name with 'Svg' prefix
      const componentName = `Svg${pascalCaseFilename}`;

      // Generate code that mocks an SVG component for testing
      // This creates both a default export (the filename) and a ReactComponent export
      // that can be used like a React component in tests
      const code = `const React = require('react');
      module.exports = {
        __esModule: true,
        default: ${assetFilename},
        ReactComponent: React.forwardRef(function ${componentName}(props, ref) {
          return {
            $$typeof: Symbol.for('react.element'),
            type: 'svg',
            ref: ref,
            key: null,
            props: Object.assign({}, props, {
              children: ${assetFilename}
            })
          };
        }),
      };`;

      return { code };
    }

    // For non-SVG files, simply export the filename as a module
    return { code: `module.exports = ${assetFilename};` };
  },
};
