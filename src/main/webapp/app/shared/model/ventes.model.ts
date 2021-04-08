import { Moment } from 'moment';
import { ILignesVentes } from 'app/shared/model/lignes-ventes.model';

export interface IVentes {
  id?: number;
  montantInitial?: number;
  remise?: number;
  montantFinal?: number;
  date?: Moment;
  annule?: boolean;
  lignesVentes?: ILignesVentes[];
  clientFullName?: string;
  clientId?: number;
}

export class Ventes implements IVentes {
  DeletedOrderItemIDs?: string;
  
  constructor(
    public id?: number,
    public montantInitial?: number,
    public remise?: number,
    public montantFinal?: number | undefined,
    public date?: Moment,
    public annule?: boolean,
    public lignesVentes?: ILignesVentes[],
    public clientFullName?: string,
    public clientId?: number,
  ) {
    this.annule = this.annule || false;
  }
}
