import { Component, OnInit } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { ModalService } from '../modals/service/modal.service';

@Component({
  selector: 'login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  loginType: string;

  constructor(
    private modalServiceLocal: ModalService,
    private modalService: BsModalService,
  ) { }

  ngOnInit(): void {
    /** Reseta o tipo de login sempre que a modal é fechada */
    this.modalService.onHide.subscribe(() => this.loginType = null);
  }

  /**
   * Salva qual o tipo de modal deve ser aberta
   * @method openModal
   * @param template nome da modal
   * @param loginType o tipo de login a ser realizado
   */
  openModal(template: string, loginType: string): void {
    this.modalServiceLocal.$openModal.next({ modalName: template, loginType });
    this.loginType = loginType;
  }

}
