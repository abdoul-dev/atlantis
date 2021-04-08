import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AtlantisPoissonnerieTestModule } from '../../../test.module';
import { DepensesDetailComponent } from 'app/entities/depenses/depenses-detail.component';
import { Depenses } from 'app/shared/model/depenses.model';

describe('Component Tests', () => {
  describe('Depenses Management Detail Component', () => {
    let comp: DepensesDetailComponent;
    let fixture: ComponentFixture<DepensesDetailComponent>;
    const route = ({ data: of({ depenses: new Depenses(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AtlantisPoissonnerieTestModule],
        declarations: [DepensesDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(DepensesDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DepensesDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load depenses on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.depenses).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
