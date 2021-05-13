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
    return this.http.post<UserModel | Establishment>('http://localhost:8080', auth);
  }
}
