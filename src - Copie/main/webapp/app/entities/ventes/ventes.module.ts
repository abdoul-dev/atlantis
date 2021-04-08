import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AtlantisPoissonnerieSharedModule } from 'app/shared/shared.module';
import { VentesComponent } from './ventes.component';
import { VentesDetailComponent } from './ventes-detail.component';
import { VentesUpdateComponent } from './ventes-update.component';
import { VentesDeleteDialogComponent } from './ventes-delete-dialog.component';
import { OrderItemsComponent} from './order-items/order-items.component'
import { ventesRoute } from './ventes.route';

@NgModule({
  imports: [AtlantisPoissonnerieSharedModule, RouterModule.forChild(ventesRoute)],
  declarations: [VentesComponent, VentesDetailComponent, VentesUpdateComponent, VentesDeleteDialogComponent, OrderItemsComponent],
  entryComponents: [VentesDeleteDialogComponent],
})
export class AtlantisPoissonnerieVentesModule {}
