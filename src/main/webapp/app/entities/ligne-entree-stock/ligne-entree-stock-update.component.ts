import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ILigneEntreeStock, LigneEntreeStock } from 'app/shared/model/ligne-entree-stock.model';
import { LigneEntreeStockService } from './ligne-entree-stock.service';
import { IProducts } from 'app/shared/model/products.model';
import { ProductsService } from 'app/entities/products/products.service';
import { IEntreeStock } from 'app/shared/model/entree-stock.model';
import { EntreeStockService } from 'app/entities/entree-stock/entree-stock.service';

type SelectableEntity = IProducts | IEntreeStock;

@Component({
  selector: 'jhi-ligne-entree-stock-update',
  templateUrl: './ligne-entree-stock-update.component.html',
})
export class LigneEntreeStockUpdateComponent implements OnInit {
  isSaving = false;
  products: IProducts[] = [];
  entreestocks: IEntreeStock[] = [];

  editForm = this.fb.group({
    id: [],
    prixUnitaire: [null, [Validators.required]],
    quantite: [null, [Validators.required]],
    prixTotal: [null, [Validators.required]],
    productsId: [],
    entreestockId: [],
  });

  constructor(
    protected ligneEntreeStockService: LigneEntreeStockService,
    protected productsService: ProductsService,
    protected entreeStockService: EntreeStockService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ligneEntreeStock }) => {
      this.updateForm(ligneEntreeStock);

      this.productsService.query().subscribe((res: HttpResponse<IProducts[]>) => (this.products = res.body || []));

      this.entreeStockService.query().subscribe((res: HttpResponse<IEntreeStock[]>) => (this.entreestocks = res.body || []));
    });
  }

  updateForm(ligneEntreeStock: ILigneEntreeStock): void {
    this.editForm.patchValue({
      id: ligneEntreeStock.id,
      prixUnitaire: ligneEntreeStock.prixUnitaire,
      quantite: ligneEntreeStock.quantite,
      prixTotal: ligneEntreeStock.prixTotal,
      productsId: ligneEntreeStock.productsId,
      entreestockId: ligneEntreeStock.entreestockId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ligneEntreeStock = this.createFromForm();
    if (ligneEntreeStock.id !== undefined) {
      this.subscribeToSaveResponse(this.ligneEntreeStockService.update(ligneEntreeStock));
    } else {
      this.subscribeToSaveResponse(this.ligneEntreeStockService.create(ligneEntreeStock));
    }
  }

  private createFromForm(): ILigneEntreeStock {
    return {
      ...new LigneEntreeStock(),
      id: this.editForm.get(['id'])!.value,
      prixUnitaire: this.editForm.get(['prixUnitaire'])!.value,
      quantite: this.editForm.get(['quantite'])!.value,
      prixTotal: this.editForm.get(['prixTotal'])!.value,
      productsId: this.editForm.get(['productsId'])!.value,
      entreestockId: this.editForm.get(['entreestockId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILigneEntreeStock>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
