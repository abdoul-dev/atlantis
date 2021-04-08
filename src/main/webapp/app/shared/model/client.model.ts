import { IVentes } from 'app/shared/model/ventes.model';
import { IReservation } from 'app/shared/model/reservation.model';

export interface IClient {
  id?: number;
  fullName?: string;
  address?: string;
  phone?: string;
  ventes?: IVentes[];
  reservations?: IReservation[];
}

export class Client implements IClient {
  constructor(
    public id?: number,
    public fullName?: string,
    public address?: string,
    public phone?: string,
    public ventes?: IVentes[],
    public reservations?: IReservation[]
  ) {}
}
