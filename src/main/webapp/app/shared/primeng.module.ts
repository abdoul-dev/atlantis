import { NgModule } from '@angular/core';
import { CheckboxModule } from 'primeng-lts/checkbox';
import { ConfirmDialogModule } from 'primeng-lts/confirmdialog';
import { DropdownModule } from 'primeng-lts/dropdown';
import { FileUploadModule } from 'primeng-lts/fileupload';
import { InputTextModule } from 'primeng-lts/inputtext';
import { MultiSelectModule } from 'primeng-lts/multiselect';
import { OverlayPanelModule } from 'primeng-lts/overlaypanel';
import { TableModule } from 'primeng-lts/table';
import { ToolbarModule } from 'primeng-lts/toolbar';
import { ProgressBarModule } from 'primeng-lts/progressbar';
import { PanelModule } from 'primeng-lts/panel';
import { InputNumberModule } from 'primeng-lts/inputnumber';
import { RadioButtonModule } from 'primeng-lts/radiobutton';
import { TabViewModule } from 'primeng-lts/tabview';
import { AccordionModule } from 'primeng-lts/accordion';
import { TooltipModule } from 'primeng-lts/tooltip';
import { PaginatorModule } from 'primeng-lts/paginator';
import { ButtonModule } from 'primeng-lts/button';
import { ToastModule} from 'primeng-lts/toast';
import {DialogModule} from 'primeng-lts/dialog';


@NgModule({
  imports: [
    ButtonModule,
    TableModule,
    ToastModule,
    InputTextModule,
    DropdownModule,
    DialogModule,
    ToolbarModule,
    MultiSelectModule,
    OverlayPanelModule,
    CheckboxModule,
    ConfirmDialogModule,
    FileUploadModule,
    ProgressBarModule,
    PanelModule,
    InputNumberModule,
    RadioButtonModule,
    TabViewModule,
    AccordionModule,
    TooltipModule,
    PaginatorModule,
  ],
  exports: [
    ButtonModule,
    TableModule,
    ToastModule,
    InputTextModule,
    DropdownModule,
    DialogModule,
    ToolbarModule,
    MultiSelectModule,
    OverlayPanelModule,
    ConfirmDialogModule,
    FileUploadModule,
    ProgressBarModule,
    PanelModule,
    InputNumberModule,
    RadioButtonModule,
    TabViewModule,
    AccordionModule,
    TooltipModule,
    PaginatorModule,
  ],
})
export class AtlantisPoissonneriePrimeNgModule {}
