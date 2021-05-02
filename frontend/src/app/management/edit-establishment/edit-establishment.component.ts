import { Component, OnInit } from '@angular/core';
import { FormGroup, Validators, FormBuilder, AbstractControl } from '@angular/forms';
import { first } from 'rxjs/operators';
import { cnpjMask } from 'src/app/masks/cnpj-mask';
import { phoneMask } from 'src/app/masks/phone-mask';
import { cepMask } from 'src/app/masks/cep-mask';
import { ModalService } from 'src/app/modals/service/modal.service';
import { cnpjValidator } from 'src/app/validators/cnpj-validator';
import { onlyInputNumber, onlyInputAlphanumeric, spaceNotAllowed, onlyPasteNumber, onlyPasteAlphanumeric } from 'src/app/validators/input-methods';
import { phoneValidator } from 'src/app/validators/phone-validator';
import { EditEstablishmentService } from '../services/edit-establishment.service';
import { Establishment } from 'src/app/models/establishment.model';

@Component({
  selector: 'edit-establishment',
  templateUrl: './edit-establishment.component.html',
  styleUrls: ['./edit-establishment.component.scss']
})
export class EditEstablishmentComponent implements OnInit {

  maskPhone = phoneMask;
  maskCnpj = cnpjMask;
  maskCep = cepMask;
  submitted = false;
  zones = [
    'Zona Norte',
    'Zona Sul',
    'Zona Leste',
    'Zona Oeste',
  ];

  formGroup: FormGroup = this.formBuilder.group({
    restaurantName: [null, [Validators.required]],
    cnpj: [null, [Validators.required, cnpjValidator]],
    cep: [null, [Validators.required]],
    zone: [null, [Validators.required]],
    street: [null, [Validators.required]],
    estabNumber: [null, [Validators.required]],
    district: [null, [Validators.required]],
    phone: [null, [Validators.required, phoneValidator]],
    email: [null, [Validators.required, Validators.email]],
    currentPass: [''],
    newPass: [''],
  });

  constructor(
    private formBuilder: FormBuilder,
    private modalServiceLocal: ModalService,
    private readonly editEstablishmentService: EditEstablishmentService,
  ) { }

  ngOnInit(): void {
  }

  ngOnDestroy(): void {
    this.modalServiceLocal.$comunication.unsubscribe();
  }

  /**
   * Deleta o usuário
   */
  deleteUser(): void {
    this.modalServiceLocal.$openModal.next({ modalName: 'confirmModal' });

    this.modalServiceLocal.$comunication.pipe(first()).subscribe(resp => {
      if (resp) this.editEstablishmentService.deleteEstablishment(1).subscribe();
    })
  }

  /**
   * Método responsável por chamar o serviço para
   * alterar os dados
   */
  saveEdit(): void {
    if (!this.validateForm()) {

      let data: Establishment = {
        restaurantName: `${this.firstLetterUpperCase(this.formGroup.get('restaurantName').value.trim())}`,
        cnpj: this.formGroup.get('cnpj').value.replace(/\D/g, ''),
        phoneNumber: this.formGroup.get('phone').value.replace(/\D/g, ''),
        address: {
          cep: this.formGroup.get('cep').value.replace(/\D/g, ''),
          district: this.formGroup.get('district').value,
          address: this.formGroup.get('street').value,
          complement: '',
          locality: 'São Paulo',
          restaurantNumber: this.formGroup.get('estabNumber').value,
          zone: this.formGroup.get('zone').value,
          uf: 'SP'
      },
        email: this.formGroup.get('email').value,
      };

      if (this.passwordChange()) data = { ...data, password: this.formGroup.get('newPass').value, actualPassword: this.formGroup.get('currentPass').value };

      this.editEstablishmentService.updateEstablishment(1, data).subscribe(() => { }, err => {
        if (err.error.apicode === '0013') this.formGroup.get('currentPass').setValue('');
      });
    }
  }

  /**
   * Valida os campos do formulário
   * @returns `true` caso o formulário esteja válido
   */
  validateForm(): boolean {
    this.submitted = true;
    let hasError = false;
    let control: AbstractControl | null = null;
    Object.keys(this.formGroup.controls).forEach(f => {
      control = this.formGroup.get(f);
      if (control && control.invalid) {
        control.markAsDirty();
        hasError = true;
      }
    });

    /**
     * Faz uma validação a parte dos campos de senha
     */
    if (this.passwordChange()) {
      hasError = !(this.formGroup.get('currentPass').value !== '' && this.formGroup.get('newPass').value !== '');
    }

    return hasError;
  }

  /**
   * Verifica se os valores dos campos senhas foram alterados
   * @returns `true` caso algum dos campos tenha sido alterado
   */
  passwordChange(): boolean {
    return this.formGroup.get('currentPass').value !== '' || this.formGroup.get('newPass').value !== '';
  }

  /**
   * Permite digitar somente números no campo
   * @param event evento do teclado
   */
  onlyInputNumber(event: any): void {
    onlyInputNumber(event);
  }

  /**
   * Permite digitar somente números no campo
   * @param event evento do teclado
   */
  onlyInputAlphanumeric(event: any): void {
    onlyInputAlphanumeric(event);
  }

  /**
   * Não permite digitar espaços
   * @param event evento do teclado
   */
  spaceNotAllowed(event: any): void {
    spaceNotAllowed(event);
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
   * Faz a tratativa e permanece somente letras quando realizado a ação COLAR
   * @param field campo do formulário
   * @param event evento COLAR
   */
  onlyPasteAlphanumeric(field: string, event: any): void {
    /** Armazena o valor tratado dentro do campo passado por parâmetro */
    this.formGroup.get(field).setValue(onlyPasteAlphanumeric(event));
  }

  /**
   * Coloca a primeira letra da palavra como maiúscula
   * @param word palavra
   * @returns palavra com a primeira letra maiúscula
   */
  firstLetterUpperCase(word: string): string {
    return word.charAt(0).toUpperCase() + word.slice(1);
  }

  getAddressByCep() {
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
}
