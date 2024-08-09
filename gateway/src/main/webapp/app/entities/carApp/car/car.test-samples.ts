import { ICar, NewCar } from './car.model';

export const sampleWithRequiredData: ICar = {
  id: 27462,
};

export const sampleWithPartialData: ICar = {
  id: 21015,
  price: 873.76,
};

export const sampleWithFullData: ICar = {
  id: 5612,
  make: 'cuddly indeed',
  model: 'boohoo midst bold',
  price: 751.3,
};

export const sampleWithNewData: NewCar = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
