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
                openPopup();
            }
        });
    }

    @Override
    public Component add(Component comp) {
        return popupMenu.add(comp);
    }

    @Override
    public void remove(Component comp) {
        popupMenu.remove(comp);
        popupMenu.pack();
        popupMenu.revalidate();
    }

    @Override
    public void removeAll() {
        popupMenu.removeAll();
    }

    /**
     * Aggiunge un separatore al menu popup.
     */
    public void addSeparator() {
        popupMenu.addSeparator();
    }

    /**
     * Apre il menu popup.
     */
    private void openPopup() {
        beforePopupOpen();
        popupMenu.show(this, 0, getHeight());
    }

    /**
     * Invocato quando viene premuto sul tasto per mostrare il menu popup.
     */
    protected void beforePopupOpen() {
        // vuoto
    }

}