package Entities.References.OnlineResources;

import Entities.Author;

public class SourceCode extends OnlineResource {

    private ProgrammingLanguage programmingLanguage = null;

    /**
     * TODO: commenta
     * 
     * @param title
     * @param authors
     *            autori del codice
     * @param URL
     * @throws IllegalArgumentException
     */
    public SourceCode(String title, Author[] authors, String URL) throws IllegalArgumentException {
        super(title, authors, URL);
    }

    /**
     * TODO: commenta
     * 
     * @param programmingLanguage
     */
    public void setProgrammingLanguage(ProgrammingLanguage programmingLanguage) {
        this.programmingLanguage = programmingLanguage;
    }

    /**
     * 
     * @return
     */
    public ProgrammingLanguage getProgrammingLanguage() {
        return programmingLanguage;
    }

}
