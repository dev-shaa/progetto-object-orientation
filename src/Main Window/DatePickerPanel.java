import java.awt.GridLayout;

import javax.swing.event.*;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.event.ChangeListener;

import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
 * Un pannello con dei selettori che permettono di scegliere un intervallo tra una data di inizio e una di fine.
 * 
 * @version 1.0
 * @author Salvatore Di Gennaro
 */
public class DatePickerPanel extends JPanel {

    private JSpinner startDate;
    private JSpinner endDate;
    private SpinnerDateModel endDateModel;

    /**
     * Crea un pannello in cui è possibile selezionare due date, una di inizio e una di fine.
     * Gli elementi sono allineati verticalmente.
     */
    public DatePickerPanel() {
        setLayout(new GridLayout(0, 1));

        Date earliestDatePossible = getMinimumDate();
        Date today = new Date();

        startDate = new JSpinner(new SpinnerDateModel(earliestDatePossible, null, today, Calendar.MONTH));
        startDate.setEditor(new JSpinner.DateEditor(startDate, "dd/MM/yyyy G"));

        // ovviamente vogliamo che startDate < endDate, quindi dobbiamo aggiornare il limite inferiore del selettore endDate
        startDate.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                setDateToLowerLimit();
            }
        });

        endDateModel = new SpinnerDateModel(today, earliestDatePossible, today, Calendar.MONTH);
        endDate = new JSpinner(endDateModel);
        endDate.setEditor(new JSpinner.DateEditor(endDate, "dd/MM/yyyy G"));

        add(new JLabel("Da:"));
        add(startDate);

        add(new JLabel("A:"));
        add(endDate);
    }

    /**
     * Restituisce la data di inizio dell'intervallo.
     * 
     * @return data di inizio
     */
    public Date getStartDate() {
        return (Date) startDate.getValue();
    }

    /**
     * Restituisce la data di fine dell'intervallo.
     * 
     * @return data di fine
     */
    public Date getEndDate() {
        return (Date) endDate.getValue();
    }

    private Date getMinimumDate() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy G");

        try {
            return format.parse("01/01/3000 a.C."); // più o meno l'inizio della scrittura umana, credo vada bene
        } catch (Exception e) {
            return new Date(Long.MIN_VALUE); // tipo il 2 milione a.C., suppongo sia eccessivo
        }
    }

    private void setDateToLowerLimit() {
        Date newLowerLimit = getStartDate();

        endDateModel.setStart(newLowerLimit);

        if (getEndDate().before(newLowerLimit))
            endDate.setValue(newLowerLimit);
    }

}
