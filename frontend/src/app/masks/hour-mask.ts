/**
 * Máscara para campo de Horas
 * @returns retorna a máscara
 */
 export function hourMask(): (string | RegExp)[] {
    return [/[0-2]/, /\d/, ':', /[0-5]/, /\d/,];
}
