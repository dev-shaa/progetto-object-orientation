package Entities;

public class Tag {
    private String value;

    public Tag(String value) throws IllegalArgumentException {
        setName(value);
    }

    public String getName() {
        return value;
    }

    public void setName(String value) throws IllegalArgumentException {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("value non può essere null");

        this.value = value;
    }
}
