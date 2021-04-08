import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { AtlantisPoissonnerieSharedModule } from 'app/shared/shared.module';
import { AtlantisPoissonnerieCoreModule } from 'app/core/core.module';
import { AtlantisPoissonnerieAppRoutingModule } from './app-routing.module';
import { AtlantisPoissonnerieHomeModule } from './home/home.module';
import { AtlantisPoissonnerieEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';
import { MatDialogModule} from '@angular/material/dialog';
import { ToastrModule } from 'ngx-toastr';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

@NgModule({
  imports: [
    BrowserModule,
    AtlantisPoissonnerieSharedModule,
    AtlantisPoissonnerieCoreModule,
    AtlantisPoissonnerieHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    AtlantisPoissonnerieEntityModule,
    AtlantisPoissonnerieAppRoutingModule,
    MatDialogModule,
    ToastrModule.forRoot(),
    BrowserAnimationsModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent],
})
export class AtlantisPoissonnerieAppModule {}
