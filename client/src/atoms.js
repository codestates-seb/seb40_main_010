import { atom } from 'recoil';

export const registerFormTitle = atom({
  key: 'registerFormTitle',
  default: '',
});

export const registerFormMaxCapacity = atom({
  key: 'registerFormMaxCapacity',
  default: 1,
});

export const registerFormAddress = atom({
  key: 'registerFormAddress',
  default: '',
});

export const registerFormDetailedAddress = atom({
  key: 'registerFormDetailedAddress',
  default: '',
});

export const registerFormDetailedInformation = atom({
  key: 'registerFormDetailedInformation',
  default: '',
});

export const registerFormCharge = atom({
  key: 'registerFormCharge',
  default: 0,
});

export const registerFormItemsCheckedState = atom({
  key: 'registerFormItemsCheckedState',
  default: new Array(10).fill(false),
});

export const registerFormPreviewImage = atom({
  key: 'registerFormPreviewImage',
  default: [],
});

export const registerFormImage = atom({
  key: 'registerFormImage',
  default: [],
});

export const navSearchValue = atom({
  key: 'navSearchValue',
  default: '',
});

export const navSearchState = atom({
  key: 'navSearchState',
  default: [],
});

export const navClickState = atom({
  key: 'navClickState',
  default: false,
});

export const navCategoryCheckState = atom({
  key: 'navCategoryCheckState',
  default: [],
});

export const reservationStartDate = atom({
  key: 'reservationStartDate',
  default: false,
});

export const reservationEndDate = atom({
  key: 'reservationEndDate',
  default: false,
});

export const reservationStartDateChangedState = atom({
  key: 'reservationStartDateChangedState',
  default: null,
});

export const reservationMaxCapacity = atom({
  key: 'reservationMaxCapacity',
  default: 1,
});

export const reservationPlaceBookmarkState = atom({
  key: 'reservationPlaceBookmarkState',
  default: false,
});
