package Entities.References.OnlineResources;

import Entities.Author;

/**
 * Classe che rappresenta un riferimento bibliografico a un sito web.
 */
public class Website extends OnlineResource {

    /**
     * Crea un nuovo riferimento a sito web con il titolo, gli autori e l'url indicati.
     * 
     * @param title
     *            titolo del riferimento
     * @param authors
     *            autori del riferimento
     * @param URL
     *            url del riferimento
     * @throws IllegalArgumentException
     *             se il titolo o l'url non sono validi
     * @see #setTitle(String)
     * @see #setURL(String)
     */
    public Website(String title, Author[] authors, String URL) throws IllegalArgumentException {
        super(title, authors, URL);
    }

}
