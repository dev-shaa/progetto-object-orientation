package Entities.References;

public class BibliographicReferenceField {
    private String name;
    private Object value;

    public BibliographicReferenceField(String name, Object value) {
        setName(name);
        setValue(value);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return this.value;
    }
}
