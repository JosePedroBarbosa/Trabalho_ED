package dataStructures.implementations;

import dataStructures.exceptions.EmptyCollectionException;
import dataStructures.exceptions.NoSuchElementException;
import dataStructures.ADTS.UnorderedListADT;

/**
 * This class represents a ArrayUnorderedList where elements are added in an unordered way.
 *
 * @param <T> the type of elements stored in the list
 */
public class ArrayUnorderedList<T> extends ArrayList<T> implements UnorderedListADT<T> {
    /**
     * Default constructor that initializes the list with a default capacity.
     */
    public ArrayUnorderedList() {
        super();
    }

    /**
     * Default constructor that initializes the list with an initial capacity.
     */
    public ArrayUnorderedList(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Adds the specified element to the front of this unordered list.
     *
     * @param element the element to be added to the front of the list
     * @throws IllegalArgumentException if the element is null
     */
    @Override
    public void addToFront(T element) {
        if (element == null){
            throw new IllegalArgumentException("Element cannot be null");
        }

        if (size() == list.length){
            expandCapacity();
        }
        for (int i = size(); i > 0; i--) {
            list[i] = list[i - 1];
        }
        list[0] = element;
        size++;
        modCount++;
    }

    /**
     * Adds the specified element to the rear of this unordered list.
     *
     * @param element the element to be added to the rear of the list
     * @throws IllegalArgumentException if the element is null
     */
    @Override
    public void addToRear(T element) {
        if (element == null){
            throw new IllegalArgumentException("Element cannot be null");
        }

        if (size() == list.length){
            expandCapacity();
        }
        list[size()] = element;
        size++;
        modCount++;
    }


    /**
     * Adds the specified element after the target element in this unordered list.
     *
     * @param element the element to be added
     * @param target the target element after which the new element will be added
     * @throws IllegalArgumentException if the element or target is null
     * @throws IllegalArgumentException if the target element is not found
     */
    @Override
    public void addAfter(T element, T target) throws EmptyCollectionException, NoSuchElementException {
        if (element == null || target == null) {
            throw new IllegalArgumentException("Element or target cannot be null");
        }

        if (isEmpty()){
            throw new EmptyCollectionException();
        }
        if (size() == list.length){
            expandCapacity();
        }

        int index = 0;
        while (index < size() && !target.equals(list[index])) {
            index++;
        }
        if (index == size) {
            throw new NoSuchElementException();
        }
        for (int i = size; i > index; i--) {
            list[i] = list[i - 1];
        }
        list[index + 1] = element;
        size++;
        modCount++;
    }
}