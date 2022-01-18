package Entities.References.PhysicalResources;

import Entities.Author;
import Entities.References.*;
import java.util.ArrayList;

/**
 * Classe che rappresenta un riferimento bibliografico a un articolo.
 */
public class Article extends Publication {

    private String ISSN;

    /**
     * Crea un nuovo riferimento a un articolo con il titolo e gli autori indicati.
     * 
     * @param title
     *            titolo del riferimento
     * @param authors
     *            autori del riferimento
     * @throws IllegalArgumentException
     *             se il titolo non è valido
     * @see #setTitle(String)
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
