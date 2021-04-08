import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AtlantisPoissonnerieTestModule } from '../../../test.module';
import { LignesReservationUpdateComponent } from 'app/entities/lignes-reservation/lignes-reservation-update.component';
import { LignesReservationService } from 'app/entities/lignes-reservation/lignes-reservation.service';
import { LignesReservation } from 'app/shared/model/lignes-reservation.model';

describe('Component Tests', () => {
  describe('LignesReservation Management Update Component', () => {
    let comp: LignesReservationUpdateComponent;
    let fixture: ComponentFixture<LignesReservationUpdateComponent>;
    let service: LignesReservationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AtlantisPoissonnerieTestModule],
        declarations: [LignesReservationUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(LignesReservationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LignesReservationUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LignesReservationService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new LignesReservation(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new LignesReservation();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
