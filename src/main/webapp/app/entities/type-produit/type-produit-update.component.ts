import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ITypeProduit, TypeProduit } from 'app/shared/model/type-produit.model';
import { TypeProduitService } from './type-produit.service';

@Component({
  selector: 'jhi-type-produit-update',
  templateUrl: './type-produit-update.component.html',
})
export class TypeProduitUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    libelle: [null, [Validators.required, Validators.maxLength(100)]],
    isDisabled: [null, [Validators.required]],
  });

  constructor(protected typeProduitService: TypeProduitService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeProduit }) => {
      this.updateForm(typeProduit);
    });
  }

  updateForm(typeProduit: ITypeProduit): void {
    this.editForm.patchValue({
      id: typeProduit.id,
      libelle: typeProduit.libelle,
      isDisabled: typeProduit.isDisabled,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typeProduit = this.createFromForm();
    if (typeProduit.id !== undefined) {
      this.subscribeToSaveResponse(this.typeProduitService.update(typeProduit));
    } else {
      this.subscribeToSaveResponse(this.typeProduitService.create(typeProduit));
    }
  }

  private createFromForm(): ITypeProduit {
    return {
      ...new TypeProduit(),
      id: this.editForm.get(['id'])!.value,
      libelle: this.editForm.get(['libelle'])!.value,
      isDisabled: this.editForm.get(['isDisabled'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeProduit>>): void {
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
}
