package GUI.Homepage.Search;

import java.util.EventListener;

/**
 * L'interfaccia per i listener che vogliono essere avvertiti quando viene eseguita una ricerca.
 */
public interface ReferenceSearchListener extends EventListener {

    /**
     * Invocato quando viene eseguito la ricerca.
     * 
     * @param search
     *            ricerca eseguita
     */
    public void onReferenceSearch(Search search);
}
