import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import * as moment from 'moment';
import { AccountType } from 'src/app/models/account.model';
import { Reserve } from 'src/app/models/reserve.model';
import { RoutesEnum } from 'src/app/models/routes.enum';
import { SessionService } from 'src/app/shared/services/session.service';
import { EditEstablishmentService } from '../services/edit-establishment.service';
import { ReserveUserService } from '../services/reserve-user.service';

@Component({
  selector: 'reserve-user',
  templateUrl: './reserve-user.component.html',
  styleUrls: ['./reserve-user.component.scss']
})
export class ReserveUserComponent implements OnInit {

  mesEnum = [
    'Janeiro',
    'Fevereiro',
    'Março',
    'Abril',
    'Maio',
    'Junho',
    'Julho',
    'Agosto',
    'Setembro',
    'Outubro',
    'Novembro',
    'Dezembro',
  ]

  reserves: Reserve[];
  loading = true;

  constructor(
    private readonly sessionService: SessionService,
    private readonly router: Router,
    private readonly reserveService: ReserveUserService,
    private readonly editEstablishmentService: EditEstablishmentService,
  ) { }

  ngOnInit(): void {
    if (this.sessionService.getLoginType() === AccountType.ESTABLISHMENT) {
      this.router.navigate([RoutesEnum.RESERVE_ESTABLISHMENT]);
    }

    this.getReserves();
  }

  getReserves(): void {
    this.reserveService.getReservesByUserId(this.sessionService.getUserSession().id).subscribe(res => {
      this.reserves = this.orderReserve(res);
      this.loading = false;
    });
  }

  orderReserve(reserve: Reserve[]) {
    return reserve.sort((a, b) => { return new Date(b.reserveDate).getTime() - new Date(a.reserveDate).getTime() });
  }

  getHour(time: string): string {
    return moment(time).format('HH:mm');
  }

  cancelReservation(reserve: Reserve): void {
    this.reserveService.cancelReservation(reserve.id).subscribe(() => {
      this.getReserves();
      const numberPeople = {
        actualNumberOfPeople: reserve.restaurant.actualNumberOfPeople - reserve.amountOfPeople
      }
      this.editEstablishmentService.updateEstablishment(reserve.restaurant.id, numberPeople).subscribe();
    });
  }

  transformDate(date: string): Date {
    return new Date(date);
  }
}
