package GUI.Categories;

import Entities.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Wrapper di DefaultMutableTreeNode che accetta Category come unico oggetto.
 * 
 * @see DefaultMutableTreeNode
 * @see Category
 */
public class CategoryMutableTreeNode extends DefaultMutableTreeNode {

    /**
     * Crea un nodo {@code CategoryMutableTreeNode} senza genitore, senza figli, che
     * ammette figli
     * e senza una categoria associata.
     */
    public CategoryMutableTreeNode() {
        super();
    }

    /**
     * Crea un nodo {@code CategoryMutableTreeNode} senza genitore, senza figli, che
     * ammette figli
     * e con una categoria associata.
     * 
     * @param category
     *            la categoria associata a questo nodo
     */
    public CategoryMutableTreeNode(Category category) {
        super(category);
    }

    /**
     * Restituisce la categoria associata a questo nodo.
     * 
     * @return
     *         la categoria di questo nodo (può essere {@code null})
     */
    @Override
    public Category getUserObject() {
        return (Category) super.getUserObject();
    }

    /**
     * Restituisce il genitore di questo nodo.
     * 
     * @return
     *         il nodo genitore
     */
    @Override
    public CategoryMutableTreeNode getParent() {
        return (CategoryMutableTreeNode) super.getParent();
    }

    /**
     * Assegna questo nodo come genitore di {@code newChild} e gli viene aggiunto il
     * nuovo figlio.
     *
     * @param newChild
     *            il nodo da inserire come figlio
     * @param childIndex
     *            l'indice dell'array di figli di questo nodo dove verrà
     *            inserito il nuovo figlio
     */
    public void insert(CategoryMutableTreeNode newChild, int childIndex) {
        super.insert(newChild, childIndex);
    }

    /**
     * Restituisce {@code true} se il nodo può essere modificato (quindi se non è la
     * radice dell'albero e la sua categoria non è nulla), {@code false} altrimenti.
     * 
     * @return
     *         {@code true} se il nodo può essere modificato
     */
    public boolean canBeChanged() {
        return !isRoot() && getUserObject() != null;
    }

}
