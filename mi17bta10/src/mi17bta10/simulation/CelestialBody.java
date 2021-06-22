package mi17bta10.simulation;

import java.awt.*;

/**
 * Class for celestial bodies
 * Earth is for instance a celestial body
 */
public class CelestialBody {

    /**
     * Name of the celestial body
     */
    private final String name;

    /**
     * Mass of the celestial body
     */
    private final double mass;

    /**
     * Radius of the celestial body
     */
    private final double radius;

    /**
     * Position of the celestial body
     */
    private Vector3 position;

    /**
     * Velocity of the celestial body
     */
    private Vector3 currentMovement;

    /**
     * Force acting on the celestial body
     */
    private Vector3 forceOnThisBody;

    /**
     * Color of the celestial body
     */
    private final Color color;

    /**
     * A constant that specifies how many seconds are calculated in a calc-move cycle
     */
    private final int simulationSpeedInSecondsCalculated;

    /**
     * The constructor - all object variables are set.
     *
     * @param name            Name of celestial body
     * @param mass            Mass of celestial body
     * @param radius          Radius of celestial body
     * @param position        Position of celestial body
     * @param currentMovement Velocity of celestial body
     * @param color           Color of celestial body
     */
    public CelestialBody(String name, double mass, double radius, Vector3 position, Vector3 currentMovement, Color color) {
        this.name = name;
        this.mass = mass;
        this.radius = radius;
        this.position = position;
        this.currentMovement = currentMovement;
        this.forceOnThisBody = new Vector3(0, 0, 0);
        this.color = color;
        this.simulationSpeedInSecondsCalculated = 1;
    }

    /**
     * Constructor including calculationsPerSecond.
     *
     * @param name                  Name of celestial body
     * @param mass                  Mass of celestial body
     * @param radius                Radius of celestial body
     * @param position              Position of celestial body
     * @param currentMovement       Velocity of celestial body
     * @param color                 Color of celestial body
     * @param calculationsPerSecond How many seconds are calculated in a calc-move cycle
     */
    public CelestialBody(String name, double mass, double radius, Vector3 position, Vector3 currentMovement, Color color, int calculationsPerSecond) {
        this.name = name;
        this.mass = mass;
        this.radius = radius;
        this.position = position;
        this.currentMovement = currentMovement;
        this.forceOnThisBody = new Vector3(0, 0, 0);
        this.color = color;
        this.simulationSpeedInSecondsCalculated = calculationsPerSecond;
    }

    /**
     * Returns a vector that represents the gravitational force exerted by 'body' on this celestial body.
     *
     * @param body Celestial body that exerts force on this body
     * @return Gravitational force exerted by the body on this body
     */
    public Vector3 gravitationalForce(CelestialBody body) {
        return gravitationalForce(body.getPosition(), body.getMass());
    }

    /**
     * Returns a vector representing the gravitational force exerted by a group of bodies on this celestial body.
     * The force depends on the total mass of the bodies and the center of mass of the bodies in the group.
     *
     * @param centerOfMass Position of the center of mass of the group of bodies
     * @param totalMass    Total mass of all bodies in the group
     * @return Gravitational force exerted by the group of bodies on this body
     */
    public Vector3 gravitationalForce(Vector3 centerOfMass, double totalMass) {
        Vector3 direction = centerOfMass.minus(this.position);
        double rSquared = this.position.distanceToPow2(centerOfMass);
        direction.normalize();
        double force = Constants.G * this.mass * totalMass / (rSquared);
        return direction.times(force);
    }

    /**
     * Moves this celestial body to a new position, according to the specified force vector 'force' exerted
     * on it, and updates the current movement accordingly.
     * (Movement depends on the mass of this body, its current movement and the exerted force.)
     * F = m*a -> a = F / m
     * Calculates x seconds at the same time
     *
     * @param force Force acting on the celestial body
     */
    public void move(Vector3 force) {
        Vector3 acceleration = force.times(1 / this.mass).times(this.simulationSpeedInSecondsCalculated);
        Vector3 deltaMovement = this.currentMovement.plus(acceleration);

        Vector3 newPosition = (deltaMovement.times(this.simulationSpeedInSecondsCalculated)).plus(this.position);

        //Also update velocity
        this.currentMovement = deltaMovement;
        this.position = newPosition;
    }

    /**
     * Barnes hut algorithm for calculating all forces acting on this celestial body.
     * If the current node is an external node (and its body is not this body), calculate the force exerted
     * by the body in current node on this body, and add this amount to the net force of this celestial body.
     * <p>
     * Otherwise, calculate the ratio distance/widthOfCube
     * distance - Distance between the bodies (distance between the center of mass of this vertex the the position of the body)
     * widthOfCube - Diameter of the group
     * <p>
     * If the ratio is greater than theta -> the group is far enough
     * Treat this internal node as a single body
     * and calculate the force it exerts on this celestial body, and add this amount to the net force acting on this celestial body.
     * <p>
     * Otherwise call the method recursively for all of the children of this node.
     *
     * @param node A node from the octree containing the celestial body for which the total acting force shall be calculated
     */
    public void calculateAllForcesOnThisBodyBeginningWithNode(Octree node) {
        //we found external node that contains a body
        if (node != null && node.getValue() != null) {
            if (node.getValue().getName().equals(this.name)) {
                return; //the body does not exert force on itself

            } else {
                Vector3 force = this.gravitationalForce(node.getValue()); //calculate the force that the found body adds to this body
                this.forceOnThisBody = this.forceOnThisBody.plus(force); //add the force to the total force of this body
            }
        } else if (node != null) {
            //calculate ratio: distance/widthOfCube=ratio
            //distance-distance between the bodies (distance between the center of mass of this vertex the the position of the body)
            //widthOfCube-diameter of the group
            double distance = node.getCenterOfMass().distanceTo(this.position);
            double widthOfCube = node.getCube().getEdgeLength();
            double ratio = distance / widthOfCube;
            if (ratio > Constants.theta) { //the group is far enough; threat this node as a single body
                Vector3 force = this.gravitationalForce(node.getCenterOfMass(), node.getTotalMass());
                this.forceOnThisBody = this.forceOnThisBody.plus(force);
            } else if (node.getChildren() != null) {
                //the position of the group is not far enough
                //calculate for every child separately
                for (int i = 0; i < node.getChildren().length; i++) {
                    this.calculateAllForcesOnThisBodyBeginningWithNode(node.getChildren()[i]);
                }
            }
        }
    }

    /**
     * Draws the celestial body to the current mi17bta10.simulation.StdDraw canvas as a dot using 'color' of this body.
     * The radius of the dot is in relation to the radius of the celestial body.
     * (Use a conversion based on the logarithm as in 'mi17bta10.simulation.Simulation.java').
     *
     * @param viewConstant A parameter that specifies how big a celestial body shall be drawn
     */
    public void draw(double viewConstant) {
        double newRadius = viewConstant / 300 * Math.log10(this.radius);  // log10 because of large variation of radii.
        this.position.drawAsDot(newRadius, this.color);
    }

    /**
     * Returns a vector that represents the position of this celestial body.
     *
     * @return Position of this celestial body
     */
    public Vector3 getPosition() {
        return this.position;
    }

    /**
     * Returns a vector that represents the velocity of this celestial body.
     *
     * @return Velocity of this celestial body
     */
    public Vector3 getVelocity() {
        return this.currentMovement;
    }

    /**
     * Returns the mass of this celestial body.
     *
     * @return Mass of this celestial body
     */
    public double getMass() {
        return this.mass;
    }

    /**
     * Returns a vector that represents the force acting on this celestial body.
     *
     * @return Total force acting on this celestial body
     */
    public Vector3 getForceOnThisBody() {
        return this.forceOnThisBody;
    }

    /**
     * Set-Method for the force acting on this celestial body.
     *
     * @param v Force that shall be set as the force that acts on this celestial body
     */
    public void setForceOnThisBody(Vector3 v) {
        this.forceOnThisBody = v;
    }

    /**
     * Returns the name of this celestial body.
     *
     * @return Name of the celestial body
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the color of this celestial body.
     *
     * @return Color of this celestial body
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Returns a clone of this celestial body.
     *
     * @return Clone of this celestial body
     */
    public CelestialBody clone() {
        return new CelestialBody(
                this.name,
                this.mass,
                this.radius,
                this.position,
                this.currentMovement,
                this.color,
                this.simulationSpeedInSecondsCalculated
        );
    }
}
