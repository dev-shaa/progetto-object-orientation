package Utilities.Functions;

/**
 * Rappresenta un'operazione che accetta un input e non restituisce un valore, con la possibilità di lanciare un eccezione controllata.
 */
@FunctionalInterface
public interface CheckedConsumer<T> {

    /**
     * Chiama la procedura.
     * 
     * @param argument
     *            valore di input
     * @throws Exception
     *             se non si è in grado di eseguire l'operazione
     */
    public void call(T argument) throws Exception;
}