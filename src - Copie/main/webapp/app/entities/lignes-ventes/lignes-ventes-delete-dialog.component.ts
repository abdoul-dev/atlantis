import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILignesVentes } from 'app/shared/model/lignes-ventes.model';
import { LignesVentesService } from './lignes-ventes.service';

@Component({
  templateUrl: './lignes-ventes-delete-dialog.component.html',
})
export class LignesVentesDeleteDialogComponent {
  lignesVentes?: ILignesVentes;

  constructor(
    protected lignesVentesService: LignesVentesService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.lignesVentesService.delete(id).subscribe(() => {
      this.eventManager.broadcast('lignesVentesListModification');
      this.activeModal.close();
    });
  }
}
