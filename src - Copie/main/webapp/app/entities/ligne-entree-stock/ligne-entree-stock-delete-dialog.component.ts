import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILigneEntreeStock } from 'app/shared/model/ligne-entree-stock.model';
import { LigneEntreeStockService } from './ligne-entree-stock.service';

@Component({
  templateUrl: './ligne-entree-stock-delete-dialog.component.html',
})
export class LigneEntreeStockDeleteDialogComponent {
  ligneEntreeStock?: ILigneEntreeStock;

  constructor(
    protected ligneEntreeStockService: LigneEntreeStockService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ligneEntreeStockService.delete(id).subscribe(() => {
      this.eventManager.broadcast('ligneEntreeStockListModification');
      this.activeModal.close();
    });
  }
}
