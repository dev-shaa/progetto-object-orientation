public abstract class CategoriesActionListener {

    /**
     * Evento azionato quando viene aggiunta una categoria.
     * 
     * @param category
     *            nuova categoria aggiunta
     */
    public void onCategoriesAdd(Category category) {

    };

    /**
     * Evento azionato quando viene modificata una categoria.
     * 
     * @param category
     *            categoria modificata
     */
    public void onCategoriesChange(Category category) {

    };

    /**
     * Evento azionato quando viene rimossa una categoria.
     * 
     * @param category
     *            categoria rimossa
     */
    public void onCategoriesRemove(Category category) {

    };
}
