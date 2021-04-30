import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, ParamMap, Router, Data } from '@angular/router';
import { Subscription, combineLatest } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProducts, Products } from 'app/shared/model/products.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ProductsService } from './products.service';
import { ProductsDeleteDialogComponent } from './products-delete-dialog.component';
import { TypeProduitService } from '../type-produit/type-produit.service';
import { ITypeProduit } from 'app/shared/model/type-produit.model';

@Component({
  selector: 'jhi-products',
  templateUrl: './products.component.html',
})
export class ProductsComponent implements OnInit, OnDestroy {
  products?: IProducts[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  typeproduits: ITypeProduit[] = [];
  typeProduit : any;
  actif: any;

  constructor(
    protected productsService: ProductsService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected typeProduitService: TypeProduitService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;

    this.productsService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IProducts[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
        () => this.onError()
      );
  }

  ngOnInit(): void {
    this.handleNavigation();
    this.registerChangeInProducts();
    this.typeProduitService.query().subscribe((res: HttpResponse<ITypeProduit[]>) => (this.typeproduits = res.body || []));

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

  trackId(index: number, item: IProducts): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInProducts(): void {
    this.eventSubscriber = this.eventManager.subscribe('productsListModification', () => this.loadPage());
  }

  delete(products: IProducts): void {
    const modalRef = this.modalService.open(ProductsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.products = products;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IProducts[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/products'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.products = data || [];
    // eslint-disable-next-line no-console
    console.log(this.products);
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  setActive(produit: Products, isActivated: boolean): void {
    this.productsService.update({ ...produit, isDisabled: isActivated }).subscribe(() => this.loadPage());
  }

  onChangeIfActif(event: any): void{
      // eslint-disable-next-line no-console
      console.log(event);
  }
  exportList(): void {
    this.productsService.downloadPdf().subscribe((res: Blob) => {
      window.open(URL.createObjectURL(new Blob([res || ''], { type: 'application/pdf; charset=utf-8' })), '_bank');
    });
  }

  restStock() : void {
    this.productsService.retriveAllProduit().subscribe((res: HttpResponse<IProducts[]>)=>{
        const products = res.body || [];
        products.forEach(element => {
          this.productsService.update({ ...element, quantite: 0 }).subscribe();
        });
    })
  }
}
