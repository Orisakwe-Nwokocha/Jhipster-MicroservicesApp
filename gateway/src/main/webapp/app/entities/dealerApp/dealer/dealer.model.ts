export interface IDealer {
  id: number;
  name?: string | null;
  address?: string | null;
}

export type NewDealer = Omit<IDealer, 'id'> & { id: null };
