package dataStructures.implementations;

import dataStructures.ADTS.ListADT;
import exceptions.ConcurrentModificationException;
import exceptions.EmptyCollectionException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class represents a generic list that uses an array to store elements.
 * This implementation allows basic operations such as adding, removing, and accessing elements.
 *
 * @param <T> the type of elements in the list
 */
public abstract class ArrayList<T> implements ListADT<T> {
    private static final int INITIAL_CAPACITY = 10;
    private static final int EXPANSION_FACTOR = 10;
    protected T[] list;
    protected int count;

    /**
     * Default constructor that initializes the list with an initial capacity.
     */
    public ArrayList() {
        this.list = (T[]) (new Object[INITIAL_CAPACITY]);
        this.count = 0;
    }

    /**
     * Default constructor that initializes the list with an given capacity.
     */
    public ArrayList(int capacity) {
        this.list = (T[]) (new Object[capacity]);
        this.count = 0;
    }

    /**
     * Removes the first element of the list.
     *
     * @return the removed element
     * @throws EmptyCollectionException if the list is empty
     */
    @Override
    public T removeFirst() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("The List Is Empty");
        }

        T element = this.list[0];

        for (int i = 0; i < this.count - 1; i++) {
            this.list[i] = this.list[i + 1];
        }

        this.list[--this.count] = null;
        return element;
    }

    /**
     * Removes the last element of the list.
     *
     * @return the removed element
     * @throws EmptyCollectionException if the list is empty
     */
    @Override
    public T removeLast() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("The List Is Empty");
        }

        T element = this.list[this.count - 1];
        this.list[--this.count] = null;

        return element;
    }

    /**
     * Removes a specific element from the list.
     *
     * @param element the element to be removed
     * @return the removed element
     * @throws EmptyCollectionException if the list is empty
     * @throws NoSuchElementException if the element does not exist in the list
     */
    @Override
    public T remove(T element) throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("The List Is Empty");
        }

        int index = this.find(element);

        if (index == -1) {
            throw new NoSuchElementException("The Element Does Not Exists");
        }

        T elementToRemove = this.list[index];

        for (int i = index; i < this.count - 1; i++) {
            this.list[i] = this.list[i + 1];
        }

        this.list[--this.count] = null;
        return elementToRemove;
    }

    /**
     * Returns the first element of the list.
     *
     * @return the first element
     * @throws EmptyCollectionException if the list is empty
     */
    @Override
    public T first() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("The List Is Empty");
        }
        return this.list[0];
    }

    /**
     * Returns the last element of the list.
     *
     * @return the last element
     * @throws EmptyCollectionException if the list is empty
     */
    @Override
    public T last() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("The List Is Empty");
        }
        return this.list[this.count - 1];
    }

    /**
     * Checks if the list contains a specific element.
     *
     * @param target the element to check for
     * @return true if the list contains the element, false otherwise
     */
    @Override
    public boolean contains(T target) {
        boolean contains = false;

        for (T element : this.list) {
            if (element.equals(target)) {
                contains = true;
                break;
            }
        }

        return contains;
    }

    /**
     * Checks if the list is empty.
     *
     * @return true if the list is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return (this.count == 0);
    }

    /**
     * Returns the number of elements in the list.
     *
     * @return the size of the list
     */
    @Override
    public int size() {
        return this.count;
    }

    /**
     * Finds the position of an element in the list.
     *
     * @param element the element to find
     * @return the index of the element if found, -1 otherwise
     */
    private int find(T element) {
        int position = -1;

        for (int i = 0; i < this.count; i++) {
            if (this.list[i].equals(element)) {
                position = i;
                break;
            }
        }

        return position;
    }

    /**
     * Returns an iterator for the list.
     *
     * @return an Iterator that iterates over the elements of the list
     */
    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator<>(list, count);
    }

    /**
     * Expands the capacity of the list.
     */
    protected void expandCapacity() {
        T[] temp = (T[]) (new Object[this.list.length + EXPANSION_FACTOR]);

        for (int i = 0; i < count; i++){
            temp[i] = list[i];
        }

        this.list = temp;
    }

    /**
     * Returns a string representation of the list.
     *
     * @return the string representation of the list
     */
    @Override
    public String toString() {
        String s = "******\n List \n******\n";

        for (T element : this.list) {
            if (element != null) {
                s += "➯ " + element.toString() + '\n';
            }
        }

        return s;
    }

    /**
     * This class implements an iterator for ArrayList.
     *
     * @param <T> the type of elements in the list
     */
    public class ArrayIterator<T> implements Iterator<T> {
        private T[] items; // Array of the collection
        private int current; // Current index
        private int expectedModCount; // Expected mod count
        private boolean okToRemove; // Flag for removal control

        /**
         * Constructor for the ArrayIterator.
         *
         * @param collection the array of the collection
         * @param modCount the modification count
         */
        public ArrayIterator(T[] collection, int modCount) {
            this.items = collection;
            this.current = 0;
            this.expectedModCount = modCount;
            this.okToRemove = false;
        }

        /**
         * Checks if there are more elements to iterate over.
         *
         * @return true if there are more elements, false otherwise
         */
        @Override
        public boolean hasNext() {
            return current < items.length;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element
         * @throws NoSuchElementException if there are no more elements
         */
        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("There are no other elements.");
            }
            okToRemove = true;
            return items[current++];
        }

        /**
         * Removes the last element returned by the next() method.
         *
         * @throws IllegalStateException if remove() is called before next()
         * @throws ConcurrentModificationException if the collection is modified while iterating
         */
        @Override
        public void remove() {
            if (!okToRemove) {
                throw new IllegalStateException("remove() method can only be called after calling the next() method.");
            }

            if (expectedModCount != items.length) {
                throw new ConcurrentModificationException("The collection was modified!");
            }

            if (current > 0){
                for (int i = current - 1; i < items.length - 1; i++){
                    items[i] = items[i + 1];
                }
                items[items.length - 1] = null;
                expectedModCount++;
                current--;
            }
            okToRemove = false;
        }
    }
}