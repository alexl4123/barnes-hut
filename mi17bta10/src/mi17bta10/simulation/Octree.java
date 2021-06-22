package mi17bta10.simulation;

/**
 * mi17bta10.simulation.Octree class - important for barnes hut algorithm
 */
public class Octree {

    /**
     * mi17bta10.simulation.Cube defines the location and size of the octree-node
     */
    private final Cube cube;

    /**
     * Represents the location of the center of mass as a mi17bta10.simulation.Vector3 (with children nodes)
     */
    private Vector3 centerOfMass;

    /**
     * Represents the middle point of the octree
     */
    private Vector3 center;

    /**
     * The sum of the mass from this node and all children nodes
     */
    private double totalMass;

    /**
     * If this node is a leaf, value is not null
     */
    private CelestialBody value;

    /**
     * The children nodes of this node -> array is initialized after constructor,
     * but not the children nodes (they are null and only get initialized if a node is added in a specific child node)
     */
    private Octree[] children;

    /**
     * Boolean if octree is a child or parent node - true after init
     */
    private boolean isLeaf;

    /**
     * The depth of this node in the octree, root is 0
     */
    private int depth;

    /**
     * mi17bta10.simulation.List of all children bodies
     */
    private List listOfBodies;

    /**
     * Constructor
     *
     * @param cube The space that the octree node contains
     */
    public Octree(Cube cube) {
        this.isLeaf = true;
        this.cube = cube;
        this.centerOfMass = new Vector3(0, 0, 0);
        this.totalMass = 0;
        this.center = cube.getCenter();
        this.listOfBodies = new List();
    }

    /**
     * Adds a new body to the octree and the list.
     * Returns true if body is successfully added to the tree and false otherwise.
     *
     * @param body Celestial Body that shall be added
     * @return True if body is successfully added and octree and list are changed
     */
    public boolean addToOctree(CelestialBody body) {
        this.depth = 0;
        if (!cube.isInsideCube(body.getPosition())) {
            return false;
        }

        if (this.value == null && isLeaf) {
            boolean addedToOctreeAsLeaf = addToOctreeAsLeaf(body, this.depth);
            if (addedToOctreeAsLeaf) {
                this.listOfBodies.addToList(body);
            }

            return addedToOctreeAsLeaf;
        }

        boolean bodyAdded = addToOctree(body, this.depth);
        if (bodyAdded) {
            this.listOfBodies.addToList(body);
        }
        return bodyAdded;
    }

    private boolean addToOctreeAsLeaf(CelestialBody body, int depth) {
        //Add first node
        this.value = body;
        this.centerOfMass = body.getPosition();
        this.totalMass = body.getMass();
        return true;
    }

    private boolean addToOctree(CelestialBody body, int depth) {
        this.depth = depth;

        if (this.value == null && isLeaf) {
            //Add first node
            this.value = body;
            this.centerOfMass = body.getPosition();
            this.totalMass = body.getMass();
            return true;
        }


        //Case when leaf is split up into eight leaves
        if (this.value != null && !this.value.getName().equals(body.getName()) && this.isLeaf) {
            return addSplitLeafIntoChildren(body);
        } else if (this.value != null && this.value.getName().equals(body.getName())) {
            return false;
        }

        return addBodyToAChild(body);
    }

    private boolean addBodyToAChild(CelestialBody body) {
        boolean bodyAdded = false;

        int index = calcCubeIndexFromPositionEfficient(body.getPosition());
        if (this.children[index] == null) {
            this.children[index] = new Octree(calcChildrenCube(index));

            this.centerOfMass = this.centerOfMass.times(this.getTotalMass());

            bodyAdded = this.children[index].addToOctreeAsLeaf(body, depth + 1);

            this.totalMass += this.children[index].getTotalMass();
            this.centerOfMass = this.centerOfMass.plus(children[index].getCenterOfMass().times(this.children[index].getTotalMass()));
        } else {
            this.totalMass -= this.children[index].getTotalMass();
            this.centerOfMass = this.centerOfMass.times(this.totalMass).minus(this.children[index].getCenterOfMass().times(this.children[index].getTotalMass()));

            bodyAdded = this.children[index].addToOctree(body, depth + 1);

            this.totalMass += this.children[index].getTotalMass();
            this.centerOfMass = this.centerOfMass.plus(children[index].getCenterOfMass().times(this.children[index].getTotalMass()));
        }

        this.centerOfMass = this.centerOfMass.times((1 / this.totalMass));

        return bodyAdded;
    }

    private boolean addSplitLeafIntoChildren(CelestialBody body) {
        //Init children
        boolean valueAdded = false;
        boolean bodyAdded = false;

        if (this.depth > 10) {
            CelestialBody body2 = new CelestialBody(this.value.getName(), this.value.getMass() + body.getMass()
                    , this.value.getMass() + body.getMass(), this.value.getPosition(),
                    this.value.getVelocity().plus(body.getVelocity()), this.value.getColor());

            this.totalMass += body.getMass();
            //Center of mass stays the same

            this.value = body2;
            return true;
        }

        this.children = new Octree[8];

        int pos1 = calcCubeIndexFromPositionEfficient(this.value.getPosition());
        int pos2 = calcCubeIndexFromPositionEfficient(body.getPosition());

        this.children[pos1] = new Octree(calcChildrenCube(pos1));
        this.children[pos2] = new Octree(calcChildrenCube(pos2));

        if (pos1 != pos2) {
            if (pos1 >= 0) {
                valueAdded = children[pos1].addToOctreeAsLeaf(this.value, this.depth + 1);
            }
            if (pos2 >= 0) {
                this.centerOfMass = this.centerOfMass.times(this.getTotalMass());

                bodyAdded = this.children[pos2].addToOctreeAsLeaf(body, this.depth + 1);

                this.totalMass += this.children[pos2].getTotalMass();
                this.centerOfMass = this.centerOfMass.plus(this.children[pos2].getCenterOfMass().times(this.children[pos2].getTotalMass()));
            }
        } else {
            valueAdded = this.children[pos1].addToOctreeAsLeaf(this.value, this.depth + 1);
            bodyAdded = this.children[pos2].addToOctree(body, this.depth + 1);
            this.totalMass = this.children[pos2].getTotalMass();
            this.centerOfMass = this.children[pos2].getCenterOfMass().times(this.children[pos2].getTotalMass());
        }

        this.value = null;
        this.isLeaf = false;
        this.centerOfMass = this.centerOfMass.times((1 / this.totalMass));

        return valueAdded && bodyAdded;
    }

    public boolean hasChildren() {
        return this.totalMass > 0 ? true : false;
    }

    private int calcCubeIndexFromPositionEfficient(Vector3 position) {
        double x = position.getX();
        double y = position.getY();
        double z = position.getZ();

        double cx = this.center.getX();
        double cy = this.center.getY();
        double cz = this.center.getZ();

        if (x >= cx) {
            if (y >= cy) {
                if (z >= cz) {
                    return 7;
                } else {
                    return 3;
                }
            } else {
                if (z >= cz) {
                    return 5;
                } else {
                    return 1;
                }
            }
        } else {
            if (y >= cy) {
                if (z >= cz) {
                    return 6;
                } else {
                    return 2;
                }
            } else {
                if (z >= cz) {
                    return 4;
                } else {
                    return 0;
                }
            }
        }
    }

    private Cube calcChildrenCube(int index) {
        Vector3 leftDownCorner = this.cube.getLeftDownCorner().clone();
        double edgeLength = this.cube.getEdgeLength() / 2;

        switch (index) {
            case 0:
                //Nothing
                break;
            case 1:
                leftDownCorner = leftDownCorner.plus(edgeLength, 0, 0);
                break;
            case 2:
                leftDownCorner = leftDownCorner.plus(0, edgeLength, 0);
                break;
            case 3:
                leftDownCorner = leftDownCorner.plus(edgeLength, edgeLength, 0);
                break;
            case 4:
                leftDownCorner = leftDownCorner.plus(0, 0, edgeLength);
                break;
            case 5:
                leftDownCorner = leftDownCorner.plus(edgeLength, 0, edgeLength);
                break;
            case 6:
                leftDownCorner = leftDownCorner.plus(0, edgeLength, edgeLength);
                break;
            case 7:
                leftDownCorner = leftDownCorner.plus(edgeLength, edgeLength, edgeLength);
                break;
            default:
                return null;
        }
        return new Cube(leftDownCorner, edgeLength);
    }

    /**
     * Calculates force exerted on all objects in the tree according to Barnes-Hut Algorithm.
     *
     * @param root      Octree with celestial bodies for which the force will be calculated
     * @param newOctree New octree, used for updating positions of the celestial bodies
     */
    public void calculateForcesOnAllBodies(Octree root, Octree newOctree) {
        ListNode currentNode = this.listOfBodies.getHead();
        CelestialBody currentBody;
        //for all bodies
        while (currentNode != null) {
            currentBody = currentNode.getBody();
            currentBody.setForceOnThisBody(new Vector3(0, 0, 0));
            //calculate the force that all other bodies exert on this body
            currentBody.calculateAllForcesOnThisBodyBeginningWithNode(root);
            //move this body according to calculated force
            currentBody.move(currentBody.getForceOnThisBody());
            //update the position of the current body by adding it to the new tree
            newOctree.addToOctree(currentBody);
            currentNode = currentNode.getNext();
        }
    }

    /**
     * Gives a graphical representation of all celestial bodies in the octree.
     *
     * @param viewConstant A parameter that specifies how big a celestial body shall be drawn
     */
    public void displayOctree(double viewConstant) {
        if (this.value != null) {
            double leftDownX = this.cube.getLeftDownCorner().getX();
            double leftDownY = this.cube.getLeftDownCorner().getY();
            double edgeLength = this.cube.getEdgeLength();
            StdDraw.setPenColor(this.value.getColor());
            StdDraw.line(leftDownX, leftDownY, leftDownX + edgeLength, leftDownY);
            StdDraw.line(leftDownX, leftDownY, leftDownX, leftDownY + edgeLength);
            StdDraw.line(leftDownX + edgeLength, leftDownY, leftDownX + edgeLength, leftDownY + edgeLength);
            StdDraw.line(leftDownX, leftDownY + edgeLength, leftDownX + edgeLength, leftDownY + edgeLength);
            this.value.draw(viewConstant);
        } else if (this.children != null) {
            //recurcive for the children
            for (int i = 0; i < this.children.length; i++) {
                if (this.children[i] != null && hasChildren()) {
                    this.children[i].displayOctree(viewConstant);
                }
            }
        }
    }

    /**
     * Prints the list of celestial bodies of this tree.
     */
    public void print() {
        this.listOfBodies.printList();
    }

    /**
     * Returns the total mass of all celestial bodies.
     *
     * @return Total mass of all celestial bodies
     */
    public double getTotalMass() {
        return this.totalMass;
    }

    /**
     * Returns a vector that represents the center of mass of the celestial bodies.
     *
     * @return Position of the center of mass of the celestial bodies
     */
    public Vector3 getCenterOfMass() {
        return this.centerOfMass;
    }

    /**
     * Returns an mi17bta10.simulation.Octree-Array with all children of this node
     *
     * @return Octree-array with all children of this node
     */
    public Octree[] getChildren() {
        return this.children;
    }

    /**
     * Returns the celestial body of this node.
     *
     * @return Celestial Body of this node
     */
    public CelestialBody getValue() {
        return this.value;
    }

    /**
     * Returns the cube of this node (location and size of the node).
     *
     * @return Cube of this node (location and size of the node)
     */
    public Cube getCube() {
        return this.cube;
    }

    /**
     * Returns the list of celestial bodies of this node
     *
     * @return List of celestial bodies of this node
     */
    public List getListOfBodies() {
        return this.listOfBodies;
    }
}