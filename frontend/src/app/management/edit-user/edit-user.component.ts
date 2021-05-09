import { Component, OnDestroy, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { onlyInputAlphanumeric, onlyInputNumber, onlyPasteAlphanumeric, onlyPasteNumber, spaceNotAllowed } from 'src/app/validators/input-methods';
import { EditUserService } from '../services/edit-user.service';
import { UserModel } from '../../models/user.model';
import { phoneMask } from '../../masks/phone-mask';
import { cpfValidator } from '../../validators/cpf-validator';
import { phoneValidator } from '../../validators/phone-validator';
import { cpfMask } from '../../masks/cpf-mask';
import { ModalService } from 'src/app/modals/service/modal.service';
import { first } from 'rxjs/operators';

@Component({
  selector: 'edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.scss']
})
export class EditUserComponent implements OnInit, OnDestroy {

  maskPhone = phoneMask;
  maskCPF = cpfMask;
  submitted = false;

  formGroup: FormGroup = this.formBuilder.group({
    name: [null, [Validators.required]],
    lastName: [null, [Validators.required]],
    cpf: [null, [Validators.required, cpfValidator]],
    birthDate: [null, [Validators.required]],
    phone: [null, [Validators.required, phoneValidator]],
    email: [null, [Validators.required, Validators.email]],
    currentPass: [''],
    newPass: [''],
  });

  constructor(
    private formBuilder: FormBuilder,
    private readonly editUserService: EditUserService,
    private modalServiceLocal: ModalService,
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
      if (resp) this.editUserService.deleteUser(1).subscribe();
    });
  }

  /**
   * Método responsável por chamar o serviço para
   * alterar os dados
   */
  saveEdit(): void {
    if (!this.validateForm()) {

      let data: UserModel = {
        name: `${this.firstLetterUpperCase(this.formGroup.get('name').value.trim())} ${this.firstLetterUpperCase(this.formGroup.get('lastName').value.trim())}`,
        cpf: this.formGroup.get('cpf').value.replace(/\D/g, ''),
        birthDate: this.formGroup.get('birthDate').value,
        phoneNumber: this.formGroup.get('phone').value.replace(/\D/g, ''),
        email: this.formGroup.get('email').value,
      };

      if (this.passwordChange()) data = { ...data, password: this.formGroup.get('newPass').value, actualPassword: this.formGroup.get('currentPass').value };

      this.editUserService.updateUserData(1, data).subscribe(() => { }, err => {
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
}

