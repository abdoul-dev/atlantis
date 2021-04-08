import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AtlantisPoissonnerieSharedModule } from 'app/shared/shared.module';
import { LigneEntreeStockComponent } from './ligne-entree-stock.component';
import { LigneEntreeStockDetailComponent } from './ligne-entree-stock-detail.component';
import { LigneEntreeStockUpdateComponent } from './ligne-entree-stock-update.component';
import { LigneEntreeStockDeleteDialogComponent } from './ligne-entree-stock-delete-dialog.component';
import { ligneEntreeStockRoute } from './ligne-entree-stock.route';

@NgModule({
  imports: [AtlantisPoissonnerieSharedModule, RouterModule.forChild(ligneEntreeStockRoute)],
  declarations: [
    LigneEntreeStockComponent,
    LigneEntreeStockDetailComponent,
    LigneEntreeStockUpdateComponent,
    LigneEntreeStockDeleteDialogComponent,
  ],
  entryComponents: [LigneEntreeStockDeleteDialogComponent],
})
export class AtlantisPoissonnerieLigneEntreeStockModule {}
