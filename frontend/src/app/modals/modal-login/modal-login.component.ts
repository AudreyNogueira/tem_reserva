import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BsModalService } from 'ngx-bootstrap/modal';
import { RoutesEnum } from 'src/app/models/routes.enum';

@Component({
  selector: 'modal-login',
  templateUrl: './modal-login.component.html',
  styleUrls: ['./modal-login.component.scss']
})
export class ModalLoginComponent implements OnInit {

  @Input() loginType: string;
  routes = RoutesEnum;

  formGroup: FormGroup = this.formBuilder.group({
    user: [null, [Validators.required]],
    pass: [null, [Validators.required]]
  });

  constructor(
    readonly modalService: BsModalService,
    private formBuilder: FormBuilder,
  ) { }

  ngOnInit(): void {
  }

  /**
   * Chama o serviço para realizar o login
   */
  submitLogin(): void {

    /** TODO Chamar o serviço de Login */

    this.modalService.hide();
  }

}
