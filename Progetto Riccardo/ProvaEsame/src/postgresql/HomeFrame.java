package postgresql;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import java.awt.Font;

public class HomeFrame extends JFrame {

	private JPanel contentPane;
	private Controller theController;
	private JTextField txtBenvenuto;

	/**
	 * Create the frame.
	 */
	public HomeFrame(Controller c) {
		
		theController = c;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtBenvenuto = new JTextField();
		txtBenvenuto.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 20));
		txtBenvenuto.setText("Benvenuto");
		txtBenvenuto.setBounds(160, 95, 126, 25);
		contentPane.add(txtBenvenuto);
		txtBenvenuto.setColumns(10);
	}
}
