import { Moment } from 'moment';
import { ILigneEntreeStock } from 'app/shared/model/ligne-entree-stock.model';

export interface IEntreeStock {
  id?: number;
  montant?: number;
  annule?: boolean;
  date?: Moment;
  ligneEntreeStocks?: ILigneEntreeStock[];
  fournisseurFullName?: string;
  fournisseurId?: number;
}

export class EntreeStock implements IEntreeStock {
  DeletedOrderItemIDs?: string;
  constructor(
    public id?: number,
    public montant?: number,
    public annule?: boolean,
    public date?: Moment,
    public ligneEntreeStocks?: ILigneEntreeStock[],
    public fournisseurFullName?: string,
    public fournisseurId?: number
  ) {
    this.annule = this.annule || false;
  }
}
