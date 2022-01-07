public abstract class Publication extends BibliographicReference {

    private int pageCount;

    public Publication(String name) throws IllegalArgumentException {
        super(name);
    }

    /**
     * Imposta il numero di pagine della pubblicazione.
     * 
     * @param pageCount
     *            numero di pagine della pubblicazione
     * @throws IllegalArgumentException
     *             se {@code pageCount <= 0}
     */
    public void setPageCount(int pageCount) throws IllegalArgumentException {
        if (pageCount <= 0)
            throw new IllegalArgumentException("Il numero di pagine del libro non può essere inferiore a 1");

        this.pageCount = pageCount;
    }

    /**
     * Restituisce il numero di pagine della pubblicazione.
     * 
     * @return
     *         numero di pagine della pubblicazione
     */
    public int getPageCount() {
        return this.pageCount;
    }

}
