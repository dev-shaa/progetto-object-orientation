package GUI.References.Editor;

import Entities.References.BibliographicReference;

/**
 * TODO: commenta
 */
public interface ReferenceEditorListener<T extends BibliographicReference> {
    public void onReferenceCreation(T newReference);
}
