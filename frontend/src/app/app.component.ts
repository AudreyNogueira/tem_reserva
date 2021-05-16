import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { EditEstablishmentService } from './management/services/edit-establishment.service';
import { ModalService } from './modals/service/modal.service';
import { AccountType } from './models/account.model';
import { Establishment } from './models/establishment.model';
import { SessionService } from './shared/services/session.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'tem-reserva-front';

  @ViewChild('modalTemplate') modalTemplate: TemplateRef<any>;
  dataModal: any;

  mock: Establishment[];
  cont = 0;

  constructor(
    private readonly modalService: BsModalService,
    private readonly modalServiceLocal: ModalService,
    private readonly editEstablishmentService: EditEstablishmentService,
    private readonly sessionService: SessionService,
  ) { }

  ngOnInit(): void {
    /** Abre a modal na qual foi passada como parâmetro */
    this.modalServiceLocal.$openModal.subscribe(m => {
      this.modalService.show(this.modalTemplate, { class: 'modal-dialog-centered' });
      this.dataModal = m;
    });

    /**
     * MOCK
     */
    this.editEstablishmentService.getEstablishmentById(1).subscribe(r => {
      this.sessionService.set$userSession(r, AccountType.ESTABLISHMENT);
    });

  }

}
