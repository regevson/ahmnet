module.exports = {
  devServer: {
    proxy: 'http://localhost:8080',
    disableHostCheck: true
  },

  transpileDependencies: [
    'vuetify'
  ]
}
