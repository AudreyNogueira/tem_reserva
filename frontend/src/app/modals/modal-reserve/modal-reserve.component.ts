import { Component, Input, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import * as moment from 'moment';
import { BsModalService } from 'ngx-bootstrap/modal';
import { Establishment } from 'src/app/models/establishment.model';
import { hourMadrugada, hourManha, hourTarde, hourNoite, Periodo } from '../../models/time.model';
import { ReservationService } from '../../establishment-dashboard/services/reservation.service';
import { Reserve } from 'src/app/models/reserve.model';
import { ModalService } from '../service/modal.service';
import { SessionService } from 'src/app/shared/services/session.service';

@Component({
  selector: 'modal-reserve',
  templateUrl: './modal-reserve.component.html',
  styleUrls: ['./modal-reserve.component.scss']
})
export class ModalReserveComponent implements OnInit {

  @Input() choosedDay: Date;
  @Input() establishment: Establishment;
  @Input() openTime: string;
  @Input() closeTime: string;

  timeAvailable = {
    [Periodo.MADRUGADA]: hourMadrugada,
    [Periodo.MANHA]: hourManha,
    [Periodo.TARDE]: hourTarde,
    [Periodo.NOITE]: hourNoite
  };
  timeSelected: number;
  qtdSelected: number;
  submitted = false;
  periodSelected: string;
  isFeedback = false;
  loading = false;

  formGroup: FormGroup = this.formBuilder.group({
    hour: [null, [Validators.required]],
    period: [null, [Validators.required]],
    quantity: [null, [Validators.required]],
    obs: [null, []],
  });

  constructor(
    readonly modalService: BsModalService,
    private formBuilder: FormBuilder,
    private readonly reservationService: ReservationService,
    private modalServiceLocal: ModalService,
    private readonly sessionService: SessionService,
  ) { }

  ngOnInit(): void {
    if (moment(this.choosedDay).isSame(Date.now(), 'day') && moment(this.choosedDay, 'HH:mm').isAfter(moment(this.closeTime, 'HH:mm'))) {
      this.modalServiceLocal.$openModal.next({ modalName: 'feedbackModal', message: 'Este dia não está mais disponível' });
    }
  }

  periodAvaible(): string[] {
    const now = moment(new Date()).get('hour');

    if (moment(this.choosedDay).isSame(Date.now(), 'day')) {
      if (now >= 18) {
        return [Periodo.NOITE];
      }
      if (now >= 12 && now < 18) {
        return [Periodo.TARDE, Periodo.NOITE];
      }
      if (now >= 6 && now < 12) {
        return [Periodo.MANHA, Periodo.TARDE, Periodo.NOITE];
      }
    }
    return [Periodo.MADRUGADA, Periodo.MANHA, Periodo.TARDE, Periodo.NOITE];
  }

  hoursAvaible(hourArray: string[]): string[] {
    const now = moment({ h: new Date().getHours(), m: new Date().getMinutes() });
    let avaible;
    avaible = hourArray.filter(h =>
      moment(h, 'HH:mm').isSameOrAfter(moment(this.openTime, 'HH:mm')) &&
      moment(h, 'HH:mm').isBefore(moment(this.closeTime, 'HH:mm'))
    );

    if (moment(this.choosedDay).isSame(Date.now(), 'day')) {
      return avaible.filter(h => moment(h, 'HH:mm').isAfter(now));
    }
    return avaible;

  }

  selectHour(date: string, index: number, period: string): void {
    this.formGroup.get('hour').setValue(moment({
      y: this.choosedDay.getFullYear(),
      M: this.choosedDay.getMonth(),
      d: this.choosedDay.getDate(),
      h: Number(date.split(':')[0]),
      m: Number(date.split(':')[1])
    }).format('YYYY-MM-DDTHH:mm:ss'));
    this.formGroup.get('period').setValue(period);
    this.timeSelected = index;
    this.periodSelected = period;
  }

  selectQuantity(qtd: number, index: number): void {
    this.formGroup.get('quantity').setValue(qtd);
    this.qtdSelected = index;
  }


  validateForm(): boolean {
    this.submitted = true;
    let hasError = false;
    let control: AbstractControl | null = null;
    Object.keys(this.formGroup.controls).forEach(f => {
      control = this.formGroup.get(f);
      if (control && control.invalid) {
        control.markAsDirty();
        hasError = true;
      }
    });
    return hasError;
  }

  makeReservation(): void {
    if (!this.validateForm()) {
      const reserve: Reserve = {
        idUser: this.sessionService.getUserSession().id,
        idRestaurant: this.establishment.id,
        reserveDate: this.formGroup.get('hour').value,
        amountOfPeople: this.formGroup.get('quantity').value,
        period: this.formGroup.get('period').value
      };
      this.loading = true;
      this.reservationService.makeReservation(reserve)
        .subscribe(() => {
          this.loading = false;
          this.isFeedback = true;
        }, () => {
          this.modalServiceLocal.$openModal.next({ modalName: 'feedbackModal', type: 'error', message: 'Erro ao criar sua reserva, tente novamente' });
        });
    }
  }

}
