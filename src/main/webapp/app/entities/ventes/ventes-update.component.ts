import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, NgForm, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { IVentes, Ventes } from 'app/shared/model/ventes.model';
import { VentesService } from './ventes.service';
import { IClient } from 'app/shared/model/client.model';
import { ClientService } from 'app/entities/client/client.service';
import { OrderItemsComponent } from './order-items/order-items.component';
import * as moment from 'moment';
import { LignesVentesService } from '../lignes-ventes/lignes-ventes.service';
import { IProducts } from 'app/shared/model/products.model';
import { ProductsService } from '../products/products.service';

@Component({
  selector: 'jhi-ventes-update',
  templateUrl: './ventes-update.component.html',
})
export class VentesUpdateComponent implements OnInit {
  isSaving = false;
  clients: IClient[] = [];
  dateDp: any;
  dataarray=[];
  isValid = true;
  result: any;

  editForm = this.fb.group({
    id: [],
    montantInitial: [null, [Validators.required]],
    remise: [null, [Validators.required]],
    montantFinal: [null, [Validators.required]],
    date: [null, [Validators.required]],
    annule: [null, [Validators.required]],
    clientId: [],
  });

  constructor(
    protected ventesService: VentesService,
    protected clientService: ClientService,
    public service: VentesService,
    // private toastr: ToastrService,
    protected activatedRoute: ActivatedRoute,
    private dialog: MatDialog,
    private fb: FormBuilder,
    private currentRoute: ActivatedRoute,
    private lignes: LignesVentesService,
    private productService: ProductsService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ventes }) => {
      this.updateForm(ventes);
      this.clientService.query().subscribe((res: HttpResponse<IClient[]>) => (this.clients = res.body || []));
    });
    const orderID = this.currentRoute.snapshot.paramMap.get('id');
    if (orderID == null)
      this.resetForm();
    else {
      this.service.find(parseInt(orderID,10)).subscribe(res =>{
        this.service.formData = res.body!;
        this.service.orderItems = res.body!.lignesVentes!;
      });

    }
  }

  updateForm(ventes: IVentes): void {
    this.editForm.patchValue({
      id: ventes.id,
      montantInitial: ventes.montantInitial,
      remise: ventes.remise,
      montantFinal: ventes.montantFinal,
      date: ventes.date,
      annule: ventes.annule,
      clientId: ventes.clientId,
    });
  }
  
  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ventes = this.createFromForm();
    if (ventes.id !== undefined) {
      this.subscribeToSaveResponse(this.ventesService.update(ventes));
    } else {
      this.subscribeToSaveResponse(this.ventesService.create(ventes));
    }
  }

  private createFromForm(): IVentes {
    return {
      ...new Ventes(),
      id: this.editForm.get(['id'])!.value,
      montantInitial: this.editForm.get(['montantInitial'])!.value,
      remise: this.editForm.get(['remise'])!.value,
      montantFinal: this.editForm.get(['montantFinal'])!.value,
      date: this.editForm.get(['date'])!.value,
      annule: this.editForm.get(['annule'])!.value,
      clientId: this.editForm.get(['clientId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVentes>>): void {
    result.subscribe(
      (res: HttpResponse<IVentes>) => {
        this.service.orderItems!.forEach(element => {
          element.ventesId = res.body?.id;
          // eslint-disable-next-line no-console
          console.log(element.productsId);
          
          this.lignes.create(element).subscribe();   
          this.productService.find(element.productsId!).subscribe((resu: HttpResponse<IProducts>) => {
             this.productService.update({... resu.body, quantite: resu.body?.quantite! - element.quantite!}).subscribe();
          });
        });
        
        this.onSaveSuccess();
        this.onSaveError();
      }
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IClient): any {
    return item.id;
  }

  AddOrEditOrderItem(orderItemIndex: any, OrderID: any):void {
    const dialogConfig = new MatDialogConfig();
    dialogConfig.autoFocus = true;
    dialogConfig.disableClose = true;'"'
    dialogConfig.width = "50%";
    dialogConfig.data = { orderItemIndex, OrderID };
    this.dialog.open(OrderItemsComponent, dialogConfig).afterClosed().subscribe(() => {
      this.updateGrandTotal();
    });
  }

  updateGrandTotal(): void {
    this.service.formData.montantInitial = this.service.orderItems!.reduce((prev, curr) => {
      return prev + curr.prixTotal!;
    }, 0);
    this.service.formData.montantFinal = this.service.formData.montantInitial - this.service.formData.remise!;
  }

  onDeleteOrderItem(orderItemID: number, i: number): void {
    if (orderItemID != null)
      this.service.formData.DeletedOrderItemIDs += orderItemID + ",";
    this.service.orderItems!.splice(i, 1);
    this.updateGrandTotal();
  }

  validateForm(): any {
    this.isValid = true;
    if (this.service.formData.clientId === 0)
      this.isValid = false;
    else if (this.service.orderItems!.length === 0)
      this.isValid = false;
    return this.isValid;
  }

  resetForm(form?: NgForm): void {
    if (form === null)
      form!.resetForm();
    this.service.formData = {
      id: null!,
      clientId: 0,
      montantInitial: 0,
      montantFinal: 0,
      remise: 0,
      annule: false,
      date: moment(),
    };
    this.service.orderItems = [];
  }
  
}
