import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IDepenses, Depenses } from 'app/shared/model/depenses.model';
import { DepensesService } from './depenses.service';
import { DepensesComponent } from './depenses.component';
import { DepensesDetailComponent } from './depenses-detail.component';
import { DepensesUpdateComponent } from './depenses-update.component';

@Injectable({ providedIn: 'root' })
export class DepensesResolve implements Resolve<IDepenses> {
  constructor(private service: DepensesService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDepenses> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((depenses: HttpResponse<Depenses>) => {
          if (depenses.body) {
            return of(depenses.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Depenses());
  }
}

export const depensesRoute: Routes = [
  {
    path: '',
    component: DepensesComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'Depenses',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DepensesDetailComponent,
    resolve: {
      depenses: DepensesResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Depenses',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DepensesUpdateComponent,
    resolve: {
      depenses: DepensesResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Depenses',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DepensesUpdateComponent,
    resolve: {
      depenses: DepensesResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'Depenses',
    },
    canActivate: [UserRouteAccessService],
  },
];
