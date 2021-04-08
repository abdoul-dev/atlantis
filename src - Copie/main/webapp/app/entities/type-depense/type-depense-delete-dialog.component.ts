import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITypeDepense } from 'app/shared/model/type-depense.model';
import { TypeDepenseService } from './type-depense.service';

@Component({
  templateUrl: './type-depense-delete-dialog.component.html',
})
export class TypeDepenseDeleteDialogComponent {
  typeDepense?: ITypeDepense;

  constructor(
    protected typeDepenseService: TypeDepenseService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.typeDepenseService.delete(id).subscribe(() => {
      this.eventManager.broadcast('typeDepenseListModification');
      this.activeModal.close();
    });
  }
}
