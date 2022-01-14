package GUI;

public class Login {

	public boolean CheckUser(String uid, String pwd) {
		
		String message = "Nome Utente o Password sbagliati";
		System.out.println(uid + " "+pwd); //per vedere su console 
		
		if(!uid.isEmpty() && !pwd.isEmpty())
			System.out.println("Ciao");
		
		else
			System.out.println("No");
//		    JOptionPane.showMessageDialog(new JFrame(), message, "Errore", JOptionPane.ERROR_MESSAGE);
		
		
		boolean result = false;
		
		return result;
	}

	
}
