package Entities;

// PLACEHOLDER

public class User {
    private String name;
    private String password;
    private int id;

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
	
	public void incrementId( int id) {
		id = id + 1;
		System.out.println("Id utente: " + id);
	}
	
}

