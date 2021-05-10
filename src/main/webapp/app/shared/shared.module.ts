import { NgModule } from '@angular/core';
import { AtlantisPoissonnerieSharedLibsModule } from './shared-libs.module';
import { AlertComponent } from './alert/alert.component';
import { AlertErrorComponent } from './alert/alert-error.component';
import { LoginModalComponent } from './login/login.component';
import { HasAnyAuthorityDirective } from './auth/has-any-authority.directive';
import { AtlantisPoissonneriePrimeNgModule} from './primeng.module';

@NgModule({
  imports: [AtlantisPoissonnerieSharedLibsModule, AtlantisPoissonneriePrimeNgModule],
  declarations: [AlertComponent, AlertErrorComponent, LoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [LoginModalComponent],
  exports: [AtlantisPoissonnerieSharedLibsModule, AlertComponent, AlertErrorComponent, LoginModalComponent, HasAnyAuthorityDirective, AtlantisPoissonneriePrimeNgModule],
})
export class AtlantisPoissonnerieSharedModule {}
