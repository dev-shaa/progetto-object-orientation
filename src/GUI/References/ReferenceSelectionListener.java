package GUI.References;

import java.util.EventListener;
import Entities.References.BibliographicReference;

/**
 * L'interfaccia per i listener che vogliono essere avvertiti quando viene selezionato un riferimento.
 */
public interface ReferenceSelectionListener extends EventListener {

    /**
     * Invocato quando viene eseguito il logout.
     * 
     * @param reference
     *            riferimento selezionato
     */
    public void onReferenceSelection(BibliographicReference reference);
}
