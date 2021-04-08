import { IDepenses } from 'app/shared/model/depenses.model';

export interface ITypeDepense {
  id?: number;
  libelle?: string;
  isDisabled?: boolean;
  depenses?: IDepenses[];
}

export class TypeDepense implements ITypeDepense {
  constructor(public id?: number, public libelle?: string, public isDisabled?: boolean, public depenses?: IDepenses[]) {
    this.isDisabled = this.isDisabled || false;
  }
}
