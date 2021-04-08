import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AtlantisPoissonnerieTestModule } from '../../../test.module';
import { VentesDetailComponent } from 'app/entities/ventes/ventes-detail.component';
import { Ventes } from 'app/shared/model/ventes.model';

describe('Component Tests', () => {
  describe('Ventes Management Detail Component', () => {
    let comp: VentesDetailComponent;
    let fixture: ComponentFixture<VentesDetailComponent>;
    const route = ({ data: of({ ventes: new Ventes(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AtlantisPoissonnerieTestModule],
        declarations: [VentesDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(VentesDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(VentesDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load ventes on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ventes).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
