import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IVentes, Ventes } from 'app/shared/model/ventes.model';
import { VentesService } from './ventes.service';
import { VentesComponent } from './ventes.component';
import { VentesDetailComponent } from './ventes-detail.component';
import { VentesUpdateComponent } from './ventes-update.component';

@Injectable({ providedIn: 'root' })
export class VentesResolve implements Resolve<IVentes> {
  constructor(private service: VentesService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVentes> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((ventes: HttpResponse<Ventes>) => {
          if (ventes.body) {
            return of(ventes.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Ventes());
  }
}

export const ventesRoute: Routes = [
  {
    path: '',
    component: VentesComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'Ventes',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VentesDetailComponent,
    resolve: {
      ventes: VentesResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Ventes',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VentesUpdateComponent,
    resolve: {
      ventes: VentesResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Ventes',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VentesUpdateComponent,
    resolve: {
      ventes: VentesResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Ventes',
    },
    canActivate: [UserRouteAccessService],
  },
];
