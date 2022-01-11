import javax.swing.JDialog;
import java.awt.BorderLayout;

public abstract class CustomDialog extends JDialog {

    public CustomDialog(String title) {
        super();
        setLayout(new BorderLayout());

        setModal(true);
        setTitle(title);
        setSize(200, 200);
        setVisible(true);
    }

}
