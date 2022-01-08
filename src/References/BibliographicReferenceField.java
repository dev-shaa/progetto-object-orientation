public class BibliographicReferenceField {
    private String name;
    private Object value;

    public BibliographicReferenceField(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public Object getValue() {
        return this.value;
    }
}
