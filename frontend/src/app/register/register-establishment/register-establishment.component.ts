import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ModalService } from 'src/app/modals/service/modal.service';
import { cnpjValidator } from '../../validators/cnpj-validator';
import { onlyInputNumber, onlyPasteNumber } from '../../validators/input-methods';
import { cnpjMask } from '../../masks/cnpj-mask';

@Component({
  selector: 'register-establishment',
  templateUrl: './register-establishment.component.html',
  styleUrls: ['./register-establishment.component.scss']
})
export class RegisterEstablishmentComponent implements OnInit {

  isFirstForm = true;
  title = 'Bem vindo ao Tem Reserva?';
  buttonLabel = 'continue';
  feedbacks: string[] = [];
  cnpjInvalid: boolean;

  checkboxValue = false;
  FIELD_ERROR = 'Todos os campos são obrigatórios';
  CHECKBOX_ERROR = 'É necessário aceitar os termos e condições';

  submitted = false;
  maskCnpj = cnpjMask;

  formGroup: FormGroup = this.formBuilder.group({
    cnpj: [null, [Validators.required, cnpjValidator]],
    establhisment: [null, [Validators.required]],
    address: [null, [Validators.required]],
    capacity: [null, [Validators.required]],
    user: [null, [Validators.required]],
    password: [null, [Validators.required]],
  });

  constructor(
    private formBuilder: FormBuilder,
    private modalServiceLocal: ModalService,
  ) { }

  ngOnInit(): void {
  }

  /**
   * Chama o serviço para realizar a conclusão do registro de estabelecimento
   */
  submitForm(): void {
    this.submitted = true;
    this.feedbacks = [];
    if (this.validateForm()) return;

    /** Caso seja o primeiro formulário, ele avança para o segundo */
    if (this.isFirstForm) {
      this.isFirstForm = false;
      this.title = 'Já estamos terminando, falta pouco.';
      this.buttonLabel = 'Finalizar Cadastro';
      window.scroll(0, 0);
    }
  }

  /**
   * Valida os cenários de erro do formulário
   * @method validateForm
   * @returns {boolean} `true` = caso possua erro no formulário, `false` = caso não possua erro no formulário
   */
  validateForm(): boolean {
    let hasError;

    /** Verifica se validará o primeiro ou o segundo formulário */
    const fields = this.isFirstForm ? Object.keys(this.formGroup.controls).slice(0, 4) : Object.keys(this.formGroup.controls).slice(4, 6);

    /** Valida se algum campo possui erro */
    if (fields.some(f => this.formGroup.get(f).getError('required'))) {
      this.feedbacks.push(this.FIELD_ERROR);
      hasError = true;
    }

    /** Verifica se o CNPJ é valido */
    if (this.formGroup.get('cnpj').getError('invalidCnpj')) {
      hasError = true;
    }

    /** Verifica se o checkbox de Termos e Condições foi selecionado */
    if (!this.isFirstForm && !this.checkboxValue) {
      this.feedbacks.push(this.CHECKBOX_ERROR);
      hasError = true;
    }
    return hasError;
  }

  /**
   * Permite digitar somente números no campo
   * @param event evento do teclado
   */
  onlyInputNumber(event: any): void {
    onlyInputNumber(event);
  }

  /**
   * Faz a tratativa e permanece somente números quando realizado a ação COLAR
   * @param event evento COLAR
   */
  onlyPasteNumber(event: any): void {
    /** Armazena o valor tratado dentro do campo CNPJ */
    this.formGroup.get('cnpj').setValue(onlyPasteNumber(event));
  }

  /**
   * Abre a modal de login de estabelecimento
   * @method openModal
   */
  openModal(): void {
    this.modalServiceLocal.$openModal.next({ modalName: 'loginModal', loginType: 'establishment' });
  }
}
