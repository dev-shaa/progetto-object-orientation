package GUI.Utilities;

import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;

/**
 * Un JCheckBoxMenuItem con alcune funzionalità aggiuntive.
 */
public class JCheckBoxMenuItemExtended extends JCheckBoxMenuItem {

    private boolean closeOnSelection = false;

    public JCheckBoxMenuItemExtended(String text, boolean b) {
        super(text, b);
    }

    public JCheckBoxMenuItemExtended(String text, Icon icon, boolean b) {
        super(text, icon, b);
    }

    public JCheckBoxMenuItemExtended(String text) {
        super(text);
    }

    public JCheckBoxMenuItemExtended(String text, Icon icon) {
        super(text, icon);
    }

    public JCheckBoxMenuItemExtended(Icon icon) {
        super(icon);
    }

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
