import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ILignesVentes } from 'app/shared/model/lignes-ventes.model';

type EntityResponseType = HttpResponse<ILignesVentes>;
type EntityArrayResponseType = HttpResponse<ILignesVentes[]>;

@Injectable({ providedIn: 'root' })
export class LignesVentesService {
  public resourceUrl = SERVER_API_URL + 'api/lignes-ventes';

  constructor(protected http: HttpClient) {}

  create(lignesVentes: ILignesVentes): Observable<EntityResponseType> {
    return this.http.post<ILignesVentes>(this.resourceUrl, lignesVentes, { observe: 'response' });
  }

  update(lignesVentes: ILignesVentes): Observable<EntityResponseType> {
    return this.http.put<ILignesVentes>(this.resourceUrl, lignesVentes, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILignesVentes>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILignesVentes[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
