import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AtlantisPoissonnerieTestModule } from '../../../test.module';
import { TypeProduitUpdateComponent } from 'app/entities/type-produit/type-produit-update.component';
import { TypeProduitService } from 'app/entities/type-produit/type-produit.service';
import { TypeProduit } from 'app/shared/model/type-produit.model';

describe('Component Tests', () => {
  describe('TypeProduit Management Update Component', () => {
    let comp: TypeProduitUpdateComponent;
    let fixture: ComponentFixture<TypeProduitUpdateComponent>;
    let service: TypeProduitService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AtlantisPoissonnerieTestModule],
        declarations: [TypeProduitUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(TypeProduitUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TypeProduitUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(TypeProduitService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new TypeProduit(123);
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
        const entity = new TypeProduit();
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
