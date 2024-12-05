package dataStructures.ADTS;

/**
 * OrderedListADT defines the interface to an ordered list collection.
 *
 * @param <T> the type of element in this list
 */
public interface OrderedListADT<T> extends ListADT<T> {
    /**
     * Adds the specified element to this list at
     * the proper location
     *
     * @param element the element to be added to this list
     */
    public void add (T element);
}