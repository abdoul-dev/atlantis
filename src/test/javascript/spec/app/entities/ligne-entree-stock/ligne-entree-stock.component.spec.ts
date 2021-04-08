import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, convertToParamMap } from '@angular/router';

import { AtlantisPoissonnerieTestModule } from '../../../test.module';
import { LigneEntreeStockComponent } from 'app/entities/ligne-entree-stock/ligne-entree-stock.component';
import { LigneEntreeStockService } from 'app/entities/ligne-entree-stock/ligne-entree-stock.service';
import { LigneEntreeStock } from 'app/shared/model/ligne-entree-stock.model';

describe('Component Tests', () => {
  describe('LigneEntreeStock Management Component', () => {
    let comp: LigneEntreeStockComponent;
    let fixture: ComponentFixture<LigneEntreeStockComponent>;
    let service: LigneEntreeStockService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AtlantisPoissonnerieTestModule],
        declarations: [LigneEntreeStockComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: {
              data: of({
                defaultSort: 'id,asc',
              }),
              queryParamMap: of(
                convertToParamMap({
                  page: '1',
                  size: '1',
                  sort: 'id,desc',
                })
              ),
            },
          },
        ],
      })
        .overrideTemplate(LigneEntreeStockComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(LigneEntreeStockComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(LigneEntreeStockService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new LigneEntreeStock(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.ligneEntreeStocks && comp.ligneEntreeStocks[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should load a page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new LigneEntreeStock(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.ligneEntreeStocks && comp.ligneEntreeStocks[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should calculate the sort attribute for an id', () => {
      // WHEN
      comp.ngOnInit();
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['id,desc']);
    });

    it('should calculate the sort attribute for a non-id attribute', () => {
      // INIT
      comp.ngOnInit();

      // GIVEN
      comp.predicate = 'name';

      // WHEN
      const result = comp.sort();

      // THEN
      expect(result).toEqual(['name,desc', 'id']);
    });
  });
});
