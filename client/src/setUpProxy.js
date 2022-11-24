const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function (app) {
  app.use(
    [
      '/home',
      '/auth',
      '/search',
      '/category',
      '/mbti',
      '/member',
      '/place',
      '/api',
      '/reserve',
      '/bookmark',
    ],
    createProxyMiddleware({
      // 서버주소 바꿔줄 것
      target: ' https://acbc-182-226-233-7.jp.ngrok.io',
      changeOrigin: true,
    }),
  );
};
