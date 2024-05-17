const {defineConfig} = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  outputDir: 'ui-zkc',
  publicPath: './',
  devServer: {
    proxy: {
      '/dev': {
        target: 'http://localhost:30140',
        changeOrigin: true,
        secure: false,
        pathRewrite: { '^/dev': '' },
      },
    },
  }
})
