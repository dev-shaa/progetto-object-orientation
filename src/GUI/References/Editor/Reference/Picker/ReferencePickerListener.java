package GUI.References.Editor.Reference.Picker;

import java.util.EventListener;
import Entities.References.BibliographicReference;

/**
 * L'interfaccia per i listener che vogliono essere avvertiti quando viene selezionato un riferimento dalla finestra di dialogo apposita.
 */
public interface ReferencePickerListener extends EventListener {

    /**
     * Invocato quando viene confermato il riferimento selezionato dalla finestra di dialogo.
     * 
     * @param reference
     *            riferimento selezionato
     */
    public void onReferencePick(BibliographicReference reference);
}
