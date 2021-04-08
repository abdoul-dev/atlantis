import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AtlantisPoissonnerieSharedModule } from 'app/shared/shared.module';

import { AuditsComponent } from './audits.component';

import { auditsRoute } from './audits.route';

@NgModule({
  imports: [AtlantisPoissonnerieSharedModule, RouterModule.forChild([auditsRoute])],
  declarations: [AuditsComponent],
})
export class AuditsModule {}
