import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AtlantisPoissonnerieTestModule } from '../../../test.module';
import { EntreeStockDetailComponent } from 'app/entities/entree-stock/entree-stock-detail.component';
import { EntreeStock } from 'app/shared/model/entree-stock.model';

describe('Component Tests', () => {
  describe('EntreeStock Management Detail Component', () => {
    let comp: EntreeStockDetailComponent;
    let fixture: ComponentFixture<EntreeStockDetailComponent>;
    const route = ({ data: of({ entreeStock: new EntreeStock(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AtlantisPoissonnerieTestModule],
        declarations: [EntreeStockDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(EntreeStockDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EntreeStockDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load entreeStock on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.entreeStock).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
