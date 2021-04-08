import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { EntreeStock, IEntreeStock } from 'app/shared/model/entree-stock.model';
import { ILigneEntreeStock } from 'app/shared/model/ligne-entree-stock.model';

type EntityResponseType = HttpResponse<IEntreeStock>;
type EntityArrayResponseType = HttpResponse<IEntreeStock[]>;

@Injectable({ providedIn: 'root' })
export class EntreeStockService {
  public resourceUrl = SERVER_API_URL + 'api/entree-stocks';
  formData!: EntreeStock;
  orderItems: ILigneEntreeStock[] = [];

  constructor(protected http: HttpClient) {}

  create(entreeStock: IEntreeStock): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(entreeStock);
    return this.http
      .post<IEntreeStock>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(entreeStock: IEntreeStock): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(entreeStock);
    return this.http
      .put<IEntreeStock>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEntreeStock>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEntreeStock[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(entreeStock: IEntreeStock): IEntreeStock {
    const copy: IEntreeStock = Object.assign({}, entreeStock, {
      date: entreeStock.date && entreeStock.date.isValid() ? entreeStock.date.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? moment(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((entreeStock: IEntreeStock) => {
        entreeStock.date = entreeStock.date ? moment(entreeStock.date) : undefined;
      });
    }
    return res;
  }
}
