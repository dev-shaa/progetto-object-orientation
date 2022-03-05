package Utilities.Functions;

/**
 * Rappresenta un'operazione senza input e e non restituisce un valore, con la possibilità di lanciare un eccezione controllata.
 */
@FunctionalInterface
public interface Procedure<E extends Exception> {

    /**
     * Chiama la procedura.
     * 
     * @throws E
     *             se non è possibile eseguire l'operazione
     */
    public void call() throws E;
}