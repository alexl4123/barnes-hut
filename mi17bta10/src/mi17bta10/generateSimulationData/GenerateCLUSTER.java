package mi17bta10.generateSimulationData;

import mi17bta10.simulation.*;

import java.awt.*;
import java.util.Random;

/**
 * This class generates cluster that can be used for simulation.
 */
public class GenerateCLUSTER extends GenerateSuperClass {

    /**
     * How many celestial bodies shall be created in this simulation
     */
    private final int numberOfCelestialBodies;

    /**
     * A constant that specifies how many seconds are calculated in a calc-move cycle
     */
    private final int simulationSpeedInSecondsCalculated;

    /**
     * Constructor
     *
     * @param simulationSpeedInSecondsCalculated How many seconds are calculated in a calc-move cycle
     * @param numberOfCelestialBodies            How many celestial bodies shall be created in this simulation
     */
    protected GenerateCLUSTER(int simulationSpeedInSecondsCalculated, int numberOfCelestialBodies) {
        this.simulationSpeedInSecondsCalculated = simulationSpeedInSecondsCalculated;
        this.numberOfCelestialBodies = numberOfCelestialBodies;
    }

    /**
     * Generates octree with bodies that represent a star cluster.
     *
     * @param edgeLengthOfSimulatedArea The length of a side in the octree
     * @param leftDownCornerOffset      Coordinates of bottom-left corner
     * @param numberOfCelestialBodies   How many celestial bodies shall be created in this simulation
     * @return Octree with bodies that represent a star cluster
     */
    @Override
    protected Octree generateAsTree(double edgeLengthOfSimulatedArea, Vector3 leftDownCornerOffset, int numberOfCelestialBodies) {
        Cube cube = new Cube(new Vector3(
                (-edgeLengthOfSimulatedArea + leftDownCornerOffset.getX()) / 2,
                (-edgeLengthOfSimulatedArea + leftDownCornerOffset.getY()) / 2,
                (-edgeLengthOfSimulatedArea + leftDownCornerOffset.getZ()) / 2),
                edgeLengthOfSimulatedArea);
        Octree octree = new Octree(cube);

        CelestialBody[] bodies = generateAsArray(cube, new Vector3(0, 0, 0), numberOfCelestialBodies);
        for (CelestialBody body : bodies) {
            octree.addToOctree(body);
        }

        return octree;
    }

    /**
     * Generates an array with bodies that represent a star cluster.
     *
     * @param cube                    Defines the location and size of the octree-node
     * @param offset                  Coordinates of bottom-left corner
     * @param numberOfCelestialBodies How many celestial bodies shall be created in this simulation
     * @return Array with bodies that represent a star cluster
     */
    @Override
    protected CelestialBody[] generateAsArray(Cube cube, Vector3 offset, int numberOfCelestialBodies) {
        CelestialBody[] bodies = new CelestialBody[numberOfCelestialBodies + 1];
        Random rand = new Random();
        double massOfBlackHole = Constants.MASS_OF_SUN + Constants.MASS_OF_SUN * rand.nextInt(1000000) * rand.nextDouble();

        CelestialBody blackHole = new CelestialBody(
                "black_hole_" + numberOfCelestialBodies,
                massOfBlackHole,
                Constants.RADIUS_OF_SUN * 10,
                cube.getCenter(),
                new Vector3(0, 0, 0),
                Color.YELLOW,
                this.simulationSpeedInSecondsCalculated);

        bodies[0] = blackHole;

        int n = numberOfCelestialBodies;
        int index = 1;

        while (n > 0) {
            CelestialBody body = createClusterBody(rand, cube.getEdgeLength(), cube.getCenter(), massOfBlackHole);
            if (body.getMass() > 0.06 * Constants.MASS_OF_SUN && cube.isInsideCube(body.getPosition())) {
                bodies[index] = body;
                n--;
                index++;
            }
        }
        return bodies;
    }

    /**
     * Creates a celestial body that belongs to a star cluster.
     *
     * @param rand                      Used for random values
     * @param edgeLengthOfSimulatedArea The length of a side in the octree
     * @param offset                    Coordinates of bottom-left corner
     * @param massOfBlackHole           Mass of black hole
     * @return Celestial body that belongs to a star cluster
     */
    private CelestialBody createClusterBody(Random rand, double edgeLengthOfSimulatedArea, Vector3 offset, double massOfBlackHole) {
        double mass, radius, posX, posY, posZ, velX = 0, velY = 0, velZ = 0;
        //Cubic root

        posX = (rand.nextGaussian() * edgeLengthOfSimulatedArea / 8) + offset.getX();
        posY = (rand.nextGaussian() * edgeLengthOfSimulatedArea / 8) + offset.getY();
        posZ = (rand.nextGaussian() * edgeLengthOfSimulatedArea / 12) + offset.getZ();

        double diffX = posX - offset.getX();
        double diffY = posY - offset.getY();

        double distanceXY = offset.distanceTo(new Vector3(posX, posY, offset.getZ()));

        mass = Math.abs(rand.nextGaussian() * Constants.MASS_OF_SUN * 3) + Constants.MASS_OF_SUN * 0.06;
        radius = Math.cbrt(mass);
        double angle = 0;
        double constantForVel = Math.sqrt(Constants.G * massOfBlackHole / distanceXY) * 0.6;
        if (diffX >= 0 && diffY >= 0) {
            angle = Math.asin(diffY / distanceXY);
        } else if (diffX < 0 && diffY >= 0) {
            angle = Math.asin((-1) * diffX / distanceXY) + Constants.PI / 2;
        } else if (diffX < 0 && diffY < 0) {
            angle = Math.asin((-1) * diffY / distanceXY) + Constants.PI;
        } else if (diffX >= 0 && diffY < 0) {
            angle = Math.asin(diffX / distanceXY) + 3 * Constants.PI / 2;
        }

        if (angle >= 0 && angle < Constants.PI / 2) {
            //posX = Math.cos(angle) * distanceXY;
            //posY = Math.sin(angle) * distanceXY;
            velX = Math.sin(angle) * constantForVel * (-1);
            velY = Math.cos(angle) * constantForVel;
        } else if (angle >= Constants.PI / 2 && angle < Constants.PI) {
            //posX = Math.cos(mi17bta10.simulation.Constants.PI - angle) * distanceXY * (-1);
            //posY = Math.sin(mi17bta10.simulation.Constants.PI - angle) * distanceXY;
            velX = Math.sin(Constants.PI - angle) * constantForVel * (-1);
            velY = Math.cos(Constants.PI - angle) * constantForVel * (-1);
        } else if (angle >= Constants.PI && angle < 3 * Constants.PI / 2) {
            //posX = Math.cos(mi17bta10.simulation.Constants.PI + angle) * distanceXY * (-1);
            //posY = Math.sin(mi17bta10.simulation.Constants.PI + angle) * distanceXY * (-1);
            velX = Math.sin(Constants.PI + angle) * constantForVel;
            velY = Math.cos(Constants.PI + angle) * constantForVel * (-1);
        } else {
            //posX = Math.cos(2 * mi17bta10.simulation.Constants.PI - angle) * distanceXY;
            //posY = Math.sin(2 * mi17bta10.simulation.Constants.PI - angle) * distanceXY * (-1);
            velX = Math.sin(2 * Constants.PI - angle) * constantForVel;
            velY = Math.cos(2 * Constants.PI - angle) * constantForVel;
        }

        velZ = rand.nextGaussian() * 10e4;

        String name = String.valueOf(rand.nextInt() * this.numberOfCelestialBodies * 100);

        int r = (int) (Math.random() * 256);
        int g = (int) (Math.random() * 256);
        int b = (int) (Math.random() * 256);
        Color randomColor = new Color(r, g, b);

        return new CelestialBody(name, mass, radius, new Vector3(posX, posY, posZ), new Vector3(velX, velY, velZ), randomColor, this.simulationSpeedInSecondsCalculated);
    }
}
