import { IProducts } from "./products.model";

export interface ILigneEntreeStock {
  id?: number;
  prixUnitaire?: number;
  quantite?: number;
  prixTotal?: number;
  productsLibelle?: string;
  productsId?: number;
  products?: IProducts;
  entreestockId?: number;
}

export class LigneEntreeStock implements ILigneEntreeStock {
  constructor(
    public id?: number,
    public prixUnitaire?: number,
    public quantite?: number,
    public prixTotal?: number,
    public productsLibelle?: string,
    public productsId?: number,
    public products?: IProducts,
    public entreestockId?: number
  ) {}
}
