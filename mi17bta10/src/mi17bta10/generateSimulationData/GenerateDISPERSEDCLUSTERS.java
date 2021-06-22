package mi17bta10.generateSimulationData;

import mi17bta10.simulation.*;

import java.util.Random;

/**
 * This class generates dispersed clusters that can be used for simulation.
 */
public class GenerateDISPERSEDCLUSTERS extends GenerateSuperClass {

    /**
     * edgeLengthOfSimulatedArea - The length of a side in the octree
     */
    private final double edgeLengthOfSimulatedArea;

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
     * @param edgeLengthOfSimulatedArea          The length of a side in the octree
     * @param simulationSpeedInSecondsCalculated How many seconds are calculated in a calc-move cycle
     * @param numberOfCelestialBodies            How many celestial bodies shall be created in this simulation
     */
    protected GenerateDISPERSEDCLUSTERS(double edgeLengthOfSimulatedArea, int simulationSpeedInSecondsCalculated, int numberOfCelestialBodies) {
        this.edgeLengthOfSimulatedArea = edgeLengthOfSimulatedArea;
        this.simulationSpeedInSecondsCalculated = simulationSpeedInSecondsCalculated;
        this.numberOfCelestialBodies = numberOfCelestialBodies;
    }

    /**
     * Generates octree with bodies that belong to dispersed clusters.
     *
     * @param edgeLengthOfSimulatedArea The length of a side in the octree
     * @param leftDownCornerOffset      Coordinates of bottom-left corner
     * @param numberOfCelestialBodies   How many celestial bodies shall be created in this simulation
     * @return Octree with bodies that belong to dispersed clusters
     */
    @Override
    protected Octree generateAsTree(double edgeLengthOfSimulatedArea, Vector3 leftDownCornerOffset, int numberOfCelestialBodies) {
        Cube cube = new Cube(new Vector3(-this.edgeLengthOfSimulatedArea / 2, -this.edgeLengthOfSimulatedArea / 2, -this.edgeLengthOfSimulatedArea / 2), this.edgeLengthOfSimulatedArea);
        Octree tree = new Octree(cube);

        for (CelestialBody body : generateAsArray(
                cube,
                cube.getCenter(),
                numberOfCelestialBodies)) {
            tree.addToOctree(body);
        }

        return tree;
    }

    /**
     * Generates array with bodies that belong to dispersed clusters.
     *
     * @param cube                    Defines the location and size of the octree-node
     * @param offset                  Coordinates of bottom-left corner
     * @param numberOfCelestialBodies How many celestial bodies shall be created in this simulation
     * @return Array with bodies that belong to dispersed clusters
     */
    @Override
    protected CelestialBody[] generateAsArray(Cube cube, Vector3 offset, int numberOfCelestialBodies) {

        Random rand = new Random();
        int starsNotInACluster = numberOfCelestialBodies;
        CelestialBody[] bodies;
        mi17bta10.simulation.List bodyList = new mi17bta10.simulation.List();

        int numberOfClusters = rand.nextInt(numberOfCelestialBodies / 2);
        GenerateCLUSTER generateCLUSTER = new GenerateCLUSTER(this.simulationSpeedInSecondsCalculated, this.numberOfCelestialBodies);
        if (numberOfClusters > 0) {
            int bodyCount = 0;
            for (int i = 0; i < numberOfClusters; numberOfClusters++) {
                int clusterCount = rand.nextInt(numberOfCelestialBodies / 10);
                int starsInCluster = 0;
                if (clusterCount > starsNotInACluster) {
                    starsInCluster = starsNotInACluster;
                    starsNotInACluster = 0;
                } else {
                    starsInCluster = clusterCount;
                    starsNotInACluster -= clusterCount;
                }

                double newEdgeLengthOfSimulatedArea = rand.nextDouble() * edgeLengthOfSimulatedArea / 2;
                int helperX = rand.nextInt(10) < 5 ? 1 : -1;
                double offsetX = helperX * rand.nextDouble() * edgeLengthOfSimulatedArea / 2;
                int helperY = rand.nextInt(10) < 5 ? 1 : -1;
                double offsetY = helperY * rand.nextDouble() * edgeLengthOfSimulatedArea / 2;
                int helperZ = rand.nextInt(10) < 5 ? 1 : -1;
                double offsetZ = helperZ * rand.nextDouble() * edgeLengthOfSimulatedArea / 2;

                CelestialBody[] bodiesOfCluster = generateCLUSTER.generateAsArray(
                        new Cube(new Vector3(offsetX, offsetY, offsetZ), newEdgeLengthOfSimulatedArea),
                        new Cube(new Vector3(offsetX, offsetY, offsetZ), newEdgeLengthOfSimulatedArea).getCenter(),
                        starsInCluster);

                for (CelestialBody body : bodiesOfCluster) {
                    bodyList.addToList(body);
                    bodyCount++;
                }

                if (starsNotInACluster <= 0) {
                    break;
                }
            }

            bodies = new CelestialBody[bodyCount];
            ListNode curNode = bodyList.getHead();
            int index = 0;
            while (curNode != null && index < bodyCount) {
                bodies[index] = curNode.getBody();

                curNode = curNode.getNext();
                index++;
            }

        } else {
            bodies = generateCLUSTER.generateAsArray(cube, new Vector3(0, 0, 0), numberOfCelestialBodies);
        }

        return bodies;
    }
}
