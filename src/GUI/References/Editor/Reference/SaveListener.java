package GUI.References.Editor.Reference;

import Entities.References.BibliographicReference;

public interface SaveListener<T extends BibliographicReference> {
    public void save(T reference);
}
