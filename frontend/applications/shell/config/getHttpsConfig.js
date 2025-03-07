"use strict";

/**
 * Required Node.js modules
 */
const fs = require("fs"); // File system operations
const path = require("path"); // Path manipulation utilities
const crypto = require("crypto"); // Cryptographic functionality
const chalk = require("react-dev-utils/chalk"); // Colored console output
const paths = require("./paths"); // Application paths configuration

/**
 * Validates SSL certificate and key files to ensure they are valid and compatible
 *
 * @param {Object} params - Parameters object
 * @param {Buffer} params.cert - Certificate file contents
 * @param {Buffer} params.key - Private key file contents
 * @param {string} params.keyFile - Path to the key file (for error reporting)
 * @param {string} params.crtFile - Path to the certificate file (for error reporting)
 * @throws {Error} If certificate or key is invalid
 */
function validateKeyAndCerts({ cert, key, keyFile, crtFile }) {
  let encrypted;
  try {
    // publicEncrypt will throw an error with an invalid cert
    encrypted = crypto.publicEncrypt(cert, Buffer.from("test"));
  } catch (err) {
    throw new Error(
      `The certificate "${chalk.yellow(crtFile)}" is invalid.\n${err.message}`,
    );
  }

  try {
    // privateDecrypt will throw an error with an invalid key
    crypto.privateDecrypt(key, encrypted);
  } catch (err) {
    throw new Error(
      `The certificate key "${chalk.yellow(keyFile)}" is invalid.\n${
        err.message
      }`,
    );
  }
}

/**
 * Reads a file specified in environment variables and validates its existence
 *
 * @param {string} file - Path to the file to read
 * @param {string} type - Environment variable name (for error reporting)
 * @returns {Buffer} Contents of the file
 * @throws {Error} If the file doesn't exist
 */
function readEnvFile(file, type) {
  if (!fs.existsSync(file)) {
    throw new Error(
      `You specified ${chalk.cyan(
        type,
      )} in your env, but the file "${chalk.yellow(file)}" can't be found.`,
    );
  }
  return fs.readFileSync(file);
}

/**
 * Determines HTTPS configuration based on environment variables
 *
 * Checks for HTTPS flag and SSL certificate files in environment variables.
 * If all are provided and valid, returns the certificate configuration.
 * Otherwise, returns a boolean indicating if HTTPS should be used.
 *
 * @returns {Object|boolean} SSL configuration object or boolean flag
 */
function getHttpsConfig() {
  const { SSL_CRT_FILE, SSL_KEY_FILE, HTTPS } = process.env;
  const isHttps = HTTPS === "true";

  if (isHttps && SSL_CRT_FILE && SSL_KEY_FILE) {
    const crtFile = path.resolve(paths.appPath, SSL_CRT_FILE);
    const keyFile = path.resolve(paths.appPath, SSL_KEY_FILE);
    const config = {
      cert: readEnvFile(crtFile, "SSL_CRT_FILE"),
      key: readEnvFile(keyFile, "SSL_KEY_FILE"),
    };

    validateKeyAndCerts({ ...config, keyFile, crtFile });
    return config;
  }
  return isHttps;
}

// Export the HTTPS configuration function
module.exports = getHttpsConfig;
