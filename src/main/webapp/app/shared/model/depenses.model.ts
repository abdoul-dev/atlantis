import { Moment } from 'moment';

export interface IDepenses {
  id?: number;
  montant?: number;
  comments?: string;
  date?: Moment;
  annule?: boolean;
  typeDepenseLibelle?: string;
  typeDepenseId?: number;
}

export class Depenses implements IDepenses {
  constructor(
    public id?: number,
    public montant?: number,
    public comments?: string,
    public date?: Moment,
    public annule?: boolean,
    public typeDepenseLibelle?: string,
    public typeDepenseId?: number
  ) {
    this.annule = this.annule || false;
  }
}
