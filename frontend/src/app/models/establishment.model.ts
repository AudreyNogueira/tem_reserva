export interface Establishment {
    email?: string,
    password?: string,
    cnpj?: string,
    restaurantName?: string;
    address?: AddressEstablishment;
    description?: string,
    openDaysOfWeek?: string,
    openingTime?: string,
    closingTime?: string,
    cleaningPeriodicity?: number,
    spacingOfTables?: number,
    phone?: string;
    carousel?: string[];
    actualNumberOfPeople?: number;
    maxNumberOfPeople?: number;
    averageStars?: number;
    image?: string;
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
