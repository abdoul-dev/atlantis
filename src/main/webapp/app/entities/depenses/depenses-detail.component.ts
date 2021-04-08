import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDepenses } from 'app/shared/model/depenses.model';

@Component({
  selector: 'jhi-depenses-detail',
  templateUrl: './depenses-detail.component.html',
})
export class DepensesDetailComponent implements OnInit {
  depenses: IDepenses | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ depenses }) => (this.depenses = depenses));
  }

  previousState(): void {
    window.history.back();
  }
}
