import { IProducts } from 'app/shared/model/products.model';

export interface ITypeProduit {
  id?: number;
  libelle?: string;
  isDisabled?: boolean;
  products?: IProducts[];
}

export class TypeProduit implements ITypeProduit {
  constructor(public id?: number, public libelle?: string, public isDisabled?: boolean, public products?: IProducts[]) {
    this.isDisabled = this.isDisabled || false;
  }
}
