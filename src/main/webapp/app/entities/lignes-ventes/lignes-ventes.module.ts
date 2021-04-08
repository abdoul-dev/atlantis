import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AtlantisPoissonnerieSharedModule } from 'app/shared/shared.module';
import { LignesVentesComponent } from './lignes-ventes.component';
import { LignesVentesDetailComponent } from './lignes-ventes-detail.component';
import { LignesVentesUpdateComponent } from './lignes-ventes-update.component';
import { LignesVentesDeleteDialogComponent } from './lignes-ventes-delete-dialog.component';
import { lignesVentesRoute } from './lignes-ventes.route';

@NgModule({
  imports: [AtlantisPoissonnerieSharedModule, RouterModule.forChild(lignesVentesRoute)],
  declarations: [LignesVentesComponent, LignesVentesDetailComponent, LignesVentesUpdateComponent, LignesVentesDeleteDialogComponent],
  entryComponents: [LignesVentesDeleteDialogComponent],
})
export class AtlantisPoissonnerieLignesVentesModule {}
