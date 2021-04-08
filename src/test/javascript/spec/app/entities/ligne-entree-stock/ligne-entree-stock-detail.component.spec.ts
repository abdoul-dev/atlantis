import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AtlantisPoissonnerieTestModule } from '../../../test.module';
import { LigneEntreeStockDetailComponent } from 'app/entities/ligne-entree-stock/ligne-entree-stock-detail.component';
import { LigneEntreeStock } from 'app/shared/model/ligne-entree-stock.model';

describe('Component Tests', () => {
  describe('LigneEntreeStock Management Detail Component', () => {
    let comp: LigneEntreeStockDetailComponent;
    let fixture: ComponentFixture<LigneEntreeStockDetailComponent>;
    const route = ({ data: of({ ligneEntreeStock: new LigneEntreeStock(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AtlantisPoissonnerieTestModule],
        declarations: [LigneEntreeStockDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(LigneEntreeStockDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LigneEntreeStockDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load ligneEntreeStock on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ligneEntreeStock).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
