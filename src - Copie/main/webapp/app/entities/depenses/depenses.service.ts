import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IDepenses } from 'app/shared/model/depenses.model';

type EntityResponseType = HttpResponse<IDepenses>;
type EntityArrayResponseType = HttpResponse<IDepenses[]>;

@Injectable({ providedIn: 'root' })
export class DepensesService {
  public resourceUrl = SERVER_API_URL + 'api/depenses';

  constructor(protected http: HttpClient) {}

  create(depenses: IDepenses): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(depenses);
    return this.http
      .post<IDepenses>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(depenses: IDepenses): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(depenses);
    return this.http
      .put<IDepenses>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDepenses>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDepenses[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(depenses: IDepenses): IDepenses {
    const copy: IDepenses = Object.assign({}, depenses, {
      date: depenses.date && depenses.date.isValid() ? depenses.date.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((depenses: IDepenses) => {
        depenses.date = depenses.date ? moment(depenses.date) : undefined;
      });
    }
    return res;
  }
}
