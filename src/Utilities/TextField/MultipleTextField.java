package Utilities.TextField;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Un pannello dinamico che permette all'utente di aggiungere o rimuovere campi di testo.
 */
public class MultipleTextField extends JPanel {

    private JTextField firstField;
    private ArrayList<JTextField> otherFields = new ArrayList<>();

    private final ImageIcon addIcon = new ImageIcon("images/button_add.png");
    private final ImageIcon removeIcon = new ImageIcon("images/button_remove.png");
    private final Dimension maximumTextFieldSize = new Dimension(Integer.MAX_VALUE, 24);

    private String tooltip;
    private int maximumLength;

    /**
     * Crea un nuovo pannello con un solo campo.
     */
    public MultipleTextField() {
        this(null);
    }

    /**
     * Crea un nuovo pannello con un solo campo, con il tooltip indicato.
     * 
     * @param tooltip
     *            testo da mostrare come tooltip
     */
    public MultipleTextField(String tooltip) {
        this(tooltip, Integer.MAX_VALUE);
    }

    /**
     * Crea un nuovo pannello con un solo campo, con il tooltip indicato e la lunghezza massima specificata.
     * 
     * @param tooltip
     *            testo da mostrare come tooltip
     * @param maximumLength
     *            lunghezza massima del testo
     * @throws IllegalArgumentException
     *             se {@code maximumLength < 0}
     */
    public MultipleTextField(String tooltip, int maximumLength) {
        if (maximumLength < 0)
            throw new IllegalArgumentException("maximumLength can't be less than 0");

        this.tooltip = tooltip;
        this.maximumLength = maximumLength;

        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        addFirstField();
    }

    /**
     * Imposta i valori iniziali dei campi.
     * 
     * @param values
     *            valori iniziali
     */
    public void setInitialValues(List<String> values) {
        clear();

        if (values == null || values.isEmpty())
            return;

        // il primo campo dobbiamo impostarlo a parte
        firstField.setText(values.get(0));

        for (int i = 1; i < values.size(); i++)
            addSecondaryField(values.get(i));

        revalidate();
    }

    /**
     * Restituisce i valori inseriti dall'utente.
     * <p>
     * La lista non contiene stringhe nulle o vuote.
     * 
     * @return lista con i valori inseriti
     */
    public ArrayList<String> getValues() {
        ArrayList<String> values = new ArrayList<>(otherFields.size() + 1); // +1 perchè c'è il campo iniziale

        String firstValue = firstField.getText();
        if (firstValue != null && !firstValue.isEmpty() && !firstValue.isBlank())
            values.add(firstValue);

        for (JTextField field : otherFields) {
            String value = field.getText();

            if (value != null && !value.isEmpty() && !value.isBlank())
                values.add(value);
        }

        values.trimToSize();
        return values;
    }

    /**
     * Reimposta tutti i campi.
     */
    public void clear() {
        otherFields.clear();
        removeAll();
        addFirstField();
        revalidate();
    }

    @Override
    public void setToolTipText(String text) {
        tooltip = text;

        firstField.setToolTipText(tooltip);
        for (JTextField field : otherFields)
            field.setToolTipText(tooltip);
    }

    /**
     * Imposta la lunghezza massima del testo.
     * 
     * @param length
     *            lunghezza massima
     * @throws IllegalArgumentException
     *             se {@code length < 0}
     */
    public void setMaximumLength(int length) {
        maximumLength = length;

        firstField.setColumns(length);
        for (JTextField field : otherFields)
            field.setColumns(length);

        revalidate();
    }

    private void addFirstField() {
        JPanel firstFieldPanel = new JPanel(new BorderLayout(0, 0));
        firstFieldPanel.setMaximumSize(maximumTextFieldSize);

        firstField = new JTextField(maximumLength);
        firstField.setToolTipText(tooltip);

        JButton addButton = new JButton(addIcon);

        firstFieldPanel.add(firstField, BorderLayout.CENTER);
        firstFieldPanel.add(addButton, BorderLayout.EAST);

        JPanel thisPanel = this;
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSecondaryField(null);
                thisPanel.revalidate();
            }
        });

        add(firstFieldPanel);
    }

    private void addSecondaryField(String initialValue) {
        JPanel fieldPanel = new JPanel(new BorderLayout(0, 0));
        fieldPanel.setMaximumSize(maximumTextFieldSize);

        JTextField textField = new JTextField(maximumLength);
        textField.setToolTipText(tooltip);
        textField.setText(initialValue);

        JButton removeButton = new JButton(removeIcon);

        Component spacing = Box.createVerticalStrut(10);

        fieldPanel.add(textField, BorderLayout.CENTER);
        fieldPanel.add(removeButton, BorderLayout.EAST);
        otherFields.add(textField);

        JPanel thisPanel = this;
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                otherFields.remove(textField);
                thisPanel.remove(fieldPanel);
                thisPanel.remove(spacing);
                thisPanel.revalidate();
            }
        });

        add(spacing);
        add(fieldPanel);
    }

}