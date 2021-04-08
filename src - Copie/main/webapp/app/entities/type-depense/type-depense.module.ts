import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AtlantisPoissonnerieSharedModule } from 'app/shared/shared.module';
import { TypeDepenseComponent } from './type-depense.component';
import { TypeDepenseDetailComponent } from './type-depense-detail.component';
import { TypeDepenseUpdateComponent } from './type-depense-update.component';
import { TypeDepenseDeleteDialogComponent } from './type-depense-delete-dialog.component';
import { typeDepenseRoute } from './type-depense.route';

@NgModule({
  imports: [AtlantisPoissonnerieSharedModule, RouterModule.forChild(typeDepenseRoute)],
  declarations: [TypeDepenseComponent, TypeDepenseDetailComponent, TypeDepenseUpdateComponent, TypeDepenseDeleteDialogComponent],
  entryComponents: [TypeDepenseDeleteDialogComponent],
})
export class AtlantisPoissonnerieTypeDepenseModule {}
