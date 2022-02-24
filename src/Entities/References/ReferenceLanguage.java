package Entities.References;

/**
 * Lingua di un riferimento.
 */
public enum ReferenceLanguage {

    ITALIAN("Italiano"), ENGLISH("Inglese"), FRENCH("Francese"), GERMAN("Tedesco"), SPANISH("Spagnolo"), RUSSIAN("Russo"), JAPANESE("Giapponese"), CHINESE("Cinese"), ARAB("Arabo"), NOTSPECIFIED("Non specificato");

    private String name;

    private ReferenceLanguage(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    /**
     * Restituisce il valore enum a partire da una stringa.
     * 
     * @param value
     *            nome dell'enum
     * @return l'enum corrispondente al nome di input, {@code NOTSPECIFIED} se la stringa di input non corrisponde a nessun enum valido.
     */
    public static ReferenceLanguage getFromString(String value) {
        try {
            return valueOf(value);
        } catch (IllegalArgumentException | NullPointerException e) {
            return NOTSPECIFIED;
        }
    }

}