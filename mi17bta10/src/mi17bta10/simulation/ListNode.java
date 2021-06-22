package mi17bta10.simulation;

/**
 * Class that represents a node in the list of bodies
 */
public class ListNode {

    /**
     * Celestial body that this node contains
     */
    private CelestialBody body;

    /**
     * Reference to the next node
     */
    private ListNode next;

    /**
     * Constructor - sets this body to body and next to null.
     *
     * @param body Celestial body
     */
    ListNode(CelestialBody body) {
        this.body = body;
        this.next = null;
    }

    /**
     * Returns this celestial body.
     *
     * @return This celestial body
     */
    public CelestialBody getBody() {
        return this.body;
    }

    /**
     * Returns the next mi17bta10.simulation.ListNode.
     *
     * @return Next node
     */
    public ListNode getNext() {
        return this.next;
    }

    /**
     * Set-Method of the next mi17bta10.simulation.ListNode of this node.
     *
     * @param next List node that should be set as next of this node
     */
    public void setNext(ListNode next) {
        this.next = next;
    }
}
