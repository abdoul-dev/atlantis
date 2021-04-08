import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { LigneEntreeStockService } from 'app/entities/ligne-entree-stock/ligne-entree-stock.service';
import { ILigneEntreeStock, LigneEntreeStock } from 'app/shared/model/ligne-entree-stock.model';

describe('Service Tests', () => {
  describe('LigneEntreeStock Service', () => {
    let injector: TestBed;
    let service: LigneEntreeStockService;
    let httpMock: HttpTestingController;
    let elemDefault: ILigneEntreeStock;
    let expectedResult: ILigneEntreeStock | ILigneEntreeStock[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(LigneEntreeStockService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new LigneEntreeStock(0, 0, 0, 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a LigneEntreeStock', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new LigneEntreeStock()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a LigneEntreeStock', () => {
        const returnedFromService = Object.assign(
          {
            prixUnitaire: 1,
            quantite: 1,
            prixTotal: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of LigneEntreeStock', () => {
        const returnedFromService = Object.assign(
          {
            prixUnitaire: 1,
            quantite: 1,
            prixTotal: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a LigneEntreeStock', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
