package Entities.References.PhysicalResources;

import Entities.References.*;
import java.util.ArrayList;

/**
 * Riferimento bibliografico di una tesi universitaria.
 */
public class Thesis extends Publication {

    private String university;
    private String faculty;

    /**
     * Crea un riferimento a una tesi universitaria dal titolo indicato.
     * 
     * @param title
     *              titolo della tesi
     * @throws IllegalArgumentException
     *                                  se il titolo della tesi non è valido
     */
    public Thesis(String title) throws IllegalArgumentException {
        super(title);
    }

    /**
     * Imposta l'università da cui proviene questa tesi.
     * 
     * @param university
     *                   università della tesi
     */
    public void setUniversity(String university) {
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
     *                facoltà della tesi
     */
    public void setFaculty(String faculty) {
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
    public ArrayList<BibliographicReferenceField> getReferenceFields() {
        ArrayList<BibliographicReferenceField> fields = super.getReferenceFields();

        fields.add(new BibliographicReferenceField("Università", getUniversity()));
        fields.add(new BibliographicReferenceField("Facoltà", getFaculty()));

        return fields;
    }

}
