package mi17bta10.simulation;

/**
 * A cube is the space which one octree node contains
 */
public class Cube {

    /**
     * Coordinates of bottom-left corner
     */
    private final Vector3 leftDownCorner;

    /**
     * Coordinates of top-right corner
     */
    private final Vector3 upperRightCorner;

    /**
     * Coordinates of center
     */
    private final Vector3 center;

    /**
     * The length of a side of the cube
     */
    private final double edgeLength;

    /**
     * Constructor
     *
     * @param leftDownCorner Coordinates of bottom-left corner
     * @param edgeLength     The length of a side of the cube
     */
    public Cube(Vector3 leftDownCorner, double edgeLength) {
        this.leftDownCorner = leftDownCorner;
        if (edgeLength >= 0) {
            this.edgeLength = edgeLength;
        } else {
            this.edgeLength = (-1) * edgeLength;
        }
        this.upperRightCorner = leftDownCorner.plus(edgeLength, edgeLength, edgeLength);
        this.center = leftDownCorner.plus(edgeLength / 2, edgeLength / 2, edgeLength / 2);
    }

    /**
     * Returns true if the coordinates of the vector are inside cube and false otherwise.
     *
     * @param position Coordinates that shall be checked
     * @return True if the coordinates of the vector are inside cube
     */
    public boolean isInsideCube(Vector3 position) {

        if (position.getX() < this.leftDownCorner.getX() || position.getX() >= this.upperRightCorner.getX()) {
            return false;
        }
        if (position.getY() < this.leftDownCorner.getY() || position.getY() >= this.upperRightCorner.getY()) {
            return false;
        }
        if (position.getZ() < this.leftDownCorner.getZ() || position.getZ() >= this.upperRightCorner.getZ()) {
            return false;
        }
        return true;
    }

    /**
     * Returns coordinates of bottom-left corner.
     *
     * @return leftDownCorner
     */
    public Vector3 getLeftDownCorner() {
        return this.leftDownCorner;
    }

    /**
     * Returns position of center of cube.
     *
     * @return center
     */
    public Vector3 getCenter() {
        return this.center;
    }

    /**
     * Returns the length of a side of the cube.
     *
     * @return edgeLength
     */
    public double getEdgeLength() {
        return this.edgeLength;
    }
}
