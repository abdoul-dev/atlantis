import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ILignesReservation } from 'app/shared/model/lignes-reservation.model';
import { LignesReservationService } from './lignes-reservation.service';

@Component({
  templateUrl: './lignes-reservation-delete-dialog.component.html',
})
export class LignesReservationDeleteDialogComponent {
  lignesReservation?: ILignesReservation;

  constructor(
    protected lignesReservationService: LignesReservationService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.lignesReservationService.delete(id).subscribe(() => {
      this.eventManager.broadcast('lignesReservationListModification');
      this.activeModal.close();
    });
  }
}
