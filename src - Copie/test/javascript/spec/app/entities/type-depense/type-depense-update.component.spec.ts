import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AtlantisPoissonnerieTestModule } from '../../../test.module';
import { TypeDepenseUpdateComponent } from 'app/entities/type-depense/type-depense-update.component';
import { TypeDepenseService } from 'app/entities/type-depense/type-depense.service';
import { TypeDepense } from 'app/shared/model/type-depense.model';

describe('Component Tests', () => {
  describe('TypeDepense Management Update Component', () => {
    let comp: TypeDepenseUpdateComponent;
    let fixture: ComponentFixture<TypeDepenseUpdateComponent>;
    let service: TypeDepenseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AtlantisPoissonnerieTestModule],
        declarations: [TypeDepenseUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(TypeDepenseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TypeDepenseUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TypeDepenseService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new TypeDepense(123);
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
        const entity = new TypeDepense();
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
