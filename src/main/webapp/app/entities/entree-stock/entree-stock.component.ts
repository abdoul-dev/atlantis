import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, ParamMap, Router, Data } from '@angular/router';
import { Subscription, combineLatest } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { EntreeStock, IEntreeStock } from 'app/shared/model/entree-stock.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { EntreeStockService } from './entree-stock.service';
import { EntreeStockDeleteDialogComponent } from './entree-stock-delete-dialog.component';
import { ProductsService } from '../products/products.service';
import { ILigneEntreeStock } from 'app/shared/model/ligne-entree-stock.model';
import { LigneEntreeStockService } from '../ligne-entree-stock/ligne-entree-stock.service';

@Component({
  selector: 'jhi-entree-stock',
  templateUrl: './entree-stock.component.html',
})
export class EntreeStockComponent implements OnInit, OnDestroy {
  entreeStocks?: IEntreeStock[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected entreeStockService: EntreeStockService,
    protected productService: ProductsService,
    protected lignEntreestock: LigneEntreeStockService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;

    this.entreeStockService
      .query({
        'annule.equals': false,
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IEntreeStock[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
        () => this.onError()
      );
  }

  ngOnInit(): void {
    this.handleNavigation();
    this.registerChangeInEntreeStocks();
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

  trackId(index: number, item: IEntreeStock): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInEntreeStocks(): void {
    this.eventSubscriber = this.eventManager.subscribe('entreeStockListModification', () => this.loadPage());
  }

  delete(entreeStock: IEntreeStock): void {
    const modalRef = this.modalService.open(EntreeStockDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.entreeStock = entreeStock;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IEntreeStock[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/entree-stock'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    // eslint-disable-next-line no-console
    console.log(data);
    this.entreeStocks = data || [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  annuleApprovisionnement(appro: EntreeStock, isAnnule: boolean): void {
    if(appro.annule === false){
      this.entreeStockService.update({ ...appro, annule: isAnnule }).subscribe(() => this.loadPage());
      this.lignEntreestock
          .query({
            'entreestockId.equals': appro.id,
          }).subscribe((li: HttpResponse<ILigneEntreeStock[]>)=>{
              li.body?.forEach(element =>{
              // eslint-disable-next-line no-console
                 console.log(element.products?.quantite! - element.quantite!);
                this.productService.update({... element.products!, quantite: element.products?.quantite! - element.quantite! }).subscribe();
              })
          })
    }  
  }
}
