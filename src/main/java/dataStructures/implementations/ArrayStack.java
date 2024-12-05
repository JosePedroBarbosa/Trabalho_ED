package dataStructures.implementations;

import dataStructures.ADTS.StackADT;
import dataStructures.exceptions.EmptyCollectionException;

/**
 * This class represents a ArrayStack that follows the Last-In-First-Out (LIFO) principle, where elements are
 * added and removed from the top of the stack.
 *
 * @param <T> the type of elements held in this stack
 */
public class ArrayStack<T> implements StackADT<T> {
    /**
     * constant to represent the default capacity of the array
     */
    private final int DEFAULT_CAPACITY = 100;

    /**
     * int that represents both the number of elements and the next
     * available position in the array
     */
    private int top;

    /**
     * array of generic elements to represent the stack
     */
    private T[] stack;

    /**
     * Creates an empty stack using the default capacity.
     */
    public ArrayStack() {
        top = 0;
        stack = (T[]) (new Object[DEFAULT_CAPACITY]);
    }

    /**
     * Creates an empty stack using the specified capacity.
     * @param initialCapacity represents the specified capacity
     */
    public ArrayStack(int initialCapacity) {
        top = 0;
        stack = (T[]) (new Object[initialCapacity]);
    }

    /**
     * Adds the specified element to the top of this stack,
     * expanding the capacity of the stack array if necessary.
     * @param element generic element to be pushed onto stack
     */
    public void push (T element) {
        if (size() == stack.length){
            expandCapacity();
        }

        stack[top] = element;
        top++;
    }

    /**
     * Removes the element at the top of this stack and
     * returns a reference to it.
     * Throws an EmptyCollectionException if the stack is empty.
     * @return T element removed from top of stack
     * @throws EmptyCollectionException if a pop
     * is attempted on empty stack
     */
    public T pop() throws EmptyCollectionException {
        if (isEmpty()){
            throw new EmptyCollectionException("Stack");
        }

        top--;
        T result = stack[top];
        stack[top] = null;

        return result;
    }

    /**
     * Returns a reference to the element at the top of this stack.
     * The element is not removed from the stack.
     * Throws an EmptyCollectionException if the stack is empty.
     * @return T element on top of stack
     * @throws EmptyCollectionException if a
     * peek is attempted on empty stack
     */
    public T peek() throws EmptyCollectionException {
        if (isEmpty()) {
            throw new EmptyCollectionException("Stack");
        }

        return stack[top - 1];
    }

    /**
     * Expands the stack capacity (with a factor of 2)
     */
    public void expandCapacity(){
        T[] newStack = (T[]) (new Object[stack.length * 2]);

        for (int i = 0; i < stack.length; i++){
            newStack[i] = stack[i];
        }

        stack = newStack;
    }

    /**  Returns true if this stack contains no elements.
     *   @return boolean whether this stack is empty or not
     */
    @Override
    public boolean isEmpty() {
        return (top == 0);
    }

    /**  Returns the number of elements in this stack.
     *   @return int number of elements in this stack
     */
    @Override
    public int size(){
        return top;
    }

    /**  Returns a string representation of this stack.
     *   @return String representation of this stack
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "ArrayStack[]";
        }

        String result = "ArrayStack[";

        for (int i = 0; i < top; i++) {
            result += stack[i].toString();

            if (i < top - 1) {
                result += ", ";
            }
        }

        result += "]";
        return result;
    }
}