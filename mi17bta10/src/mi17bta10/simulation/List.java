package mi17bta10.simulation;

/**
 * Class that represents a linked list
 * Nodes of the list are of type ListNode
 */
public class List {

    /**
     * First node of the list
     */
    private ListNode head;

    /**
     * Last node of the list
     */
    private ListNode tail;

    /**
     * Constructor - sets head and tail initially to null.
     */
    public List() {
        this.head = null;
        this.tail = null;
    }

    /**
     * Adds a celestial body to the list.
     *
     * @param body Celestial body that shall be added
     */
    public void addToList(CelestialBody body) {
        if (this.head == null) {
            this.head = this.tail = new ListNode(body);
        } else {
            this.tail.setNext(new ListNode(body));
            this.tail = tail.getNext();
        }
    }

    /**
     * Prints the name and position of all celestial bodies in the list.
     */
    public void printList() {
        ListNode current = head;
        while (current != null) {
            System.out.println(current.getBody().getName() + " " + current.getBody().getPosition());
            current = current.getNext();
        }
    }

    /**
     * Returns first node of the this list (head).
     *
     * @return Head of the list
     */
    public ListNode getHead() {
        return this.head;
    }

    /**
     * Returns last element of this list (tail).
     *
     * @return Tail of the list
     */
    public ListNode getTail() {
        return this.tail;
    }
}
