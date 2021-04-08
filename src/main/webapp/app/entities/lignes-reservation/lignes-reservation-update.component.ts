import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ILignesReservation, LignesReservation } from 'app/shared/model/lignes-reservation.model';
import { LignesReservationService } from './lignes-reservation.service';
import { IProducts } from 'app/shared/model/products.model';
import { ProductsService } from 'app/entities/products/products.service';

@Component({
  selector: 'jhi-lignes-reservation-update',
  templateUrl: './lignes-reservation-update.component.html',
})
export class LignesReservationUpdateComponent implements OnInit {
  isSaving = false;
  products: IProducts[] = [];

  editForm = this.fb.group({
    id: [],
    quantite: [null, [Validators.required]],
    productsId: [],
  });

  constructor(
    protected lignesReservationService: LignesReservationService,
    protected productsService: ProductsService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lignesReservation }) => {
      this.updateForm(lignesReservation);

      this.productsService.query().subscribe((res: HttpResponse<IProducts[]>) => (this.products = res.body || []));
    });
  }

  updateForm(lignesReservation: ILignesReservation): void {
    this.editForm.patchValue({
      id: lignesReservation.id,
      quantite: lignesReservation.quantite,
      productsId: lignesReservation.productsId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const lignesReservation = this.createFromForm();
    if (lignesReservation.id !== undefined) {
      this.subscribeToSaveResponse(this.lignesReservationService.update(lignesReservation));
    } else {
      this.subscribeToSaveResponse(this.lignesReservationService.create(lignesReservation));
    }
  }

  private createFromForm(): ILignesReservation {
    return {
      ...new LignesReservation(),
      id: this.editForm.get(['id'])!.value,
      quantite: this.editForm.get(['quantite'])!.value,
      productsId: this.editForm.get(['productsId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILignesReservation>>): void {
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

  trackById(index: number, item: IProducts): any {
    return item.id;
  }
}
