/**
 * Máscara para campo de CEP
 * @returns retorna a máscara
 */
 export function cepMask(): (string | RegExp)[] {
    return [/\d/, /\d/, /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/];
}
