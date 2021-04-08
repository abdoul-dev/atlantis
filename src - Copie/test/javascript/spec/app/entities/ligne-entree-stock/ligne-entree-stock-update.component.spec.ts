import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AtlantisPoissonnerieTestModule } from '../../../test.module';
import { LigneEntreeStockUpdateComponent } from 'app/entities/ligne-entree-stock/ligne-entree-stock-update.component';
import { LigneEntreeStockService } from 'app/entities/ligne-entree-stock/ligne-entree-stock.service';
import { LigneEntreeStock } from 'app/shared/model/ligne-entree-stock.model';

describe('Component Tests', () => {
  describe('LigneEntreeStock Management Update Component', () => {
    let comp: LigneEntreeStockUpdateComponent;
    let fixture: ComponentFixture<LigneEntreeStockUpdateComponent>;
    let service: LigneEntreeStockService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AtlantisPoissonnerieTestModule],
        declarations: [LigneEntreeStockUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(LigneEntreeStockUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LigneEntreeStockUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LigneEntreeStockService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new LigneEntreeStock(123);
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
        const entity = new LigneEntreeStock();
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
