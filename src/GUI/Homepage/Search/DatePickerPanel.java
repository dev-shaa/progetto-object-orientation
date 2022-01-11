package GUI.Homepage.Search;

import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import com.toedter.calendar.JDateChooser;
import java.util.Date;

/**
 * Un pannello con dei selettori che permettono di scegliere un intervallo tra
 * una data di inizio e una di fine.
 * 
 * @version 1.0
 * @author Salvatore Di Gennaro
 */
public class DatePickerPanel extends JPanel {

    private JDateChooser startDate;
    private JDateChooser endDate;

    /**
     * Crea un pannello in cui è possibile selezionare due date, una di inizio e una
     * di fine.
     * Gli elementi sono allineati verticalmente.
     */
    public DatePickerPanel() {
        setLayout(new GridLayout(0, 1));

        startDate = new JDateChooser();
        endDate = new JDateChooser();

        add(new JLabel("Da:"));
        add(startDate);

        add(new JLabel("A:"));
        add(endDate);
    }

    /**
     * Restituisce la data di inizio dell'intervallo.
     * 
     * @return data di inizio {@code null se la data non è selezionata}
     */
    public Date getStartDate() {
        return startDate.getDate();
    }

    /**
     * Restituisce la data di fine dell'intervallo.
     * 
     * @return data di fine {@code null se la data non è selezionata}
     */
    public Date getEndDate() {
        return endDate.getDate();
    }

}
