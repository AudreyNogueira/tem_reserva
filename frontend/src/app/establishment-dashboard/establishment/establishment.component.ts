import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { defineLocale } from 'ngx-bootstrap/chronos';
import { BsLocaleService } from 'ngx-bootstrap/datepicker';
import { ptBrLocale } from 'ngx-bootstrap/locale';
import { ModalService } from 'src/app/modals/service/modal.service';
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
    DayOfWeekEnum.TUESDAY,
    DayOfWeekEnum.MONDAY,
    DayOfWeekEnum.FRIDAY,
    DayOfWeekEnum.WEDNESDAY,
    DayOfWeekEnum.THURSDAY,
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

    const idEstablishment = this.route.snapshot.paramMap.get('id');
    this.establishmentListService.getEstablishmentById(Number(idEstablishment)).subscribe(e => {
      this.establishment = e;
      this.images = e.restaurantImages.map(img => img.image);
      this.dayDisabled = this.dayOfWeek.filter(disable => !this.establishment.restaurantDateTime.some(d => d.day === disable)).map(w => this.weekEnum[w]);
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
        this.modalServiceLocal.$openModal.next({ modalName: 'reserveModal', choosedDay: date, establishment: this.establishment });
      }
    }
    this.firstDate = false;
  }

}
