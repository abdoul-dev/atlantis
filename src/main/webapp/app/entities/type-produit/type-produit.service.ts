import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITypeProduit } from 'app/shared/model/type-produit.model';

type EntityResponseType = HttpResponse<ITypeProduit>;
type EntityArrayResponseType = HttpResponse<ITypeProduit[]>;

@Injectable({ providedIn: 'root' })
export class TypeProduitService {
  public resourceUrl = SERVER_API_URL + 'api/type-produits';

  constructor(protected http: HttpClient) {}

  create(typeProduit: ITypeProduit): Observable<EntityResponseType> {
    return this.http.post<ITypeProduit>(this.resourceUrl, typeProduit, { observe: 'response' });
  }

  update(typeProduit: ITypeProduit): Observable<EntityResponseType> {
    return this.http.put<ITypeProduit>(this.resourceUrl, typeProduit, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypeProduit>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeProduit[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
