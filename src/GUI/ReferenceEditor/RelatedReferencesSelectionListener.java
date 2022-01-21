package GUI.ReferenceEditor;

import java.util.EventListener;
import Entities.References.BibliographicReference;

/**
 * L'interfaccia per i listener che vogliono essere avvertiti quando viene selezionato
 * un rimando per un riferimento.
 */
public interface RelatedReferencesSelectionListener extends EventListener {

    /**
     * Invocato quando viene confermato il rimando da associare a un riferimento.
     * 
     * @param reference
     *            rimando selezionato
     */
    public void onRelatedReferenceSelection(BibliographicReference reference);
}
