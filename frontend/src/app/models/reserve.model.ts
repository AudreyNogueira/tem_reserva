import { UserModel } from "./user.model";

export interface Reserve {
    idReserve?: number;
    idUser?: number;
    idRestaurant?: number;
    period: string;
    reserveDate: Date;
    amountOfPeople: number;
    observation?: string;
    user?: UserModel;
}

export interface ReservePerDay {
    day: string;
    maxNumberOfPeople: number;
    reserves: Reserve[];
    currentPeople: number;
}
