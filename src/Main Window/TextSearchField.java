import java.awt.*;
import javax.swing.*;
// import java.util.*;
// import java.util.Date;

public class TextSearchField extends JPanel {

    private JTextField textField;

    public TextSearchField(String label, String tooltip) {
        setLayout(new GridLayout(0, 1));

        JLabel labelField = new JLabel(label);
        // labelField.setAlignmentX(JLabel.LEFT_ALIGNMENT);

        textField = new JTextField();
        textField.setToolTipText(tooltip);

        add(labelField);
        add(textField);
    }

    public String getText() {
        return textField.getText();
    }

}
