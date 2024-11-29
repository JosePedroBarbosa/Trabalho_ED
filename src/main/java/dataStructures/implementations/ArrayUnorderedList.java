package dataStructures.implementations;

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

        if (this.count == this.list.length) {
            this.expandCapacity();
        }

        // Move os elementos para a direita (ter espaço na posição 0 para o novo elemento)
        for (int i = this.count; i > 0; i--){
            this.list[i] = this.list[i - 1];
        }

        this.list[0] = element;
        count++;
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

        if (this.count == this.list.length) {
            this.expandCapacity();
        }

        this.list[this.count] = element;
        count++;
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
    public void addAfter(T element, T target) {
        if (element == null || target == null) {
            throw new IllegalArgumentException("Element or target cannot be null");
        }

        int targetIndex = -1;

        for(int i = 0; i < this.count; i++){
            if(this.list[i].equals(target)){
                targetIndex = i;
                break;
            }
        }

        if (targetIndex == -1) {
            throw new IllegalArgumentException("Target element not found");
        }

        if (this.count == this.list.length) {
            this.expandCapacity();
        }

        for(int i = this.count; i > targetIndex + 1; i--){
            this.list[i] = this.list[i - 1];
        }

        this.list[targetIndex + 1] = element;
        count++;
    }
}