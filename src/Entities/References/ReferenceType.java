package Entities.References;

import Entities.References.PhysicalResources.Article;

// TODO: boh forse è una buona idea

public enum ReferenceType {
    ARTICLE(Article.class);

    private Class<? extends BibliographicReference> type;

    ReferenceType(Class<? extends BibliographicReference> type) {
        this.type = type;
    }

    private Class<? extends BibliographicReference> get() {
        return type;
    }

    public ReferenceType getTypeFromClass(Class<? extends BibliographicReference> c) {
        for (ReferenceType type : ReferenceType.values()) {
            if (type.get().equals(c)) {
                return type;
            }
        }

        return null;
    }

    // @Override
    // public String toString() {
    // return this.name;
    // }
}
