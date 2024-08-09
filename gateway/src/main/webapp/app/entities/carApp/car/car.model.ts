export interface ICar {
  id: number;
  make?: string | null;
  model?: string | null;
  price?: number | null;
}

export type NewCar = Omit<ICar, 'id'> & { id: null };
