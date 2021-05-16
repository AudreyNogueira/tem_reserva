import { Component, OnInit } from '@angular/core';
import * as moment from 'moment';
import { ReservePerDay } from 'src/app/models/reserve.model';
import { DayOfWeekEnum } from 'src/app/models/week.enum';
import { ReserveEstablishmentService } from '../services/reserve-establishment.service';

@Component({
  selector: 'reserve-establishment',
  templateUrl: './reserve-establishment.component.html',
  styleUrls: ['./reserve-establishment.component.scss']
})
export class ReserveEstablishmentComponent implements OnInit {

  dayOfWeek = [
    DayOfWeekEnum.SUNDAY,
    DayOfWeekEnum.MONDAY,
    DayOfWeekEnum.TUESDAY,
    DayOfWeekEnum.WEDNESDAY,
    DayOfWeekEnum.THURSDAY,
    DayOfWeekEnum.FRIDAY,
    DayOfWeekEnum.SATURDAY,
  ];

  reserves: ReservePerDay[] = [];

  idEstablishment = JSON.parse(window.localStorage.getItem('UserSession')).id;

  constructor(
    private reserveService: ReserveEstablishmentService,
  ) { }

  ngOnInit(): void {

    this.reserveService.getReservesByUserEstablishmentId(this.idEstablishment).subscribe(res => this.reserves = res);
  }

  maskCpf(cpf: string): string {
    return cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, (regex, block1, block2, block3, block4) => {
      regex;
      return block1 + '.' + block2 + '.' + block3 + '-' + block4;
    }
    );
  }

  maskHour(hour: string): string {
    return moment(hour).format('HH:mm');
  }

  mapDate(date: string): string {
    const day = new Date(date.replace('-', '/'));
    return `${this.dayOfWeek[day.getDay()]} ${day.toLocaleDateString('pt-br')}`;
  }

}
