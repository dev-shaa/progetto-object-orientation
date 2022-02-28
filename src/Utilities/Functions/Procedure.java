package Utilities.Functions;

/**
 * Rappresenta un'operazione senza input e e non restituisce un valore, con la possibilità di lanciare un eccezione controllata.
 */
@FunctionalInterface
public abstract interface Procedure {

    /**
     * Chiama la procedura.
     * 
     * @throws Exception
     *             se non si è in grado di eseguire l'operazione
     */
    public abstract void execute() throws Exception;
}