/**
 * Máscara para campo de telefone
 * @param phone telefone
 * @returns retorna a máscara
 */
export function phoneMask(phone: string): (string | RegExp)[] {
    const numbers = (phone || '').replace(/\D/g, '');
    if (numbers.length <= 10)
        return ['(', /[1-9]/, /[1-9]/, ')', ' ', /\d/, /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, /\d/];
    return ['(', /[1-9]/, /[1-9]/, ')', ' ', /\d/, /\d/, /\d/, /\d/, /\d/, '-', /\d/, /\d/, /\d/, /\d/];
}
