package Entities.References.PhysicalResources;

import Entities.References.*;
import java.util.List;

/**
 * Classe che rappresenta un riferimento bibliografico a una tesi.
 */
public class Thesis extends Publication {

    private String university;
    private String faculty;

    /**
     * Crea un nuovo riferimento a una tesi con il titolo indicato.
     * 
     * @param title
     *            titolo del riferimento
     * @throws IllegalArgumentException
     *             se il titolo non è valido
     * @see #setTitle(String)
     */
    public Thesis(String title) {
        super(title);
    }

    /**
     * Imposta l'università da cui proviene questa tesi.
     * 
     * @param university
     *            università della tesi
     */
    public void setUniversity(String university) {
        if (isStringNullOrEmpty(university))
            this.university = null;
        else
            this.university = university;
    }

    /**
     * Restituisce l'università da cui proviene questa tesi.
     * 
     * @return
     *         università della tesi ({@code null} se non è indicata)
     */
    public String getUniversity() {
        return this.university;
    }

    /**
     * Imposta la facoltà da cui proviene questa tesi.
     * 
     * @param faculty
     *            facoltà della tesi
     */
    public void setFaculty(String faculty) {
        if (isStringNullOrEmpty(faculty))
            this.faculty = null;
        else
            this.faculty = faculty;
    }

    /**
     * Restituisce la facoltà da cui proviene questa tesi.
     * 
     * @return
     *         facoltà della tesi ({@code null} se non è indicata)
     */
    public String getFaculty() {
        return this.faculty;
    }

    @Override
    public List<BibliographicReferenceField> getReferenceFields() {
        List<BibliographicReferenceField> fields = super.getReferenceFields();

        fields.add(new BibliographicReferenceField("Università", getUniversity()));
        fields.add(new BibliographicReferenceField("Facoltà", getFaculty()));

        return fields;
    }

}
