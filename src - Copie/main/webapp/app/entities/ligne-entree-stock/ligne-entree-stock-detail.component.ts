import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILigneEntreeStock } from 'app/shared/model/ligne-entree-stock.model';

@Component({
  selector: 'jhi-ligne-entree-stock-detail',
  templateUrl: './ligne-entree-stock-detail.component.html',
})
export class LigneEntreeStockDetailComponent implements OnInit {
  ligneEntreeStock: ILigneEntreeStock | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ligneEntreeStock }) => (this.ligneEntreeStock = ligneEntreeStock));
  }

  previousState(): void {
    window.history.back();
  }
}
