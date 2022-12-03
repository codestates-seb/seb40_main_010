const getHeader = (AccessToken = '', RefreshToken = '') => ({
  headers: {
    'ngrok-skip-browser-warning': '010',
    Authorization: `Bearer ${AccessToken}`,
    RefreshToken,
  },
});

export default getHeader;
