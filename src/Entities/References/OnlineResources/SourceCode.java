package Entities.References.OnlineResources;

public class SourceCode extends OnlineResource {

    private ProgrammingLanguage programmingLanguage = null;

    public SourceCode(String title, String URL) throws IllegalArgumentException {
        super(title, URL);
    }

    /**
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
