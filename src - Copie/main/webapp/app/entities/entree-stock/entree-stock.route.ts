import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IEntreeStock, EntreeStock } from 'app/shared/model/entree-stock.model';
import { EntreeStockService } from './entree-stock.service';
import { EntreeStockComponent } from './entree-stock.component';
import { EntreeStockDetailComponent } from './entree-stock-detail.component';
import { EntreeStockUpdateComponent } from './entree-stock-update.component';

@Injectable({ providedIn: 'root' })
export class EntreeStockResolve implements Resolve<IEntreeStock> {
  constructor(private service: EntreeStockService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEntreeStock> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((entreeStock: HttpResponse<EntreeStock>) => {
          if (entreeStock.body) {
            return of(entreeStock.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EntreeStock());
  }
}

export const entreeStockRoute: Routes = [
  {
    path: '',
    component: EntreeStockComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'EntreeStocks',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EntreeStockDetailComponent,
    resolve: {
      entreeStock: EntreeStockResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'EntreeStocks',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EntreeStockUpdateComponent,
    resolve: {
      entreeStock: EntreeStockResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'EntreeStocks',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EntreeStockUpdateComponent,
    resolve: {
      entreeStock: EntreeStockResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'EntreeStocks',
    },
    canActivate: [UserRouteAccessService],
  },
];
