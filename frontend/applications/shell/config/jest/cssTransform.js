"use strict";

/**
 * Custom Jest transformer for CSS/style files.
 *
 * This transformer converts CSS imports into empty JavaScript objects during Jest test runs.
 * Since Jest runs in a Node.js environment where CSS processing isn't needed,
 * this transformer effectively mocks CSS imports to prevent test failures.
 *
 * Documentation: http://facebook.github.io/jest/docs/en/webpack.html
 */

module.exports = {
  /**
   * Process method that transforms CSS imports.
   * @returns {Object} Object containing the transformed code as an empty module.
   */
  process() {
    return { code: "module.exports = {};" };
  },

  /**
   * Provides a cache key for the transformer.
   * Since this transformer always produces the same output regardless of input,
   * we can use a static string as the cache key for better performance.
   *
   * @returns {string} A static cache key.
   */
  getCacheKey() {
    // The output is always the same.
    return "cssTransform";
  },
};
