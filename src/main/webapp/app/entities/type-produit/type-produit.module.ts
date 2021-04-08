import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AtlantisPoissonnerieSharedModule } from 'app/shared/shared.module';
import { TypeProduitComponent } from './type-produit.component';
import { TypeProduitDetailComponent } from './type-produit-detail.component';
import { TypeProduitUpdateComponent } from './type-produit-update.component';
import { TypeProduitDeleteDialogComponent } from './type-produit-delete-dialog.component';
import { typeProduitRoute } from './type-produit.route';

@NgModule({
  imports: [AtlantisPoissonnerieSharedModule, RouterModule.forChild(typeProduitRoute)],
  declarations: [TypeProduitComponent, TypeProduitDetailComponent, TypeProduitUpdateComponent, TypeProduitDeleteDialogComponent],
  entryComponents: [TypeProduitDeleteDialogComponent],
})
export class AtlantisPoissonnerieTypeProduitModule {}
