package dataStructures.implementations;

/**
 * Represents a node in a heap structure, extending the functionality of a binary tree node.
 * A heap node maintains a reference to its parent node.
 *
 * @param <T> the type of the element stored in this heap node
 */
public class HeapNode<T> extends BinaryTreeNode<T>{
    /**
     * A reference to the parent node of this heap node.
     */
    protected HeapNode<T> parent;

    /**
     * Creates a new heap node with the specified data.
     *
     * @param obj the data to be contained within
     * the new heap nodes
     */
    HeapNode (T obj) {
        super(obj);
        parent = null;
    }

    /**
     * Retrieves the parent of this heap node.
     *
     * @return a reference to the parent node.
     */
    public HeapNode<T> getParent() {
        return parent;
    }

    /**
     * Sets the parent of this heap node.
     *
     * @param parent the node to be set as the parent of this node
     */
    public void setParent(HeapNode<T> parent) {
        this.parent = parent;
    }
}
