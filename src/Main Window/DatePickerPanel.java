import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.util.Date;

public class DatePickerPanel extends JPanel {

    private JSpinner dateSpinner;

    public DatePickerPanel(String label) {
        setLayout(new GridLayout(0, 1));

        Date today = new Date();

        dateSpinner = new JSpinner(new SpinnerDateModel(today, null, today, Calendar.MONTH));
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy"));

        add(new JLabel(label));
        add(dateSpinner);
    }

    public Date getDate() {
        return (Date) dateSpinner.getValue();
    }

}
