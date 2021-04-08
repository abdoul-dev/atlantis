import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILignesVentes } from 'app/shared/model/lignes-ventes.model';

@Component({
  selector: 'jhi-lignes-ventes-detail',
  templateUrl: './lignes-ventes-detail.component.html',
})
export class LignesVentesDetailComponent implements OnInit {
  lignesVentes: ILignesVentes | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lignesVentes }) => (this.lignesVentes = lignesVentes));
  }

  previousState(): void {
    window.history.back();
  }
}
