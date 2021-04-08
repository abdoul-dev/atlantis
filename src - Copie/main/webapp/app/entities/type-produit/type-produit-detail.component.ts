import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITypeProduit } from 'app/shared/model/type-produit.model';

@Component({
  selector: 'jhi-type-produit-detail',
  templateUrl: './type-produit-detail.component.html',
})
export class TypeProduitDetailComponent implements OnInit {
  typeProduit: ITypeProduit | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeProduit }) => (this.typeProduit = typeProduit));
  }

  previousState(): void {
    window.history.back();
  }
}
