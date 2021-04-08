import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AtlantisPoissonnerieSharedModule } from 'app/shared/shared.module';
import { LignesReservationComponent } from './lignes-reservation.component';
import { LignesReservationDetailComponent } from './lignes-reservation-detail.component';
import { LignesReservationUpdateComponent } from './lignes-reservation-update.component';
import { LignesReservationDeleteDialogComponent } from './lignes-reservation-delete-dialog.component';
import { lignesReservationRoute } from './lignes-reservation.route';

@NgModule({
  imports: [AtlantisPoissonnerieSharedModule, RouterModule.forChild(lignesReservationRoute)],
  declarations: [
    LignesReservationComponent,
    LignesReservationDetailComponent,
    LignesReservationUpdateComponent,
    LignesReservationDeleteDialogComponent,
  ],
  entryComponents: [LignesReservationDeleteDialogComponent],
})
export class AtlantisPoissonnerieLignesReservationModule {}
