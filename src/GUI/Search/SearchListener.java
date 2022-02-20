package GUI.Search;

import java.util.EventListener;
import Entities.Search;

/**
 * L'interfaccia per i listener che vogliono essere avvertiti quando viene eseguita una ricerca.
 */
public interface SearchListener extends EventListener {

    /**
     * Invocato quando viene eseguito la ricerca.
     * 
     * @param search
     *            ricerca eseguita
     */
    public void onSearch(Search search);
}
