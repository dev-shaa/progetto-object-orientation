package GUI;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Login {

	public boolean CheckUser(String uid, String pwd) {		
		
		String message = "Nome Utente o Password sbagliati";

		
		if(!uid.isEmpty() && !pwd.isEmpty())
			System.out.println(uid + " "+pwd); //per vedere su console 

		else		     
		    JOptionPane.showMessageDialog(new JFrame(), message, "Errore", JOptionPane.ERROR_MESSAGE);
		
		
		boolean result = false;
		
		return result;
	}
		
		
	}

	


