import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVentes } from 'app/shared/model/ventes.model';

@Component({
  selector: 'jhi-ventes-detail',
  templateUrl: './ventes-detail.component.html',
})
export class VentesDetailComponent implements OnInit {
  ventes: IVentes | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ventes }) => (this.ventes = ventes));
    // eslint-disable-next-line no-console
    console.log(this.ventes);
  }

  previousState(): void {
    window.history.back();
  }
}
