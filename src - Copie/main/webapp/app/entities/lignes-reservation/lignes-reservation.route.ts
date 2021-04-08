import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ILignesReservation, LignesReservation } from 'app/shared/model/lignes-reservation.model';
import { LignesReservationService } from './lignes-reservation.service';
import { LignesReservationComponent } from './lignes-reservation.component';
import { LignesReservationDetailComponent } from './lignes-reservation-detail.component';
import { LignesReservationUpdateComponent } from './lignes-reservation-update.component';

@Injectable({ providedIn: 'root' })
export class LignesReservationResolve implements Resolve<ILignesReservation> {
  constructor(private service: LignesReservationService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILignesReservation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((lignesReservation: HttpResponse<LignesReservation>) => {
          if (lignesReservation.body) {
            return of(lignesReservation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LignesReservation());
  }
}

export const lignesReservationRoute: Routes = [
  {
    path: '',
    component: LignesReservationComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'LignesReservations',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LignesReservationDetailComponent,
    resolve: {
      lignesReservation: LignesReservationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'LignesReservations',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LignesReservationUpdateComponent,
    resolve: {
      lignesReservation: LignesReservationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'LignesReservations',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LignesReservationUpdateComponent,
    resolve: {
      lignesReservation: LignesReservationResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'LignesReservations',
    },
    canActivate: [UserRouteAccessService],
  },
];
