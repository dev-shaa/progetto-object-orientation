package GUI.Utilities;

import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;

/**
 * Un JCheckBoxMenuItem con alcune funzionalità aggiuntive.
 */
public class JCheckBoxMenuItemExtended extends JCheckBoxMenuItem {

    private boolean closeOnSelection = false;

    /**
     * Crea un checkbox con testo e imposta il valore della selezione.
     * 
     * @param text
     *            testo del checkbox
     * @param isSelected
     *            se questo checkbox deve essere selezionato all'inizio
     */
    public JCheckBoxMenuItemExtended(String text, boolean isSelected) {
        super(text, isSelected);
    }

    /**
     * Crea un checkbox con testo, icona e imposta il valore della selezione.
     * 
     * @param text
     *            testo del checkbox
     * @param icon
     *            icona del checkbox
     * @param isSelected
     *            se questo checkbox deve essere selezionato all'inizio
     */
    public JCheckBoxMenuItemExtended(String text, Icon icon, boolean isSelected) {
        super(text, icon, isSelected);
    }

    /**
     * Crea un checkbox con testo, non selezionato.
     * 
     * @param text
     *            testo del checkbox
     */
    public JCheckBoxMenuItemExtended(String text) {
        super(text);
    }

    /**
     * Crea un checkbox con testo e icona, non selezionato.
     * 
     * @param text
     *            testo del checkbox
     * @param icon
     *            icona del checkbox
     */
    public JCheckBoxMenuItemExtended(String text, Icon icon) {
        super(text, icon);
    }

    /**
     * Crea un checkbox con icona, non selezionato.
     * 
     * @param icon
     *            icona del checkbox
     */
    public JCheckBoxMenuItemExtended(Icon icon) {
        super(icon);
    }

    /**
     * Crea un checkbox senza testo o icona, non selezionato.
     */
    public JCheckBoxMenuItemExtended() {
        super();
    }

    @Override
    protected void processMouseEvent(MouseEvent e) {
        if (!closeOnSelection && e.getID() == MouseEvent.MOUSE_RELEASED && contains(e.getPoint())) {
            doClick();
            setArmed(true);
        } else {
            super.processMouseEvent(e);
        }
    }

    /**
     * Imposta se, una volta selezionato, l'elemento dovrebbe comunicare al menu di chiudersi.
     * 
     * @param flag
     *            {@code true} se il menu dovrebbe chiudersi, {@code false} altrimenti
     */
    public void setCloseOnSelection(boolean flag) {
        closeOnSelection = flag;
    }

    /**
     * Restituisce se l'elemento è impostato per chiudere il menu quando selezionato.
     * 
     * @return
     *         {@code true} se impostato per chiudere quando selezionato
     */
    public boolean getCloseOnSelection() {
        return closeOnSelection;
    }

}
