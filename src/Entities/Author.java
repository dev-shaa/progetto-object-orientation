package Entities;

/**
 * Classe che rappresenta l'autore di un riferimento bibliografico.
 */
public class Author {
    private String firstName;
    private String lastName;

    /**
     * Crea un nuovo autore con il nome e cognome dati.
     * 
     * @param firstName
     *                  nome dell'autore
     * @param lastName
     *                  cognome dell'autore
     * @throws IllegalArgumentException
     *                                  se {@code firstName == null } o
     *                                  {@code lastName == null }
     */
    public Author(String firstName, String lastName) throws IllegalArgumentException {
        setFirstName(firstName);
        setLastName(lastName);
    }

    /**
     * Imposta il nome dell'autore.
     * 
     * @param firstName
     *                  nome dell'autore
     * @throws IllegalArgumentException
     *                                  se {@code firstName == null }
     */
    public void setFirstName(String firstName) throws IllegalArgumentException {
        if (firstName == null)
            throw new IllegalArgumentException();

        this.firstName = firstName;
    }

    /**
     * Restituisce il nome dell'autore.
     * 
     * @return
     *         nome dell'autore
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Imposta il cognome dell'autore.
     * 
     * @param lastName
     *                 cognome dell'autore
     * @throws IllegalArgumentException
     *                                  se {@code lastName == null }
     */
    public void setLastName(String lastName) throws IllegalArgumentException {
        if (lastName == null)
            throw new IllegalArgumentException();

        this.lastName = lastName;
    }

    /**
     * Restituisce il cognome dell'autore.
     * 
     * @return
     *         cognome dell'autore
     */
    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

}
