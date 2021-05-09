export interface Reserve {
    idUser: number;
    idRestaurant: number;
    period: string;
    reserveDate: Date;
    amountOfPeople: number;
    obs?: string;
}
