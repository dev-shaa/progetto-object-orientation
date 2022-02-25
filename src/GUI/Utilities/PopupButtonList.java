package GUI.Utilities;

import java.util.ArrayList;
import java.util.Collection;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Un {@code PopupButton} che mostra una lista di elementi rimovibili.
 */
public class PopupButtonList<T extends Object> extends PopupButton {

    private ArrayList<T> items;

    /**
     * Crea un nuovo pulsante senza elementi con il testo indicato.
     * 
     * @param text
     *            testo del pulsante
     */
    public PopupButtonList(String text) {
        super(text);
        items = new ArrayList<>();
    }

    /**
     * Imposta gli elementi presenti nella lista.
     * <p>
     * Se la lista è nulla, ha lo stesso effetto di {@link #clear()}.
     * 
     * @param items
     *            elementi da aggiungere
     */
    public void setItems(Collection<? extends T> items) {
        clear();

        if (items != null) {
            for (T item : items)
                addItem(item);
        }
    }

    /**
     * Aggiunge un elemento alla lista.
     * <p>
     * Se l'elemento è nullo, non viene eseguito nulla.
     * 
     * @param item
     *            elemento da aggiungere
     */
    public void addItem(T item) {
        if (item == null)
            return;

        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 5));
        panel.setBackground(new Color(0, 0, 0, 0));

        JButton removeButton = new JButton(new ImageIcon("images/button_remove.png"));
        removeButton.setBorderPainted(false);
        removeButton.setFocusPainted(false);
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                items.remove(item);
                removeFromPopupMenu(panel);
            }
        });

        panel.add(new JLabel(item.toString()), BorderLayout.CENTER);
        panel.add(removeButton, BorderLayout.EAST);

        items.add(item);
        addToPopupMenu(panel);
    }

    /**
     * Restituisce gli elementi presenti nella lista.
     * 
     * @return elementi della lista
     */
    public ArrayList<T> getItems() {
        return items;
    }

    /**
     * Rimuove tutti i campi.
     */
    public void clear() {
        items.clear();
        removeAllFromPopupMenu();
    }

}