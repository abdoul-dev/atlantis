import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { NgForm } from '@angular/forms';
import { LignesVentes } from 'app/shared/model/lignes-ventes.model';
import { ProductsService } from 'app/entities/products/products.service';
import { VentesService } from '../ventes.service';
import { IProducts, Products } from 'app/shared/model/products.model';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-order-items',
  templateUrl: './order-items.component.html',
  styles: []
})

export class OrderItemsComponent implements OnInit {
  formData!: LignesVentes;
  itemList: Products[] = [];
  isValid = true;

  constructor(
    @Inject(MAT_DIALOG_DATA) 
    public data: any,
    public dialogRef: MatDialogRef<OrderItemsComponent>,
    private itemService: ProductsService,
    private orderSevice: VentesService) { }

    ngOnInit(): void {
      this.itemService.query().subscribe((res : HttpResponse<IProducts[]>) => (this.itemList = res.body || []));
      if (this.data.orderItemIndex == null)
        this.formData = {
          id: 0,
          prixUnitaire: 0,
          quantite: 0,
          prixTotal: 0,
          productsId: 0,
          productsLibelle: '',
          ventesId: this.data.ventesId,
        }
      else
        this.formData = Object.assign({}, this.orderSevice.orderItems[this.data.orderItemIndex]);
    }

  updatePrice(ctrl:any):void {
    if (ctrl.selectedIndex === 0) {
      this.formData.prixUnitaire = 0;
      this.formData.productsLibelle = '';
    }
    else {
      this.formData.prixUnitaire = this.itemList[ctrl.selectedIndex - 1].prixVente;
      this.formData.productsLibelle = this.itemList[ctrl.selectedIndex - 1].libelle;
    }
    this.updateTotal();
  }

  updateTotal(): void {
    this.formData.prixTotal = parseFloat((this.formData.quantite! * this.formData.prixUnitaire!).toFixed(2));
  }

  onSubmit(form: NgForm): void {
    if (this.validateForm(form.value)) {
      if (this.data.orderItemIndex == null)
        this.orderSevice.orderItems.push(form.value);
      else
        this.orderSevice.orderItems[this.data.orderItemIndex] = form.value;
      this.dialogRef.close();
    }
  }

  validateForm(formData: LignesVentes): any {
    this.isValid = true;
    if (formData.productsId === 0)
      this.isValid = false;
    else if (formData.quantite === 0)
      this.isValid = false;
    return this.isValid;
  }

}
