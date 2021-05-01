import { ValidatorFn, AbstractControl, ValidationErrors } from '@angular/forms';

/**
 * Função para verificar se o CNPJ é válido
 * @param control controle do CNPJ
 * @returns validador de CNPJ
 */

export const cnpjValidator: ValidatorFn = (control: AbstractControl): ValidationErrors => {
    let valorCnpj: string = control.value;

    if (!valorCnpj) {
        return {};
    }

    const multiplicador1: number[] = [5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2];
    const multiplicador2: number[] = [6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2];
    let soma: number;
    let resto: number;
    let digito: string;
    let tempCnpj: string;
    valorCnpj = valorCnpj.trim();
    valorCnpj = valorCnpj.replace(/[\.\-\/]/g, '');

    if (valorCnpj.length !== 14) {
        return { invalidCnpj: true };
    }
    let isEqual = true;

    for (let i = 1; i < 14 && isEqual; i++) {
        if (valorCnpj[i] !== valorCnpj[0]) {
            isEqual = false;
        }
    }

    if (isEqual) {
        return { invalidCnpj: true };
    }

    tempCnpj = valorCnpj.substring(0, 12);
    soma = 0;
    for (let i = 0; i < 12; i++) {
        soma += Number(tempCnpj[i].toString()) * multiplicador1[i];
    }
    resto = (soma % 11);
    if (resto < 2) {
        resto = 0;
    } else {
        resto = 11 - resto;
    }
    digito = resto.toString();
    tempCnpj = tempCnpj + digito;
    soma = 0;
    for (let i = 0; i < 13; i++) {
        soma += Number(tempCnpj[i].toString()) * multiplicador2[i];
    }
    resto = (soma % 11);
    if (resto < 2) {
        resto = 0;
    } else {
        resto = 11 - resto;
    }
    digito = digito + resto.toString();
    return valorCnpj.endsWith(digito) ? {} : { invalidCnpj: true };
};
