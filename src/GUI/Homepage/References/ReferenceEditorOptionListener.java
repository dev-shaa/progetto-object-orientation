package GUI.Homepage.References;

import java.util.EventListener;

import Entities.References.OnlineResources.*;
import Entities.References.PhysicalResources.*;

/**
 * L'interfaccia per i listener che vogliono essere avvertiti quando viene dato l'input di aprire l'editor di riferimenti.
 */
public interface ReferenceEditorOptionListener extends EventListener {

    /**
     * Invocato quando viene dato l'input di aprire l'editor per articoli.
     * <p>
     * Quando {@code reference == null}, vuol dire che è stato l'input di creare un nuovo articolo.
     * 
     * @param reference
     *            eventuale riferimento da modificare (può essere {@code null})
     */
    public void onArticleEditorCall(Article reference);

    /**
     * Invocato quando viene dato l'input di aprire l'editor per libri.
     * <p>
     * Quando {@code reference == null}, vuol dire che è stato l'input di creare un nuovo libro.
     * 
     * @param reference
     *            eventuale riferimento da modificare (può essere {@code null})
     */
    public void onBookEditorCall(Book reference);

    /**
     * Invocato quando viene dato l'input di aprire l'editor per tesi.
     * <p>
     * Quando {@code reference == null}, vuol dire che è stato l'input di creare una nuova tesi.
     * 
     * @param reference
     *            eventuale riferimento da modificare (può essere {@code null})
     */
    public void onThesisEditorCall(Thesis reference);

    /**
     * Invocato quando viene dato l'input di aprire l'editor per codice sorgente.
     * <p>
     * Quando {@code reference == null}, vuol dire che è stato l'input di creare un nuovo codice sorgente.
     * 
     * @param reference
     *            eventuale riferimento da modificare (può essere {@code null})
     */
    public void onSourceCodeEditorCall(SourceCode reference);

    /**
     * Invocato quando viene dato l'input di aprire l'editor per immagini.
     * <p>
     * Quando {@code reference == null}, vuol dire che è stato l'input di creare una nuova immagine.
     * 
     * @param reference
     *            eventuale riferimento da modificare (può essere {@code null})
     */
    public void onImageEditorCall(Image reference);

    /**
     * Invocato quando viene dato l'input di aprire l'editor per video.
     * <p>
     * Quando {@code reference == null}, vuol dire che è stato l'input di creare un nuovo video.
     * 
     * @param reference
     *            eventuale riferimento da modificare (può essere {@code null})
     */
    public void onVideoEditorCall(Video reference);

    /**
     * Invocato quando viene dato l'input di aprire l'editor per siti web.
     * <p>
     * Quando {@code reference == null}, vuol dire che è stato l'input di creare un nuovo sito web.
     * 
     * @param reference
     *            eventuale riferimento da modificare (può essere {@code null})
     */
    public void onWebsiteEditorCall(Website reference);
}
