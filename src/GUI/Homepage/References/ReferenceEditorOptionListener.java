package GUI.Homepage.References;

import java.util.EventListener;

import Entities.References.OnlineResources.*;
import Entities.References.PhysicalResources.*;

public interface ReferenceEditorOptionListener extends EventListener {
    public void onArticleEditorCall(Article article);

    public void onBookEditorCall(Book book);

    public void onThesisEditorCall(Thesis thesis);

    public void onSourceCodeEditorCall(SourceCode sourceCode);

    public void onImageEditorCall(Image image);

    public void onVideoEditorCall(Video video);

    public void onWebsiteEditorCall(Website website);
}
