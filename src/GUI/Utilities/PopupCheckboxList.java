package GUI.Utilities;

import java.util.Collection;
import java.util.List;

import javax.swing.DefaultListModel;
import com.jidesoft.swing.CheckBoxList;

/**
 * Un pulsante che, se premuto, apre un menu a tendina in cui è possibile selezionare uno o più elementi.
 */
public class PopupCheckboxList<T> extends JPopupButton {

    private CheckBoxList checkboxList;
    private DefaultListModel<T> listModel;

    /**
     * Crea un JPopupItemSelection con testo ed elementi da selezionare.
     * 
     * @param text
     *            testo del pulsante
     * @param elements
     *            elementi da aggiungere al menu di selezione
     */
    public <V extends Collection<T>> PopupCheckboxList(String text, V elements) {
        super(text);

        if (listModel == null)
            listModel = new DefaultListModel<>();

        this.checkboxList = new CheckBoxList(listModel);
        addToPopupMenu(this.checkboxList);

        setElements(elements);
    }

    /**
     * Imposta gli elementi selezionabili.
     * 
     * @param elements
     *            elementi selezionabili
     */
    public <V extends Collection<T>> void setElements(V elements) {
        listModel.clear();

        if (elements == null)
            return;

        for (T item : elements)
            listModel.addElement(item);
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
        listModel.addElement(element);
    }

    /**
     * Restituisce gli elementi selezionati.
     * 
     * @return
     *         elementi selezionati
     */
    @SuppressWarnings("unchecked")
    public List<T> getSelectedElements() {
        return (List<T>) checkboxList.getSelectedValuesList();
    }

    /**
     * Seleziona un elemento, se presente.
     * 
     * @param element
     *            elemento da selezionare
     */
    public void selectElement(T element) {
        checkboxList.setSelectedValue(element, true);
    }

    /**
     * Deseleziona tutti gli elementi.
     */
    public void deselectAll() {
        checkboxList.selectNone();
    }

}
