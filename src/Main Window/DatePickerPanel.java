import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import java.util.Date;
import java.util.Calendar;

public class DatePickerPanel extends JPanel {

    private JSpinner dateFrom;
    private JSpinner dateTo;

    public DatePickerPanel() {
        setLayout(new GridLayout(0, 1));

        add(getDateFromPanel());
        add(getDateToPanel());
    }

    /**
     * 
     * @return
     */
    public Date getEarliestDate() {
        return (Date) dateFrom.getValue();
    }

    /**
     * 
     * @return
     */
    public Date getLatestDate() {
        return (Date) dateTo.getValue();
    }

    private JPanel getDateFromPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1));

        // FIXME: trova una data che sia accettabile
        Date earliestDatePossible = new Date(Long.MIN_VALUE);
        Date today = new Date();

        dateFrom = new JSpinner(new SpinnerDateModel(earliestDatePossible, null, today, Calendar.MONTH));
        dateFrom.setEditor(new JSpinner.DateEditor(dateFrom, "dd/MM/yyyy G"));

        panel.add(new JLabel("Da:"));
        panel.add(dateFrom);

        return panel;
    }

    private JPanel getDateToPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1));

        Date today = new Date();

        dateTo = new JSpinner(new SpinnerDateModel(today, null, today, Calendar.MONTH));
        dateTo.setEditor(new JSpinner.DateEditor(dateTo, "dd/MM/yyyy G"));

        panel.add(new JLabel("A:"));
        panel.add(dateTo);

        return panel;
    }

}
