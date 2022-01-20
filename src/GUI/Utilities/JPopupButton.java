package GUI.Utilities;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Un JButton che, quando premuto, apre un menu popup.
 */
public class JPopupButton extends JButton implements ActionListener {

    private JPopupMenu popupMenu;
    private final JMenuItem emptyPopupLabel;

    /**
     * Crea un pulsante senza testo o icona.
     */
    public JPopupButton() {
        this(null, null);
    }

    /**
     * Crea un pulsante con testo ma senza icona.
     * 
     * @param text
     *            testo del pulsante
     */
    public JPopupButton(String text) {
        this(text, null);
    }

    /**
     * Crea un pulsante con un'icona ma senza testo.
     * 
     * @param icon
     *            icona del pulsante
     */
    public JPopupButton(Icon icon) {
        this(null, icon);
    }

    /**
     * Crea un pulsante con testo e icona.
     * 
     * @param text
     *            testo del pulsante
     * @param icon
     *            icona del pulsante
     */
    public JPopupButton(String text, Icon icon) {
        super(text, icon);

        emptyPopupLabel = new JMenuItem("Nessun elemento");
        emptyPopupLabel.setEnabled(false);

        popupMenu = new JPopupMenu();

        addToPopupMenu(emptyPopupLabel);

        addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        popupMenu.show(this, 0, getHeight());
    }

    /**
     * Aggiunge un elemento al menu popup, se non è null.
     * 
     * @param component
     *            elemento da aggiungere
     */
    public void addToPopupMenu(Component component) {
        if (component != null) {
            popupMenu.remove(emptyPopupLabel);
            popupMenu.add(component);
        }
    }

    /**
     * Rimuove un elemento dal menu popup.
     * 
     * @param component
     *            componente da rimuovere
     * @throws IllegalArgumentException
     *             se {@code component == null}
     */
    public void removeFromPopupMenu(Component component) throws IllegalArgumentException {
        popupMenu.remove(component);

        if (popupMenu.getComponentCount() == 0) {
            addToPopupMenu(emptyPopupLabel);
        }
    }

    /**
     * Rimuove tutti gli elementi dal menu popup.
     */
    public void removeAllFromPopupMenu() {
        popupMenu.removeAll();

        addToPopupMenu(emptyPopupLabel);
    }

    /**
     * Aggiunge un separatore al menu popup.
     */
    public void addPopupSeparator() {
        popupMenu.addSeparator();
    }

}
