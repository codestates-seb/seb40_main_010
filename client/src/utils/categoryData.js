export const categoryData = [
  { categoryId: 0, name: '전체', icon: 'fa-solid fa-house' },
  { categoryId: 1, name: '공유오피스', icon: 'fa-solid fa-user-group' },
  { categoryId: 2, name: '캠핑', icon: 'fa-solid fa-campground' },
  { categoryId: 3, name: '바다근처', icon: 'fa-solid fa-umbrella-beach' },
  { categoryId: 4, name: '짐보관', icon: 'fa-solid fa-suitcase-rolling' },
  { categoryId: 5, name: '파티룸', icon: 'fa-solid fa-champagne-glasses' },
  { categoryId: 6, name: '게스트하우스', icon: 'fa-solid fa-bed' },
  { categoryId: 7, name: '호텔', icon: 'fa-solid fa-hotel' },
  { categoryId: 8, name: '스터디룸', icon: 'fa-solid fa-pen-to-square' },
  { categoryId: 9, name: '계곡근처', icon: 'fa-solid fa-water' },
  { categoryId: 10, name: '공연장', icon: 'fa-solid fa-microphone' },
];

export const getURL = (index, search) => {
  if (index === 0 && search) {
    return `/search/${encodeURI(search)}?size=20&page=`;
  }
  if (index === 0 && !search) {
    return `/home?size=20&page=`;
  }
  if (index !== 0 && search) {
    return `/category/${index}/search/${encodeURI(search)}?size=20&page=`;
  }
  if (index !== 0 && !search) {
    return `/category/${index}?size=20&page=`;
  }
  return '';
};

export const categories = [
  { id: 0, place: '공유오피스' },
  { id: 1, place: '캠핑' },
  { id: 2, place: '바다근처' },
  { id: 3, place: '짐보관' },
  { id: 4, place: '파티룸' },
  { id: 5, place: '게스트하우스' },
  { id: 6, place: '호텔' },
  { id: 7, place: '스터디룸' },
  { id: 8, place: '계곡근처' },
  { id: 9, place: '공연장' },
];
