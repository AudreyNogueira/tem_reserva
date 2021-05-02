import { ValidatorFn, AbstractControl, ValidationErrors } from '@angular/forms';

const regex = '^[\(]?[0-9]{2}[\)]?[ ]?[0-9]?[0-9]{4,5}[\-]?[0-9]{4}$';
const initialInvalidNumbers: string[] = ['0', '1'];

/**
 * Função para verificar se o telefone é válido
 * @param control controle do telefone
 * @returns validador de telefone
 */

export const phoneValidator: ValidatorFn = (control: AbstractControl): ValidationErrors => {
    const value: string = control.value;

    if (!value) {
        return {};
    }

    if (!value.match(regex)) {
        return { pattern: true };
    }

    const unmasked = control.value.replace(/\D/g, '');

    const charArrayWithDDD = unmasked.split('');
    const charArrayWithoutDDD = charArrayWithDDD;
    charArrayWithoutDDD.splice(0, 2);

    if (initialInvalidNumbers.includes(charArrayWithoutDDD[0])) {
        return { invalid: true };
    }

    // Verifica se o numero inteiro (tanto com DDD igual quanto DDD diferente) é composto pelo mesmo dígito
    const sameDigitsWithDDD = charArrayWithDDD.filter(char => {
        const firstChar = charArrayWithDDD[0];
        return char.toLowerCase() === firstChar.toLowerCase();
    });
    const sameDigitsWithoutDDD = charArrayWithoutDDD.filter(char => {
        const firstChar = charArrayWithoutDDD[0];
        return char.toLowerCase() === firstChar.toLowerCase();
    });

    if ((sameDigitsWithoutDDD.length === charArrayWithDDD.length)
        || (sameDigitsWithDDD.length === charArrayWithoutDDD.length)) {
        return { invalid: true };
    }

    return {};
};
