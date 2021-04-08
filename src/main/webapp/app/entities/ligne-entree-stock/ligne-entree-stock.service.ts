import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ILigneEntreeStock } from 'app/shared/model/ligne-entree-stock.model';

type EntityResponseType = HttpResponse<ILigneEntreeStock>;
type EntityArrayResponseType = HttpResponse<ILigneEntreeStock[]>;

@Injectable({ providedIn: 'root' })
export class LigneEntreeStockService {
  public resourceUrl = SERVER_API_URL + 'api/ligne-entree-stocks';

  constructor(protected http: HttpClient) {}

  create(ligneEntreeStock: ILigneEntreeStock): Observable<EntityResponseType> {
    return this.http.post<ILigneEntreeStock>(this.resourceUrl, ligneEntreeStock, { observe: 'response' });
  }

  update(ligneEntreeStock: ILigneEntreeStock): Observable<EntityResponseType> {
    return this.http.put<ILigneEntreeStock>(this.resourceUrl, ligneEntreeStock, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILigneEntreeStock>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILigneEntreeStock[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
