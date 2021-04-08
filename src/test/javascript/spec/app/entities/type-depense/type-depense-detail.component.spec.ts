import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AtlantisPoissonnerieTestModule } from '../../../test.module';
import { TypeDepenseDetailComponent } from 'app/entities/type-depense/type-depense-detail.component';
import { TypeDepense } from 'app/shared/model/type-depense.model';

describe('Component Tests', () => {
  describe('TypeDepense Management Detail Component', () => {
    let comp: TypeDepenseDetailComponent;
    let fixture: ComponentFixture<TypeDepenseDetailComponent>;
    const route = ({ data: of({ typeDepense: new TypeDepense(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AtlantisPoissonnerieTestModule],
        declarations: [TypeDepenseDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(TypeDepenseDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TypeDepenseDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load typeDepense on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.typeDepense).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
