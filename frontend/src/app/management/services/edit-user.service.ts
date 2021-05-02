import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserModel } from 'src/app/models/user.model';

@Injectable({
  providedIn: 'root'
})
export class EditUserService {

  constructor(
    private readonly http: HttpClient,
  ) { }


  /**
   * Chama a rota para alterar os dados do usuário
   * @param id Id do usuário
   * @param data dados para realizar a alteração
   * @returns Resposta da requisição
   */
  updateUserData(id: number, data: UserModel): Observable<any> {
    return this.http.put(`http://localhost:8080/user/id=${id}`, data);
  }

  /**
   * Chama a rota para deletar o usuário
   * @param id Id do usuário
   * @returns Resposta da requisição
   */
  deleteUser(id: number): Observable<any> {
    return this.http.delete(`http://localhost:8080/user/${id}`);
  }

  createUser(user: UserModel): Observable<any> {
    return this.http.post('http://localhost:8080/user/create', user);
  }
}
