import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, NgForm, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IEntreeStock, EntreeStock } from 'app/shared/model/entree-stock.model';
import { EntreeStockService } from './entree-stock.service';
import { IFournisseur } from 'app/shared/model/fournisseur.model';
import { FournisseurService } from 'app/entities/fournisseur/fournisseur.service';
import * as moment from 'moment';
import { MatDialog } from '@angular/material/dialog';
import { OrderItemsComponent } from './order-items/order-items.component';
import { MatDialogConfig } from '@angular/material/dialog';
import { LigneEntreeStockService } from '../ligne-entree-stock/ligne-entree-stock.service';
import { ProductsService } from '../products/products.service';
import { IProducts } from 'app/shared/model/products.model';

@Component({
  selector: 'jhi-entree-stock-update',
  templateUrl: './entree-stock-update.component.html',
})
export class EntreeStockUpdateComponent implements OnInit {
  isSaving = false;
  fournisseurs: IFournisseur[] = [];
  dateDp: any;
  isValid = true;

  editForm = this.fb.group({
    id: [],
    montant: [null, [Validators.required]],
    annule: [null, [Validators.required]],
    date: [null, [Validators.required]],
    fournisseurId: [],
  });

  constructor(
    public entreeStockService: EntreeStockService,
    protected productService: ProductsService,
    protected fournisseurService: FournisseurService,
    protected activatedRoute: ActivatedRoute,
    private dialog: MatDialog,
    private currentRoute: ActivatedRoute,
    private fb: FormBuilder,
    private lignes: LigneEntreeStockService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ entreeStock }) => {
      this.updateForm(entreeStock);

      this.fournisseurService.query().subscribe((res: HttpResponse<IFournisseur[]>) => (this.fournisseurs = res.body || []));
    });
    const orderID = this.currentRoute.snapshot.paramMap.get('id');
    if (orderID == null)
      this.resetForm();
    else {
      this.entreeStockService.find(parseInt(orderID,10)).subscribe(res =>{
        this.entreeStockService.formData = res.body!;
        this.entreeStockService.orderItems = res.body!.ligneEntreeStocks!;
      });
    }
  }

  updateForm(entreeStock: IEntreeStock): void {
    this.editForm.patchValue({
      id: entreeStock.id,
      montant: entreeStock.montant,
      annule: entreeStock.annule,
      date: entreeStock.date,
      fournisseurId: entreeStock.fournisseurId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const entreeStock = this.createFromForm();
    if (entreeStock.id !== undefined) {
      this.subscribeToSaveResponse(this.entreeStockService.update(entreeStock));
    } else {
      this.subscribeToSaveResponse(this.entreeStockService.create(entreeStock));
    }
  }

  private createFromForm(): IEntreeStock {
    return {
      ...new EntreeStock(),
      id: this.editForm.get(['id'])!.value,
      montant: this.editForm.get(['montant'])!.value,
      annule: this.editForm.get(['annule'])!.value,
      date: this.editForm.get(['date'])!.value,
      fournisseurId: this.editForm.get(['fournisseurId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEntreeStock>>): void {
    result.subscribe(
      (res: HttpResponse<IEntreeStock>)=>{
        if(this.entreeStockService.orderItems != null)
        {
            this.entreeStockService.orderItems.forEach(element => {
            element.entreestockId = res.body?.id;
            this.lignes.create(element).subscribe();
            this.productService.find(element.productsId!).subscribe((prod: HttpResponse<IProducts>)=>{
              this.productService.update({... prod.body, quantite: prod.body?.quantite! + element.quantite!}).subscribe();          
            })         
          });
        }
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

  trackById(index: number, item: IFournisseur): any {
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
    this.entreeStockService.formData.montant = this.entreeStockService.orderItems.reduce((prev, curr) => {
      return prev + curr.prixTotal!;
    }, 0);
  }

  onDeleteOrderItem(orderItemID: number, i: number): void {
    if (orderItemID != null)
      this.entreeStockService.formData.DeletedOrderItemIDs += orderItemID + ",";
    this.entreeStockService.orderItems.splice(i, 1);
    this.updateGrandTotal();
  }

  validateForm(): any {
    this.isValid = true;
    if (this.entreeStockService.formData.fournisseurId === 0)
      this.isValid = false;
    else if (this.entreeStockService.orderItems.length === 0)
      this.isValid = false;
    return this.isValid;
  }

  resetForm(form?: NgForm): void {
    if (form === null)
      form!.resetForm();
    this.entreeStockService.formData = {
      id: null!,
      fournisseurId: 0,
      montant: 0,
      annule: true,
      date: moment(),
    };
    this.entreeStockService.orderItems = [];
  }
}
