import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.PopupMenuUI;

// import javax.swing.table.DefaultTableModel;
// import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.GridBagLayout;
import java.util.*;
// import java.util.ArrayList;
import java.util.Date;

public class SelectionPopupMenu<T> extends JButton {

    private JPopupMenu popupMenu;
    // private ArrayList<JCheckBoxMenuItem> menuItems;
    private HashMap<JCheckBoxMenuItem, T> menuItems;

    public SelectionPopupMenu(T[] elements) {
        setText("<Niente>");

        popupMenu = new JPopupMenu();
        menuItems = new HashMap<JCheckBoxMenuItem, T>();
        // menuItems = new ArrayList<JCheckBoxMenuItem>(elements.length);

        ActionListener foo = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ArrayList<T> selectedItems = getSelectedItems();
                String displayText = "";

                if (selectedItems.isEmpty()) {
                    displayText = "<Niente>";
                } else {
                    for (T item : getSelectedItems()) {
                        displayText += item.toString() + ", ";
                    }
                }

                setText(displayText);
            }
        };

        for (int i = 0; i < elements.length; i++) {
            JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(elements[i].toString());
            menuItem.addActionListener(foo);

            menuItems.put(menuItem, elements[i]);
            popupMenu.add(menuItem);
        }

        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showPopupMenu();
            }
        });
    }

    private void showPopupMenu() {
        popupMenu.show(this, 0, getSize().height);
    }

    public ArrayList<T> getSelectedItems() {
        ArrayList<T> selectedItems = new ArrayList<T>();

        for (MenuElement item : popupMenu.getSubElements()) {
            JCheckBoxMenuItem checkBox = (JCheckBoxMenuItem) item;

            if (checkBox.isSelected())
                selectedItems.add(menuItems.get(checkBox));
        }

        return selectedItems;
    }

}
