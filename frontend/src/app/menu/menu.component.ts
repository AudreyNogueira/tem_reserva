import { Component, HostListener, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AccountType } from '../models/account.model';
import { RoutesEnum } from '../models/routes.enum';
import { SessionService } from '../shared/services/session.service';

@Component({
  selector: 'menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {

  isCollapsed = true;
  routes = RoutesEnum;
  typeMenu: string;
  user: any;

  constructor(
    public sessionService: SessionService,
    private readonly router: Router,
  ) { }

  /**
   * Fecha o menu caso esteja aberto e ocorrá um redimensionamento da tela
   */
  @HostListener('window:resize', ['$event'])
  onResize(): void {
    if (window.innerWidth > 768) this.isCollapsed = true;
  }

  ngOnInit(): void {
    this.sessionService.$userSession.subscribe(u => {
      if (u && u?.est) {
        this.user = u.est;
        this.typeMenu = 'est';
      } else if (u && u?.user) {
        this.user = u.user;
        this.typeMenu = 'user';
      }
    });
  }

  /**
   * Fecha o menu caso esteja aberto (método só funcionará quando a resolução for de tablet ou smartphones)
   * @method closeMenu
   */
  closeMenu(): void {
    if (!this.isCollapsed) this.isCollapsed = !this.isCollapsed;
  }

  getEditRoute(): string {
    return this.typeMenu === 'user' ? this.routes.EDIT_USER : this.routes.EDIT_ESTABLISHMENT;
  }

  logoNavigation() {
    if (this.sessionService.getAuthenticated()) {
      this.sessionService.getLoginType() === AccountType.USER ? this.router.navigate([RoutesEnum.ESTABLISHMENTS_DASHBOARD]) : this.router.navigate([RoutesEnum.RESERVE_ESTABLISHMENT]);
    } else {
      this.router.navigate([RoutesEnum.ABOUT]);
    }
  }
}
