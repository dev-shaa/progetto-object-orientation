package GUI.References;

import Entities.References.*;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Pannello che mostra le informazioni relative a un riferimento bibliografico.
 */
public class ReferenceInfoPanel extends JScrollPane {

    private JTextArea textArea;

    /**
     * Crea un pannello contenente una tabella composta da due colonne,
     * la prima contenente il nome dell'informazione e la seconda l'informazione stessa.
     */
    public ReferenceInfoPanel() {
        super();

        textArea = new JTextArea();
        textArea.setRows(10);
        textArea.setEditable(false);

        setViewportView(textArea);
    }

    /**
     * Imposta il riferimento bibliografico di cui mostrare i dettagli.
     * Se {@code reference == null}, ha lo stesso effetto di {@link #clear()}
     * 
     * @param reference
     *            riferimento da mostrare
     */
    public void showReference(BibliographicReference reference) {
        if (reference == null)
            clear();
        else
            textArea.setText(reference.getInfo());
    }

    /**
     * Rimuove il riferimento mostrato.
     */
    public void clear() {
        textArea.setText(null);
    }

}
