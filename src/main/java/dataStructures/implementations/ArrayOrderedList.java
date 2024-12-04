package dataStructures.implementations;

import dataStructures.ADTS.OrderedListADT;

public class ArrayOrderedList<T> extends ArrayList<T> implements OrderedListADT<T> {

    public ArrayOrderedList() {
        super();
    }

    public ArrayOrderedList(int initialCapacity) {
        super(initialCapacity);
    }

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
