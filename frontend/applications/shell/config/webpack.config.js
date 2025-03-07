"use strict";

/**
 * Webpack configuration file for the shell application
 * This file configures the build process for both development and production environments
 */

// Core Node.js modules
const fs = require("fs");
const path = require("path");

// Webpack and related plugins
const webpack = require("webpack");
const resolve = require("resolve");
const HtmlWebpackPlugin = require("html-webpack-plugin");
const CaseSensitivePathsPlugin = require("case-sensitive-paths-webpack-plugin");
const InlineChunkHtmlPlugin = require("react-dev-utils/InlineChunkHtmlPlugin");
const TerserPlugin = require("terser-webpack-plugin");
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const CssMinimizerPlugin = require("css-minimizer-webpack-plugin");
const { WebpackManifestPlugin } = require("webpack-manifest-plugin");
const InterpolateHtmlPlugin = require("react-dev-utils/InterpolateHtmlPlugin");
const WorkboxWebpackPlugin = require("workbox-webpack-plugin");
const ModuleScopePlugin = require("react-dev-utils/ModuleScopePlugin");
const getCSSModuleLocalIdent = require("react-dev-utils/getCSSModuleLocalIdent");
const ESLintPlugin = require("eslint-webpack-plugin");

// Local configuration files
const paths = require("./paths");
const modules = require("./modules");
const getClientEnvironment = require("./env");
const ModuleNotFoundPlugin = require("react-dev-utils/ModuleNotFoundPlugin");

// TypeScript checking plugins - different versions based on environment variable
const ForkTsCheckerWebpackPlugin =
  process.env.TSC_COMPILE_ON_ERROR === "true"
    ? require("react-dev-utils/ForkTsCheckerWarningWebpackPlugin")
    : require("react-dev-utils/ForkTsCheckerWebpackPlugin");
const ReactRefreshWebpackPlugin = require("@pmmmwh/react-refresh-webpack-plugin");

// Helper for persistent cache
const createEnvironmentHash = require("./webpack/persistentCache/createEnvironmentHash");

// Source maps configuration - disabled for production if GENERATE_SOURCEMAP=false
const shouldUseSourceMap = process.env.GENERATE_SOURCEMAP !== "false";

// React refresh and Babel runtime entries for development mode
const reactRefreshRuntimeEntry = require.resolve("react-refresh/runtime");
const reactRefreshWebpackPluginRuntimeEntry = require.resolve(
  "@pmmmwh/react-refresh-webpack-plugin",
);
const babelRuntimeEntry = require.resolve("babel-preset-react-app");
const babelRuntimeEntryHelpers = require.resolve(
  "@babel/runtime/helpers/esm/assertThisInitialized",
  { paths: [babelRuntimeEntry] },
);
const babelRuntimeRegenerator = require.resolve("@babel/runtime/regenerator", {
  paths: [babelRuntimeEntry],
});

// Configuration for runtime chunk inlining
const shouldInlineRuntimeChunk = process.env.INLINE_RUNTIME_CHUNK !== "false";

// ESLint configuration options
const emitErrorsAsWarnings = process.env.ESLINT_NO_DEV_ERRORS === "true";
const disableESLintPlugin = process.env.DISABLE_ESLINT_PLUGIN === "true";

// Size limit for inlining images as data URLs (defaults to 10KB)
const imageInlineSizeLimit = parseInt(
  process.env.IMAGE_INLINE_SIZE_LIMIT || "10000",
);

// Feature detection flags
const useTypeScript = fs.existsSync(paths.appTsConfig);
const useTailwind = fs.existsSync(
  path.join(paths.appPath, "tailwind.config.js"),
);

// Service worker configuration
const swSrc = paths.swSrc;

// Regular expressions for identifying different types of style files
const cssRegex = /\.css$/;
const cssModuleRegex = /\.module\.css$/;
const sassRegex = /\.(scss|sass)$/;
const sassModuleRegex = /\.module\.(scss|sass)$/;

/**
 * Determines if the new JSX transform can be used
 * Returns false if explicitly disabled or if react/jsx-runtime cannot be resolved
 */
const hasJsxRuntime = (() => {
  if (process.env.DISABLE_NEW_JSX_TRANSFORM === "true") {
    return false;
  }

  try {
    require.resolve("react/jsx-runtime");
    return true;
  } catch (e) {
    return false;
  }
})();

/**
 * Main webpack configuration function
 * @param {string} webpackEnv - Environment ('development' or 'production')
 * @returns {Object} Webpack configuration object
 */
module.exports = function (webpackEnv) {
  const isEnvDevelopment = webpackEnv === "development";
  const isEnvProduction = webpackEnv === "production";

  // Flag for enabling React profiling in production
  const isEnvProductionProfile =
    isEnvProduction && process.argv.includes("--profile");

  // Environment variables to inject into the application
  const env = getClientEnvironment(paths.publicUrlOrPath.slice(0, -1));

  // Flag for enabling React Fast Refresh in development
  const shouldUseReactRefresh = env.raw.FAST_REFRESH;

  /**
   * Helper function to generate style loaders configuration
   * @param {Object} cssOptions - Options for css-loader
   * @param {string} preProcessor - Optional preprocessor (e.g., 'sass-loader')
   * @returns {Array} Array of loader configurations
   */
  const getStyleLoaders = (cssOptions, preProcessor) => {
    const loaders = [
      // Development: style-loader injects CSS into the DOM
      isEnvDevelopment && require.resolve("style-loader"),
      // Production: extract CSS into separate files
      isEnvProduction && {
        loader: MiniCssExtractPlugin.loader,
        // css is located in `static/css`, use '../../' to locate index.html folder
        // in production `paths.publicUrlOrPath` can be a relative path
        options: paths.publicUrlOrPath.startsWith(".")
          ? { publicPath: "../../" }
          : {},
      },
      // CSS loader resolves imports and URLs in CSS
      {
        loader: require.resolve("css-loader"),
        options: cssOptions,
      },
      // PostCSS for vendor prefixing and other transformations
      {
        // Options for PostCSS as we reference these options twice
        // Adds vendor prefixing based on your specified browser support in
        // package.json
        loader: require.resolve("postcss-loader"),
        options: {
          postcssOptions: {
            // Necessary for external CSS imports to work
            // https://github.com/facebook/create-react-app/issues/2677
            ident: "postcss",
            config: false,
            plugins: !useTailwind
              ? [
                  "postcss-flexbugs-fixes",
                  [
                    "postcss-preset-env",
                    {
                      autoprefixer: {
                        flexbox: "no-2009",
                      },
                      stage: 3,
                    },
                  ],
                  // Adds PostCSS Normalize as the reset css with default options,
                  // so that it honors browserslist config in package.json
                  // which in turn let's users customize the target behavior as per their needs.
                  "postcss-normalize",
                ]
              : [
                  "tailwindcss",
                  "postcss-flexbugs-fixes",
                  [
                    "postcss-preset-env",
                    {
                      autoprefixer: {
                        flexbox: "no-2009",
                      },
                      stage: 3,
                    },
                  ],
                ],
          },
          sourceMap: isEnvProduction ? shouldUseSourceMap : isEnvDevelopment,
        },
      },
    ].filter(Boolean);

    // Add preprocessor loaders if specified (e.g., sass-loader)
    if (preProcessor) {
      loaders.push(
        {
          // resolve-url-loader is needed for SASS to correctly resolve relative paths
          loader: require.resolve("resolve-url-loader"),
          options: {
            sourceMap: isEnvProduction ? shouldUseSourceMap : isEnvDevelopment,
            root: paths.appSrc,
          },
        },
        {
          // The actual preprocessor (e.g., sass-loader)
          loader: require.resolve(preProcessor),
          options: {
            sourceMap: true,
          },
        },
      );
    }
    return loaders;
  };

  // The complete webpack configuration object
  return {
    // Target environment
    target: ["browserslist"],
    // Limit webpack output to errors and warnings
    stats: "errors-warnings",
    // Set mode based on environment
    mode: isEnvProduction ? "production" : isEnvDevelopment && "development",
    // Stop compilation early in production on errors
    bail: isEnvProduction,
    // Source map configuration
    devtool: isEnvProduction
      ? shouldUseSourceMap
        ? "source-map"
        : false
      : isEnvDevelopment && "cheap-module-source-map",
    // Application entry point
    entry: paths.appIndexJs,
    // Output configuration
    output: {
      // Build output directory
      path: paths.appBuild,
      // Include comments in development for better debugging
      pathinfo: isEnvDevelopment,
      // Output filename configuration - hashed in production for cache busting
      filename: isEnvProduction
        ? "static/js/[name].[contenthash:8].js"
        : isEnvDevelopment && "static/js/bundle.js",
      // Chunk filename configuration for code splitting
      chunkFilename: isEnvProduction
        ? "static/js/[name].[contenthash:8].chunk.js"
        : isEnvDevelopment && "static/js/[name].chunk.js",
      // Asset filename configuration
      assetModuleFilename: "static/media/[name].[hash][ext]",
      // Public URL path for assets
      publicPath: paths.publicUrlOrPath,
      // Source map file naming
      devtoolModuleFilenameTemplate: isEnvProduction
        ? (info) =>
            path
              .relative(paths.appSrc, info.absoluteResourcePath)
              .replace(/\\/g, "/")
        : isEnvDevelopment &&
          ((info) =>
            path.resolve(info.absoluteResourcePath).replace(/\\/g, "/")),
    },
    // Filesystem cache configuration for faster rebuilds
    cache: {
      type: "filesystem",
      version: createEnvironmentHash(env.raw),
      cacheDirectory: paths.appWebpackCache,
      store: "pack",
      buildDependencies: {
        defaultWebpack: ["webpack/lib/"],
        config: [__filename],
        tsconfig: [paths.appTsConfig, paths.appJsConfig].filter((f) =>
          fs.existsSync(f),
        ),
      },
    },
    // Reduce logging noise
    infrastructureLogging: {
      level: "none",
    },
    // Optimization configuration
    optimization: {
      // Only minimize in production
      minimize: isEnvProduction,
      minimizer: [
        // JavaScript minification with Terser
        new TerserPlugin({
          terserOptions: {
            parse: {
              // Parse ECMA 8 code
              ecma: 8,
            },
            compress: {
              // Output ECMA 5 compatible code
              ecma: 5,
              warnings: false,
              // Disabled because of an issue with Uglify breaking seemingly valid code:
              // https://github.com/facebook/create-react-app/issues/2376
              // Pending further investigation:
              // https://github.com/mishoo/UglifyJS2/issues/2011
              comparisons: false,
              // Disabled because of an issue with Terser breaking valid code:
              // https://github.com/facebook/create-react-app/issues/5250
              // Pending further investigation:
              // https://github.com/terser-js/terser/issues/120
              inline: 2,
            },
            mangle: {
              // Fix for Safari 10 bugs
              safari10: true,
            },
            // Keep class names and function names when profiling is enabled
            keep_classnames: isEnvProductionProfile,
            keep_fnames: isEnvProductionProfile,
            output: {
              // Output ECMA 5 compatible code
              ecma: 5,
              comments: false,
              // Turned on because emoji and regex is not minified properly using default
              // https://github.com/facebook/create-react-app/issues/2488
              ascii_only: true,
            },
          },
        }),
        // CSS minification
        new CssMinimizerPlugin(),
      ],
    },
    // Module resolution configuration
    resolve: {
      // Module resolution paths
      modules: ["node_modules", paths.appNodeModules].concat(
        modules.additionalModulePaths || [],
      ),
      // File extensions to resolve
      extensions: paths.moduleFileExtensions
        .map((ext) => `.${ext}`)
        .filter((ext) => useTypeScript || !ext.includes("ts")),
      // Module aliases
      alias: {
        // Support React Native Web
        // https://www.smashingmagazine.com/2016/08/a-glimpse-into-the-future-with-react-native-for-web/
        "react-native": "react-native-web",
        // Allows for better profiling with ReactDevTools
        ...(isEnvProductionProfile && {
          "react-dom$": "react-dom/profiling",
          "scheduler/tracing": "scheduler/tracing-profiling",
        }),
        ...(modules.webpackAliases || {}),
      },
      plugins: [
        // Prevents users from importing files from outside of src/ (or node_modules/).
        // This often causes confusion because we only process files within src/ with babel.
        // To fix this, we prevent you from importing files out of src/ -- if you'd like to,
        // please link the files into your node_modules/ and let module-resolution kick in.
        // Make sure your source files are compiled, as they will not be processed in any way.
        new ModuleScopePlugin(paths.appSrc, [
          paths.appPackageJson,
          reactRefreshRuntimeEntry,
          reactRefreshWebpackPluginRuntimeEntry,
          babelRuntimeEntry,
          babelRuntimeEntryHelpers,
          babelRuntimeRegenerator,
        ]),
      ],
    },
    // Module loaders configuration
    module: {
      strictExportPresence: true,
      rules: [
        // Handle source maps in node_modules
        shouldUseSourceMap && {
          enforce: "pre",
          exclude: /@babel(?:\/|\\{1,2})runtime/,
          test: /\.(js|mjs|jsx|ts|tsx|css)$/,
          loader: require.resolve("source-map-loader"),
        },
        {
          // "oneOf" will traverse all following loaders until one will
          // match the requirements. When no loader matches it will fall
          // back to the "file" loader at the end of the loader list.
          oneOf: [
            // AVIF image format support
            {
              test: [/\.avif$/],
              type: "asset",
              mimetype: "image/avif",
              parser: {
                dataUrlCondition: {
                  maxSize: imageInlineSizeLimit,
                },
              },
            },
            // Common image formats support
            {
              test: [/\.bmp$/, /\.gif$/, /\.jpe?g$/, /\.png$/],
              type: "asset",
              parser: {
                dataUrlCondition: {
                  maxSize: imageInlineSizeLimit,
                },
              },
            },
            // SVG handling with special configuration
            {
              test: /\.svg$/,
              use: [
                {
                  loader: require.resolve("@svgr/webpack"),
                  options: {
                    prettier: false,
                    svgo: false,
                    svgoConfig: {
                      plugins: [{ removeViewBox: false }],
                    },
                    titleProp: true,
                    ref: true,
                  },
                },
                {
                  loader: require.resolve("file-loader"),
                  options: {
                    name: "static/media/[name].[hash].[ext]",
                  },
                },
              ],
              issuer: {
                and: [/\.(ts|tsx|js|jsx|md|mdx)$/],
              },
            },
            // JavaScript/TypeScript processing with Babel
            {
              test: /\.(js|mjs|jsx|ts|tsx)$/,
              include: paths.appSrc,
              loader: require.resolve("babel-loader"),
              options: {
                customize: require.resolve(
                  "babel-preset-react-app/webpack-overrides",
                ),
                presets: [
                  [
                    require.resolve("babel-preset-react-app"),
                    {
                      runtime: hasJsxRuntime ? "automatic" : "classic",
                    },
                  ],
                ],
                // React refresh for development
                plugins: [
                  isEnvDevelopment &&
                    shouldUseReactRefresh &&
                    require.resolve("react-refresh/babel"),
                ].filter(Boolean),
                // Enable caching for faster rebuilds
                cacheDirectory: true,
                cacheCompression: false,
                compact: isEnvProduction,
              },
            },
            // Process external JavaScript files with Babel
            {
              test: /\.(js|mjs)$/,
              exclude: /@babel(?:\/|\\{1,2})runtime/,
              loader: require.resolve("babel-loader"),
              options: {
                babelrc: false,
                configFile: false,
                compact: false,
                presets: [
                  [
                    require.resolve("babel-preset-react-app/dependencies"),
                    { helpers: true },
                  ],
                ],
                cacheDirectory: true,
                cacheCompression: false,
                // Enable source maps for debugging
                sourceMaps: shouldUseSourceMap,
                inputSourceMap: shouldUseSourceMap,
              },
            },
            // Process standard CSS files
            {
              test: cssRegex,
              exclude: cssModuleRegex,
              use: getStyleLoaders({
                importLoaders: 1,
                sourceMap: isEnvProduction
                  ? shouldUseSourceMap
                  : isEnvDevelopment,
                modules: {
                  mode: "icss",
                },
              }),
              // Preserve CSS imports even with side effects disabled
              sideEffects: true,
            },
            // Process CSS Modules
            {
              test: cssModuleRegex,
              use: getStyleLoaders({
                importLoaders: 1,
                sourceMap: isEnvProduction
                  ? shouldUseSourceMap
                  : isEnvDevelopment,
                modules: {
                  mode: "local",
                  getLocalIdent: getCSSModuleLocalIdent,
                },
              }),
            },
            // Process SASS files
            {
              test: sassRegex,
              exclude: sassModuleRegex,
              use: getStyleLoaders(
                {
                  importLoaders: 3,
                  sourceMap: isEnvProduction
                    ? shouldUseSourceMap
                    : isEnvDevelopment,
                  modules: {
                    mode: "icss",
                  },
                },
                "sass-loader",
              ),
              // Preserve SASS imports even with side effects disabled
              sideEffects: true,
            },
            // Process SASS Modules
            {
              test: sassModuleRegex,
              use: getStyleLoaders(
                {
                  importLoaders: 3,
                  sourceMap: isEnvProduction
                    ? shouldUseSourceMap
                    : isEnvDevelopment,
                  modules: {
                    mode: "local",
                    getLocalIdent: getCSSModuleLocalIdent,
                  },
                },
                "sass-loader",
              ),
            },
            // Fallback file loader for all other assets
            {
              // Exclude files that should be handled by specific loaders
              exclude: [/^$/, /\.(js|mjs|jsx|ts|tsx)$/, /\.html$/, /\.json$/],
              type: "asset/resource",
            },
            // ** STOP ** Are you adding a new loader?
            // Make sure to add the new loader(s) before the "file" loader.
          ],
        },
      ].filter(Boolean),
    },
    // Webpack plugins
    plugins: [
      // Generate HTML file with injected bundles
      new HtmlWebpackPlugin(
        Object.assign(
          {},
          {
            inject: true,
            template: paths.appHtml,
          },
          isEnvProduction
            ? {
                minify: {
                  removeComments: true,
                  collapseWhitespace: true,
                  removeRedundantAttributes: true,
                  useShortDoctype: true,
                  removeEmptyAttributes: true,
                  removeStyleLinkTypeAttributes: true,
                  keepClosingSlash: true,
                  minifyJS: true,
                  minifyCSS: true,
                  minifyURLs: true,
                },
              }
            : undefined,
        ),
      ),
      // Inline runtime chunk for better performance
      isEnvProduction &&
        shouldInlineRuntimeChunk &&
        new InlineChunkHtmlPlugin(HtmlWebpackPlugin, [/runtime-.+[.]js/]),
      // Make environment variables available in HTML template
      new InterpolateHtmlPlugin(HtmlWebpackPlugin, env.raw),
      // Provide helpful context for module not found errors
      new ModuleNotFoundPlugin(paths.appPath),
      // Make environment variables available in JavaScript code
      new webpack.DefinePlugin(env.stringified),
      // Enable React Fast Refresh in development
      isEnvDevelopment &&
        shouldUseReactRefresh &&
        new ReactRefreshWebpackPlugin({
          overlay: false,
        }),
      // Enforce case-sensitive paths to avoid issues on case-insensitive filesystems
      isEnvDevelopment && new CaseSensitivePathsPlugin(),
      // Extract CSS into separate files in production
      isEnvProduction &&
        new MiniCssExtractPlugin({
          filename: "static/css/[name].[contenthash:8].css",
          chunkFilename: "static/css/[name].[contenthash:8].chunk.css",
        }),
      // Generate asset manifest for tools and PWA
      new WebpackManifestPlugin({
        fileName: "asset-manifest.json",
        publicPath: paths.publicUrlOrPath,
        generate: (seed, files, entrypoints) => {
          const manifestFiles = files.reduce((manifest, file) => {
            manifest[file.name] = file.path;
            return manifest;
          }, seed);
          const entrypointFiles = entrypoints.main.filter(
            (fileName) => !fileName.endsWith(".map"),
          );

          return {
            files: manifestFiles,
            entrypoints: entrypointFiles,
          };
        },
      }),
      // Reduce Moment.js bundle size by excluding unnecessary locales
      new webpack.IgnorePlugin({
        resourceRegExp: /^\.\/locale$/,
        contextRegExp: /moment$/,
      }),
      // Generate service worker for PWA capabilities in production
      isEnvProduction &&
        fs.existsSync(swSrc) &&
        new WorkboxWebpackPlugin.InjectManifest({
          swSrc,
          dontCacheBustURLsMatching: /\.[0-9a-f]{8}\./,
          exclude: [/\.map$/, /asset-manifest\.json$/, /LICENSE/],
          // Bump up the default maximum size (2mb) that's precached,
          // to make lazy-loading failure scenarios less likely.
          // See https://github.com/cra-template/pwa/issues/13#issuecomment-722667270
          maximumFileSizeToCacheInBytes: 5 * 1024 * 1024,
        }),
      // TypeScript type checking in a separate process
      useTypeScript &&
        new ForkTsCheckerWebpackPlugin({
          async: isEnvDevelopment,
          typescript: {
            typescriptPath: resolve.sync("typescript", {
              basedir: paths.appNodeModules,
            }),
            configOverwrite: {
              compilerOptions: {
                sourceMap: isEnvProduction
                  ? shouldUseSourceMap
                  : isEnvDevelopment,
                skipLibCheck: true,
                inlineSourceMap: false,
                declarationMap: false,
                noEmit: true,
                incremental: true,
                tsBuildInfoFile: paths.appTsBuildInfoFile,
              },
            },
            context: paths.appPath,
            diagnosticOptions: {
              syntactic: true,
            },
            mode: "write-references",
            // profile: true,
          },
          issue: {
            // Include/exclude patterns for TypeScript files
            include: [
              { file: "../**/src/**/*.{ts,tsx}" },
              { file: "**/src/**/*.{ts,tsx}" },
            ],
            exclude: [
              { file: "**/src/**/__tests__/**" },
              { file: "**/src/**/?(*.){spec|test}.*" },
              { file: "**/src/setupProxy.*" },
              { file: "**/src/setupTests.*" },
            ],
          },
          logger: {
            infrastructure: "silent",
          },
        }),
      // ESLint integration for linting during build
      !disableESLintPlugin &&
        new ESLintPlugin({
          // Plugin options
          extensions: ["js", "mjs", "jsx", "ts", "tsx"],
          formatter: require.resolve("react-dev-utils/eslintFormatter"),
          eslintPath: require.resolve("eslint"),
          failOnError: !(isEnvDevelopment && emitErrorsAsWarnings),
          context: paths.appSrc,
          cache: true,
          cacheLocation: path.resolve(
            paths.appNodeModules,
            ".cache/.eslintcache",
          ),
          // ESLint class options
          cwd: paths.appPath,
          resolvePluginsRelativeTo: __dirname,
          baseConfig: {
            extends: [require.resolve("eslint-config-react-app/base")],
            rules: {
              ...(!hasJsxRuntime && {
                "react/react-in-jsx-scope": "error",
              }),
            },
          },
        }),
    ].filter(Boolean),
    // Disable performance hints as we use our own size reporter
    performance: false,
  };
};
