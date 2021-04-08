import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITypeDepense } from 'app/shared/model/type-depense.model';

type EntityResponseType = HttpResponse<ITypeDepense>;
type EntityArrayResponseType = HttpResponse<ITypeDepense[]>;

@Injectable({ providedIn: 'root' })
export class TypeDepenseService {
  public resourceUrl = SERVER_API_URL + 'api/type-depenses';

  constructor(protected http: HttpClient) {}

  create(typeDepense: ITypeDepense): Observable<EntityResponseType> {
    return this.http.post<ITypeDepense>(this.resourceUrl, typeDepense, { observe: 'response' });
  }

  update(typeDepense: ITypeDepense): Observable<EntityResponseType> {
    return this.http.put<ITypeDepense>(this.resourceUrl, typeDepense, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypeDepense>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeDepense[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
