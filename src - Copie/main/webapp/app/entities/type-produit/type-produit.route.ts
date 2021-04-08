import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ITypeProduit, TypeProduit } from 'app/shared/model/type-produit.model';
import { TypeProduitService } from './type-produit.service';
import { TypeProduitComponent } from './type-produit.component';
import { TypeProduitDetailComponent } from './type-produit-detail.component';
import { TypeProduitUpdateComponent } from './type-produit-update.component';

@Injectable({ providedIn: 'root' })
export class TypeProduitResolve implements Resolve<ITypeProduit> {
  constructor(private service: TypeProduitService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITypeProduit> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((typeProduit: HttpResponse<TypeProduit>) => {
          if (typeProduit.body) {
            return of(typeProduit.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TypeProduit());
  }
}

export const typeProduitRoute: Routes = [
  {
    path: '',
    component: TypeProduitComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'TypeProduits',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TypeProduitDetailComponent,
    resolve: {
      typeProduit: TypeProduitResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'TypeProduits',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TypeProduitUpdateComponent,
    resolve: {
      typeProduit: TypeProduitResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'TypeProduits',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TypeProduitUpdateComponent,
    resolve: {
      typeProduit: TypeProduitResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'TypeProduits',
    },
    canActivate: [UserRouteAccessService],
  },
];
