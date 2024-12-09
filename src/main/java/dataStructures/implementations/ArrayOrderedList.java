package dataStructures.implementations;

import dataStructures.ADTS.OrderedListADT;

/**
 * ArrayOrderedList implements an ordered list using an array.
 *
 * @param <T> the type of elements in the list, which must be comparable.
 */
public class ArrayOrderedList<T> extends ArrayList<T> implements OrderedListADT<T> {
    /**
     * Constructs an empty ordered list with the default capacity.
     */
    public ArrayOrderedList() {
        super();
    }

    /**
     * Constructs an empty ordered list with a specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the list
     */
    public ArrayOrderedList(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Adds the specified element to the ordered list, maintaining the list's order.
     *
     * The element must implement the Comparable interface to be added.
     *
     * @param element the element to be added to the ordered list
     * @throws IllegalArgumentException if the element is null or does not implement Comparable
     */
    @Override
    public void add(T element) {

        if (element == null) {
            throw new IllegalArgumentException("Element cannot be null");
        }
        if (!(element instanceof Comparable)) {
            throw new IllegalArgumentException("Element must be comparable");
        }

        if (this.size == this.list.length) {
            this.expandCapacity();
        }

        Comparable<T> comparableElement = (Comparable<T>) element;

        int position = 0;

        while (position < this.size && comparableElement.compareTo(this.list[position]) > 0) {
            position++;
        }

        for (int i = this.size; i > position; i--) {
            this.list[i] = this.list[i - 1];
        }

        this.list[position] = element;
        this.size++;
        modCount++;
    }
}
