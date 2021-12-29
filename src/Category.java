// PLACEHOLDER

public class Category {
    private String name;
    private Category parent;

    public Category(String name, Category parent) {
        setName(name);
        setParent(parent);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public Category getParent() {
        return this.parent;
    }

    @Override
    public String toString() {
        return getName();
    }
}
