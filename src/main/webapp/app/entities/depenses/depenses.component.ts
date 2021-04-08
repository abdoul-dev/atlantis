import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, ParamMap, Router, Data } from '@angular/router';
import { Subscription, combineLatest } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDepenses } from 'app/shared/model/depenses.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { DepensesService } from './depenses.service';
import { DepensesDeleteDialogComponent } from './depenses-delete-dialog.component';
import * as moment from 'moment';
import { ITypeDepense } from 'app/shared/model/type-depense.model';
import { TypeDepenseService } from '../type-depense/type-depense.service';

@Component({
  selector: 'jhi-depenses',
  templateUrl: './depenses.component.html',
})
export class DepensesComponent implements OnInit, OnDestroy {
  depenses?: IDepenses[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  typedepense?: any;
  dateDebut = moment();
  dateFin = moment();
  typedepenses: ITypeDepense[] = [];


  constructor(
    protected depensesService: DepensesService,
    protected typeDepenseService: TypeDepenseService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;

    this.depensesService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IDepenses[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
        () => this.onError()
      );
  }

  ngOnInit(): void {
    this.handleNavigation();
    this.registerChangeInDepenses();
    this.typeDepenseService.query().subscribe((res: HttpResponse<ITypeDepense[]>) => (this.typedepenses = res.body || []));
  }

  protected handleNavigation(): void {
    combineLatest(this.activatedRoute.data, this.activatedRoute.queryParamMap, (data: Data, params: ParamMap) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get('sort') ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === 'asc';
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    }).subscribe();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IDepenses): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInDepenses(): void {
    this.eventSubscriber = this.eventManager.subscribe('depensesListModification', () => this.loadPage());
  }

  delete(depenses: IDepenses): void {
    const modalRef = this.modalService.open(DepensesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.depenses = depenses;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IDepenses[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/depenses'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.depenses = data || [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  downloadPdf(): void{

  }

  onChangeDate(event: any): void {
    this.dateDebut = event
  } 
}
