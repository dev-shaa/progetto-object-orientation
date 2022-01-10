package postgresql;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Controller {
	
	LoginFrame lf;
	HomeFrame hf;

//	public static void main(String[] args) {
//		Controller c = new Controller();
//	}

	public Controller() {
		lf = new LoginFrame(this);
		hf = new HomeFrame(this);
		lf.setVisible(true);
	}
	
	public boolean CheckUser(String uid, String pwd) {
	
		String message = "Nome Utente o Password sbagliati";
		System.out.println(uid + " "+pwd); //per vedere su console 
		
		if(!uid.isEmpty() && !pwd.isEmpty())
		//lf.setVisible(false);
		hf.setVisible(true);
		
		else		     
		    JOptionPane.showMessageDialog(new JFrame(), message, "Errore", JOptionPane.ERROR_MESSAGE);
		
		
		boolean result = false;
		
		return result;
	}
	
	
	
	
	
	
}
