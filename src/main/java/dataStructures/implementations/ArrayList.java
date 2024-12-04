package dataStructures.implementations;

import dataStructures.exceptions.EmptyCollectionException;
import dataStructures.ADTS.ListADT;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;

public class ArrayList<T> implements ListADT<T>, Iterable<T> {
    private final static int DEFAULT_CAPACITY = 100;
    protected T[] list;
    protected int size, modCount;

    public ArrayList() {
        this.list = (T[]) (new Object[DEFAULT_CAPACITY]);
        this.size = 0;
        this.modCount = 0;
    }

    public ArrayList(int initialCapacity) {
        this.list = (T[]) (new Object[initialCapacity]);
        this.size = 0;
        this.modCount = 0;
    }

    protected void expandCapacity() {
        T[] newList = (T[]) (new Object[list.length * 2]);
        for (int i = 0; i < list.length; i++) {
            newList[i] = list[i];
        }
        list = newList;
    }

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

    @Override
    public T first() throws EmptyCollectionException {
        if (isEmpty()){
            throw new EmptyCollectionException();
        }
        return list[0];
    }

    @Override
    public T last() throws EmptyCollectionException {
        if (isEmpty()){
            throw new EmptyCollectionException();
        }
        return list[size() - 1];
    }

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

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new BasicIterator(){};
    }

    @Override
    public String toString() {
        String result = "";
        if (!isEmpty()) {
            for (int i = 0; i < size(); i++) {
                result += list[i] + " ";
                result += "--------------- \n";
            }
        }
        return result;
    }

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

    public T getByIndex(int index){
        return this.list[index];
    }
}