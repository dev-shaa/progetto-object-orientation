package Entities.References;

public enum ReferenceLanguage {
    // Italiano, Inglese, Francese, Tedesco, Spagnolo, Russo, Giapponese, Cinese, Arabo, Altro
    ITALIAN("Italiano"), ENGLISH("Inglese"), FRENCH("Francese"), GERMAN("Tedesco"), SPANISH("Spagnolo");

    private String name;

    ReferenceLanguage(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}