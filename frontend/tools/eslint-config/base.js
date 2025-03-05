// Import the core ESLint JavaScript configuration
import js from "@eslint/js";
// Import Prettier configuration to avoid conflicts between ESLint and Prettier
import eslintConfigPrettier from "eslint-config-prettier";
// Import TypeScript ESLint plugin for TypeScript support
import tseslint from "typescript-eslint";
// Import plugin that downgrades all errors to warnings
import onlyWarn from "eslint-plugin-only-warn";

/**
 * A shared ESLint configuration for the repository.
 *
 * @type {import("eslint").Linter.Config}
 * */
export const config = [
  // Use ESLint's recommended rules for JavaScript
  js.configs.recommended,
  // Apply Prettier config to avoid rule conflicts
  eslintConfigPrettier,
  // Spread TypeScript ESLint recommended configuration
  ...tseslint.configs.recommended,
  // Empty configuration object for additional plugins and rules
  // This can be extended later with project-specific settings
  {
    plugins: {
      // Custom plugins can be added here
    },
    rules: {
      // Custom rules can be added here
    },
  },
  // Configure the only-warn plugin to downgrade all errors to warnings
  // This is useful during development to see all issues without breaking the build
  {
    plugins: {
      onlyWarn,
    },
  },
  // Ignore the dist directory to prevent linting of compiled/built files
  {
    ignores: ["dist/**"],
  },
];
