package GUI.Utilities;

import java.util.ArrayList;

/**
 * Un pulsante che, se premuto, apre un menu a tendina in cui è possibile selezionare uno o più elementi.
 */
public class JPopupItemSelection<T> extends JPopupButton {

    private ArrayList<T> elements;
    private ArrayList<JCheckBoxMenuItemExtended> checkboxes;

    /**
     * Crea un JPopupItemSelection senza testo o elementi.
     */
    public JPopupItemSelection() {
        super();
        setElements(null);
    }

    /**
     * Crea un JPopupItemSelection con testo ma senza elementi.
     * 
     * @param text
     *            testo del pulsante
     */
    public JPopupItemSelection(String text) {
        super(text);
        setElements(null);
    }

    /**
     * Crea un JPopupItemSelection con degli elementi ma senza testo.
     * 
     * @param elements
     *            elementi da aggiungere al menu di selezione
     */
    public JPopupItemSelection(T[] elements) {
        super();
        setElements(elements);
    }

    /**
     * Crea un JPopupItemSelection con testo ed elementi da selezionare.
     * 
     * @param text
     *            testo del pulsante
     * @param elements
     *            elementi da aggiungere al menu di selezione
     */
    public JPopupItemSelection(String text, T[] elements) {
        super(text);
        setElements(elements);
    }

    /**
     * Imposta gli elementi selezionabili.
     * 
     * @param elements
     *            elementi selezionabili
     */
    public void setElements(T[] elements) {
        removeAllFromPopupMenu();

        if (elements == null) {
            this.elements = new ArrayList<T>(0);
            this.checkboxes = new ArrayList<JCheckBoxMenuItemExtended>(0);
            return;
        }

        this.elements = new ArrayList<T>(elements.length);
        this.checkboxes = new ArrayList<JCheckBoxMenuItemExtended>(elements.length);

        for (T item : elements) {
            this.elements.add(item);

            JCheckBoxMenuItemExtended checkbox = new JCheckBoxMenuItemExtended(String.valueOf(item));
            this.checkboxes.add(checkbox);
            addToPopupMenu(checkbox);
        }
    }

    /**
     * Aggiunge un elemento da selezionare.
     * 
     * @param element
     *            elemento da aggiungere
     * @throws IllegalArgumentException
     *             se {@code element == null}
     */
    public void addElement(T element) throws IllegalArgumentException {
        if (element == null)
            throw new IllegalArgumentException("element non può essere null");

        elements.add(element);

        JCheckBoxMenuItemExtended checkbox = new JCheckBoxMenuItemExtended(element.toString());
        checkboxes.add(checkbox);
        addToPopupMenu(checkbox);
    }

    /**
     * Restituisce gli elementi selezionati.
     * 
     * @return
     *         elementi selezionati
     */
    public ArrayList<T> getSelectedElements() {
        ArrayList<T> selectedItems = new ArrayList<T>(0);

        for (int i = 0; i < checkboxes.size(); i++) {
            if (checkboxes.get(i).isSelected())
                selectedItems.add(elements.get(i));
        }

        return selectedItems;
    }

    /**
     * Seleziona un elemento, se presente.
     * 
     * @param element
     *            elemento da selezionare
     */
    public void selectElement(T element) {
        int index = elements.indexOf(element);
        if (index != -1) {
            checkboxes.get(index).setSelected(true);
        }
    }

}
