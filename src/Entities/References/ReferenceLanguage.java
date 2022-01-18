package Entities.References;

public enum ReferenceLanguage {
    ITALIAN("Italiano"), ENGLISH("Inglese"), FRENCH("Francese"), GERMAN("Tedesco"), SPANISH("Spagnolo"), RUSSIAN("Russo"), JAPANESE("Giapponese"), CHINESE("Cinese"), ARAB("Arabo"), OTHER("Altro");

    private String name;

    ReferenceLanguage(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}