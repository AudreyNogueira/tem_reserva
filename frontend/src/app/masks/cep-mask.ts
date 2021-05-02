/**
 * Máscara para campo de CEP
 * @param cep CEP
 * @returns retorna a máscara
 */
 export function cepMask(): (string | RegExp)[] {
    return [/\d/, /\d/, /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/];
}
