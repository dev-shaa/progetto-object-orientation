package Entities.References.OnlineResources;

public enum ProgrammingLanguage {
    C("C/C++"), CSHARP("C#"), JAVA("Java"), PYTHON("Python"), LUA("LUA"), OTHER("Altro");

    private String name;

    ProgrammingLanguage(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
