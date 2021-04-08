import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ITypeDepense, TypeDepense } from 'app/shared/model/type-depense.model';
import { TypeDepenseService } from './type-depense.service';

@Component({
  selector: 'jhi-type-depense-update',
  templateUrl: './type-depense-update.component.html',
})
export class TypeDepenseUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    libelle: [null, [Validators.maxLength(50)]],
    isDisabled: [null, [Validators.required]],
  });

  constructor(protected typeDepenseService: TypeDepenseService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeDepense }) => {
      this.updateForm(typeDepense);
    });
  }

  updateForm(typeDepense: ITypeDepense): void {
    this.editForm.patchValue({
      id: typeDepense.id,
      libelle: typeDepense.libelle,
      isDisabled: typeDepense.isDisabled,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typeDepense = this.createFromForm();
    if (typeDepense.id !== undefined) {
      this.subscribeToSaveResponse(this.typeDepenseService.update(typeDepense));
    } else {
      this.subscribeToSaveResponse(this.typeDepenseService.create(typeDepense));
    }
  }

  private createFromForm(): ITypeDepense {
    return {
      ...new TypeDepense(),
      id: this.editForm.get(['id'])!.value,
      libelle: this.editForm.get(['libelle'])!.value,
      isDisabled: this.editForm.get(['isDisabled'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeDepense>>): void {
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
