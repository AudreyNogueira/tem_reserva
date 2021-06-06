import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { BsModalService } from 'ngx-bootstrap/modal';
import { AccountType } from 'src/app/models/account.model';
import { RoutesEnum } from 'src/app/models/routes.enum';
import { SessionService } from 'src/app/shared/services/session.service';
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
  submitting = false;

  formGroup: FormGroup = this.formBuilder.group({
    user: [null, [Validators.required, Validators.email]],
    pass: [null, [Validators.required]]
  });

  constructor(
    readonly modalService: BsModalService,
    private formBuilder: FormBuilder,
    private readonly loginService: LoginService,
    private readonly router: Router,
    private sessionService: SessionService,
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

      this.submitting = true;
      this.loginService.login(auth).subscribe(authLogin => {
        this.sessionService.setAuth(authLogin.accessToken);
        this.modalService.hide();
        if (this.loginType === AccountType.USER) {
          this.sessionService.setTypeLogin(AccountType.USER);
          this.router.navigate([RoutesEnum.ESTABLISHMENTS_DASHBOARD]);
          this.sessionService.set$userSession(authLogin.user, AccountType.USER);
        }
        else {
          this.sessionService.setTypeLogin(AccountType.ESTABLISHMENT);
          this.router.navigate([RoutesEnum.RESERVE_ESTABLISHMENT]);
          this.sessionService.set$userSession(authLogin.restaurant, AccountType.ESTABLISHMENT);
        }
      }, () => this.submitting = false);
    }
  }

}
