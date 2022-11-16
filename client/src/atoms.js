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
