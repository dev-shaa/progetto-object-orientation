package Controller;

import java.util.EventListener;

import Entities.References.PhysicalResources.Article;

public interface ReferenceEditorListener extends EventListener {
    public void onArticleEditRequest(Article article);
}
