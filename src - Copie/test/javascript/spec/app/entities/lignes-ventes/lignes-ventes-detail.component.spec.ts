import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AtlantisPoissonnerieTestModule } from '../../../test.module';
import { LignesVentesDetailComponent } from 'app/entities/lignes-ventes/lignes-ventes-detail.component';
import { LignesVentes } from 'app/shared/model/lignes-ventes.model';

describe('Component Tests', () => {
  describe('LignesVentes Management Detail Component', () => {
    let comp: LignesVentesDetailComponent;
    let fixture: ComponentFixture<LignesVentesDetailComponent>;
    const route = ({ data: of({ lignesVentes: new LignesVentes(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AtlantisPoissonnerieTestModule],
        declarations: [LignesVentesDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(LignesVentesDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LignesVentesDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load lignesVentes on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.lignesVentes).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
