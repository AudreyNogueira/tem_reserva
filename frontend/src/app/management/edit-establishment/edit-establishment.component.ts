import { Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren } from '@angular/core';
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
import { AccountType } from 'src/app/models/account.model';

@Component({
  selector: 'edit-establishment',
  templateUrl: './edit-establishment.component.html',
  styleUrls: ['./edit-establishment.component.scss']
})
export class EditEstablishmentComponent implements OnInit {

  @ViewChild('slide') slide: ElementRef<HTMLElement>;
  @ViewChildren('element') element: QueryList<ElementRef<HTMLElement>>;

  maskPhone = phoneMask;
  maskCnpj = cnpjMask;
  maskCep = cepMask;
  submitted = false;
  pic: string;

  zones: string[] = [
    'Zona Norte',
    'Zona Sul',
    'Zona Leste',
    'Zona Oeste',
  ];

  cleaningProtocol = [
    {
      value: "Distanciamento de mesas",
      checked: false,
    },
    {
      value: "Uso de máscara",
      checked: false,
    },
    {
      value: "Verificação de temperatura",
      checked: false,
    },
    {
      value: "Disponibilidade de Álcool em Gel",
      checked: false,
    }
  ];

  formGroup: FormGroup = this.formBuilder.group({
    restaurantName: [null, [Validators.required]],
    cnpj: [null, [Validators.required, cnpjValidator]],
    phone: [null, [Validators.required, phoneValidator]],
    cep: [null, [Validators.required]],
    zone: [null, [Validators.required]],
    street: [null, [Validators.required]],
    estabNumber: [null, [Validators.required]],
    district: [null, [Validators.required]],
    description: [null, [Validators.required]],
    payment: [null, [Validators.required]],
    cleaning: [null],
    email: [null, [Validators.required, Validators.email]],
    currentPass: [''],
    newPass: [''],
  });

  userData: Establishment;

  constructor(
    private formBuilder: FormBuilder,
    private modalServiceLocal: ModalService,
    private readonly editEstablishmentService: EditEstablishmentService,
  ) { }

  ngOnInit(): void {

    this.editEstablishmentService.$userSession.subscribe(user => {
      this.userData = user.est;
      this.initialValues(this.userData);
    });
  }

  ngOnDestroy(): void {
    this.modalServiceLocal.$comunication.unsubscribe();
  }

  initialValues(userData: Establishment) {
    this.pic = `data:image/png;base64,${this.userData.profileImage.image}`;
    this.formGroup.get('restaurantName').setValue(userData.restaurantName);
    this.formGroup.get('cnpj').setValue(userData.cnpj);
    this.formGroup.get('phone').setValue(userData.phoneNumber);
    this.formGroup.get('cep').setValue(userData.address.cep);
    this.formGroup.get('zone').setValue(userData.address.zone);
    this.formGroup.get('street').setValue(userData.address.address);
    this.formGroup.get('estabNumber').setValue(userData.address.restaurantNumber);
    this.formGroup.get('district').setValue(userData.address.district);
    this.formGroup.get('description').setValue(userData.description);
    this.formGroup.get('payment').setValue(userData.payment);
    this.formGroup.get('email').setValue(userData.email);
    this.treatCheckBox(userData.cleaning ? userData.cleaning : '');
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
        payment: this.formGroup.get('payment').value,
        description: this.formGroup.get('description').value,
      };


      if (this.cleaningProtocol.some(p => p.checked)) {
        let cleaningProtocolValue = '';
        this.cleaningProtocol.filter(p => p.checked).forEach(item => cleaningProtocolValue += item.value + ',');
        data = { ...data, cleaning: cleaningProtocolValue }
      }

      if (this.passwordChange()) data = { ...data, password: this.formGroup.get('newPass').value, actualPassword: this.formGroup.get('currentPass').value };

      this.editEstablishmentService.updateEstablishment(this.userData.id, data).subscribe(() => {
        this.editEstablishmentService.set$userSession(data, AccountType.ESTABLISHMENT);
      }, err => {
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

  treatCheckBox(protocols: string): void {
    const list = protocols.split(',');

    list.forEach(p => {
      this.cleaningProtocol.filter(item => item.value === p).map(c => c.checked = true);
    });
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

  onCheckboxChange(protocol: any) {
    this.cleaningProtocol.filter(p => protocol === p).map(v => v.checked = !v.checked);
  }

  public onFileChanged(event: any, isProfileImg: boolean, index?: number) {
    console.log(index);
    const uploadImageData = new FormData();
    uploadImageData.append('imageFile', event.target.files[0], event.target.files[0].name);
    uploadImageData.append('isProfilePic', String(isProfileImg));
    console.log(uploadImageData.get('imageFile'));

    if (isProfileImg) {
      this.pic = (window.URL ? URL : webkitURL).createObjectURL(event.target.files[0]);
      uploadImageData.append('restaurantId', this.userData.id.toString());
      this.editEstablishmentService.setImage(uploadImageData).subscribe(() => {

      });
    }
  }

  /**
 * Método para rolar o carrossel pelas setas
 * @method navigateCarousel
 * @param direction direção para qual o carrossel vai
 */
  navigateCarousel(direction: string): void {
    direction === 'next' ?
      this.slide.nativeElement.scrollBy({ left: this.element.last.nativeElement.scrollWidth + 50 }) :
      this.slide.nativeElement.scrollBy({ left: -this.element.last.nativeElement.scrollWidth + 50 });
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
