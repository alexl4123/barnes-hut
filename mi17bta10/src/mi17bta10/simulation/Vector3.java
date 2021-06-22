package mi17bta10.simulation;

import java.awt.*;

/**
 * This class represents vectors in a 3D vector space.
 */
public class Vector3 {

    /**
     * x-coordinate
     */
    private double x;

    /**
     * y-coordinate
     */
    private double y;

    /**
     * z-coordinate
     */
    private double z;

    /**
     * Default constructor - sets everything explicitly to 0.
     */
    public Vector3() {
        this(0, 0, 0);
    }

    /**
     * Constructor if the coordinates are already known.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @param z z-coordinate
     */
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns the sum of this vector and vector 'v'.
     *
     * @param v Vector that shall be added
     * @return Sum of this vector and vector 'v'
     */
    public Vector3 plus(Vector3 v) {
        return this.plus(v.x, v.y, v.z);
    }

    /**
     * Returns the sum of this vector and the vector with coordinates x,y,z.
     *
     * @param x x-coodinate
     * @param y y-coodinate
     * @param z z-coodinate
     * @return Sum of this vector and the vector with coordinates x,y,z
     */
    public Vector3 plus(double x, double y, double z) {
        Vector3 result = new Vector3();
        result.x = this.x + x;
        result.y = this.y + y;
        result.z = this.z + z;
        return result;
    }

    /**
     * Returns the product of this vector and vector 'd'.
     *
     * @param d Vector that shall be multiplied
     * @return Product of this vector and vector 'd'
     */
    public Vector3 times(double d) {
        Vector3 result = new Vector3();
        result.x = this.x * d;
        result.y = this.y * d;
        result.z = this.z * d;
        return result;
    }

    /**
     * Returns the difference between this vector and the vector 'v'.
     * (Sum of this vector and -1*v).
     *
     * @param v Vector that shall be subtracted
     * @return Difference between this vector and the vector 'v'
     */
    public Vector3 minus(Vector3 v) {
        Vector3 result = new Vector3();
        result.x = this.x - v.x;
        result.y = this.y - v.y;
        result.z = this.z - v.z;
        return result;
    }

    /**
     * Returns the Euclidean distance of this vector to the specified vector 'v'.
     *
     * @param v Vector to which the distance shall be calculated
     * @return Euclidean distance of this vector to the specified vector 'v'
     */
    public double distanceTo(Vector3 v) {
        double dX = this.x - v.x;
        double dY = this.y - v.y;
        double dZ = this.z - v.z;
        return Math.sqrt(dX * dX + dY * dY + dZ * dZ);
    }

    /**
     * Returns the second power of the Euclidean distance of this vector to the specified vector 'v'.
     *
     * @param v Vector to which the distance shall be calculated
     * @return Second power of the Euclidean distance of this vector to the specified vector 'v'
     */
    public double distanceToPow2(Vector3 v) {
        double dX = this.x - v.x;
        double dY = this.y - v.y;
        double dZ = this.z - v.z;
        return (dX * dX + dY * dY + dZ * dZ);
    }

    /**
     * Returns the length (norm) of this vector.
     *
     * @return Length (norm) of this vector
     */
    public double distanceToCenter() {
        return distanceTo(new Vector3()); // distance to origin.
    }

    /**
     * Normalizes this vector: changes the length of this vector such that it becomes one.
     * The direction and orientation of the vector are not affected.
     */
    public void normalize() {
        double length = distanceToCenter();
        this.x /= length;
        this.y /= length;
        this.z /= length;
    }

    /**
     * Draws a filled circle with the center at (x,y) coordinates of this vector
     * in the existing mi17bta10.simulation.StdDraw canvas. The z-coordinate is not used.
     *
     * @param radius Radius of the circle
     * @param color  Color of the circle
     */
    public void drawAsDot(double radius, Color color) {
        StdDraw.setPenColor(color);
        StdDraw.filledCircle(this.x, this.y, radius);
    }

    /**
     * Returns the coordinates of this vector in brackets as a string in the form "[x,y,z]".
     * e.g. "[1.48E11,0.0,0.0]"
     *
     * @return Coordinates of this vector in brackets as string
     */
    public String toString() {
        return "[" + this.x + "," + this.y + "," + this.z + "]";
    }

    /**
     * Prints the coordinates of this vector in brackets to the console (without newline) in the form [x,y,z].
     * e.g. [1.48E11,0.0,0.0]
     */
    public void print() {
        System.out.print(this.toString());
    }

    /**
     * Returns a new vector wih the same coordinates (clone).
     *
     * @return New vector with same coordinates (clone)
     */
    public Vector3 clone() {
        return new Vector3(this.x, this.y, this.z);
    }

    /**
     * Returns true if this vector has same coordinates as the position-vector of the body o and false otherwise.
     *
     * @param o Body whose position shall be compared
     * @return True if this vector has same coordinates as the position-vector of body o
     */
    public boolean equals(Object o) {
        if (o instanceof Vector3)
            return equals((Vector3) o);
        return false;
    }

    /**
     * Returns true if the coordinates of the new vector are same with the coordinates of this vector and false otherwise.
     *
     * @param vector Vector whose position shall be compared
     * @return True if this vector has same coordinates as the parameter
     */
    public boolean equals(Vector3 vector) {
        if (vector.x != this.x || vector.y != this.y || vector.z != this.z)
            return false;
        return true;
    }

    /**
     * Returns x-coordinate.
     *
     * @return x-coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Returns y-Coordinate.
     *
     * @return y-coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Returns z-Coordinate.
     *
     * @return z-coordinate
     */
    public double getZ() {
        return z;
    }
}
