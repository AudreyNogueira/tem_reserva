import { DayOfWeekModel } from './day-hour.model';

export interface Establishment {
    id?: number;
    email?: string;
    password?: string;
    cnpj?: string;
    restaurantName?: string;
    address?: AddressEstablishment;
    description?: string;
    openDaysOfWeek?: DayOfWeekModel[];
    cleaning?: string;
    spacingOfTables?: number;
    phoneNumber?: string;
    carousel?: string[];
    actualNumberOfPeople?: number;
    maxNumberOfPeople?: number;
    averageStars?: number;
    restaurantImages?: Image[];
    profileImage?: Image;
    actualPassword?: string;
    payment?: string;
}

export interface EstablishmentZone {
    topRated: Establishment[];
    zone: Zone[];
}

export interface Zone {
    zoneName: string;
    topRated?: Establishment[];
    restaurantList: Establishment[];
    maxItems?: number;
}

export interface AddressEstablishment {
    address: string;
    cep: string;
    complement: string;
    district: string;
    locality: string;
    restaurantNumber: number;
    uf: string;
    zone: string;
}

export interface Image {
    id: number;
    image: string;
}
