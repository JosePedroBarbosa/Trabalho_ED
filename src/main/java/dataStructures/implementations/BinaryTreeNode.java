package dataStructures.implementations;

/**
 * BinaryTreeNode represents a node in a binary tree with a left and right child.
 */
public class BinaryTreeNode<T> {
    /**
     * The element stored in this binary tree node.
     */
    protected T element;

    /**
     * A reference to the left and right child of this node.
     */
    protected BinaryTreeNode<T> left, right;

    /**
     * Creates a new tree node with the specified data.
     *
     * @param obj the element that will become a part of the new tree node
     */
    public BinaryTreeNode (T obj) {
        element = obj;
        left = null;
        right = null;
    }

    /**
     * Creates a new binary tree node with the specified element and children.
     *
     * @param obj the element that will become a part of the new tree node
     * @param left the left child of the new tree node
     * @param right the right child of the new tree node
     */
    public BinaryTreeNode(T obj, BinaryTreeNode<T> left, BinaryTreeNode<T> right) {
        this.element = obj;
        this.left = left;
        this.right = right;
    }

    /**
     * Retrieves the element stored in this node.
     *
     * @return the element stored in this node
     */
    public T getElement() {
        return element;
    }

    /**
     * Updates the element stored in this node.
     *
     * @param element the new element to be stored in this node
     */
    public void setElement(T element) {
        this.element = element;
    }

    /**
     * Retrieves the left child of this node.
     *
     * @return a reference to the left child node, or null if no left child exists
     */
    public BinaryTreeNode<T> getLeft() {
        return left;
    }

    /**
     * Sets the left child of this node.
     *
     * @param left the node to be set as the left child
     */
    public void setLeft(BinaryTreeNode<T> left) {
        this.left = left;
    }

    /**
     * Retrieves the right child of this node.
     *
     * @return a reference to the right child node, or null if no right child exists
     */
    public BinaryTreeNode<T> getRight() {
        return right;
    }

    /**
     * Sets the right child of this node.
     *
     * @param right the node to be set as the right child
     */
    public void setRight(BinaryTreeNode<T> right) {
        this.right = right;
    }

    /**
     * Returns the number of non-null children of this node.
     * This method may be able to be written more efficiently.
     *
     * @return the integer number of non-null children of this node
     */
    public int numChildren() {
        int children = 0;

        if (left != null){
            children = 1 + left.numChildren();
        }

        if (right != null){
            children = children + 1 + right.numChildren();
        }

        return children;
    }

    /**
     * Returns a string representation of this binary tree node.
     * The string includes the element stored in the node, as well as references
     * to its left and right children.
     *
     * @return a string representation of the binary tree node
     */
    @Override
    public String toString() {
        return "BinaryTreeNode{" + "element=" + element + ", left=" + left + ", right=" + right + '}';
    }
}