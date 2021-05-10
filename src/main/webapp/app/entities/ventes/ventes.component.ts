import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, ParamMap, Router, Data } from '@angular/router';
import { Subscription, combineLatest } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IVentes, Ventes } from 'app/shared/model/ventes.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { VentesService } from './ventes.service';
import { VentesDeleteDialogComponent } from './ventes-delete-dialog.component';
import { LignesVentesService } from '../lignes-ventes/lignes-ventes.service';
import { ILignesVentes } from 'app/shared/model/lignes-ventes.model';
import { ProductsService } from '../products/products.service';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { Moment } from 'moment';
import {Table} from 'primeng-lts/table';

@Component({
  selector: 'jhi-ventes',
  templateUrl: './ventes.component.html',
})
export class VentesComponent implements OnInit, OnDestroy {
  ventes?: IVentes[];
  allVentes?: IVentes[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  items?: ILignesVentes[];
  today = moment();
  dateDebut = moment();
  dateFin = moment();
  venteduJour: number | undefined;
  client?: String;
  cols?: any[];
  selectedCustomers: IVentes[] = [];
  loading = true;
  @ViewChild('dt') table?: Table;


  constructor(
    protected ventesService: VentesService,
    protected lignesVentesService: LignesVentesService,
    protected productService: ProductsService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {
    this.cols = [
      { field: 'id', header: 'ID', type: 'string', filter: true, input: false },
      { field: 'montantInitial', header: 'Montant Initial', type: 'string', filter: true, input: false },
      { field: 'remise', header: 'Remise', type: 'string', filter: true, input: false },
      { field: 'montantFinal', header: 'Montant Final', type: 'string', filter: true, input: false },
      { field: 'date', header: 'Date ', type: 'moment', filter: true, input: false },
      { field: 'clientFullName', header: 'Client', type: 'string', filter: true, input: false },
    ];
  }

    loadPage(page?: number, dontNavigate?: boolean, date?: Moment): void {
    const pageToLoad: number = page || this.page || 1;
    // eslint-disable-next-line no-console
    console.log("Je suis dans loadPage");
    this.ventesService
      .query({
        'annule.equals': false,
        'date.equals': date?.format(DATE_FORMAT),
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IVentes[]>) => {
          
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
          () => this.onError()
        }
      );
  } 

  ngOnInit(): void {
    this.handleNavigation(this.today);
    this.registerChangeInVentes();
    this.ventesService.venteDuJour(this.today).subscribe((t =>{
      this.venteduJour = t.body!;
    }))
    this.ventesService.findAllVentes().subscribe(r =>{ this.allVentes = r.body || []});
    this.loading = false;
    
  }

  protected handleNavigation(date: Moment): void {
    // eslint-disable-next-line no-console
    console.log("JE SUIS DANS HANDLENAV");
    combineLatest(this.activatedRoute.data, this.activatedRoute.queryParamMap, (data: Data, params: ParamMap) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get('sort') ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === 'desc';
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        // eslint-disable-next-line no-console
        console.log(pageNumber, this.page);
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true, date);
      }
    }).subscribe();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IVentes): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInVentes(): void {
    this.eventSubscriber = this.eventManager.subscribe('ventesListModification', () => this.loadPage());
  }

  delete(ventes: IVentes): void {
    const modalRef = this.modalService.open(VentesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.ventes = ventes;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result; 
  }

  protected onSuccess(data: IVentes[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/ventes'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    
    // eslint-disable-next-line no-console
    console.log(data);
    
    this.ventes = data!;
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  annuleVente(vente: Ventes, isAnnule: boolean): void {
    if(vente.annule === false){
      this.ventesService.update({ ...vente, annule: isAnnule }).subscribe(() => this.loadPage());
    
      vente.lignesVentes!.forEach(element => {
        // eslint-disable-next-line no-console
        console.log(element);
        this.productService.update({... element.products, quantite: element.products?.quantite! + element.quantite!}).subscribe();
      });
    }  
  }

  downloadPdf(): void {
      this.ventesService.downloadPdf(this.dateDebut).subscribe((res: Blob) => {
        window.open(URL.createObjectURL(new Blob([res || ''], { type: 'application/pdf; charset=utf-8' })), '_bank');
      });
  }

  onChangeDate(event: any): void {
    this.dateDebut = event
    this.handleNavigation(this.dateDebut);
  }
  
}
