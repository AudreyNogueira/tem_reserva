import { Establishment } from './establishment.model';

export interface ModalData {
    modalName?: any;
    loginType?: string;
    choosedDay?: Date;
    establishment?: Establishment;
    type?: string;
    message?: string;
}
