package Entities;

public class User {
    private String name;
    private String password;

    public User(String name, String password) {
        setName(name);
        setPassword(password);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return getName();
    }
}
