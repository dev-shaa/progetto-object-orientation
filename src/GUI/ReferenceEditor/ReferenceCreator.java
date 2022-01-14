package GUI.ReferenceEditor;

import GUI.Categories.*;
import GUI.Homepage.Search.CategoriesSelectionPopupMenu;
import GUI.Utilities.JTermsField;
import Entities.Category;
import Entities.Tag;
import Entities.References.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JDateChooser;

public abstract class ReferenceCreator extends JDialog implements ActionListener {

    private JTextField title;
    private JDateChooser pubblicationDate;
    private JTextField DOI;
    private JTermsField tags;
    private JTextArea description;
    private JComboBox<ReferenceLanguage> language;
    private CategoriesSelectionPopupMenu categories;

    private JPanel fieldPanel;

    private final String searchFieldSeparator = ",";
    private final Dimension maximumSize = new Dimension(Integer.MAX_VALUE, 24);
    private final float alignment = Container.LEFT_ALIGNMENT;

    public ReferenceCreator(String dialogueTitle, CategoriesTreeManager categoriesTreeManager) {
        super();

        setTitle(dialogueTitle);
        setSize(500, 500);
        setMinimumSize(new Dimension(300, 300));

        // vogliamo che sia un'interfaccia modale
        setModal(true);

        // in questo modo le classi che ereditano ReferenceCreator possono fare l'overriding
        // e aggiungere altri elementi prima delle descrizione
        setup(categoriesTreeManager);

        // la descrizione è un elemento più grande, quindi vogliamo che sia alla fine
        setupDescription();

        // mostra automaticamente quando viene creato
        setVisible(true);
    }

    /**
     * TODO: commenta
     * 
     * @param categoriesTreeManager
     */
    protected void setup(CategoriesTreeManager categoriesTreeManager) {
        JPanel contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.PAGE_AXIS));
        fieldPanel.setBorder(new EmptyBorder(50, 30, 50, 30));

        JScrollPane scrollPane = new JScrollPane(fieldPanel);
        contentPane.add(scrollPane);

        title = new JTextField();
        tags = new JTermsField(searchFieldSeparator);
        DOI = new JTextField();
        pubblicationDate = new JDateChooser();
        language = new JComboBox<>(ReferenceLanguage.values());
        categories = new CategoriesSelectionPopupMenu(categoriesTreeManager);

        addComponent("Titolo", title);
        addComponent("Parole chiave", tags);
        addComponent("DOI", DOI);
        addComponent("Data di pubblicazione", pubblicationDate);
        addComponent("Lingua", language);
        addComponent("Categorie", categories);

        JButton confirmButton = new JButton("OK");
        confirmButton.addActionListener(this);
        contentPane.add(confirmButton, BorderLayout.SOUTH);
    }

    private void setupDescription() {
        // FIXME: con troppi caratteri comincia a comportarsi in maniera strana

        JLabel descriptionLabel = new JLabel("Descrizione");
        descriptionLabel.setMaximumSize(maximumSize);
        descriptionLabel.setAlignmentX(alignment);

        description = new JTextArea();
        description.setLineWrap(true);
        description.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        description.setAlignmentX(alignment);

        fieldPanel.add(descriptionLabel);
        fieldPanel.add(description);
    }

    protected void addComponent(JComponent component) {
        component.setMaximumSize(maximumSize);
        component.setAlignmentX(alignment);

        fieldPanel.add(component);

        Component spacing = Box.createHorizontalGlue();
        spacing.setMaximumSize(new Dimension(Integer.MAX_VALUE, 10));

        fieldPanel.add(spacing);

    }

    protected void addComponent(String label, JComponent component) {
        JLabel labelField = new JLabel(label);
        labelField.setMaximumSize(maximumSize);
        labelField.setAlignmentX(alignment);

        addComponent(labelField);
        addComponent(component);
    }

    protected void addComponent(String label, JComponent component, String tooltip) {
        component.setToolTipText(tooltip);
        addComponent(label, component);
    }

    @Override
    public abstract void actionPerformed(ActionEvent e);

    protected String getReferenceTitle() {
        return title.getText().trim();
    }

    protected Date getPubblicationDate() {
        return pubblicationDate.getDate();
    }

    protected String getDOI() {
        return DOI.getText().trim();
    }

    protected String getDescription() {
        return description.getText().trim();
    }

    protected Tag[] getTags() {
        // FIXME:
        return null;
    }

    protected ReferenceLanguage getLanguage() {
        return (ReferenceLanguage) language.getSelectedItem();
    }

    protected Category[] getCategories() {
        return categories.getSelectedCategories();
    }

}
