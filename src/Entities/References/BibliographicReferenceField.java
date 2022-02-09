package Entities.References;

/**
 * Classe che rappresenta un valore di un riferimento.
 * Contiene il nome e il valore.
 */
public class BibliographicReferenceField {
    private String name;
    private Object value;

    /**
     * Crea una nuova informazione con il nome e il valore indicati.
     * 
     * @param name
     *            nome dell'informazione
     * @param value
     *            valore dell'informazione
     */
    public BibliographicReferenceField(String name, Object value) {
        setName(name);
        setValue(value);
    }

    /**
     * Imposta il nome dell'informazione.
     * 
     * @param name
     *            nome dell'informazione
     * @throws IllegalArgumentException
     *             se {@code name == null} o {@code name.isBlank()}
     */
    public void setName(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("name can't be null");

        this.name = name.trim();
    }

    /**
     * Restituisce il nome dell'informazione.
     * 
     * @return
     *         nome dell'informazione
     */
    public String getName() {
        return this.name;
    }

    /**
     * Imposta il valore dell'informazione.
     * 
     * @param value
     *            valore dell'informazione
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Restituisce il valore dell'informazione.
     * 
     * @return
     *         valore dell'informazione
     */
    public Object getValue() {
        return this.value;
    }
}
