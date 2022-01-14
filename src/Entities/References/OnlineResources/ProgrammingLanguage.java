package Entities.References.OnlineResources;

// TODO: https://howtodoinjava.com/java/enum/java-enum-string-example/

public enum ProgrammingLanguage {
    C("C/C++"), CSharp("C#"), Java("Java"), Python("Python"), Lua("LUA");

    private String name;

    ProgrammingLanguage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
