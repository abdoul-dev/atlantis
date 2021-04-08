import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AtlantisPoissonnerieSharedModule } from 'app/shared/shared.module';
import { DepensesComponent } from './depenses.component';
import { DepensesDetailComponent } from './depenses-detail.component';
import { DepensesUpdateComponent } from './depenses-update.component';
import { DepensesDeleteDialogComponent } from './depenses-delete-dialog.component';
import { depensesRoute } from './depenses.route';

@NgModule({
  imports: [AtlantisPoissonnerieSharedModule, RouterModule.forChild(depensesRoute)],
  declarations: [DepensesComponent, DepensesDetailComponent, DepensesUpdateComponent, DepensesDeleteDialogComponent],
  entryComponents: [DepensesDeleteDialogComponent],
})
export class AtlantisPoissonnerieDepensesModule {}
