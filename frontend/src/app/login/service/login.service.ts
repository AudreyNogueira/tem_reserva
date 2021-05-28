import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Establishment } from 'src/app/models/establishment.model';
import { AuthModel } from '../../models/auth.model';
import { UserModel } from '../../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(
    private readonly http: HttpClient,
  ) { }

  login(auth: AuthModel): Observable<UserModel | Establishment> {
    if (auth.loginType === 'user') return this.http.post<UserModel | Establishment>('https://run.mocky.io/v3/7021bce0-a1d2-4b78-b3a9-67698a60f0c6', auth);
    return this.http.post<UserModel | Establishment>('https://run.mocky.io/v3/39b222f4-297a-4ca1-8978-588b7d5cb28e', auth);
  }
}
