export interface ILignesReservation {
  id?: number;
  quantite?: number;
  productsLibelle?: string;
  productsId?: number;
}

export class LignesReservation implements ILignesReservation {
  constructor(public id?: number, public quantite?: number, public productsLibelle?: string, public productsId?: number) {}
}
