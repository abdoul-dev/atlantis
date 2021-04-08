import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITypeProduit } from 'app/shared/model/type-produit.model';
import { TypeProduitService } from './type-produit.service';

@Component({
  templateUrl: './type-produit-delete-dialog.component.html',
})
export class TypeProduitDeleteDialogComponent {
  typeProduit?: ITypeProduit;

  constructor(
    protected typeProduitService: TypeProduitService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.typeProduitService.delete(id).subscribe(() => {
      this.eventManager.broadcast('typeProduitListModification');
      this.activeModal.close();
    });
  }
}
