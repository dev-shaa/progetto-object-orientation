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

    /**
     * Imposta il nome dell'utente.
     * <p>
     * Eventuali spazi all'inizio o alla fine verranno rimossi.
     * 
     * @param name
     *            nome dell'utente
     * @throws IllegalArgumentException
     *             se {@code name == null} o {@code name.isBlank()}
     */
    public void setName(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Il nome non può essere nullo o vuoto.");

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
     *             se {@code password == null} o {@code password.isBlank()}
     */
    public void setPassword(String password) {
        if (password == null || password.isBlank())
            throw new IllegalArgumentException("La password non può essere nulla o vuota.");

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

    @Override
    public String toString() {
        return getName();
    }

}
