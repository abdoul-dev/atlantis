import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ILignesReservation } from 'app/shared/model/lignes-reservation.model';

@Component({
  selector: 'jhi-lignes-reservation-detail',
  templateUrl: './lignes-reservation-detail.component.html',
})
export class LignesReservationDetailComponent implements OnInit {
  lignesReservation: ILignesReservation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ lignesReservation }) => (this.lignesReservation = lignesReservation));
  }

  previousState(): void {
    window.history.back();
  }
}
