import java.awt.*;
import javax.swing.*;

public class TextSearchField extends JPanel {

    private JTextField textField;

    public TextSearchField(String label, String tooltip) {
        setLayout(new GridLayout(0, 1));

        textField = new JTextField();
        textField.setToolTipText(tooltip);

        add(new JLabel(label));
        add(textField);
    }

    public String getText() {
        return textField.getText();
    }

}
