const BLOCK_2: RegExp[] = [/[0-9]/, /[0-9]/];
const BLOCK_3: RegExp[] = [/[0-9]/, /[0-9]/, /[0-9]/];

/**
 * Máscara para campo de CPF
 * @returns retorna a máscara
 */
export function cpfMask(): (RegExp | string)[] {
    return [...BLOCK_3, '.', ...BLOCK_3, '.', ...BLOCK_3, '-', ...BLOCK_2];
}
