package dataStructures.implementations;

import dataStructures.exceptions.EmptyCollectionException;
import dataStructures.ADTS.ListADT;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

/**
 * ArrayList is a resizable array-based implementation of a list.
 *
 * @param <T> the type of elements in this list
 */
public class ArrayList<T> implements ListADT<T>, Iterable<T> {
    private final static int DEFAULT_CAPACITY = 100;
    protected T[] list;
    protected int size, modCount;

    /**
     * Default constructor that initializes the list with a default capacity of 100.
     */
    public ArrayList() {
        this.list = (T[]) (new Object[DEFAULT_CAPACITY]);
        this.size = 0;
        this.modCount = 0;
    }

    /**
     * Constructor that initializes the list with a specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the list
     */
    public ArrayList(int initialCapacity) {
        this.list = (T[]) (new Object[initialCapacity]);
        this.size = 0;
        this.modCount = 0;
    }


    /**
     * Expands the capacity of the list when it reaches its limit.
     * Doubles the current capacity and transfers the elements to the new list.
     */
    protected void expandCapacity() {
        T[] newList = (T[]) (new Object[list.length * 2]);
        for (int i = 0; i < list.length; i++) {
            newList[i] = list[i];
        }
        list = newList;
    }

    /**
     * Removes and returns the first element of the list.
     *
     * @return the first element of the list
     * @throws EmptyCollectionException if the list is empty
     */
    @Override
    public T removeFirst() throws EmptyCollectionException {
        if (isEmpty()){
            throw new EmptyCollectionException();
        }
        T removed = list[0];
        for (int shift = 0; shift < size() - 1; shift++) {
            list[shift] = list[shift + 1];
        }
        list[size() - 1] = null;
        size--;
        modCount++;
        return removed;
    }

    /**
     * Removes and returns the last element of the list.
     *
     * @return the last element of the list
     * @throws EmptyCollectionException if the list is empty
     */
    @Override
    public T removeLast() throws EmptyCollectionException {
        if (isEmpty()){
            throw new EmptyCollectionException();
        }
        T result = list[size() - 1];
        list[size() - 1] = null;
        size--;
        modCount++;
        return result;
    }

    /**
     * Removes the specified element from the list and returns it.
     *
     * @param element the element to be removed
     * @return the removed element
     * @throws EmptyCollectionException if the list is empty
     * @throws NoSuchElementException if the element is not found in the list
     */
    @Override
    public T remove(T element) throws EmptyCollectionException, NoSuchElementException {
        if (isEmpty()){
            throw new EmptyCollectionException();
        }
        int index = 0;
        while (index < size() && !list[index].equals(element)) {
            index++;
        }
        if (index == size()){
            throw new NoSuchElementException();
        }
        T removed = list[index];
        for (int shift = index; shift < size() - 1; shift++) {
            list[shift] = list[shift + 1];
        }
        list[size() - 1] = null;
        size--;
        modCount++;
        return removed;
    }

    /**
     * Returns the first element of the list without removing it.
     *
     * @return the first element of the list
     * @throws EmptyCollectionException if the list is empty
     */
    @Override
    public T first() throws EmptyCollectionException {
        if (isEmpty()){
            throw new EmptyCollectionException();
        }
        return list[0];
    }

    /**
     * Returns the last element of the list without removing it.
     *
     * @return the last element of the list
     * @throws EmptyCollectionException if the list is empty
     */
    @Override
    public T last() throws EmptyCollectionException {
        if (isEmpty()){
            throw new EmptyCollectionException();
        }
        return list[size() - 1];
    }

    /**
     * Checks if the list contains the specified element.
     *
     * @param target the element to search for
     * @return true if the list contains the target element, false otherwise
     * @throws EmptyCollectionException if the list is empty
     */
    @Override
    public boolean contains(T target) throws EmptyCollectionException {
        if (isEmpty()){
            throw new EmptyCollectionException();
        }
        int index = 0;
        while (index < size() && !list[index].equals(target)) {
            index++;
        }
        return index < size();
    }

    /**
     * Checks if the list is empty.
     *
     * @return true if the list is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns the current size of the list.
     *
     * @return the number of elements in the list
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns an iterator over the elements in the list.
     *
     * @return an iterator over the elements in the list
     */
    @Override
    public Iterator<T> iterator() {
        return new BasicIterator(){};
    }

    /**
     * Returns a string representation of the list.
     *
     * @return a string representation of the list
     */
    @Override
    public String toString() {
        String result = "";
        if (!isEmpty()) {
            for (int i = 0; i < size(); i++) {
                result += list[i].toString();
                result += "---------------\n";
            }
        }
        return result;
    }

    /**
     * Implements a basic iterator for the ArrayList.
     */
    private abstract class BasicIterator implements Iterator<T> {
        private int current;
        private int expectedModCount;
        private boolean okToRemove;

        public BasicIterator() {
            this.current = 0;
            this.expectedModCount = modCount;
            this.okToRemove = false;
        }

        @Override
        public boolean hasNext() {
            return current < size();
        }

        @Override
        public T next() {
            if (modCount != expectedModCount){
                throw new ConcurrentModificationException();
            }
            if (!hasNext()){
                throw new NoSuchElementException();
            }
            okToRemove = true;
            return list[current++];
        }

        @Override
        public void remove() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }
            if (!okToRemove) {
                throw new NoSuchElementException();
            }
            ArrayList.this.remove(list[current - 1]);
            current--;
            expectedModCount++;
            okToRemove = false;
        }
    }

    /**
     * Returns the element at the specified index.
     *
     * @param index the index of the element to retrieve
     * @return the element at the specified index
     */
    public T getByIndex(int index){
        return this.list[index];
    }
}