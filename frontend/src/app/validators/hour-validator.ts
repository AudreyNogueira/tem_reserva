import { ValidatorFn, AbstractControl, ValidationErrors } from '@angular/forms';

/**
 * Função para verificar se o Horário é válido
 * @param control controle do Horário
 * @returns validador de Horário
 */

export const hourValidator: ValidatorFn = (control: AbstractControl): ValidationErrors => {

    const value: string = control.value;

    if (!value) {
        return {};
    }

    const hour = Number(value.split(':')[0]);
    const minute = Number(value.split(':')[1]);

    if (isNaN(hour) || isNaN(minute) || hour > 24) {
        return { invalidHour: true };
    }
};
