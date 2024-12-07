package dataStructures.implementations;

import dataStructures.ADTS.BinaryTreeADT;
import dataStructures.exceptions.EmptyCollectionException;

import java.util.Iterator;

/**
 * LinkedBinaryTree implements the BinaryTreeADT interface
 *
 */
public class LinkedBinaryTree<T> implements BinaryTreeADT<T> {
    protected int count;
    protected BinaryTreeNode<T> root;

    /**
     * Creates an empty binary tree with no elements.
     */
    public LinkedBinaryTree() {
        count = 0;
        root = null;
    }

    /**
     * Creates a binary tree with the specified element as its root.
     *
     * @param element the element that will become the root of the new binary tree
     */
    public LinkedBinaryTree(T element) {
        count = 1;
        root = new BinaryTreeNode<T>(element);
    }

    /**
     * Retrieves the root element of the binary tree.
     *
     * @return the element stored in the root node
     */
    @Override
    public T getRoot(){
        return root.element;
    }

    /**
     * Checks if the binary tree is empty.
     *
     * @return true if the binary tree is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return (count == 0);
    }

    /**
     * Returns the number of elements in the binary tree.
     *
     * @return the total number of nodes in the binary tree
     */
    @Override
    public int size(){
        return count;
    }

    /**
     * Checks if the binary tree contains a specific target element.
     *
     * @param targetElement the element to search for in the binary tree
     * @return true if the element is found, false otherwise
     */
    @Override
    public boolean contains(T targetElement) {
        try {
            find(targetElement);
        }catch (EmptyCollectionException ex){
            return false;
        }

        return true;
    }

    /**
     * Searches for and retrieves a specific element in the binary tree.
     *
     * @param targetElement the element to find in the binary tree
     * @return the found element
     * @throws EmptyCollectionException if the tree is empty or the element is not found
     */
    @Override
    public T find(T targetElement) throws EmptyCollectionException {
        BinaryTreeNode<T> current = findAgain(targetElement, root);

        if(current == null) {
            throw new EmptyCollectionException("binary tree");
        }

        return (current.element);
    }

    /**
     * Searches for an element in the binary tree.
     * This method performs a recursive search through the tree.
     *
     * @param targetElement the element to search for in the binary tree
     * @param next the current node being examined during the recursive search
     * @return the element found in the tree
     */
    private BinaryTreeNode<T> findAgain(T targetElement, BinaryTreeNode<T> next) {
        if(next == null) {
            return null;
        }

        if(next.element.equals(targetElement)) {
            return next;
        }

        BinaryTreeNode<T> tmp = findAgain(targetElement, next.left);

        if(tmp == null) {
            tmp = findAgain(targetElement, next.right);
        }

        return tmp;
    }

    /**
     * Performs an in-order traversal of the binary tree and returns an iterator.
     *
     * @return an iterator for the elements in in-order
     */
    @Override
    public Iterator<T> iteratorInOrder() {
        ArrayUnorderedList<T> tmpList = new ArrayUnorderedList<T>();
        inorder(root, tmpList);

        return tmpList.iterator();
    }

    /**
     * Performs an in-order traversal of the binary tree. In this traversal,
     * the left subtree is visited first, then the root node, and finally the right subtree.
     * The elements are added to a temporary list, which is later used to return an iterator.
     *
     * @param node the node to start the in-order traversal from
     * @param tmpList the list to store the elements during the traversal
     */
    protected void inorder(BinaryTreeNode<T> node, ArrayUnorderedList<T> tmpList) {
        if(node != null) {
            inorder(node.left, tmpList);
            tmpList.addToRear(node.element);
            inorder(node.right, tmpList);
        }
    }

    /**
     * Performs a pre-order traversal of the binary tree and returns an iterator.
     *
     * @return an iterator for the elements in pre-order
     */
    @Override
    public Iterator<T> iteratorPreOrder() {
        ArrayUnorderedList<T> tmpList = new ArrayUnorderedList<T>();
        preorder(root, tmpList);

        return tmpList.iterator();
    }

    /**
     * Performs a pre-order traversal of the binary tree. In this traversal,
     * the root node is visited first, followed by the left and right subtrees.
     * The elements are added to a temporary list, which is later used to return an iterator.
     *
     * @param node the node to start the pre-order traversal from
     * @param tmpList the list to store the elements during the traversal
     */
    protected void preorder(BinaryTreeNode<T> node, ArrayUnorderedList<T> tmpList) {
        if(node != null) {
            tmpList.addToRear(node.element);
            preorder(node.left, tmpList);
            preorder(node.right, tmpList);
        }
    }

    /**
     * Performs a post-order traversal of the binary tree and returns an iterator.
     *
     * @return an iterator for the elements in post-order
     */
    @Override
    public Iterator<T> iteratorPostOrder() {
        ArrayUnorderedList<T> tmpList = new ArrayUnorderedList<T>();
        postorder(root, tmpList);

        return tmpList.iterator();
    }

    /**
     * Performs a post-order traversal of the binary tree. In this traversal,
     * the left and right subtrees are visited first, followed by the root node.
     * The elements are added to a temporary list, which is later used to return an iterator.
     *
     * @param node the node to start the post-order traversal from
     * @param tmpList the list to store the elements during the traversal
     */
    protected void postorder(BinaryTreeNode<T> node, ArrayUnorderedList<T> tmpList) {
        if(node != null) {
            postorder(node.left, tmpList);
            postorder(node.right, tmpList);
            tmpList.addToRear(node.element);
        }
    }

    /**
     * Performs a level-order traversal of the binary tree and returns an iterator.
     *
     * @return an iterator for the elements in level-order
     */
    @Override
    public Iterator<T> iteratorLevelOrder() {
        LinkedQueue<BinaryTreeNode<T>> nodes = new LinkedQueue<>();
        ArrayUnorderedList<T> results = new ArrayUnorderedList<>();

        nodes.enqueue(root);

        BinaryTreeNode<T> current;

        while (!nodes.isEmpty()) {
            current = nodes.dequeue();
            results.addToRear(current.element);

            if(current.left != null) {
                nodes.enqueue(current.left);
            }

            if(current.right != null) {
                nodes.enqueue(current.right);
            }
        }

        return results.iterator();
    }

    /**
     * Returns a string representation of the binary tree, including its size and root.
     *
     * @return a string representation of the binary tree
     */
    @Override
    public String toString() {
        return "LinkedBinaryTree{" + "count=" + count + ", root=" + root + '}';
    }

}