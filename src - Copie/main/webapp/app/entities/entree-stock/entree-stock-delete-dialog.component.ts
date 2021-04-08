import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IEntreeStock } from 'app/shared/model/entree-stock.model';
import { EntreeStockService } from './entree-stock.service';

@Component({
  templateUrl: './entree-stock-delete-dialog.component.html',
})
export class EntreeStockDeleteDialogComponent {
  entreeStock?: IEntreeStock;

  constructor(
    protected entreeStockService: EntreeStockService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.entreeStockService.delete(id).subscribe(() => {
      this.eventManager.broadcast('entreeStockListModification');
      this.activeModal.close();
    });
  }
}
