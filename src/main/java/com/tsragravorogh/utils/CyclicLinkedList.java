package com.tsragravorogh.utils;

public class CyclicLinkedList<T> {
    public class ListItem {
        private T value;
        private transient ListItem next;

        public ListItem(T value, ListItem next) {
            this.value = value;
            this.next = next;
        }
    }

    protected ListItem head = null;
    protected ListItem tail = null;

    protected int size = 0;

    public void addFirst(T value) {
        head = new ListItem(value, head);
        if (tail == null) {
            tail = head;
        }
        size++;
    }

    public boolean isExist(T player) {
        ListItem item = head;
        for (int i = 0; i < size(); i++) {
            if (item.value == player) return true;
            item = item.next;
        }
        return false;
    }

    public void addLast(T value) {
        ListItem temp = new ListItem(value, null);
        if (tail == null) {
            head = tail = temp;
        } else {
            tail.next = temp;
            tail = temp;
            temp.next = head;
        }
        size++;
    }

    public T getNext(T player) {
        ListItem curr = head;
        while (true) {
            if (curr.value.equals(player)) {
                return curr.next.value;
            } else {
                curr = curr.next;
            }
        }
    }
    public void removePlayer(T player) {
        ListItem curr = head;
        while (curr.next != null) {
            if (curr.next.value.equals(player)) {
                if (curr == tail) {
                    head = head.next;
                    tail.next = head;
                    size--;
                    return;
                }
                if (curr.next == tail) {
                    curr.next = head;
                    tail = curr;
                    size--;
                    return;
                }
                curr.next = curr.next.next;
                size--;
                return;
            }
            curr = curr.next;
        }
    }

    public T playerBeforePlayer(T player) {
        ListItem curr = head;
        while (curr.next != null) {
            if(curr.next.value == player) {
                return curr.value;
            }
            curr = curr.next;
        }
        return curr.value;
    }

    public T get(int index) {
        ListItem curr = head;
        while (index != 0) {
            index--;
            curr = curr.next;
        }
        return curr.value;
    }

    public int size() {
        return size;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(head.value).append(" ");
        ListItem item = head;
        for (int i = 1; i < size(); i++) {
            item = item.next;
            sb.append(item.value);
        }
        return sb.toString();
    }
}
