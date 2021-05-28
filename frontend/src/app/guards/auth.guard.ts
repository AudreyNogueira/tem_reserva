import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AccountType } from '../models/account.model';
import { RoutesEnum } from '../models/routes.enum';
import { SessionService } from '../shared/services/session.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(
    private sessionService: SessionService,
    private readonly router: Router,
  ) { }

  notAuthRoutes = [
    RoutesEnum.LOGIN,
    RoutesEnum.REGISTER_CLIENT,
    RoutesEnum.REGISTER_ESTABLHISHMENT,
  ]

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    if (
      this.sessionService.isAuthenticated() &&
      this.sessionService.getLoginType() &&
      this.sessionService.getUserSession()
    ) {

      if (this.notAuthRoutes.some(r => r === state.url)) {
        this.sessionService.getLoginType() === AccountType.USER ? this.router.navigate([RoutesEnum.EDIT_USER]) : this.router.navigate([RoutesEnum.EDIT_ESTABLISHMENT]);
        return false;
      }
      
      if(state.url === RoutesEnum.EDIT_USER && this.sessionService.getLoginType() === AccountType.ESTABLISHMENT) {
        this.router.navigate([RoutesEnum.EDIT_ESTABLISHMENT]);
        return false;
      }

      if (state.url === RoutesEnum.ESTABLISHMENTS_DASHBOARD && this.sessionService.getLoginType() === AccountType.ESTABLISHMENT) {
        this.router.navigate([RoutesEnum.RESERVE_ESTABLISHMENT]);
        return false;
      }
      return true;
    }
    this.router.navigate([RoutesEnum.ABOUT]);
    return false;
  }
}
