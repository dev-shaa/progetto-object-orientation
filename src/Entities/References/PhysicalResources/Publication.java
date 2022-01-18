package Entities.References.PhysicalResources;

import Entities.Author;
import Entities.References.*;
import java.util.ArrayList;

/**
 * Classe che rappresenta un riferimento bibliografico a una pubblicazione.
 */
public abstract class Publication extends BibliographicReference {

    private Integer pageCount;
    private String URL;
    private String publisher;

    /**
     * Crea un nuovo riferimento a una pubblicazione con il titolo e gli autori indicati.
     * 
     * @param title
     *            titolo del riferimento
     * @param authors
     *            autori del riferimento
     * @throws IllegalArgumentException
     *             se il titolo non è valido
     * @see #setTitle(String)
     */
    public Publication(String title, Author[] authors) throws IllegalArgumentException {
        super(title, authors);
    }

    /**
     * Imposta il numero di pagine della pubblicazione.
     * 
     * @param pageCount
     *            numero di pagine della pubblicazione
     * @throws IllegalArgumentException
     *             se {@code pageCount <= 0}
     */
    public void setPageCount(Integer pageCount) throws IllegalArgumentException {
        if (pageCount != null && pageCount <= 0)
            throw new IllegalArgumentException("Il numero di pagine del libro non può essere inferiore a 1");

        this.pageCount = pageCount;
    }

    /**
     * Restituisce il numero di pagine della pubblicazione.
     * 
     * @return
     *         numero di pagine della pubblicazione ({@code null} se non è indicato)
     */
    public Integer getPageCount() {
        return this.pageCount;
    }

    /**
     * Imposta l'URL del riferimento
     * 
     * @param URL
     *            URL del riferimento
     */
    public void setURL(String URL) {
        this.URL = URL;
    }

    /**
     * Restituisce l'URL del riferimento.
     * 
     * @return
     *         URL del riferimento ({@code null} se non è indicato)
     */
    public String getURL() {
        return this.URL;
    }

    /**
     * Imposta l'editore della pubblicazione.
     * 
     * @param publisher
     *            editore della pubblicazione
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * Restituisce l'editore della pubblicazione.
     * 
     * @return
     *         editore della pubblicazione ({@code null} se non è indicato)
     */
    public String getPublisher() {
        return this.publisher;
    }

    @Override
    public ArrayList<BibliographicReferenceField> getReferenceFields() {
        ArrayList<BibliographicReferenceField> fields = super.getReferenceFields();

        fields.add(new BibliographicReferenceField("Numero di pagine", getPageCount()));
        fields.add(new BibliographicReferenceField("URL", getURL()));
        fields.add(new BibliographicReferenceField("Editore", getPublisher()));

        return fields;
    }

}
