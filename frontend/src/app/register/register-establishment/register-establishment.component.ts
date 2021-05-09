import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ModalService } from 'src/app/modals/service/modal.service';
import { cnpjValidator } from '../../validators/cnpj-validator';
import { onlyInputNumber, onlyPasteNumber } from '../../validators/input-methods';
import { cnpjMask } from '../../masks/cnpj-mask';
import { EditEstablishmentService } from 'src/app/management/services/edit-establishment.service';
import { Establishment } from 'src/app/models/establishment.model';
import { cepMask } from 'src/app/masks/cep-mask';

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
  maskCep = cepMask;

  zones: string[] = [
    'Zona Norte',
    'Zona Sul',
    'Zona Leste',
    'Zona Oeste',
  ];

  formGroup: FormGroup = this.formBuilder.group({
    cnpj: [null, [Validators.required, cnpjValidator]],
    establhisment: [null, [Validators.required]],
    cep: [null, [Validators.required]],
    zone: [null, [Validators.required]],
    street: [null, [Validators.required]],
    estabNumber: [null, [Validators.required]],
    district: [null, [Validators.required]],
    capacity: [null, [Validators.required]],
    user: [null, [Validators.required]],
    password: [null, [Validators.required]],
  });

  constructor(
    private formBuilder: FormBuilder,
    private modalServiceLocal: ModalService,
    private readonly editEstablishmentService: EditEstablishmentService,
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
    } else {
      const estab: Establishment = {
        cnpj: this.formGroup.get('cnpj').value,
        restaurantName: this.formGroup.get('establhisment').value,
        maxNumberOfPeople: Number(this.formGroup.get('capacity').value),
        email: this.formGroup.get('user').value,
        password: this.formGroup.get('password').value,
        address: {
          cep: this.formGroup.get('cep').value,
          district: this.formGroup.get('district').value,
          address: this.formGroup.get('street').value,
          complement: '',
          locality: 'São Paulo',
          restaurantNumber: this.formGroup.get('estabNumber').value,
          zone: this.formGroup.get('zone').value,
          uf: 'SP'
        },
      };
      this.editEstablishmentService.createEstablishment(estab).subscribe(() => {
        this.formGroup.reset();
        this.isFirstForm = true;
        this.modalServiceLocal.$openModal.next({ modalName: 'feedbackModal', type: 'success', message: 'Cadastro do estabelecimento concluído com sucesso.' });
      }, () => {
        this.modalServiceLocal.$openModal.next({ modalName: 'feedbackModal', type: 'error', message: 'Erro ao cadastrar do estabelecimento, tente novamente.' });
      });
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
    const fields = this.isFirstForm ? Object.keys(this.formGroup.controls).slice(0, 8) : Object.keys(this.formGroup.controls).slice(8, 10);

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
   * Faz uma busca pelo CEP e popula os outros campos com a resposta
   */
  getAddressByCep(): void {
    const cep = this.formGroup.get('cep').value.replace(/\D/g, '');
    if (cep.length === 8) {
      this.editEstablishmentService.getCEP(cep).subscribe(c => {
        if (c.localidade !== 'São Paulo') {
          this.formGroup.get('cep').setErrors({ outRange: true });
        }
        if (c?.erro) {
          this.formGroup.get('cep').setErrors({ invalidCep: true });
        } else {
          this.formGroup.get('street').setValue(c.logradouro);
          this.formGroup.get('district').setValue(c.bairro);
          this.formGroup.get('zone').setValue('');
        }
      });
    }
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
   * @param field campo do formulário
   * @param event evento COLAR
   */
  onlyPasteNumber(field: string, event: any): void {
    /** Armazena o valor tratado dentro do campo passado por parâmetro */
    this.formGroup.get(field).setValue(onlyPasteNumber(event));
  }

  /**
   * Abre a modal de login de estabelecimento
   * @method openModal
   */
  openModal(): void {
    this.modalServiceLocal.$openModal.next({ modalName: 'loginModal', loginType: 'establishment' });
  }
}
