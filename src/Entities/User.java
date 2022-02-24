package Entities;

/**
 * Utente registrato che usa l'applicazione.
 */
public class User {

    private String name;
    private String password;

    private final int NAME_MAX_LENGTH = 128;
    private final int PASSWORD_MAX_LENGTH = 64;

    /**
     * Crea un nuovo utente con il nome e la password indicati.
     * 
     * @param name
     *            nome dell'utente
     * @param password
     *            password dell'utente
     * @throws IllegalArgumentException
     *             se il nome o la password non sono validi
     * @see #setName(String)
     * @see #setPassword(String)
     */
    public User(String name, String password) {
        setName(name);
        setPassword(password);
    }

    @Override
    public String toString() {
        return getName();
    }

    /**
     * Imposta il nome dell'utente.
     * <p>
     * Eventuali spazi all'inizio o alla fine verranno rimossi.
     * 
     * @param name
     *            nome dell'utente
     * @throws IllegalArgumentException
     *             se {@code name} è nullo o vuoto
     */
    public void setName(String name) {
        name = name.trim();

        if (isNameValid(name))
            this.name = name;
        else
            throw new IllegalArgumentException("Il nome dell'utente non può essere vuoto o più lungo di " + NAME_MAX_LENGTH + " caratteri.");
    }

    /**
     * Restituisce il nome dell'utente.
     * 
     * @return nome dell'utente
     */
    public String getName() {
        return this.name;
    }

    /**
     * Imposta la password dell'utente.
     * 
     * @param password
     *            password dell'utente
     * @throws IllegalArgumentException
     *             se la password è nulla o vuota
     */
    public void setPassword(String password) {
        if (isPasswordValid(password))
            this.password = password;
        else
            throw new IllegalArgumentException("La password non può essere vuota o più lunga di " + PASSWORD_MAX_LENGTH + " caratteri.");
    }

    /**
     * Restituisce la password dell'utente.
     * 
     * @return password dell'utente
     */
    public String getPassword() {
        return password;
    }

    private boolean isNameValid(String name) {
        return name != null && !name.isEmpty() && !name.isBlank() && name.length() <= NAME_MAX_LENGTH;
    }

    private boolean isPasswordValid(String password) {
        return password != null && !password.isEmpty() && !password.isBlank() && password.length() <= PASSWORD_MAX_LENGTH;
    }

}
