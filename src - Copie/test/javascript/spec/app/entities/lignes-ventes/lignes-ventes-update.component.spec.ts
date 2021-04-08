import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AtlantisPoissonnerieTestModule } from '../../../test.module';
import { LignesVentesUpdateComponent } from 'app/entities/lignes-ventes/lignes-ventes-update.component';
import { LignesVentesService } from 'app/entities/lignes-ventes/lignes-ventes.service';
import { LignesVentes } from 'app/shared/model/lignes-ventes.model';

describe('Component Tests', () => {
  describe('LignesVentes Management Update Component', () => {
    let comp: LignesVentesUpdateComponent;
    let fixture: ComponentFixture<LignesVentesUpdateComponent>;
    let service: LignesVentesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AtlantisPoissonnerieTestModule],
        declarations: [LignesVentesUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(LignesVentesUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LignesVentesUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LignesVentesService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new LignesVentes(123);
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
        const entity = new LignesVentes();
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
