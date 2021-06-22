package mi17bta10.tests;

import mi17bta10.generateSimulationData.GenerateSimulationData;
import mi17bta10.generateSimulationData.SimulationType;
import mi17bta10.simulation.*;

import java.util.Random;

/**
 * This class is for testing the octree and barnes hut algorithm on speed
 */
public class SpeedTests implements TestInterface {
    private long createTreesTime;
    private long calcForceTimeWithSmallSystem, calcForceTimeWithBigSystem, calcForceWithNPow2AlgorithmTime;
    private long distanceNormalTime;
    private long getDistanceWithoutPowTime;


    public long measureCreateTrees(int numberOfMeasurements) {

        double cubeLength = 10 * Constants.AU;
        GenerateSimulationData genData = new GenerateSimulationData(10000, SimulationType.CLUSTER, cubeLength, 1);
        long times[] = new long[numberOfMeasurements];
        Octree octrees;
        long startTime, endTime;
        for (int i = 0; i < numberOfMeasurements; i++) {
            octrees = new Octree(new Cube(new Vector3(-cubeLength / 2, -cubeLength / 2, -cubeLength / 2), cubeLength));
            CelestialBody[] bodies = genData.generateAsArray();

            startTime = System.nanoTime();
            for (CelestialBody body : bodies) {
                octrees.addToOctree(body);
            }
            endTime = System.nanoTime();
            times[i] = endTime - startTime;
        }

        long sum = 0;
        for (int i = 0; i < numberOfMeasurements; i++) {
            sum += times[i];
        }
        return sum / numberOfMeasurements;
    }

    public long measureForceCalcTime(int numberOfMeasurements, double cubeLength) {
        GenerateSimulationData genData = new GenerateSimulationData(1000, SimulationType.CLUSTER, cubeLength, 1);
        long times[] = new long[numberOfMeasurements];
        long startTime, endTime;
        Cube cube = new Cube(new Vector3(-cubeLength / 2, -cubeLength / 2, -cubeLength / 2), cubeLength);
        Octree newTree;
        Octree tree = genData.generateAsTree();
        for (int i = 0; i < numberOfMeasurements; i++) {

            startTime = System.nanoTime();

            newTree = new Octree(cube);
            tree.calculateForcesOnAllBodies(tree, newTree);
            tree = newTree;

            endTime = System.nanoTime();
            times[i] = endTime - startTime;
        }

        long sum = 0;
        for (int i = 0; i < numberOfMeasurements; i++) {
            sum += times[i];
        }
        return sum / numberOfMeasurements;
    }


    public long measureForceCalcTimeWithDifferentTrees(int numberOfMeasurements, double cubeLength) {

        long startTime, endTime;
        long times[] = new long[numberOfMeasurements];

        for (int i = 0; i < numberOfMeasurements; i++) {
            times[i] = measureForceCalcTime(100, cubeLength);
        }

        long sum = 0;
        for (int i = 0; i < numberOfMeasurements; i++) {
            sum += times[i];
        }
        return sum / numberOfMeasurements;
    }

    private void measureVector3DistanceCalc(int numberOfMeasurements) {

        long startTime, endTime, startTime2, endTime2;
        long timeNormal[] = new long[numberOfMeasurements];
        long timeWithoutPow[] = new long[numberOfMeasurements];
        Vector3[] arr1 = new Vector3[numberOfMeasurements];
        Vector3[] arr2 = new Vector3[numberOfMeasurements];

        Random rand = new Random();
        for (int i = 0; i < numberOfMeasurements; i++) {
            arr1[i] = new Vector3(rand.nextDouble() * 10e6, rand.nextDouble() * 10e6, rand.nextDouble() * 10e6);
            arr2[i] = new Vector3(rand.nextDouble() * 10e6, rand.nextDouble() * 10e6, rand.nextDouble() * 10e6);
        }

        for (int i = 0; i < numberOfMeasurements; i++) {
            startTime = System.nanoTime();
            arr1[i].distanceTo(arr2[i]);
            endTime = System.nanoTime();

            startTime2 = System.nanoTime();
            arr1[i].distanceToPow2(arr2[i]);
            endTime2 = System.nanoTime();

            timeNormal[i] = endTime - startTime;
            timeWithoutPow[i] = endTime2 - startTime2;
        }

        long sum = 0;
        long sum2 = 0;
        for (int i = 0; i < numberOfMeasurements; i++) {
            sum += timeNormal[i];
            sum2 += timeWithoutPow[i];
        }
        this.distanceNormalTime = sum;
        this.getDistanceWithoutPowTime = sum2;
    }


    @Override
    public void startTests() {
        double smallLength = Constants.AU * 10;
        this.createTreesTime = measureCreateTrees(500);
        this.calcForceTimeWithSmallSystem = measureForceCalcTimeWithDifferentTrees(10, smallLength);
        measureVector3DistanceCalc(10000);
    }

    @Override
    public void printResults() {
        System.out.println("The average time to create trees is in ms: " + this.createTreesTime / 10e6);
        System.out.println("The average time to calc the force in a small system (10*AU-1000 bodies) is in ms: " + this.calcForceTimeWithSmallSystem / 10e6);
        System.out.println("The sum time to calc distance to normal is: " + distanceNormalTime / 10e6);
        System.out.println("The sum time to calc distance without sqrt is: " + getDistanceWithoutPowTime / 10e6);
    }
}
