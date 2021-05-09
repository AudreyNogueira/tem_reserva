import { Component, ElementRef, OnDestroy, OnInit, QueryList, ViewChild, ViewChildren } from '@angular/core';
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
import { DayOfWeekEnum } from '../../models/week.enum';
import { hourMask } from '../../masks/hour-mask';
import { hourValidator } from '../../validators/hour-validator';
import * as moment from 'moment';
import { DayHour, DayOfWeekModel } from '../../models/day-hour.model';

@Component({
  selector: 'edit-establishment',
  templateUrl: './edit-establishment.component.html',
  styleUrls: ['./edit-establishment.component.scss']
})
export class EditEstablishmentComponent implements OnInit, OnDestroy {

  @ViewChild('slide') slide: ElementRef<HTMLElement>;
  @ViewChildren('element') element: QueryList<ElementRef<HTMLElement>>;

  maskPhone = phoneMask;
  maskCnpj = cnpjMask;
  maskCep = cepMask;
  maskHour = hourMask;
  submitted = false;
  submittedHour = false;
  countIdHour = 0;
  pic: string;
  hourArray: DayHour[] = [];

  /**
   * Serve apenas commo parâmetro para a ordenação
   */
  weekEnum = {
    [DayOfWeekEnum.SUNDAY]: 1,
    [DayOfWeekEnum.MONDAY]: 2,
    [DayOfWeekEnum.TUESDAY]: 3,
    [DayOfWeekEnum.WEDNESDAY]: 4,
    [DayOfWeekEnum.THURSDAY]: 5,
    [DayOfWeekEnum.FRIDAY]: 6,
    [DayOfWeekEnum.SATURDAY]: 7,
    [DayOfWeekEnum.HOLIDAY]: 8,
  };

  dayOfWeek = [
    null,
    DayOfWeekEnum.SUNDAY,
    DayOfWeekEnum.TUESDAY,
    DayOfWeekEnum.MONDAY,
    DayOfWeekEnum.FRIDAY,
    DayOfWeekEnum.WEDNESDAY,
    DayOfWeekEnum.THURSDAY,
    DayOfWeekEnum.SATURDAY,
    DayOfWeekEnum.HOLIDAY,
  ];

  zones: string[] = [
    'Zona Norte',
    'Zona Sul',
    'Zona Leste',
    'Zona Oeste',
  ];

  cleaningProtocol = [
    {
      value: 'Distanciamento de mesas',
      checked: false,
    },
    {
      value: 'Uso de máscara',
      checked: false,
    },
    {
      value: 'Verificação de temperatura',
      checked: false,
    },
    {
      value: 'Disponibilidade de Álcool em Gel',
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
    hourWork: [null, [Validators.required]],
    description: [null, [Validators.required]],
    payment: [null, [Validators.required]],
    cleaning: [null],
    email: [null, [Validators.required, Validators.email]],
    currentPass: [''],
    newPass: [''],
  });

  formHour: FormGroup = this.formBuilder.group({
    day: [null, [Validators.required]],
    open: [null, [Validators.required, hourValidator]],
    close: [null, [Validators.required, hourValidator]],
  });

  userData: Establishment;

  constructor(
    private formBuilder: FormBuilder,
    private modalServiceLocal: ModalService,
    private readonly editEstablishmentService: EditEstablishmentService,
  ) { }

  ngOnInit(): void {
    this.dayOfWeek = this.sortDayOfWeek(this.dayOfWeek);

    this.editEstablishmentService.$userSession.subscribe(user => {
      if (user) {
        this.userData = user.est;
        this.initialValues(this.userData);
      }
    });
  }

  sortDayOfWeek(daysList: DayOfWeekEnum[]): DayOfWeekEnum[] {
    return daysList.sort((a, b) => this.weekEnum[a] - this.weekEnum[b]);
  }

  ngOnDestroy(): void {
    /** Se desinscreve do Observable */
    this.modalServiceLocal.$comunication.unsubscribe();
  }

  /**
   * Método para tratar os dados do restaurante na tela de edição
   * @param userData objeto do estabelecimento
   */
  initialValues(userData: Establishment): void {
    this.pic = `data:image/png;base64,${this.userData.profileImage.image}`;
    this.formGroup.get('restaurantName').setValue(userData.restaurantName);
    this.formGroup.get('cnpj').setValue(userData.cnpj);
    this.formGroup.get('phone').setValue(userData.phoneNumber);
    this.formGroup.get('cep').setValue(userData.address.cep);
    this.formGroup.get('zone').setValue(userData.address.zone);
    this.formGroup.get('street').setValue(userData.address.address);
    this.formGroup.get('estabNumber').setValue(userData.address.restaurantNumber);
    this.formGroup.get('district').setValue(userData.address.district);
    this.treatOpenDays(userData.openDaysOfWeek ? userData.openDaysOfWeek : []);
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
    });
  }

  /**
   * Método responsável por chamar o serviço para
   * alterar os dados
   */
  saveEdit(): void {
    this.submitted = true;

    /** Verifica se o formulário é válido */
    if (!this.validateForm(this.formGroup)) {

      /** Monta o objeto a ser enviado */
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
        openDaysOfWeek: this.mapOpenDaysOfWeek(this.formGroup.get('hourWork').value)
      };

      /** Verifica se possui critérios de limpeza, caso sim, adiciona
       * eles no objeto a ser enviado
       */
      if (this.cleaningProtocol.some(p => p.checked)) {
        let cleaningProtocolValue = '';
        this.cleaningProtocol.filter(p => p.checked).forEach(item => cleaningProtocolValue += item.value + ',');
        data = { ...data, cleaning: cleaningProtocolValue };
      }

      /** Verifica se possui informações nos campos de senhas,
       * caso sim, adiciona else no objeto a ser enviado
       */
      if (this.passwordChange()) data = { ...data, password: this.formGroup.get('newPass').value, actualPassword: this.formGroup.get('currentPass').value };

      /** Chama o serviço que atualizará os dados do restaurante */
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
  validateForm(group: FormGroup): boolean {
    let hasError = false;
    let control: AbstractControl | null = null;
    Object.keys(group.controls).forEach(f => {
      control = group.get(f);
      if (control && control.invalid) {
        control.markAsDirty();
        hasError = true;
      }
    });

    /**
     * Faz uma validação a parte dos campos de senha
     */
    if (group === this.formGroup && this.passwordChange()) {
      hasError = !(this.formGroup.get('currentPass').value !== '' && this.formGroup.get('newPass').value !== '');
    }

    if (
      group === this.formHour &&
      this.formHour.valid &&
      moment(group.get('close').value, 'HH:mm').isSameOrBefore(moment(group.get('open').value, 'HH:mm'))
    ) {
      group.get('close').setErrors({ invalidHour: true });
      hasError = true;
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
   * Método para tratar os critérios de limpeza ja cadastrados
   * do restaurante na tela de edição
   * @param list critérios de limpeza
   */
  treatCheckBox(protocols: string): void {
    const list = protocols.split(',');

    list.forEach(p => {
      this.cleaningProtocol.filter(item => item.value === p).map(c => c.checked = true);
    });
  }

  /**
   * Método para tratar os horários ja cadastrados do restaurante
   * na tela de edição
   * @param list lista de horários
   */
  treatOpenDays(list: DayOfWeekModel[]): void {
    list.forEach(d => {
      this.formHour.get('day').setValue(d.day);
      this.formHour.get('open').setValue(moment({ h: Number(d.open.split(':')[0]), m: Number(d.open.split(':')[1]) }).format('HH:mm'));
      this.formHour.get('close').setValue(moment({ h: Number(d.close.split(':')[0]), m: Number(d.close.split(':')[1]) }).format('HH:mm'));
      this.addHour();
    });
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
   * Alterna o valor de selecionad do checkbox
   * @param protocol checkbox que será modificado
   */
  onCheckboxChange(protocol: any): void {
    this.cleaningProtocol.filter(p => protocol === p).map(v => v.checked = !v.checked);
  }

  /**
   * Atualiza ou adiciona novas imagens para o restaurante
   * @param event imagem
   * @param isProfileImg parâmetro para validar se é imagem de perfil
   */
  onFileChanged(event: any, isProfileImg: boolean): void {
    const uploadImageData = new FormData();
    uploadImageData.append('imageFile', event.target.files[0], event.target.files[0].name);
    uploadImageData.append('isProfilePic', String(isProfileImg));
    uploadImageData.append('restaurantId', this.userData.id.toString());

    if (isProfileImg) {
      this.pic = (window.URL ? URL : webkitURL).createObjectURL(event.target.files[0]);
    }
    this.editEstablishmentService.setImage(uploadImageData).subscribe(resp => {
      /** Atualiza o LocalStorage */
      if (isProfileImg) this.editEstablishmentService.set$userSession({ ...this.userData, profileImage: resp }, AccountType.ESTABLISHMENT);
      else this.editEstablishmentService.set$userSession(
        {
          ...this.userData,
          profileImage: this.userData.restaurantImages.push(resp)
        },
        AccountType.ESTABLISHMENT
      );
    });
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
   * Cadastra o horário de funcionamento
   */
  addHour(): void {
    this.submittedHour = true;
    /** Valida se o formulário de horário */
    if (!this.validateForm(this.formHour)) {
      const itemHour: DayHour = {
        id: this.countIdHour++,
        dayOfWeek: {
          day: this.formHour.get('day').value,
          open: this.treatMidnight(this.formHour.get('open').value),
          close: this.treatMidnight(this.formHour.get('close').value)
        }
      };

      this.hourArray.push(itemHour);
      this.hourArray = this.hourArray.sort((a, b) => this.weekEnum[a.dayOfWeek.day] - this.weekEnum[b.dayOfWeek.day]);
      this.formGroup.get('hourWork').setValue(this.hourArray);

      /** Remove da lista de dias da semana o dia já utilizado */
      this.dayOfWeek = this.sortDayOfWeek(this.dayOfWeek.filter(d => d !== this.formHour.get('day').value));

      /** Reset no formulário */
      this.formHour.reset();

      /** Transforma variável em falso para não validar o formulário até ser enviado novamente */
      this.submittedHour = false;
    }
  }

  /**
   * Faz tratativa quando o horário é meia noite
   * @param time horário
   * @returns retorna o horário tratadando meia noite como 00 e não 24
   */
  treatMidnight(time: string): string {
    if (Number(time.split(':')[0]) === 24) return '00:' + time.split(':')[1];
    return time;
  }

  /**
   * Mapeia o objeto para um array de DayOfWeekModel
   * @param list lista de horários
   * @returns lista de DayOfWeekModel
   */
  mapOpenDaysOfWeek(list: DayHour[]): DayOfWeekModel[] {
    return list.map(hr => {
      hr.dayOfWeek.open = moment({ h: Number(hr.dayOfWeek.open.split(':')[0]), m: Number(hr.dayOfWeek.open.split(':')[1]), s: 0 }).format('HH:mm:ss');
      hr.dayOfWeek.close = moment(
        {
          h: Number(hr.dayOfWeek.close.split(':')[0]),
          m: Number(hr.dayOfWeek.close.split(':')[1]),
          s: 0
        }).format('HH:mm:ss');
      return hr.dayOfWeek;
    });
  }

  /**
   * Remove horário de funcionamento, e popula novamente a lista dos dias da semana
   * @param date Horário de funcionamento que será removido
   */
  removeHour(date: DayHour): void {
    this.dayOfWeek.push(date.dayOfWeek.day as DayOfWeekEnum);
    this.dayOfWeek = this.sortDayOfWeek(this.dayOfWeek);
    this.hourArray = this.hourArray.filter(h => h.id !== date.id);
  }

  /**
   * Método para deletar imagem do Restaurante
   * @param id id da imagem
   */
  deleteImage(id: number): void {
    this.editEstablishmentService.deleteImage(id).subscribe(() => {
      /** Salva no LocalStorage */
      this.editEstablishmentService.set$userSession(
        {
          ...this.userData,
          restaurantImages: this.userData.restaurantImages.filter(r => r.id !== id)
        },
        AccountType.ESTABLISHMENT
      );
    });
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
