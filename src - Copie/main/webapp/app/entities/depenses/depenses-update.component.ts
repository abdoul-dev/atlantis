import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IDepenses, Depenses } from 'app/shared/model/depenses.model';
import { DepensesService } from './depenses.service';
import { ITypeDepense } from 'app/shared/model/type-depense.model';
import { TypeDepenseService } from 'app/entities/type-depense/type-depense.service';

@Component({
  selector: 'jhi-depenses-update',
  templateUrl: './depenses-update.component.html',
})
export class DepensesUpdateComponent implements OnInit {
  isSaving = false;
  typedepenses: ITypeDepense[] = [];
  dateDp: any;

  editForm = this.fb.group({
    id: [],
    montant: [null, [Validators.required]],
    comments: [],
    date: [null, [Validators.required]],
    annule: [null, [Validators.required]],
    typeDepenseId: [],
  });

  constructor(
    protected depensesService: DepensesService,
    protected typeDepenseService: TypeDepenseService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ depenses }) => {
      this.updateForm(depenses);

      this.typeDepenseService.query().subscribe((res: HttpResponse<ITypeDepense[]>) => (this.typedepenses = res.body || []));
    });
  }

  updateForm(depenses: IDepenses): void {
    this.editForm.patchValue({
      id: depenses.id,
      montant: depenses.montant,
      comments: depenses.comments,
      date: depenses.date,
      annule: depenses.annule,
      typeDepenseId: depenses.typeDepenseId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const depenses = this.createFromForm();
    if (depenses.id !== undefined) {
      this.subscribeToSaveResponse(this.depensesService.update(depenses));
    } else {
      this.subscribeToSaveResponse(this.depensesService.create(depenses));
    }
  }

  private createFromForm(): IDepenses {
    return {
      ...new Depenses(),
      id: this.editForm.get(['id'])!.value,
      montant: this.editForm.get(['montant'])!.value,
      comments: this.editForm.get(['comments'])!.value,
      date: this.editForm.get(['date'])!.value,
      annule: this.editForm.get(['annule'])!.value,
      typeDepenseId: this.editForm.get(['typeDepenseId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDepenses>>): void {
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

  trackById(index: number, item: ITypeDepense): any {
    return item.id;
  }
}
