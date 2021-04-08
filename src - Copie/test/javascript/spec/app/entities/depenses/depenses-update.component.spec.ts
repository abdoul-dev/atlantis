import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AtlantisPoissonnerieTestModule } from '../../../test.module';
import { DepensesUpdateComponent } from 'app/entities/depenses/depenses-update.component';
import { DepensesService } from 'app/entities/depenses/depenses.service';
import { Depenses } from 'app/shared/model/depenses.model';

describe('Component Tests', () => {
  describe('Depenses Management Update Component', () => {
    let comp: DepensesUpdateComponent;
    let fixture: ComponentFixture<DepensesUpdateComponent>;
    let service: DepensesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AtlantisPoissonnerieTestModule],
        declarations: [DepensesUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(DepensesUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DepensesUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(DepensesService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Depenses(123);
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
        const entity = new Depenses();
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
