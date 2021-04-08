import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IProducts, Products } from 'app/shared/model/products.model';
import { ProductsService } from './products.service';
import { ITypeProduit } from 'app/shared/model/type-produit.model';
import { TypeProduitService } from 'app/entities/type-produit/type-produit.service';

@Component({
  selector: 'jhi-products-update',
  templateUrl: './products-update.component.html',
})
export class ProductsUpdateComponent implements OnInit {
  isSaving = false;
  typeproduits: ITypeProduit[] = [];

  editForm = this.fb.group({
    id: [],
    libelle: [null, [Validators.required, Validators.maxLength(100)]],
    prixVente: [null, [Validators.required]],
    isDisabled: [null, [Validators.required]],
    quantite: [],
    comments: [],
    typeProduitId: [],
  });

  constructor(
    protected productsService: ProductsService,
    protected typeProduitService: TypeProduitService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ products }) => {
      this.updateForm(products);

      this.typeProduitService.query().subscribe((res: HttpResponse<ITypeProduit[]>) => (this.typeproduits = res.body || []));
    });
  }

  updateForm(products: IProducts): void {
    this.editForm.patchValue({
      id: products.id,
      libelle: products.libelle,
      prixVente: products.prixVente,
      isDisabled: products.isDisabled,
      quantite: products.quantite,
      comments: products.comments,
      typeProduitId: products.typeProduitId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const products = this.createFromForm();
    if (products.id !== undefined) {
      this.subscribeToSaveResponse(this.productsService.update(products));
    } else {
      this.subscribeToSaveResponse(this.productsService.create(products));
    }
  }

  private createFromForm(): IProducts {
    return {
      ...new Products(),
      id: this.editForm.get(['id'])!.value,
      libelle: this.editForm.get(['libelle'])!.value,
      prixVente: this.editForm.get(['prixVente'])!.value,
      isDisabled: this.editForm.get(['isDisabled'])!.value,
      quantite: this.editForm.get(['quantite'])!.value,
      comments: this.editForm.get(['comments'])!.value,
      typeProduitId: this.editForm.get(['typeProduitId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProducts>>): void {
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

  trackById(index: number, item: ITypeProduit): any {
    return item.id;
  }
}
