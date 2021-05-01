const BLOCK_2: RegExp[] = [/[0-9]/, /[0-9]/];
const BLOCK_3: RegExp[] = [/[0-9]/, /[0-9]/, /[0-9]/];
const BLOCK_4: RegExp[] = [/[0-9]/, /[0-9]/, /[0-9]/, /[0-9]/];

/**
 * Máscara para campo de CNPJ
 * @returns retorna a máscara
 */
export function cnpjMask(): (RegExp | string)[] {
    return [...BLOCK_2, '.', ...BLOCK_3, '.', ...BLOCK_3, '/', ...BLOCK_4, '-', ...BLOCK_2];
}
