import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITypeDepense } from 'app/shared/model/type-depense.model';

@Component({
  selector: 'jhi-type-depense-detail',
  templateUrl: './type-depense-detail.component.html',
})
export class TypeDepenseDetailComponent implements OnInit {
  typeDepense: ITypeDepense | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeDepense }) => (this.typeDepense = typeDepense));
  }

  previousState(): void {
    window.history.back();
  }
}
