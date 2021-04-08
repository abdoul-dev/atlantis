import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ILignesReservation } from 'app/shared/model/lignes-reservation.model';

type EntityResponseType = HttpResponse<ILignesReservation>;
type EntityArrayResponseType = HttpResponse<ILignesReservation[]>;

@Injectable({ providedIn: 'root' })
export class LignesReservationService {
  public resourceUrl = SERVER_API_URL + 'api/lignes-reservations';

  constructor(protected http: HttpClient) {}

  create(lignesReservation: ILignesReservation): Observable<EntityResponseType> {
    return this.http.post<ILignesReservation>(this.resourceUrl, lignesReservation, { observe: 'response' });
  }

  update(lignesReservation: ILignesReservation): Observable<EntityResponseType> {
    return this.http.put<ILignesReservation>(this.resourceUrl, lignesReservation, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILignesReservation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILignesReservation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
