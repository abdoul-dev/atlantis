import { IEntreeStock } from 'app/shared/model/entree-stock.model';

export interface IFournisseur {
  id?: number;
  fullName?: string;
  address?: string;
  phone?: string;
  stocks?: IEntreeStock[];
}

export class Fournisseur implements IFournisseur {
  constructor(
    public id?: number,
    public fullName?: string,
    public address?: string,
    public phone?: string,
    public stocks?: IEntreeStock[]
  ) {}
}
