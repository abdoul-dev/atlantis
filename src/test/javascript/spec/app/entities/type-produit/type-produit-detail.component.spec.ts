import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AtlantisPoissonnerieTestModule } from '../../../test.module';
import { TypeProduitDetailComponent } from 'app/entities/type-produit/type-produit-detail.component';
import { TypeProduit } from 'app/shared/model/type-produit.model';

describe('Component Tests', () => {
  describe('TypeProduit Management Detail Component', () => {
    let comp: TypeProduitDetailComponent;
    let fixture: ComponentFixture<TypeProduitDetailComponent>;
    const route = ({ data: of({ typeProduit: new TypeProduit(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AtlantisPoissonnerieTestModule],
        declarations: [TypeProduitDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(TypeProduitDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TypeProduitDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load typeProduit on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.typeProduit).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
