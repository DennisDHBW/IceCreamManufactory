package shared;

import container.ContainerType;
import lombok.Data;
import java.util.NoSuchElementException;

@Data
public class Stack<T> {
    private final Object[] elements;
    private int top;

    public Stack(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("capacity must be positive");
        this.elements = new Object[capacity];
        this.top = -1; // empty stack marker
    }

    public void push(T item) {
        if (isFull()) throw new IllegalStateException("stack overflow");
        elements[++top] = item;
    }

    public T pop() {
        if (isEmpty()) throw new NoSuchElementException("stack underflow");
        @SuppressWarnings("unchecked")
        T item = (T) elements[top];
        elements[top--] = null; // prevent memory leak
        return item;
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public boolean isFull() {
        return top == elements.length - 1;
    }

    public int size() {
        return top + 1;
    }
}
