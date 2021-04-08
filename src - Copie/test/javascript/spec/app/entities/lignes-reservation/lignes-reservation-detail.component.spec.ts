import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AtlantisPoissonnerieTestModule } from '../../../test.module';
import { LignesReservationDetailComponent } from 'app/entities/lignes-reservation/lignes-reservation-detail.component';
import { LignesReservation } from 'app/shared/model/lignes-reservation.model';

describe('Component Tests', () => {
  describe('LignesReservation Management Detail Component', () => {
    let comp: LignesReservationDetailComponent;
    let fixture: ComponentFixture<LignesReservationDetailComponent>;
    const route = ({ data: of({ lignesReservation: new LignesReservation(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AtlantisPoissonnerieTestModule],
        declarations: [LignesReservationDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(LignesReservationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(LignesReservationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load lignesReservation on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.lignesReservation).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
