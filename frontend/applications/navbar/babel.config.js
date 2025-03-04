module.exports = {
  presets: [
    [
      "@babel/preset-env",
      {
        targets: {
          browsers: [">0.2%", "not dead", "not op_mini all"],
        },
        useBuiltIns: "usage",
        corejs: 3,
      },
    ],
    "@babel/preset-react",
  ],
  plugins: ["@babel/plugin-transform-runtime"],
};
