package GUI.Utilities;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Un {@code JButton} che, quando premuto, apre un menu popup.
 */
public class PopupButton extends JButton {

    private JPopupMenu popupMenu;

    /**
     * Crea un pulsante senza testo o icona.
     */
    public PopupButton() {
        this(null, null);
    }

    /**
     * Crea un pulsante con testo ma senza icona.
     * 
     * @param text
     *            testo del pulsante
     */
    public PopupButton(String text) {
        this(text, null);
    }

    /**
     * Crea un pulsante con un'icona ma senza testo.
     * 
     * @param icon
     *            icona del pulsante
     */
    public PopupButton(Icon icon) {
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
    public PopupButton(String text, Icon icon) {
        super(text, icon);

        popupMenu = new JPopupMenu();

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onPopupOpen();
            }
        });
    }

    /**
     * Aggiunge un elemento al menu popup, se non è nullo.
     * 
     * @param component
     *            elemento da aggiungere
     */
    public void addToPopupMenu(Component component) {
        if (component != null)
            popupMenu.add(component);
    }

    /**
     * Rimuove un elemento dal menu popup.
     * 
     * @param component
     *            componente da rimuovere
     */
    public void removeFromPopupMenu(Component component) {
        if (component != null) {
            popupMenu.remove(component);
            popupMenu.pack();
            popupMenu.revalidate();
        }
    }

    /**
     * Rimuove tutti gli elementi dal menu popup.
     */
    public void removeAllFromPopupMenu() {
        popupMenu.removeAll();
    }

    /**
     * Aggiunge un separatore al menu popup.
     */
    public void addPopupSeparator() {
        popupMenu.addSeparator();
    }

    /**
     * Invocato quando viene premuto sul tasto per mostrare il menu popup.
     */
    protected void onPopupOpen() {
        popupMenu.show(this, 0, getHeight());
    }

}
