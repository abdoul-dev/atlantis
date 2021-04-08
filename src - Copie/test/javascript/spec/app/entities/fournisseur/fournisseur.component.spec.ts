import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, convertToParamMap } from '@angular/router';

import { AtlantisPoissonnerieTestModule } from '../../../test.module';
import { FournisseurComponent } from 'app/entities/fournisseur/fournisseur.component';
import { FournisseurService } from 'app/entities/fournisseur/fournisseur.service';
import { Fournisseur } from 'app/shared/model/fournisseur.model';

describe('Component Tests', () => {
  describe('Fournisseur Management Component', () => {
    let comp: FournisseurComponent;
    let fixture: ComponentFixture<FournisseurComponent>;
    let service: FournisseurService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AtlantisPoissonnerieTestModule],
        declarations: [FournisseurComponent],
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
        .overrideTemplate(FournisseurComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FournisseurComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FournisseurService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Fournisseur(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.fournisseurs && comp.fournisseurs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });

    it('should load a page', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Fournisseur(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.loadPage(1);

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.fournisseurs && comp.fournisseurs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
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
