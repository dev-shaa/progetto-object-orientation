package Entities;

/**
 * Classe che rappresenta una parola chiave di un riferimento.
 */
public class Tag {
    private String value;

    /**
     * Crea una nuova parola chiave.
     * 
     * @param value
     *            nome della parole chiave
     * @throws IllegalArgumentException
     *             se il nome non è valido
     * @see #setName(String)
     */
    public Tag(String value) {
        setName(value);
    }

    /**
     * Restituisce il nome della parola chiave.
     * 
     * @return
     *         nome della parola chiave
     */
    public String getName() {
        return value;
    }

    /**
     * Imposta il nome della parola chiave.
     * 
     * @param value
     *            nome della parola chiave
     * @throws IllegalArgumentException
     *             se il nome è nullo o vuoto
     */
    public void setName(String value) {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("value non può essere null");

        this.value = value;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof Tag))
            return false;

        return getName().equals(((Tag) obj).getName());
    }

}
