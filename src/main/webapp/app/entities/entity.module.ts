import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'client',
        loadChildren: () => import('./client/client.module').then(m => m.AtlantisPoissonnerieClientModule),
      },
      {
        path: 'products',
        loadChildren: () => import('./products/products.module').then(m => m.AtlantisPoissonnerieProductsModule),
      },
      {
        path: 'type-produit',
        loadChildren: () => import('./type-produit/type-produit.module').then(m => m.AtlantisPoissonnerieTypeProduitModule),
      },
      {
        path: 'entree-stock',
        loadChildren: () => import('./entree-stock/entree-stock.module').then(m => m.AtlantisPoissonnerieEntreeStockModule),
      },
      {
        path: 'ventes',
        loadChildren: () => import('./ventes/ventes.module').then(m => m.AtlantisPoissonnerieVentesModule),
      },
      {
        path: 'depenses',
        loadChildren: () => import('./depenses/depenses.module').then(m => m.AtlantisPoissonnerieDepensesModule),
      },
      {
        path: 'type-depense',
        loadChildren: () => import('./type-depense/type-depense.module').then(m => m.AtlantisPoissonnerieTypeDepenseModule),
      },
      {
        path: 'reservation',
        loadChildren: () => import('./reservation/reservation.module').then(m => m.AtlantisPoissonnerieReservationModule),
      },
      {
        path: 'fournisseur',
        loadChildren: () => import('./fournisseur/fournisseur.module').then(m => m.AtlantisPoissonnerieFournisseurModule),
      },
      {
        path: 'lignes-ventes',
        loadChildren: () => import('./lignes-ventes/lignes-ventes.module').then(m => m.AtlantisPoissonnerieLignesVentesModule),
      },
      {
        path: 'ligne-entree-stock',
        loadChildren: () =>
          import('./ligne-entree-stock/ligne-entree-stock.module').then(m => m.AtlantisPoissonnerieLigneEntreeStockModule),
      },
      {
        path: 'lignes-reservation',
        loadChildren: () =>
          import('./lignes-reservation/lignes-reservation.module').then(m => m.AtlantisPoissonnerieLignesReservationModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class AtlantisPoissonnerieEntityModule {}
