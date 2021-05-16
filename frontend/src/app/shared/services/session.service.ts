import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { AccountType } from 'src/app/models/account.model';
import { Establishment } from 'src/app/models/establishment.model';
import { UserModel } from 'src/app/models/user.model';

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  $userSession = new BehaviorSubject<{ est?: Establishment, user?: UserModel }>(null);

  constructor() { }


  set$userSession(data: any, type: string): void {
    type === AccountType.USER ? this.$userSession.next({ user: data }) : this.$userSession.next({ est: data });
    localStorage.setItem('UserSession', JSON.stringify({ ...data }));
  }
}
