package Utilities.Functions;

/**
 * Rappresenta un'operazione senza input e e non restituisce un valore, con la possibilità di lanciare un eccezione controllata.
 */
@FunctionalInterface
public interface Procedure {

    /**
     * Chiama la procedura.
     * 
     * @throws Exception
     *             se non è possibile eseguire l'operazione
     */
    public void execute() throws Exception;
}