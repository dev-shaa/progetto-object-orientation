package Entities.References.PhysicalResources;

import Entities.Author;
import Entities.References.*;
import java.util.ArrayList;

/**
 * 
 */
public class Article extends Publication {

    private String ISSN;

    /**
     * Crea un riferimento a un articolo di una pubblicazione con un titolo
     * indicato.
     * 
     * @param title
     *            titolo dell'articolo
     * @param authors
     *            autori dell'articolo
     * @throws IllegalArgumentException
     *             se il titolo dell'articolo non è valido
     */
    public Article(String title, Author[] authors) throws IllegalArgumentException {
        super(title, authors);
    }

    /**
     * Imposta il codice identificativo ISSN dell'articolo.
     * 
     * @param ISSN
     *            codice identificativo ISSN
     */
    public void setISSN(String ISSN) {
        this.ISSN = ISSN;
    }

    /**
     * Restituisce il codice identificativo ISSN dell'articolo.
     * 
     * @return
     *         codice identificativo ISSN ({@code null} se non è indicato)
     */
    public String getISSN() {
        return this.ISSN;
    }

    @Override
    public ArrayList<BibliographicReferenceField> getReferenceFields() {
        ArrayList<BibliographicReferenceField> fields = super.getReferenceFields();

        fields.add(new BibliographicReferenceField("ISSN", getISSN()));

        return fields;
    }
}
