package dataStructures.implementations;

import dataStructures.ADTS.QueueADT;
import dataStructures.exceptions.EmptyCollectionException;

/**
 * A linked implementation of a Queue (FIFO - First In, First Out).
 *
 * @param <T> the type of elements in the queue
 */
public class LinkedQueue<T> implements QueueADT<T> {
    private int size;
    private LinearNode<T> front;
    private LinearNode<T> rear;

    /**
     * Default constructor. Initializes an empty queue.
     */
    public LinkedQueue() {
        this.size = 0;
        this.front = null;
        this.rear = null;
    }

    /**
     * Constructs a queue with a single element.
     *
     * @param element the initial element to be added
     */
    public LinkedQueue(T element) {
        LinearNode<T> newNode = new LinearNode<>(element);

        this.front = newNode;
        this.rear = newNode;
        this.size = 1;
    }

    /**
     * Adds an element to the rear of the queue.
     *
     * @param element the element to be added
     */
    @Override
    public void enqueue(T element) {
        LinearNode<T> newElement = new LinearNode<>(element);

        if (isEmpty()) {
            this.front = newElement;
            this.rear = newElement;
        } else {
            this.rear.setNext(newElement);
            this.rear = newElement;
        }

        this.size++;
    }

    /**
     * Removes and returns the element from the front of the queue.
     *
     * @return the element removed
     * @throws EmptyCollectionException if the queue is empty
     */
    @Override
    public T dequeue() throws EmptyCollectionException {
        if (this.isEmpty()) {
            throw new EmptyCollectionException("The Queue Is Empty");
        }

        LinearNode<T> removedNode = this.front;

        this.front = this.front.getNext();
        size--;

        if (isEmpty()) {
            this.rear = null;
        }

        return removedNode.getElement();
    }

    /**
     * Returns the element at the front of the queue without removing it.
     *
     * @return the element at the front
     * @throws EmptyCollectionException if the queue is empty
     */
    @Override
    public T first() throws EmptyCollectionException {
        if (this.isEmpty()) {
            throw new EmptyCollectionException("The Queue Is Empty");
        }

        return this.front.getElement();
    }

    /**
     * Checks if the queue is empty.
     *
     * @return true if the queue is empty, false otherwise.
     */
    @Override
    public boolean isEmpty() {
        return (this.size == 0);
    }

    /**
     * Returns the number of elements in the queue.
     *
     * @return the size of the queue
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns a string representation of the queue.
     *
     * @return the string representation of the queue
     */
    @Override
    public String toString() {
        String s = "";

        LinearNode<T> current = this.front;

        while (current != null) {
            s += current.getElement().toString() + '\n';

            if (current.getNext() != null) {
                s += "next: " + current.getNext().getElement().toString() + '\n';
            } else {
                s += "next: null \n";
            }
            s += "---\n";

            current = current.getNext();
        }

        return s;
    }
}