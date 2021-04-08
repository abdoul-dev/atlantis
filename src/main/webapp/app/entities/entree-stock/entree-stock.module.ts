import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AtlantisPoissonnerieSharedModule } from 'app/shared/shared.module';
import { EntreeStockComponent } from './entree-stock.component';
import { EntreeStockDetailComponent } from './entree-stock-detail.component';
import { EntreeStockUpdateComponent } from './entree-stock-update.component';
import { EntreeStockDeleteDialogComponent } from './entree-stock-delete-dialog.component';
import { OrderItemsComponent} from './order-items/order-items.component'
import { entreeStockRoute } from './entree-stock.route';

@NgModule({
  imports: [AtlantisPoissonnerieSharedModule, RouterModule.forChild(entreeStockRoute)],
  declarations: [EntreeStockComponent, EntreeStockDetailComponent, EntreeStockUpdateComponent, EntreeStockDeleteDialogComponent, OrderItemsComponent],
  entryComponents: [EntreeStockDeleteDialogComponent],
})
export class AtlantisPoissonnerieEntreeStockModule {}
