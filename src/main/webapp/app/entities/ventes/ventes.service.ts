import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IVentes, Ventes } from 'app/shared/model/ventes.model';
import { LignesVentes } from 'app/shared/model/lignes-ventes.model';
import { Moment } from 'moment';

type EntityResponseType = HttpResponse<IVentes>;
type EntityArrayResponseType = HttpResponse<IVentes[]>;

@Injectable({ providedIn: 'root' })
export class VentesService {
  public resourceUrl = SERVER_API_URL + 'api/ventes';
  formData!: Ventes; 
  orderItems?: LignesVentes[] = [];

  constructor(protected http: HttpClient) {}

  create(ventes: IVentes): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ventes);
    return this.http
      .post<IVentes>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(ventes: IVentes): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ventes);
    return this.http
      .put<IVentes>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVentes>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  venteDuJour(today: Moment): Observable<HttpResponse<number>> {
    return this.http.get<number>(`${this.resourceUrl +'/date'}/${today.format(DATE_FORMAT)}`,{ observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVentes[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(ventes: IVentes): IVentes {
    const copy: IVentes = Object.assign({}, ventes, {
      date: ventes.date && ventes.date.isValid() ? ventes.date.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((ventes: IVentes) => {
        ventes.date = ventes.date ? moment(ventes.date) : undefined;
      });
    }
    return res;
  }


  downloadPdf(date: Moment): Observable<Blob> {
    return this.http.get(`${this.resourceUrl +'/genererPDF/date'}/${date.format(DATE_FORMAT)}`,{ responseType: 'blob' });
  }

  findAllVentes() : Observable<EntityArrayResponseType>{
    return this.http.get<IVentes[]>(this.resourceUrl+'/all', { observe: 'response' });
  }

}
