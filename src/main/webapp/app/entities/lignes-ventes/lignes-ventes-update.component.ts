import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ILignesVentes, LignesVentes } from 'app/shared/model/lignes-ventes.model';
import { LignesVentesService } from './lignes-ventes.service';
import { IProducts } from 'app/shared/model/products.model';
import { ProductsService } from 'app/entities/products/products.service';
import { IVentes } from 'app/shared/model/ventes.model';
import { VentesService } from 'app/entities/ventes/ventes.service';

type SelectableEntity = IProducts | IVentes;

@Component({
  selector: 'jhi-lignes-ventes-update',
  templateUrl: './lignes-ventes-update.component.html',
})
export class LignesVentesUpdateComponent implements OnInit {
  isSaving = false;
  products: IProducts[] = [];
  ventes: IVentes[] = [];

  editForm = this.fb.group({
    id: [],
    prixUnitaire: [null, [Validators.required]],
    quantite: [null, [Validators.required]],
    prixTotal: [null, [Validators.required]],
    productsId: [],
    ventesId: [],
  });

  constructor(
    protected lignesVentesService: LignesVentesService,
    protected productsService: ProductsService,
    protected ventesService: VentesService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lignesVentes }) => {
      this.updateForm(lignesVentes);

      this.productsService.query().subscribe((res: HttpResponse<IProducts[]>) => (this.products = res.body || []));

      this.ventesService.query().subscribe((res: HttpResponse<IVentes[]>) => (this.ventes = res.body || []));
    });
  }

  updateForm(lignesVentes: ILignesVentes): void {
    this.editForm.patchValue({
      id: lignesVentes.id,
      prixUnitaire: lignesVentes.prixUnitaire,
      quantite: lignesVentes.quantite,
      prixTotal: lignesVentes.prixTotal,
      productsId: lignesVentes.productsId,
      ventesId: lignesVentes.ventesId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lignesVentes = this.createFromForm();
    if (lignesVentes.id !== undefined) {
      this.subscribeToSaveResponse(this.lignesVentesService.update(lignesVentes));
    } else {
      this.subscribeToSaveResponse(this.lignesVentesService.create(lignesVentes));
    }
  }

  private createFromForm(): ILignesVentes {
    return {
      ...new LignesVentes(),
      id: this.editForm.get(['id'])!.value,
      prixUnitaire: this.editForm.get(['prixUnitaire'])!.value,
      quantite: this.editForm.get(['quantite'])!.value,
      prixTotal: this.editForm.get(['prixTotal'])!.value,
      productsId: this.editForm.get(['productsId'])!.value,
      ventesId: this.editForm.get(['ventesId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILignesVentes>>): void {
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
