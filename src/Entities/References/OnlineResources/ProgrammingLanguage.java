package Entities.References.OnlineResources;

/**
 * Linguaggi di programmazione per i riferimenti a codice sorgente.
 */
public enum ProgrammingLanguage {

    C("C/C++"), CSHARP("C#"), JAVA("Java"), PYTHON("Python"), LUA("LUA"), FORTRAN("Fortran"), OTHER("Altro");

    private String name;

    ProgrammingLanguage(String name) {
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
     * @return l'enum corrispondente al nome di input, {@code OTHER} se la stringa di input non corrisponde a nessun enum valido.
     */
    public static ProgrammingLanguage getFromString(String value) {
        try {
            return valueOf(value);
        } catch (IllegalArgumentException | NullPointerException e) {
            return OTHER;
        }
    }

}
