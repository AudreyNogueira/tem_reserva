import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { BsModalService } from 'ngx-bootstrap/modal';
import { RoutesEnum } from 'src/app/models/routes.enum';
import { LoginService } from '../../login/service/login.service';
import { AuthModel } from '../../models/auth.model';

@Component({
  selector: 'modal-login',
  templateUrl: './modal-login.component.html',
  styleUrls: ['./modal-login.component.scss']
})
export class ModalLoginComponent implements OnInit {

  @Input() loginType: string;
  routes = RoutesEnum;

  formGroup: FormGroup = this.formBuilder.group({
    user: [null, [Validators.required, Validators.email]],
    pass: [null, [Validators.required]]
  });

  constructor(
    readonly modalService: BsModalService,
    private formBuilder: FormBuilder,
    private readonly loginService: LoginService,
    private readonly router: Router,
  ) { }

  ngOnInit(): void {
  }

  /**
   * Chama o serviço para realizar o login
   */
  submitLogin(): void {

    if (this.formGroup.valid) {
      const auth: AuthModel = {
        user: this.formGroup.get('user').value,
        password: this.formGroup.get('pass').value,
        loginType: this.loginType,
      };

      this.loginService.login(auth).subscribe(() => {
        this.modalService.hide();
        this.router.navigate([RoutesEnum.ESTABLISHMENTS_DASHBOARD]);
      });
    }
  }

}
