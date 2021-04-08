import { ILigneEntreeStock } from 'app/shared/model/ligne-entree-stock.model';
import { ILignesVentes } from 'app/shared/model/lignes-ventes.model';
import { ILignesReservation } from 'app/shared/model/lignes-reservation.model';

export interface IProducts {
  id?: number;
  libelle?: string;
  prixVente?: number;
  isDisabled?: boolean;
  comments?: string;
  ligneStocks?: ILigneEntreeStock[];
  ligneVentes?: ILignesVentes[];
  ligneReservations?: ILignesReservation[];
  typeProduitLibelle?: string;
  typeProduitId?: number;
}
 
export class Products implements IProducts {
  constructor(
    public id?: number,
    public libelle?: string,
    public prixVente?: number,
    public isDisabled?: boolean,
    public comments?: string,
    public ligneStocks?: ILigneEntreeStock[],
    public ligneVentes?: ILignesVentes[],
    public ligneReservations?: ILignesReservation[],
    public typeProduitLibelle?: string,
    public typeProduitId?: number
  ) {
    this.isDisabled = this.isDisabled || false;
  }
}
