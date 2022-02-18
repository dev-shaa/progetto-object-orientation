package GUI.Utilities;

import java.util.ArrayList;
import java.util.Collection;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PopupButtonList<T extends Object> extends PopupButton {

    private ArrayList<T> items;

    public PopupButtonList() {
        this(null);
    }

    public PopupButtonList(String text) {
        super(text);
        items = new ArrayList<>();
    }

    public void setItems(Collection<? extends T> items) {
        if (items == null)
            return;

        this.items.clear();
        removeAllFromPopupMenu();

        for (T item : items)
            addItem(item);
    }

    public void addItem(T item) {
        if (item == null || items.contains(item))
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

    public ArrayList<T> getItems() {
        return items;
    }

}
