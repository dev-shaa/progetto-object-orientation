package Entities.References.PhysicalResources;

/**
 * Classe che rappresenta un riferimento bibliografico a una tesi.
 */
public class Thesis extends Publication {

    private String university;
    private String faculty;

    private final int UNIVERSITY_MAX_LENGTH = 128;
    private final int FACULTY_MAX_LENGTH = 128;

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
        if (isUniversityValid(university))
            this.university = university;
        else
            throw new IllegalArgumentException("La facoltà non può essere più lunga di " + FACULTY_MAX_LENGTH + " caratteri.");
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
        if (isFacultyValid(faculty))
            this.faculty = faculty;
        else
            throw new IllegalArgumentException("La facoltà non può essere più lunga di " + FACULTY_MAX_LENGTH + " caratteri.");
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
    public String getInfo() {
        return super.getInfo()
                + "\nUniversità: " + (getUniversity() == null ? "" : getUniversity())
                + "\nFacoltà: " + (getFaculty() == null ? "" : getFaculty());
    }

    private boolean isUniversityValid(String university) {
        return university == null || university.length() <= UNIVERSITY_MAX_LENGTH;
    }

    private boolean isFacultyValid(String faculty) {
        return faculty == null || faculty.length() <= FACULTY_MAX_LENGTH;
    }

}
