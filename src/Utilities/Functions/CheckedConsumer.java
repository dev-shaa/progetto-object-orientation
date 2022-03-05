package Utilities.Functions;

/**
 * Rappresenta un'operazione che accetta un input e non restituisce un valore, con la possibilità di lanciare un eccezione controllata.
 */
@FunctionalInterface
public interface CheckedConsumer<T, E extends Exception> {

    /**
     * Chiama la procedura.
     * 
     * @param argument
     *            valore di input
     * @throws E
     *             se non è possibile eseguire l'operazione
     */
    public void call(T argument) throws E;
}