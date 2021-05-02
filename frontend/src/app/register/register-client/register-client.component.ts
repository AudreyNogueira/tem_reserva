import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormBuilder, Validators } from '@angular/forms';
import { onlyInputNumber, onlyPasteNumber } from '../../validators/input-methods';
import { EditUserService } from 'src/app/management/services/edit-user.service';
import { UserModel } from 'src/app/models/user.model';
import { cpfValidator } from 'src/app/validators/cpf-validator';
import { cpfMask } from 'src/app/masks/cpf-mask';
import { phoneMask } from 'src/app/masks/phone-mask';
@Component({
  selector: 'register-client',
  templateUrl: './register-client.component.html',
  styleUrls: ['./register-client.component.scss']
})
export class RegisterClientComponent implements OnInit {

  maskCPF = cpfMask;
  maskPhone = phoneMask;

  formCliente: FormGroup = this.formBuilder.group({
    nome: [null, [Validators.required]],
    sobrenome: [null, [Validators.required]],
    dataNascimento: [null, [Validators.required]],
    cpf: [null, [Validators.required, cpfValidator]],
    sexo: [null, [Validators.required]],
    celular: [null, [Validators.required]],
    email: [null, [Validators.required]],
    senha: [null, [Validators.required]],
    permissao: [null, [Validators.required]],
  });

  client: UserModel;
  constructor(
    private formBuilder: FormBuilder, private editUserService: EditUserService) { }

  ngOnInit(): void {
  }
  // 
  // configurarFormulario() {

  // }
  validar(input: FormControl) {
    return (input.value ? null : { obrigatorio: true });
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
  onlyPasteNumber(field: string, event: any): void {
    this.formCliente.get(field).setValue(onlyPasteNumber(event));
  }
  onSubmit() {
    const user: UserModel = {
      name: `${this.formCliente.get('nome').value.trim()} ${this.formCliente.get('sobrenome').value.trim()}`,
      birthDate: this.formCliente.get('dataNascimento').value,
      password: this.formCliente.get('senha').value,
      email: this.formCliente.get('email').value,
      phoneNumber: this.formCliente.get('celular').value.replace(/\D/g, ''),
      cpf: this.formCliente.get('cpf').value.replace(/\D/g, '')
    };
    this.editUserService.createUser(user).subscribe();
  }
}
