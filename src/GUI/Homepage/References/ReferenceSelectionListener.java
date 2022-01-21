package GUI.Homepage.References;

import java.util.EventListener;

import Entities.References.BibliographicReference;

public interface ReferenceSelectionListener extends EventListener {
    public void onReferenceSelection(BibliographicReference reference);
}
