import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: '66e8dfc3-14af-447a-a37a-598b79c83e07',
};

export const sampleWithPartialData: IAuthority = {
  name: '64b78c90-7ba0-4490-8199-18a4bce3684a',
};

export const sampleWithFullData: IAuthority = {
  name: 'd1acbeb6-1c46-4d15-8f91-b46fdd27cd1f',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
