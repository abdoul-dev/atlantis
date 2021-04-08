import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AtlantisPoissonnerieTestModule } from '../../../test.module';
import { VentesUpdateComponent } from 'app/entities/ventes/ventes-update.component';
import { VentesService } from 'app/entities/ventes/ventes.service';
import { Ventes } from 'app/shared/model/ventes.model';

describe('Component Tests', () => {
  describe('Ventes Management Update Component', () => {
    let comp: VentesUpdateComponent;
    let fixture: ComponentFixture<VentesUpdateComponent>;
    let service: VentesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AtlantisPoissonnerieTestModule],
        declarations: [VentesUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(VentesUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VentesUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(VentesService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Ventes(123);
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
        const entity = new Ventes();
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
