package Entities;

/**
 * Classe che rappresenta una parola chiave di un riferimento.
 */
public class Tag {

    private String name;

    /**
     * Crea una nuova parola chiave.
     * 
     * @param name
     *            nome della parole chiave
     * @throws IllegalArgumentException
     *             se il nome non è valido
     * @see #setName(String)
     */
    public Tag(String name) {
        setName(name);
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (obj == null || !(obj instanceof Tag))
            return false;

        return getName().equalsIgnoreCase(((Tag) obj).getName());
    }

    /**
     * Imposta il nome della parola chiave.
     * 
     * @param name
     *            nome della parola chiave
     * @throws IllegalArgumentException
     *             se il nome è nullo o vuoto
     */
    public void setName(String name) {
        if (!isNameValid(name))
            throw new IllegalArgumentException("Il nome della parola chiave non può essere nullo o vuoto.");

        this.name = name.trim().toLowerCase();
    }

    /**
     * Restituisce il nome della parola chiave.
     * 
     * @return
     *         nome della parola chiave
     */
    public String getName() {
        return name;
    }

    private boolean isNameValid(String name) {
        return name != null && !name.isEmpty() && !name.isBlank();
    }

}
