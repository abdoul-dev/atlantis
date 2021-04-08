import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AtlantisPoissonnerieTestModule } from '../../../test.module';
import { EntreeStockUpdateComponent } from 'app/entities/entree-stock/entree-stock-update.component';
import { EntreeStockService } from 'app/entities/entree-stock/entree-stock.service';
import { EntreeStock } from 'app/shared/model/entree-stock.model';

describe('Component Tests', () => {
  describe('EntreeStock Management Update Component', () => {
    let comp: EntreeStockUpdateComponent;
    let fixture: ComponentFixture<EntreeStockUpdateComponent>;
    let service: EntreeStockService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AtlantisPoissonnerieTestModule],
        declarations: [EntreeStockUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(EntreeStockUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EntreeStockUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EntreeStockService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new EntreeStock(123);
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
        const entity = new EntreeStock();
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
