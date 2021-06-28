import { Establishment } from "./establishment.model";
import { UserModel } from "./user.model";

export interface AuthModel {
    user: string,
    password: string,
    loginType: string,
    grant_type?: string,
}

export interface AuthResponse {
    accessToken: string,
    restaurant?: Establishment,
    user?: UserModel,
}