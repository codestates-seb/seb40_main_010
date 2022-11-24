const header = {
  headers: {
    'ngrok-skip-browser-warning': '010',
    Authorization: `Bearer ${localStorage.getItem('ACCESS')}`,
    RefreshToken: localStorage.getItem('REFRESH'),
  },
};

export default header;
