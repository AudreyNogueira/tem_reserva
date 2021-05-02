import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { EditEstablishmentService } from './management/services/edit-establishment.service';
import { ModalService } from './modals/service/modal.service';
import { Establishment } from './models/establishment.model';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'tem-reserva-front';

  @ViewChild('modalTemplate') modalTemplate: TemplateRef<any>;
  modalName: string;
  loginType: string;

  mock: Establishment[];
  cont = 0;

  constructor(
    private readonly modalService: BsModalService,
    private readonly modalServiceLocal: ModalService,
    private readonly editEstablishmentService: EditEstablishmentService,
  ) { }

  ngOnInit(): void {
    /** Abre a modal na qual foi passada como parâmetro */
    this.modalServiceLocal.$openModal.subscribe(m => {
      this.modalService.show(this.modalTemplate, { class: 'modal-dialog-centered' });
      this.modalName = m.modalName;
      this.loginType = m.loginType;
    });

    /**
     * MOCK
     */
    this.editEstablishmentService.getEstablishmentById(1).subscribe(r => {
      this.editEstablishmentService.set$userSession(r);
    });

  }

}
