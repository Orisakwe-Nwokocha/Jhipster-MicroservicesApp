import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 8406,
  login: 'L.qPn',
};

export const sampleWithPartialData: IUser = {
  id: 2543,
  login: 'tQj',
};

export const sampleWithFullData: IUser = {
  id: 18371,
  login: 'HHW=@Eeh6UV\\Qm\\dewgE',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
