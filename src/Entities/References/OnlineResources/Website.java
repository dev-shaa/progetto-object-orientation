package Entities.References.OnlineResources;

import Entities.Author;

/**
 * 
 */
public class Website extends OnlineResource {

    /**
     * TODO: commenta
     * 
     * @param title
     * @param authors
     * @param URL
     * @throws IllegalArgumentException
     */
    public Website(String title, Author[] authors, String URL) throws IllegalArgumentException {
        super(title, authors, URL);
    }

}
