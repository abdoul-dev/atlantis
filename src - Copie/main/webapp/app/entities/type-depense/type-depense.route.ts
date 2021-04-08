import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ITypeDepense, TypeDepense } from 'app/shared/model/type-depense.model';
import { TypeDepenseService } from './type-depense.service';
import { TypeDepenseComponent } from './type-depense.component';
import { TypeDepenseDetailComponent } from './type-depense-detail.component';
import { TypeDepenseUpdateComponent } from './type-depense-update.component';

@Injectable({ providedIn: 'root' })
export class TypeDepenseResolve implements Resolve<ITypeDepense> {
  constructor(private service: TypeDepenseService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITypeDepense> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((typeDepense: HttpResponse<TypeDepense>) => {
          if (typeDepense.body) {
            return of(typeDepense.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TypeDepense());
  }
}

export const typeDepenseRoute: Routes = [
  {
    path: '',
    component: TypeDepenseComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'TypeDepenses',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TypeDepenseDetailComponent,
    resolve: {
      typeDepense: TypeDepenseResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'TypeDepenses',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TypeDepenseUpdateComponent,
    resolve: {
      typeDepense: TypeDepenseResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'TypeDepenses',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TypeDepenseUpdateComponent,
    resolve: {
      typeDepense: TypeDepenseResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'TypeDepenses',
    },
    canActivate: [UserRouteAccessService],
  },
];
