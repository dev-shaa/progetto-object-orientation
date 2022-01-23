package GUI.Homepage.References.Chooser;

import java.util.EventListener;
import Entities.References.BibliographicReference;

/**
 * L'interfaccia per i listener che vogliono essere avvertiti quando viene selezionato un riferimento dalla finestra di dialogo apposita.
 */
public interface ReferenceChooserSelectionListener extends EventListener {

    /**
     * Invocato quando viene confermato il riferimento selezionato dalla finestra di dialogo.
     * 
     * @param reference
     *            riferimento selezionato
     */
    public void onReferenceChooserSelection(BibliographicReference reference);
}
