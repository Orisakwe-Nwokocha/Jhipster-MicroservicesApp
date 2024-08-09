import { IDealer, NewDealer } from './dealer.model';

export const sampleWithRequiredData: IDealer = {
  id: 7743,
  name: 'flinch glide implement',
};

export const sampleWithPartialData: IDealer = {
  id: 12325,
  name: 'among',
};

export const sampleWithFullData: IDealer = {
  id: 12012,
  name: 'originate whittle',
  address: 'muscat',
};

export const sampleWithNewData: NewDealer = {
  name: 'sensitize promptly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
