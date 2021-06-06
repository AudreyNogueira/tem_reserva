import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AuthModel, AuthResponse } from '../../models/auth.model';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(
    private readonly http: HttpClient,
  ) { }

  login(auth: AuthModel): Observable<AuthResponse> {
    let headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Basic ' + btoa('tem-reserva-frontend:b4fa16a4-dc5c-4d45-95ea-0cc29bb2def3')
    });
    let options = { headers: headers };
    auth.grant_type = 'password';
    return this.http.post<AuthResponse>('http://localhost:8080/login', auth, options);
  }
}
