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
      target:
        'http://ec2-15-164-93-71.ap-northeast-2.compute.amazonaws.com:8080',
      changeOrigin: true,
    }),
  );
};
