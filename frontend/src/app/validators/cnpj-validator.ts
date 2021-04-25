/**
 * Função para verificar se o CNPJ é válido
 * @param cnpj CNPJ
 * @returns {boolean} `true` = se o CNPJ for válido, `falso` = se o CNPJ for inválido
 */
export function cnpjValidator(cnpj: string): boolean {

    if (cnpj === '' || cnpj === null) return false;

    cnpj = cnpj.replace(/[^\d]+/g, '');

    if (cnpj.length !== 14) return false;

    if (Object.keys(cnpj).every(n => cnpj.charAt(0) === cnpj[n])) return false;

    let tamanho;
    let numeros;
    let digitos;
    let soma;
    let pos;
    let resultado;

    tamanho = cnpj.length - 2;
    numeros = cnpj.substring(0, tamanho);
    digitos = cnpj.substring(tamanho);
    soma = 0;
    pos = tamanho - 7;
    for (let i = tamanho; i >= 1; i--) {
        soma += numeros.charAt(tamanho - i) * pos--;
        if (pos < 2) pos = 9;
    }
    resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
    if (resultado !== Number(digitos.charAt(0))) return false;

    tamanho = tamanho + 1;
    numeros = cnpj.substring(0, tamanho);
    soma = 0;
    pos = tamanho - 7;
    for (let i = tamanho; i >= 1; i--) {
        soma += numeros.charAt(tamanho - i) * pos--;
        if (pos < 2) pos = 9;
    }
    resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
    if (resultado !== Number(digitos.charAt(1))) return false;

    return true;
}
