import { Component, OnInit } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { ModalService } from '../service/modal.service';

@Component({
  selector: 'modal-confirm',
  templateUrl: './modal-confirm.component.html',
  styleUrls: ['./modal-confirm.component.scss']
})
export class ModalConfirmComponent implements OnInit {

  constructor(
    readonly modalService: BsModalService,
    private modalServiceLocal: ModalService,
  ) { }

  ngOnInit(): void {
  }

  /**
   * Emite resposta da modal
   * @param resp resposta selecionada
   */
  confirmationResponse(resp: boolean): void {
    this.modalServiceLocal.$comunication.next(resp);
    this.modalService.hide();
  }
}
