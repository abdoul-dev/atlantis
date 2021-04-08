import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { AtlantisPoissonnerieTestModule } from '../../../test.module';
import { MockEventManager } from '../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';
import { LigneEntreeStockDeleteDialogComponent } from 'app/entities/ligne-entree-stock/ligne-entree-stock-delete-dialog.component';
import { LigneEntreeStockService } from 'app/entities/ligne-entree-stock/ligne-entree-stock.service';

describe('Component Tests', () => {
  describe('LigneEntreeStock Management Delete Component', () => {
    let comp: LigneEntreeStockDeleteDialogComponent;
    let fixture: ComponentFixture<LigneEntreeStockDeleteDialogComponent>;
    let service: LigneEntreeStockService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AtlantisPoissonnerieTestModule],
        declarations: [LigneEntreeStockDeleteDialogComponent],
      })
        .overrideTemplate(LigneEntreeStockDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LigneEntreeStockDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LigneEntreeStockService);
      mockEventManager = TestBed.get(JhiEventManager);
      mockActiveModal = TestBed.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.closeSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
      });
    });
  });
});
