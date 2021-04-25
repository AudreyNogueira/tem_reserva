/**
 * Permite somente as teclas numéricas
 * @param event evento do teclado
 */
export function onlyInputNumber(event: any): void {
    const key = event.keyCode;
    if ((event.shift === true) || (key === 32) || (key < 48 || key > 57)) {
        if (event.preventDefault) {
            event.preventDefault();
        }
    }
}

/**
 * Retorna o valor da ação `COLAR` somente com os valores numéricos
 * @param event evento `COLAR`
 * @returns {string} somente valores numéricos
 */
export function onlyPasteNumber(event: any): string {
    event.preventDefault();
    const value = event.clipboardData.getData('Text');
    const numbers = value.replace(/[^0-9]/g, '');
    event.target.value = numbers;
    return event.target.value;
}
