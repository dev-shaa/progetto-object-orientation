package Utilities.Functions;

/**
 * Rappresenta un'operazione che accetta un input e restituisce un valore, con la possibilità di lanciare un eccezione controllata.
 */
@FunctionalInterface
public interface CheckedFunction<T, R, E extends Exception> {

    /**
     * Chiama la funzione.
     * 
     * @param argument
     *            valore di input
     * @return valore di output
     * @throws E
     *             se non è possibile eseguire l'operazione
     */
    public R call(T argument) throws E;
}
