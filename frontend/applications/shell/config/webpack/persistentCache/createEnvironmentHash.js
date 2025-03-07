"use strict";

/**
 * This module creates a hash based on the environment configuration.
 * It's commonly used for cache busting in webpack to ensure rebuilds
 * when the environment changes.
 */

const { createHash } = require("crypto");

/**
 * Creates an MD5 hash from the provided environment object
 * @param {Object} env - The environment configuration object
 * @returns {string} - Hexadecimal representation of the MD5 hash
 */
module.exports = (env) => {
  // Create an MD5 hash instance
  const hash = createHash("md5");
  // Update the hash with the stringified environment object
  hash.update(JSON.stringify(env));

  // Return the hash as a hexadecimal string
  return hash.digest("hex");
};
