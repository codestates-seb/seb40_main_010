const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function (app) {
  app.use(
    [
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
      target: 'https://f971-182-226-233-7.jp.ngrok.io',
      changeOrigin: true,
    }),
  );
};
