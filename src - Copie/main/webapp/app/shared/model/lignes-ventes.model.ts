import { IProducts } from "./products.model";

export interface ILignesVentes {
  id?: number;
  prixUnitaire?: number;
  quantite?: number;
  prixTotal?: number;
  productsLibelle?: string;
  productsId?: number;
  products?: IProducts;
  ventesId?: number;
}

export class LignesVentes implements ILignesVentes {
  constructor(
    public id?: number,
    public prixUnitaire?: number,
    public quantite?: number,
    public prixTotal?: number,
    public productsLibelle?: string,
    public productsId?: number,
    public products?: IProducts,
    public ventesId?: number
  ) {}
}
