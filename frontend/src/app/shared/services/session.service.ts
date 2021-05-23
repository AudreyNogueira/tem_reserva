import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { AccountType } from 'src/app/models/account.model';
import { Establishment } from 'src/app/models/establishment.model';
import { UserModel } from 'src/app/models/user.model';

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  $userSession = new BehaviorSubject<{ est?: Establishment, user?: UserModel }>(
    this.getLoginType() === AccountType.USER ?
      { user: this.getUserSession() } : { est: this.getUserSession() });

  constructor() { }

  getLoginType() {
    return JSON.parse(window.localStorage.getItem('loginType'));
  }

  getUserSession() {
    return JSON.parse(window.localStorage.getItem('UserSession'));
  }

  set$userSession(data: any, type: string): void {
    type === AccountType.USER ? this.$userSession.next({ user: data }) : this.$userSession.next({ est: data });
    localStorage.setItem('UserSession', JSON.stringify({ ...data }));
  }

  setAuth(value: boolean) {
    window.localStorage.setItem('authenticated', JSON.stringify(value));
  }

  setTypeLogin(type: string) {
    window.localStorage.setItem('loginType', JSON.stringify(type));
  }

  isAuthenticated(): boolean {
    return JSON.parse(window.localStorage.getItem('authenticated'));
  }

  logout(): void {
    window.localStorage.removeItem('authenticated');
    window.localStorage.removeItem('loginType');
    window.localStorage.removeItem('UserSession');
  }
}
