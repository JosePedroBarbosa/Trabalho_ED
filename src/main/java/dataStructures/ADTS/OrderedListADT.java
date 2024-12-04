package dataStructures.ADTS;

import dataStructures.exceptions.NonComparableElementException;

/**
 * OrderedListADT defines the interface to an ordered list collection.
 *
 * @param <T> the type of element in this list
 */
public interface OrderedListADT<T> extends ListADT<T> {

    /**
     * Adds the specified element to this list at the proper location according to its value.
     *
     * @param element the element to be added to this list
     * @throws NonComparableElementException if the element is not Comparable
     */
    void add(T element) throws NonComparableElementException;
}