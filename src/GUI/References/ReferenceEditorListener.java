package GUI.References;

import java.util.EventListener;
import Entities.References.BibliographicReference;

/**
 * Interfaccia per i listener che vogliono essere avvertiti quando viene creato un riferimento.
 */
public interface ReferenceEditorListener<T extends BibliographicReference> extends EventListener {

    /**
     * Invocato quando viene creato un riferimento.
     * 
     * @param reference
     *            riferimento creato
     */
    public void onReferenceCreation(T newReference);
}