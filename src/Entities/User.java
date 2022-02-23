package Entities;

/**
 * Utente registrato che usa l'applicazione.
 */
public class User {

    private String name;
    private String password;

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
        if (!isNameValid(name))
            throw new IllegalArgumentException("Il nome dell'utente non può essere nullo o vuoto.");

        this.name = name.trim();
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
        if (!isPasswordValid(password))
            throw new IllegalArgumentException("La password non può essere vuota.");

        this.password = password;
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
        return name != null && !name.isEmpty() && !name.isBlank();
    }

    private boolean isPasswordValid(String password) {
        return password != null && !password.isEmpty() && !password.isBlank();
    }

}
