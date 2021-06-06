import { Establishment } from "./establishment.model";
import { UserModel } from "./user.model";

export interface Reserve {
    id?: number;
    idUser?: number;
    idRestaurant?: number;
    period: string;
    reserveDate: Date;
    amountOfPeople: number;
    observation?: string;
    user?: UserModel;
    restaurant?: Establishment,
    confirmed?: boolean;
}

export interface ReservePerDay {
    day: string;
    maxNumberOfPeople: number;
    reserves: Reserve[];
    currentPeople: number;
}
