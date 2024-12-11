package Common;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

public class Stack<E> implements Interfaces.Stack<E>, Serializable {
    // Implements a stack using a LinkedList.
    // The top of the stack is at the beginning of the LinkedList.

    // deque contains the elements in the stack.
    private final LinkedList<E> stack = new LinkedList<>();

    /**
     * Add the element at the top of this stack.
     */
    @Override
    public void push(E entry) {
        stack.push(entry);
    }

    /**
     * Remove and return this stack's top element.
     * Throw NoSuchElementException, if the stack is empty.
     */
    @Override
    public E pop() {
        return stack.pop();
    }

    /**
     * Return this stack's top element.
     * Throw NoSuchElementException, if the stack is empty.
     */
    @Override
    public E peek() {
        return stack.peek();
    }

    /**
     * Return true, if this stack is empty.
     */
    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }

    /**
     * Remove all elements from this stack.
     */
    @Override
    public void clear() {
        stack.clear();
    }

    /**
     * Return the number of elements in this stack.
     */
    @Override
    public int size() {
        return stack.size();
    }

    @Override
    public Iterator<E> iterator() {
        return stack.iterator();
    }
}
