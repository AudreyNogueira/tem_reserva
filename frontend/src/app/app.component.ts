import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { BsModalService } from 'ngx-bootstrap/modal';
import { EstablishmentListService } from './establishment-dashboard/services/establishment-list.service';
import { ModalService } from './modals/service/modal.service';
import { Establishment } from './models/establishment.model';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'tem-reserva-front';

  @ViewChild('modalTemplate') modalTemplate: TemplateRef<any>;
  modalName: string;
  loginType: string;

  mock: Establishment[];
  cont = 0;

  constructor(
    private readonly modalService: BsModalService,
    private readonly modalServiceLocal: ModalService,
    private readonly establishmentListService: EstablishmentListService,
  ) { }

  ngOnInit(): void {
    /** Abre a modal na qual foi passada como parâmetro */
    this.modalServiceLocal.$openModal.subscribe(m => {
      this.modalService.show(this.modalTemplate, { class: 'modal-dialog-centered' });
      this.modalName = m.modalName;
      this.loginType = m.loginType;
    });

    this.criarMock();

  }


  /**
   * MOCK
   * TODAS AS TRATATIVAS FEITAS SÃO POR CAUSA DO MOCK
   * SOLUÇÃO PALEATIVA
   */
  criarMock(): void {
    this.mock = [
      {
        password: '123',
        email: 'restaurante1@hotmail.com',
        cnpj: '54644325000136',
        restaurantName: 'Fogão a lenha',
        description: 'teste restaurante',
        openDaysOfWeek: 'MONDAY;TUESDAY',
        openingTime: new Date(0, 0, 0, 9, 0, 0).toLocaleTimeString('pt-BR'),
        closingTime: new Date(0, 0, 0, 18, 0, 0).toLocaleTimeString('pt-BR'),
        cleaningPeriodicity: 15,
        spacingOfTables: 2,
        maxNumberOfPeople: 100,
        address: {
          uf: 'SP',
          complement: '',
          cep: '03208030',
          district: 'Mooca',
          address: 'Rua Serra de Botucatu',
          restaurantNumber: 661,
          locality: 'São Paulo',
          zone: 'Zona Leste',
        }
      },
      {
        password: '123',
        email: 'restaurante2@hotmail.com',
        cnpj: '38064409000144',
        restaurantName: 'Quibebe Cozinha Brasileira',
        description: 'teste restaurante',
        openDaysOfWeek: 'MONDAY;TUESDAY',
        openingTime: new Date(0, 0, 0, 9, 0, 0).toLocaleTimeString('pt-BR'),
        closingTime: new Date(0, 0, 0, 18, 0, 0).toLocaleTimeString('pt-BR'),
        cleaningPeriodicity: 15,
        spacingOfTables: 2,
        maxNumberOfPeople: 200,
        address: {
          uf: 'SP',
          complement: '',
          cep: '76960288',
          district: 'Tatuapé',
          address: 'Rua Emilia Marengo',
          restaurantNumber: 337,
          locality: 'São Paulo',
          zone: 'Zona Oeste'
        }
      },
      {
        password: '123',
        email: 'restaurante3@hotmail.com',
        cnpj: '60012422000109',
        restaurantName: 'Panela de ferro',
        description: 'teste restaurante',
        openDaysOfWeek: 'MONDAY;TUESDAY',
        openingTime: new Date(0, 0, 0, 9, 0, 0).toLocaleTimeString('pt-BR'),
        closingTime: new Date(0, 0, 0, 18, 0, 0).toLocaleTimeString('pt-BR'),
        cleaningPeriodicity: 15,
        spacingOfTables: 2,
        maxNumberOfPeople: 50,
        address: {
          uf: 'SP',
          complement: 'Conjunto D',
          cep: '72321514',
          district: 'Samambaia Norte',
          address: 'Quadra QS',
          restaurantNumber: 407,
          locality: 'São Paulo',
          zone: 'Zona Oeste'
        }
      },
      {
        password: '123',
        email: 'restaurante4@hotmail.com',
        cnpj: '45679986000106',
        restaurantName: 'Bom de garfo',
        description: 'teste restaurante',
        openDaysOfWeek: 'MONDAY;TUESDAY',
        openingTime: new Date(0, 0, 0, 9, 0, 0).toLocaleTimeString('pt-BR'),
        closingTime: new Date(0, 0, 0, 18, 0, 0).toLocaleTimeString('pt-BR'),
        cleaningPeriodicity: 15,
        spacingOfTables: 2,
        maxNumberOfPeople: 90,
        address: {
          uf: 'SP',
          complement: '',
          cep: '18103127',
          district: 'Éden',
          address: 'Estrada Isaltino da Silva',
          restaurantNumber: 265,
          locality: 'São Paulo',
          zone: 'Zona Oeste'
        }
      },
      {
        password: '123',
        email: 'restaurante5@hotmail.com',
        cnpj: '74427961000113',
        restaurantName: 'Cozinha do Zé',
        description: 'teste restaurante',
        openDaysOfWeek: 'MONDAY;TUESDAY',
        openingTime: new Date(0, 0, 0, 9, 0, 0).toLocaleTimeString('pt-BR'),
        closingTime: new Date(0, 0, 0, 18, 0, 0).toLocaleTimeString('pt-BR'),
        cleaningPeriodicity: 15,
        spacingOfTables: 2,
        maxNumberOfPeople: 20,
        address: {
          uf: 'SP',
          complement: '',
          cep: '78138221',
          district: 'Marajoara',
          address: 'Rua Primavera',
          restaurantNumber: 984,
          locality: 'São Paulo',
          zone: 'Zona Sul'
        }
      },
      {
        password: '123',
        email: 'restaurante6@hotmail.com',
        cnpj: '97837844000123',
        restaurantName: 'La gourmet',
        description: 'teste restaurante',
        openDaysOfWeek: 'MONDAY;TUESDAY',
        openingTime: new Date(0, 0, 0, 9, 0, 0).toLocaleTimeString('pt-BR'),
        closingTime: new Date(0, 0, 0, 18, 0, 0).toLocaleTimeString('pt-BR'),
        cleaningPeriodicity: 15,
        spacingOfTables: 2,
        maxNumberOfPeople: 500,
        address: {
          uf: 'SP',
          complement: '',
          cep: '42800500',
          district: 'Centro',
          address: 'Rua Francisco Drumond',
          restaurantNumber: 312,
          locality: 'São Paulo',
          zone: 'Zona Leste'
        }
      },
      {
        password: '123',
        email: 'restaurante7@hotmail.com',
        cnpj: '01021277000163',
        restaurantName: 'Amadeirado',
        description: 'teste restaurante',
        openDaysOfWeek: 'MONDAY;TUESDAY',
        openingTime: new Date(0, 0, 0, 9, 0, 0).toLocaleTimeString('pt-BR'),
        closingTime: new Date(0, 0, 0, 18, 0, 0).toLocaleTimeString('pt-BR'),
        cleaningPeriodicity: 15,
        spacingOfTables: 2,
        maxNumberOfPeople: 20,
        address: {
          uf: 'SP',
          complement: '',
          cep: '68900071',
          district: 'Central',
          address: 'Avenida Duque de Caxias',
          restaurantNumber: 553,
          locality: 'São Paulo',
          zone: 'Zona Norte'
        }
      },
      {
        password: '123',
        email: 'restaurante8@hotmail.com',
        cnpj: '18260770000139',
        restaurantName: 'Cantina rústica',
        description: 'teste restaurante',
        openDaysOfWeek: 'MONDAY;TUESDAY',
        openingTime: new Date(0, 0, 0, 9, 0, 0).toLocaleTimeString('pt-BR'),
        closingTime: new Date(0, 0, 0, 18, 0, 0).toLocaleTimeString('pt-BR'),
        cleaningPeriodicity: 15,
        spacingOfTables: 2,
        maxNumberOfPeople: 660,
        address: {
          uf: 'SP',
          complement: '',
          cep: '60763550',
          district: 'Conjunto Esperança',
          address: 'Rua 106',
          restaurantNumber: 200,
          locality: 'São Paulo',
          zone: 'Zona Sul'
        }
      },
      {
        password: '123',
        email: 'restaurante9@hotmail.com',
        cnpj: '81455584000191',
        restaurantName: 'Sabor campestre',
        description: 'teste restaurante',
        openDaysOfWeek: 'MONDAY;TUESDAY',
        openingTime: new Date(0, 0, 0, 9, 0, 0).toLocaleTimeString('pt-BR'),
        closingTime: new Date(0, 0, 0, 18, 0, 0).toLocaleTimeString('pt-BR'),
        cleaningPeriodicity: 15,
        spacingOfTables: 2,
        maxNumberOfPeople: 94,
        address: {
          uf: 'SP',
          complement: '',
          cep: '78121142',
          district: 'Parque do Lago',
          address: 'Rua dos Jaçanãs',
          restaurantNumber: 2262,
          locality: 'São Paulo',
          zone: 'Zona Leste'
        }
      },
      {
        password: '123',
        email: 'restaurante10@hotmail.com',
        cnpj: '23233862000160',
        restaurantName: 'Delícias do Rancho',
        description: 'teste restaurante',
        openDaysOfWeek: 'MONDAY;TUESDAY',
        openingTime: new Date(0, 0, 0, 9, 0, 0).toLocaleTimeString('pt-BR'),
        closingTime: new Date(0, 0, 0, 18, 0, 0).toLocaleTimeString('pt-BR'),
        cleaningPeriodicity: 15,
        spacingOfTables: 2,
        maxNumberOfPeople: 330,
        address: {
          uf: 'SP',
          complement: '',
          cep: '79081728',
          district: 'Jardim das Nações',
          address: 'Rua Galdina Ifran Catarinelli',
          restaurantNumber: 8439,
          locality: 'São Paulo',
          zone: 'Zona Norte'
        }
      },
      {
        password: '123',
        email: 'restaurante11@hotmail.com',
        cnpj: '55300154000190',
        restaurantName: 'Kyoto Sushi;',
        description: 'teste restaurante',
        openDaysOfWeek: 'MONDAY;TUESDAY',
        openingTime: new Date(0, 0, 0, 9, 0, 0).toLocaleTimeString('pt-BR'),
        closingTime: new Date(0, 0, 0, 18, 0, 0).toLocaleTimeString('pt-BR'),
        cleaningPeriodicity: 15,
        spacingOfTables: 2,
        maxNumberOfPeople: 260,
        address: {
          uf: 'SP',
          complement: '',
          cep: '78005909',
          district: 'Araés',
          address: 'Travessa São João',
          restaurantNumber: 13,
          locality: 'São Paulo',
          zone: 'Zona Sul'
        }
      },
      {
        password: '123',
        email: 'restaurante12@hotmail.com',
        cnpj: '03791612000138',
        restaurantName: 'Oishii ',
        description: 'teste restaurante',
        openDaysOfWeek: 'MONDAY;TUESDAY',
        openingTime: new Date(0, 0, 0, 9, 0, 0).toLocaleTimeString('pt-BR'),
        closingTime: new Date(0, 0, 0, 18, 0, 0).toLocaleTimeString('pt-BR'),
        cleaningPeriodicity: 15,
        spacingOfTables: 2,
        maxNumberOfPeople: 20,
        address: {
          uf: 'SP',
          complement: '',
          cep: '57052421',
          district: 'Gruta de Lourdes',
          address: 'Quadra L',
          restaurantNumber: 200,
          locality: 'São Paulo',
          zone: 'Zona Sul'
        }
      },
      {
        password: '123',
        email: 'restaurante13@hotmail.com',
        cnpj: '68643460000171',
        restaurantName: 'Sabor natural',
        description: 'teste restaurante',
        openDaysOfWeek: 'MONDAY;TUESDAY',
        openingTime: new Date(0, 0, 0, 9, 0, 0).toLocaleTimeString('pt-BR'),
        closingTime: new Date(0, 0, 0, 18, 0, 0).toLocaleTimeString('pt-BR'),
        cleaningPeriodicity: 15,
        spacingOfTables: 2,
        maxNumberOfPeople: 40,
        address: {
          uf: 'SP',
          complement: '',
          cep: '49090293',
          district: 'Jardim Centenário',
          address: 'Travessa Um',
          restaurantNumber: 521,
          locality: 'São Paulo',
          zone: 'Zona Oeste'
        }
      },
      {
        password: '123',
        email: 'restaurante14@hotmail.com',
        cnpj: '95874942000105',
        restaurantName: 'Green house',
        description: 'teste restaurante',
        openDaysOfWeek: 'MONDAY;TUESDAY',
        openingTime: new Date(0, 0, 0, 9, 0, 0).toLocaleTimeString('pt-BR'),
        closingTime: new Date(0, 0, 0, 18, 0, 0).toLocaleTimeString('pt-BR'),
        cleaningPeriodicity: 15,
        spacingOfTables: 2,
        maxNumberOfPeople: 20,
        address: {
          uf: 'SP',
          complement: '',
          cep: '82400310',
          district: 'Butiatuvinha',
          address: 'Rua Aécio Marcelo Antoniacomi',
          restaurantNumber: 3,
          locality: 'São Paulo',
          zone: 'Zona Sul'
        }
      },
      {
        password: '123',
        email: 'restaurante15@hotmail.com',
        cnpj: '26167556000160',
        restaurantName: 'Nachos & Tacos',
        description: 'teste restaurante',
        openDaysOfWeek: 'MONDAY;TUESDAY',
        openingTime: new Date(0, 0, 0, 9, 0, 0).toLocaleTimeString('pt-BR'),
        closingTime: new Date(0, 0, 0, 18, 0, 0).toLocaleTimeString('pt-BR'),
        cleaningPeriodicity: 15,
        spacingOfTables: 2,
        maxNumberOfPeople: 80,
        address: {
          uf: 'SP',
          complement: '',
          cep: '66842030',
          district: 'Estrada Itaiteua',
          address: 'Itaiteua',
          restaurantNumber: 200,
          locality: 'São Paulo',
          zone: 'Zona Leste'
        }
      },
      {
        password: '123',
        email: 'restaurante16@hotmail.com',
        cnpj: '44819949000193',
        restaurantName: 'Margarita',
        description: 'teste restaurante',
        openDaysOfWeek: 'MONDAY;TUESDAY',
        openingTime: new Date(0, 0, 0, 9, 0, 0).toLocaleTimeString('pt-BR'),
        closingTime: new Date(0, 0, 0, 18, 0, 0).toLocaleTimeString('pt-BR'),
        cleaningPeriodicity: 15,
        spacingOfTables: 2,
        maxNumberOfPeople: 132,
        address: {
          uf: 'SP',
          complement: '',
          cep: '55022166',
          district: 'Rendeiras',
          address: 'Rua Casa Nova',
          restaurantNumber: 8195,
          locality: 'São Paulo',
          zone: 'Zona Norte'
        }
      },
      {
        password: '123',
        email: 'restaurante17@hotmail.com',
        cnpj: '65781701000150',
        restaurantName: 'Palak Panner',
        description: 'teste restaurante',
        openDaysOfWeek: 'MONDAY;TUESDAY',
        openingTime: new Date(0, 0, 0, 9, 0, 0).toLocaleTimeString('pt-BR'),
        closingTime: new Date(0, 0, 0, 18, 0, 0).toLocaleTimeString('pt-BR'),
        cleaningPeriodicity: 15,
        spacingOfTables: 2,
        maxNumberOfPeople: 70,
        address: {
          uf: 'SP',
          complement: '',
          cep: '61934075',
          district: 'Jardim Bandeirantes',
          address: 'Rua Central',
          restaurantNumber: 200,
          locality: 'São Paulo',
          zone: 'Zona Norte'
        }
      },
      {
        password: '123',
        email: 'restaurante18@hotmail.com',
        cnpj: '36411258000128',
        restaurantName: 'Varanasi',
        description: 'teste restaurante',
        openDaysOfWeek: 'MONDAY;TUESDAY',
        openingTime: new Date(0, 0, 0, 9, 0, 0).toLocaleTimeString('pt-BR'),
        closingTime: new Date(0, 0, 0, 18, 0, 0).toLocaleTimeString('pt-BR'),
        cleaningPeriodicity: 15,
        spacingOfTables: 2,
        maxNumberOfPeople: 62,
        address: {
          uf: 'SP',
          complement: '',
          cep: '52080251',
          district: 'Alto José Bonifácio',
          address: 'Rua Abreus',
          restaurantNumber: 642,
          locality: 'São Paulo',
          zone: 'Zona Leste'
        }
      },
      {
        password: '123',
        email: 'restaurante19@hotmail.com',
        cnpj: '70557131000150',
        restaurantName: 'Forte de Amber',
        description: 'teste restaurante',
        openDaysOfWeek: 'MONDAY;TUESDAY',
        openingTime: new Date(0, 0, 0, 9, 0, 0).toLocaleTimeString('pt-BR'),
        closingTime: new Date(0, 0, 0, 18, 0, 0).toLocaleTimeString('pt-BR'),
        cleaningPeriodicity: 15,
        spacingOfTables: 2,
        maxNumberOfPeople: 10,
        address: {
          uf: 'SP',
          complement: '',
          cep: '24350025',
          district: 'Piratininga',
          address: 'Rua Oitenta e Oito',
          restaurantNumber: 110,
          locality: 'São Paulo',
          zone: 'Zona Sul'
        }
      },
      {
        password: '123',
        email: 'restaurante20@hotmail.com',
        cnpj: '85215953000120',
        restaurantName: 'Recanto do Nordeste',
        description: 'teste restaurante',
        openDaysOfWeek: 'MONDAY;TUESDAY',
        openingTime: new Date(0, 0, 0, 9, 0, 0).toLocaleTimeString('pt-BR'),
        closingTime: new Date(0, 0, 0, 18, 0, 0).toLocaleTimeString('pt-BR'),
        cleaningPeriodicity: 15,
        spacingOfTables: 2,
        maxNumberOfPeople: 20,
        address: {
          uf: 'SP',
          complement: '',
          cep: '35054818',
          district: 'Figueira do Rio Doce',
          address: 'Rua C',
          restaurantNumber: 297,
          locality: 'São Paulo',
          zone: 'Zona Leste'
        }
      },
    ];

    let id = 0;
    this.establishmentListService.getEstablishmentById(1).subscribe(() => { },
      err => {
        if (err.error.apicode === '0006') {

          this.mock.forEach(m => {
            id++;
            this.establishmentListService.postEstab(m).subscribe(() => this.test(id));
          });
        }
      });
  }

  test(id?: any): void {
    const num = Math.floor(Math.random() * (8) + 1);
    let blob = null;
    const xhr = new XMLHttpRequest();
    let file;
    xhr.open('GET', `/assets/images/Painting${num}.png`);
    xhr.responseType = 'blob';
    xhr.onload = () => {
      blob = xhr.response;
      file = new File([blob], 'logo.png', { type: 'image/png' });
      const uploadImageData = new FormData();
      uploadImageData.append('imageFile', file, file.name);
      uploadImageData.append('restaurantId', id);
      setTimeout(() => {
        this.establishmentListService.setImage(uploadImageData).subscribe(() => {
          this.cont++;
          if (this.cont === this.mock.length) {
            setTimeout(() => {
              this.establishmentListService.loadMock$.next(true);
            }, 500);
          }
        });
      }, 750);
    };
    xhr.send();
  }
  /** MOCK */
}
