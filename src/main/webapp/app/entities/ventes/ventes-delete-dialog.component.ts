import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IVentes } from 'app/shared/model/ventes.model';
import { VentesService } from './ventes.service';

@Component({
  templateUrl: './ventes-delete-dialog.component.html',
})
export class VentesDeleteDialogComponent {
  ventes?: IVentes;

  constructor(protected ventesService: VentesService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.ventesService.delete(id).subscribe(() => {
      this.eventManager.broadcast('ventesListModification');
      this.activeModal.close();
    });
  }
}
