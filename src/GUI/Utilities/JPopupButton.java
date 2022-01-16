package GUI.Utilities;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Un JButton che, quando premuto, apre un menu popup.
 */
public class JPopupButton extends JButton implements ActionListener {

    private JPopupMenu popupMenu;

    /**
     * Crea un pulsante senza testo o icone.
     */
    public JPopupButton() {
        super();

        popupMenu = new JPopupMenu();
        addActionListener(this);
    }

    /**
     * Crea un pulsante con del testo.
     * 
     * @param text
     *            testo del pulsante
     */
    public JPopupButton(String text) {
        this();
        setText(text);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        popupMenu.show(this, 0, getHeight());
    }

    /**
     * Aggiunge un elemento al menu popup.
     * 
     * @param component
     *            elemento da aggiungere
     * @throws NullPointerException
     *             se {@code component == null}
     */
    public void addToPopupMenu(Component component) throws NullPointerException {
        popupMenu.add(component);
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
    }

    /**
     * Rimuove tutti gli elementi dal menu popup.
     */
    public void removeAllFromPopupMenu() {
        popupMenu.removeAll();
    }

}
