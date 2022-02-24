package Entities;

/**
 * Classe che rappresenta una parola chiave di un riferimento.
 */
public class Tag {

    private String name;

    private final int NAME_MAX_LENGTH = 128;

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
        name = name.trim().toLowerCase();

        if (isNameValid(name))
            this.name = name;
        else
            throw new IllegalArgumentException("Il nome della parola chiave non può essere vuoto o più lungo di " + NAME_MAX_LENGTH + " caratteri.");

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
        return name != null && !name.isEmpty() && !name.isBlank() && name.length() <= NAME_MAX_LENGTH;
    }

}
