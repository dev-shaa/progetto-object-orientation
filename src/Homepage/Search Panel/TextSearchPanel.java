import java.awt.*;
import javax.swing.*;

public class TextSearchPanel extends JPanel {

    private JLabel label;
    private JTextField textField;

    public TextSearchPanel(String label, String tooltip) {
        setLayout(new GridLayout(0, 1));

        this.label = new JLabel(label);

        this.textField = new JTextField();
        this.textField.setToolTipText(tooltip);

        add(this.label);
        add(this.textField);
    }

    public String[] getSearchTerms() {
        return textField.getText().split(",");
    }

}
