package mi17bta10.tests;

import mi17bta10.simulation.StdDraw;

/**
 * This class tests most relevant features and moves automatically through all tests
 */
public class AutomaticTests {

    public static void main(String[] args) {
        StdDraw.setVisible(false);

        CubeTest cubeTest = new CubeTest();
        OctreeTest octreeTestInstance = new OctreeTest();
        SpeedTests speedTests = new SpeedTests();

        cubeTest.startTests();
        octreeTestInstance.startTests();

        cubeTest.printResults();
        octreeTestInstance.printResults();

        speedTests.startTests();
        speedTests.printResults();

        System.exit(0);
    }
}
