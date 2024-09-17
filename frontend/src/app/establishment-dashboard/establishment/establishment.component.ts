import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import * as moment from 'moment';
import { defineLocale } from 'ngx-bootstrap/chronos';
import { BsLocaleService } from 'ngx-bootstrap/datepicker';
import { ptBrLocale } from 'ngx-bootstrap/locale';
import { switchMap } from 'rxjs/operators';
import { ModalService } from 'src/app/modals/service/modal.service';
import { DayOfWeekModel } from 'src/app/models/day-hour.model';
import { Establishment } from 'src/app/models/establishment.model';
import { DayOfWeekEnum } from 'src/app/models/week.enum';
import { EstablishmentListService } from '../services/establishment-list.service';
defineLocale('pt-br', ptBrLocale);

@Component({
  selector: 'establishment',
  templateUrl: './establishment.component.html',
  styleUrls: ['./establishment.component.scss']
})
export class EstablishmentComponent implements OnInit {

  bsInlineValue = new Date().toLocaleDateString('pt-br');
  minDate = new Date();
  firstDate = true;
  images: string[];
  indexImages = 0;
  establishment: Establishment;
  dayDisabled;
  datesWork: DayOfWeekModel[] = [];

  weekEnum = {
    [DayOfWeekEnum.SUNDAY]: 0,
    [DayOfWeekEnum.MONDAY]: 1,
    [DayOfWeekEnum.TUESDAY]: 2,
    [DayOfWeekEnum.WEDNESDAY]: 3,
    [DayOfWeekEnum.THURSDAY]: 4,
    [DayOfWeekEnum.FRIDAY]: 5,
    [DayOfWeekEnum.SATURDAY]: 6,
  };

  dayOfWeek = [
    DayOfWeekEnum.SUNDAY,
    DayOfWeekEnum.MONDAY,
    DayOfWeekEnum.TUESDAY,
    DayOfWeekEnum.WEDNESDAY,
    DayOfWeekEnum.THURSDAY,
    DayOfWeekEnum.FRIDAY,
    DayOfWeekEnum.SATURDAY,
  ];

  constructor(
    private localeService: BsLocaleService,
    private modalServiceLocal: ModalService,
    private establishmentListService: EstablishmentListService,
    private readonly route: ActivatedRoute,
  ) {
    this.localeService.use('pt-br');
  }

  ngOnInit(): void {
    window.scroll(0, 0);

    const idEstablishment = this.route.snapshot.paramMap.get('id');
    this.establishmentListService.getEstablishmentById(Number(idEstablishment)).subscribe(e => {
      this.establishment = e;
      this.images = e.restaurantImages.map(img => img.image);
      this.dayDisabled = this.dayOfWeek.filter(disable => !this.establishment.restaurantDateTime.some(d => d.day === disable)).map(w => this.weekEnum[w]);
      this.treatOpenDays(this.establishment.restaurantDateTime);
    });

    this.establishmentListService.reserve$.pipe(switchMap(() =>
      this.establishmentListService.getEstablishmentById(Number(idEstablishment))
    )).subscribe(e => {
      this.establishment = e;
      this.images = e.restaurantImages.map(img => img.image);
      this.dayDisabled = this.dayOfWeek.filter(disable => !this.establishment.restaurantDateTime.some(d => d.day === disable)).map(w => this.weekEnum[w]);
      this.treatOpenDays(this.establishment.restaurantDateTime);
    });
  }

  /**
   * Método para rotacionar o array de imagens
   * @param reverse caso `true` altera o sentido ("para trás")
   */
  rotate(reverse?: boolean): void {
    if (reverse) this.images.unshift(this.images.pop());
    else this.images.push(this.images.shift());
  }

  makeReservation(date: Date): void {
    if (!this.firstDate) {
      if (this.dayDisabled.some(d => d === date.getDay())) {
        this.modalServiceLocal.$openModal.next({ modalName: 'feedbackModal', message: 'Dia indisponível para realizar reserva' });
      } else {
        this.modalServiceLocal.$openModal.next(
          {
            modalName: 'reserveModal',
            choosedDay: date,
            establishment: this.establishment,
            openingTime: this.getHours(date, true),
            closingTime: this.getHours(date, false),
          });
      }
    }
    this.firstDate = false;
  }

  getHours(date: Date, isOpen?: boolean) {
    const day = this.dayOfWeek[date.getDay()];
    if (isOpen) {
      return this.establishment.restaurantDateTime.find(d => d.day === day).openingTime;
    }
    return this.establishment.restaurantDateTime.find(d => d.day === day).closingTime;

  }

  treatOpenDays(list: DayOfWeekModel[]): void {
    list.forEach(d => {
      this.datesWork.push(
        {
          day: d.day,
          openingTime: moment({ h: Number(d.openingTime.split(':')[0]), m: Number(d.openingTime.split(':')[1]) }).format('HH:mm'),
          closingTime: moment({ h: Number(d.closingTime.split(':')[0]), m: Number(d.closingTime.split(':')[1]) }).format('HH:mm')
        }
      );
    });
  }

  getCurrentPeople() {
    return this.establishment.currentPeople.find(p => p.period === this.establishmentListService.getPeriod())?.currentPeople ?
      this.establishment.currentPeople.find(p => p.period === this.establishmentListService.getPeriod())?.currentPeople : 0;
  }

}
