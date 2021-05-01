import { ValidatorFn, AbstractControl, ValidationErrors } from '@angular/forms';

/**
 * Função para verificar se o CPF é válido
 * @param control controle do CPF
 * @returns validador de cpf
 */

export const cpfValidator: ValidatorFn = (control: AbstractControl): ValidationErrors => {

    const value: string = control.value ? control.value.replace(/\D/g, '') : '';

    if (!value) {
        return {};
    }

    if (value.length !== 11) {
        return { invalidCpf: true };
    }

    let isEqual = true;

    for (let i = 1; i < 11 && isEqual; i++) {
        if (value[i] !== value[0]) {
            isEqual = false;
        }
    }

    if (isEqual || value === '12345678909') {
        return { invalidCpf: true };
    }

    const numbers: number[] = [];

    for (let i = 0; i < 11; i++) {
        numbers[i] = Number(value[i].toString());
    }

    let sumValue = 0;

    for (let i = 0; i < 9; i++) {
        sumValue += (10 - i) * numbers[i];
    }

    let resultValue = sumValue % 11;

    if (resultValue === 1 || resultValue === 0) {
        if (numbers[9] !== 0) {
            return { invalidCpf: true };
        }
    } else if (numbers[9] !== 11 - resultValue) {
        return { invalidCpf: true };
    }

    sumValue = 0;

    for (let i = 0; i < 10; i++) {
        sumValue += (11 - i) * numbers[i];
    }

    resultValue = sumValue % 11;

    if (resultValue === 1 || resultValue === 0) {
        if (numbers[10] !== 0) {
            return { invalidCpf: true };
        }
    } else {
        if (numbers[10] !== 11 - resultValue) {
            return { invalidCpf: true };
        }
    }
    return {};
};
