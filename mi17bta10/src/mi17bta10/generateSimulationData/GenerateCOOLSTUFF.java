package mi17bta10.generateSimulationData;

import mi17bta10.simulation.*;

import java.awt.*;
import java.util.Random;

/**
 * This class generates bodies that can be used for abstract simulation.
 */
public class GenerateCOOLSTUFF extends GenerateSuperClass {

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
     * @param numberOfCelestialBodies            How many celestial bodies shall be created in this simulation
     * @param simulationSpeedInSecondsCalculated How many seconds are calculated in a calc-move cycle
     */
    protected GenerateCOOLSTUFF(int numberOfCelestialBodies, int simulationSpeedInSecondsCalculated) {
        this.numberOfCelestialBodies = numberOfCelestialBodies;
        this.simulationSpeedInSecondsCalculated = simulationSpeedInSecondsCalculated;
    }

    /**
     * Generates octree with bodies used for abstract simulation.
     *
     * @param edgeLengthOfSimulatedArea The length of a side in the octree
     * @param leftDownCornerOffset      Coordinates of bottom-left corner
     * @param numberOfCelestialBodies   How many celestial bodies shall be created in this simulation
     * @return Octree with bodies used for abstract simulation
     */
    @Override
    protected Octree generateAsTree(double edgeLengthOfSimulatedArea, Vector3 leftDownCornerOffset, int numberOfCelestialBodies) {
        Cube cube = new Cube(new Vector3(-edgeLengthOfSimulatedArea / 2 + leftDownCornerOffset.getX(),
                -edgeLengthOfSimulatedArea / 2 + leftDownCornerOffset.getY(),
                -edgeLengthOfSimulatedArea / 2 + leftDownCornerOffset.getZ()), edgeLengthOfSimulatedArea);
        Octree tree = new Octree(cube);

        for (CelestialBody body : generateAsArray(cube, leftDownCornerOffset, numberOfCelestialBodies)) {
            tree.addToOctree(body);
        }

        return tree;
    }

    /**
     * Generates array with bodies used for abstract simulation.
     *
     * @param cube                    Defines the location and size of the octree-node
     * @param offset                  Coordinates of bottom-left corner
     * @param numberOfCelestialBodies How many celestial bodies shall be created in this simulation
     * @return Array with bodies used for abstract simulation
     */
    @Override
    protected CelestialBody[] generateAsArray(Cube cube, Vector3 offset, int numberOfCelestialBodies) {

        CelestialBody[] bodies = new CelestialBody[numberOfCelestialBodies + 1];

        CelestialBody sun = new CelestialBody("Sol", 2e31, 696340e3, cube.getCenter(), new Vector3(0, 0, 0), StdDraw.YELLOW);
        bodies[0] = sun;

        int n = numberOfCelestialBodies;
        int index = 1;
        while (n > 0) {
            CelestialBody body = createCoolStuffBody(new Random(), cube.getEdgeLength(), cube.getCenter());
            if (cube.isInsideCube(body.getPosition())) {
                bodies[index] = body;
                index++;
                n--;
            }
        }
        return bodies;
    }

    /**
     * Creates a celestial body used for the abstract simulation.
     *
     * @param rand                      Used for random values
     * @param edgeLengthOfSimulatedArea The length of a side in the octree
     * @param offset                    Coordinates of bottom-left corner
     * @return Celestial body used for the abstract simulation
     */
    private CelestialBody createCoolStuffBody(Random rand, double edgeLengthOfSimulatedArea, Vector3 offset) {
        double mass, radius, posX, posY, posZ, velX, velY, velZ = 0;
        double minDistanceFromSun = 2.2 * Constants.AU;
        double maxDistanceFromSun = 3.2 * Constants.AU;
        double randomAngle = Math.random();
        randomAngle *= 2 * Constants.PI;
        if (randomAngle >= Constants.PI / 2 && randomAngle < Constants.PI) {
            randomAngle -= Constants.PI / 2;
        } else if (randomAngle >= Constants.PI * 3 / 2 && randomAngle < 2 * Constants.PI) {
            randomAngle -= Constants.PI / 2;
        }

        radius = Math.random() * 10e3 + 50;
        mass = 4 / 3 * Math.pow(radius, 3) * 2000;
        double constantForVel = 29.8e3;

        double distance = Math.random() * (maxDistanceFromSun - minDistanceFromSun) + minDistanceFromSun;
        posZ = rand.nextGaussian() * 10000;
        if (randomAngle >= 0 && randomAngle < Constants.PI / 2) {
            posX = Math.cos(randomAngle) * distance;
            posY = Math.sin(randomAngle) * distance;
            velX = Math.cos(randomAngle) * constantForVel * (-1);
            velY = Math.sin(randomAngle) * constantForVel;
        } else if (randomAngle >= Constants.PI / 2 && randomAngle < Constants.PI) {
            posX = Math.cos(Constants.PI - randomAngle) * distance * (-1);
            posY = Math.sin(Constants.PI - randomAngle) * distance;
            velX = Math.cos(Constants.PI - randomAngle) * constantForVel * (-1);
            velY = Math.sin(Constants.PI - randomAngle) * constantForVel * (-1);
        } else if (randomAngle >= Constants.PI && randomAngle < 3 * Constants.PI / 2) {
            posX = Math.cos(Constants.PI + randomAngle) * distance * (-1);
            posY = Math.sin(Constants.PI + randomAngle) * distance * (-1);
            velX = Math.cos(Constants.PI + randomAngle) * constantForVel;
            velY = Math.sin(Constants.PI + randomAngle) * constantForVel * (-1);
        } else {
            posX = Math.cos(2 * Constants.PI - randomAngle) * distance;
            posY = Math.sin(2 * Constants.PI - randomAngle) * distance * (-1);
            velX = Math.cos(2 * Constants.PI - randomAngle) * constantForVel;
            velY = Math.sin(2 * Constants.PI - randomAngle) * constantForVel;
        }
        posX += offset.getX();
        posY += offset.getY();

        String name = String.valueOf(rand.nextInt() * this.numberOfCelestialBodies * 100);

        int r = (int) (Math.random() * 256);
        int g = (int) (Math.random() * 256);
        int b = (int) (Math.random() * 256);
        Color randomColor = new Color(r, g, b);

        return new CelestialBody(name, mass, radius, new Vector3(posX, posY, posZ), new Vector3(velX, velY, velZ), randomColor, this.simulationSpeedInSecondsCalculated);
    }
}
