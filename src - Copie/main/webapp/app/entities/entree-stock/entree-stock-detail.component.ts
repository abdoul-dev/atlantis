import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEntreeStock } from 'app/shared/model/entree-stock.model';

@Component({
  selector: 'jhi-entree-stock-detail',
  templateUrl: './entree-stock-detail.component.html',
})
export class EntreeStockDetailComponent implements OnInit {
  entreeStock: IEntreeStock | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ entreeStock }) => (this.entreeStock = entreeStock));
  }

  previousState(): void {
    window.history.back();
  }
}
