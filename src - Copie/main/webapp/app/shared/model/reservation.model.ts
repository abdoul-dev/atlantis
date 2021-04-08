import { Moment } from 'moment';

export interface IReservation {
  id?: number;
  revervePour?: Moment;
  date?: Moment;
  annule?: boolean;
  clientFullName?: string;
  clientId?: number;
}

export class Reservation implements IReservation {
  constructor(
    public id?: number,
    public revervePour?: Moment,
    public date?: Moment,
    public annule?: boolean,
    public clientFullName?: string,
    public clientId?: number
  ) {
    this.annule = this.annule || false;
  }
}
