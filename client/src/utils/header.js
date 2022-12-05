const header = {
  headers: {
    'ngrok-skip-browser-warning': '010',
    Authorization: localStorage.getItem('ACCESS')
      ? `Bearer ${localStorage.getItem('ACCESS')}`
      : '',
    RefreshToken: localStorage.getItem('REFRESH')
      ? localStorage.getItem('REFRESH')
      : '',
  },
};

export default header;
