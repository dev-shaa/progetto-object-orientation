package GUI.Utilities;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Un {@code JButton} che, quando premuto, apre un menu popup.
 */
public class JPopupButton extends JButton implements ActionListener {

    private JPopupMenu categoriesSelectionPopup;

    /**
     * Crea un pulsante senza testo o icone.
     */
    public JPopupButton() {
        super();
        setup();
    }

    /**
     * Crea un pulsante con del testo.
     * 
     * @param text
     *            testo del pulsante
     */
    public JPopupButton(String text) {
        super(text);
        setup();
    }

    private void setup() {
        categoriesSelectionPopup = new JPopupMenu();
        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        categoriesSelectionPopup.show(this, 0, getHeight());
    }

    /**
     * Aggiunge un elemento al menu popup.
     * 
     * @param component
     *            elemento da aggiungere
     * @throws NullPointerException
     *             se {@code component == null}
     */
    public void addComponentToPopupMenu(Component component) throws NullPointerException {
        categoriesSelectionPopup.add(component);
    }
}
