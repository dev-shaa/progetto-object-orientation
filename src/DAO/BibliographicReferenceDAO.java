package DAO;

import java.util.List;

import Entities.References.*;
import Entities.References.OnlineResources.*;
import Entities.References.PhysicalResources.*;
import Exceptions.ReferenceDatabaseException;

/**
 * Interfaccia che deve essere implementata per gestire la parte di database relativo ai riferimenti.
 */
public interface BibliographicReferenceDAO {

    /**
     * Restituisce tutti i riferimenti associati all'utente.
     * 
     * @return
     *         lista con i riferimenti dell'utente
     * @throws ReferenceDatabaseException
     *             se il recupero non va a buon fine
     */
    public List<BibliographicReference> getAll() throws ReferenceDatabaseException;

    /**
     * Rimuove un riferimento dal database.
     * 
     * @param reference
     *            riferimento da rimuovere
     * @throws ReferenceDatabaseException
     *             se la rimozione non va a buon fine
     */
    public void remove(BibliographicReference reference) throws ReferenceDatabaseException;

    /**
     * Salva un articolo nel database (modificandolo se già esiste un riferimento con lo stesso id).
     * 
     * @param article
     *            articolo da salvare
     * @throws ReferenceDatabaseException
     *             se il salvataggio non va a buon fine
     */
    public void save(Article article) throws ReferenceDatabaseException;

    /**
     * Salva un libro nel database (modificandolo se già esiste un riferimento con lo stesso id).
     * 
     * @param book
     *            libro da salvare
     * @throws ReferenceDatabaseException
     *             se il salvataggio non va a buon fine
     */
    public void save(Book book) throws ReferenceDatabaseException;

    /**
     * Salva una tesi nel database (modificandola se già esiste un riferimento con lo stesso id).
     * 
     * @param thesis
     *            tesi da salvare
     * @throws ReferenceDatabaseException
     *             se il salvataggio non va a buon fine
     */
    public void save(Thesis thesis) throws ReferenceDatabaseException;

    /**
     * Salva un'immagine nel database (modificandola se già esiste un riferimento con lo stesso id).
     * 
     * @param image
     *            immagine da salvare
     * @throws ReferenceDatabaseException
     *             se il salvataggio non va a buon fine
     */
    public void save(Image image) throws ReferenceDatabaseException;

    /**
     * Salva un articolo nel database (modificandolo se già esiste un riferimento con lo stesso id).
     * 
     * @param article
     *            articolo da salvare
     * @throws ReferenceDatabaseException
     *             se il salvataggio non va a buon fine
     */
    public void save(SourceCode sourceCode) throws ReferenceDatabaseException;

    /**
     * Salva un video nel database (modificandolo se già esiste un riferimento con lo stesso id).
     * 
     * @param video
     *            video da salvare
     * @throws ReferenceDatabaseException
     *             se il salvataggio non va a buon fine
     */
    public void save(Video video) throws ReferenceDatabaseException;

    /**
     * Salva un sito web nel database (modificandolo se già esiste un riferimento con lo stesso id).
     * 
     * @param website
     *            sito web da salvare
     * @throws ReferenceDatabaseException
     *             se il salvataggio non va a buon fine
     */
    public void save(Website website) throws ReferenceDatabaseException;

}
