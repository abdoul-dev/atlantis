import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDepenses } from 'app/shared/model/depenses.model';
import { DepensesService } from './depenses.service';

@Component({
  templateUrl: './depenses-delete-dialog.component.html',
})
export class DepensesDeleteDialogComponent {
  depenses?: IDepenses;

  constructor(protected depensesService: DepensesService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.depensesService.delete(id).subscribe(() => {
      this.eventManager.broadcast('depensesListModification');
      this.activeModal.close();
    });
  }
}
