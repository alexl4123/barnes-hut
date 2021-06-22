package mi17bta10.tests;

import mi17bta10.generateSimulationData.GenerateSimulationData;
import mi17bta10.generateSimulationData.SimulationType;
import mi17bta10.simulation.*;

/**
 * Test class for testing the octree
 */
public class OctreeTest implements TestInterface {

    private boolean isAddCorrect, isMassCalculationCorrect, isForceCalculationValid;
    private double averageCalculationError;
    private int numberOfMeasurementsForce, numberOfCelestialBodiesForce;

    public void startTests() {
        this.isAddCorrect = testAdd();
        this.isMassCalculationCorrect = testMassCalculation();
        this.numberOfCelestialBodiesForce = 1000;
        this.numberOfMeasurementsForce = 10;
        this.isForceCalculationValid = testForceCalculation(this.numberOfMeasurementsForce, this.numberOfCelestialBodiesForce);
    }

    public void printResults() {
        System.out.println("-" + this.isAddCorrect + ":: Does adding bodies to the tree work?");
        System.out.println("-" + this.isMassCalculationCorrect + ":: Is mass calculation correct?");
        System.out.println("-" + this.isForceCalculationValid + ":: Is force calculation correct?");
        System.out.println("-" + this.averageCalculationError + ":: The average error between the barnes hut and n^2 method for " + this.numberOfCelestialBodiesForce + " bodies and " + this.numberOfMeasurementsForce + " measurements.");
    }

    /**
     * Tests if the mass calculation of the octree is correct
     *
     * @return returns true if it is correct, false otherwise
     */
    private boolean testMassCalculation() {
        boolean right = true;

        Cube cube = new Cube(new Vector3(0, 0, 0), 1000);
        Octree tree = new Octree(cube);

        CelestialBody body1 = new CelestialBody("1", 250, 1, new Vector3(250, 250, 250), new Vector3(0, 0, 0), StdDraw.BLUE);
        CelestialBody body2 = new CelestialBody("2", 500, 1, new Vector3(750, 250, 250), new Vector3(0, 0, 0), StdDraw.BLUE);
        CelestialBody body3 = new CelestialBody("3", 750, 1, new Vector3(250, 750, 250), new Vector3(0, 0, 0), StdDraw.BLUE);
        CelestialBody body4 = new CelestialBody("4", 1000, 1, new Vector3(250, 250, 750), new Vector3(0, 0, 0), StdDraw.BLUE);

        if (tree.getTotalMass() != 0 || !tree.getCenterOfMass().equals(new Vector3(0, 0, 0))) {
            right = false;
        }

        tree.addToOctree(body1);

        if (tree.getTotalMass() != 250 || !tree.getCenterOfMass().equals(new Vector3(250, 250, 250))) {
            right = false;
        }

        tree.addToOctree(body2);

        if (tree.getTotalMass() != 750
                || tree.getCenterOfMass().getX() > 583.4
                || tree.getCenterOfMass().getX() < 583.3
                || tree.getCenterOfMass().getY() != 250
                || tree.getCenterOfMass().getZ() != 250) {
            right = false;
        }

        tree.addToOctree(body3);

        if (tree.getTotalMass() != 1500
                || tree.getCenterOfMass().getX() > 416.7
                || tree.getCenterOfMass().getX() < 416.6
                || tree.getCenterOfMass().getY() != 500
                || tree.getCenterOfMass().getZ() != 250) {
            right = false;
        }


        tree.addToOctree(body4);

        if (tree.getTotalMass() != 2500
                || tree.getCenterOfMass().getX() != 350
                || tree.getCenterOfMass().getY() != 400
                || tree.getCenterOfMass().getZ() != 450) {
            right = false;
        }

        return right;
    }

    /**
     * tests if adding CelestialBodies to the octree works
     *
     * @return returns true if it works, false otherwise
     */
    private boolean testAdd() {
        boolean right = true;
        //Space starts at 0,0,0 and ends at 1000,1000,1000
        Cube cube = new Cube(new Vector3(0, 0, 0), 1000);
        Octree tree = new Octree(cube);
        //Standard test bodies
        CelestialBody body1 = new CelestialBody("1", 1, 1, new Vector3(250, 250, 250), new Vector3(0, 0, 0), StdDraw.BLUE);
        CelestialBody body2 = new CelestialBody("2", 1, 1, new Vector3(750, 250, 250), new Vector3(0, 0, 0), StdDraw.BLUE);
        CelestialBody body3 = new CelestialBody("3", 1, 1, new Vector3(250, 750, 250), new Vector3(0, 0, 0), StdDraw.BLUE);
        CelestialBody body4 = new CelestialBody("4", 1, 1, new Vector3(750, 750, 250), new Vector3(0, 0, 0), StdDraw.BLUE);
        CelestialBody body5 = new CelestialBody("5", 1, 1, new Vector3(250, 250, 750), new Vector3(0, 0, 0), StdDraw.BLUE);
        CelestialBody body6 = new CelestialBody("6", 1, 1, new Vector3(750, 250, 750), new Vector3(0, 0, 0), StdDraw.BLUE);
        CelestialBody body7 = new CelestialBody("7", 1, 1, new Vector3(250, 750, 750), new Vector3(0, 0, 0), StdDraw.BLUE);
        CelestialBody body8 = new CelestialBody("8", 1, 1, new Vector3(750, 750, 750), new Vector3(0, 0, 0), StdDraw.BLUE);
        CelestialBody[] trivialBodyList = new CelestialBody[]{body1, body2, body3, body4, body5, body6, body7, body8};


        CelestialBody body9 = new CelestialBody("9", 1, 1, new Vector3(125, 125, 125), new Vector3(0, 0, 0), StdDraw.BLUE);
        if (tree.getValue() != null) {
            right = false;
        }

        //One body
        if (!tree.addToOctree(trivialBodyList[0]) || tree.getValue() == null) {
            right = false;
        }

        if (tree.getChildren() != null) {
            right = false;
        }

        //Two bodies
        right = simpleAddCheck(tree, trivialBodyList[1]) ? right : false;

        for (int i = 0; i < 2; i++) {
            if (tree.getChildren()[i] == null) {
                right = false;
            }
        }

        right = addCheckRightPos(tree, trivialBodyList, new int[]{0, 1}) ? right : false;

        for (int i = 2; i < 8; i++) {
            if (tree.getChildren()[i] != null) {
                right = false;
            }
        }

        //Three bodies
        right = simpleAddCheck(tree, trivialBodyList[2]) ? right : false;
        right = addCheckRightPos(tree, trivialBodyList, new int[]{0, 2}) ? right : false;
        //Four bodies
        right = simpleAddCheck(tree, trivialBodyList[3]) ? right : false;
        right = addCheckRightPos(tree, trivialBodyList, new int[]{0, 3}) ? right : false;
        //Five bodies
        right = simpleAddCheck(tree, trivialBodyList[4]) ? right : false;
        right = addCheckRightPos(tree, trivialBodyList, new int[]{0, 4}) ? right : false;
        //Six bodies
        right = simpleAddCheck(tree, trivialBodyList[5]) ? right : false;
        right = addCheckRightPos(tree, trivialBodyList, new int[]{0, 5}) ? right : false;
        //Seven bodies
        right = simpleAddCheck(tree, trivialBodyList[6]) ? right : false;
        right = addCheckRightPos(tree, trivialBodyList, new int[]{0, 6}) ? right : false;
        //Eight bodies
        right = simpleAddCheck(tree, trivialBodyList[7]) ? right : false;
        right = addCheckRightPos(tree, trivialBodyList, new int[]{0, 7}) ? right : false;

        if (!tree.getCenterOfMass().equals(new Vector3(500, 500, 500))) {
            right = false;
        }

        //Special body 1 - edge case test
        right = simpleAddCheck(tree, body9) ? right : false;
        if (tree.getChildren()[0].getValue() != null
                || tree.getChildren()[0].getChildren()[0].getValue() == null
                || tree.getChildren()[0].getChildren()[7].getValue() == null) {
            right = false;
        }
        return right;
    }

    private boolean simpleAddCheck(Octree tree, CelestialBody body) {
        if (!tree.addToOctree(body) || tree.getValue() != null) {
            return false;
        }
        return true;
    }

    private boolean addCheckRightPos(Octree tree, CelestialBody[] bodies, int[] boundaries) {
        for (int i = boundaries[0]; i <= boundaries[1]; i++) {
            if (tree.getChildren()[i] == null || !tree.getChildren()[i].getValue().equals(bodies[i])) {
                return false;
            }
        }
        return true;
    }


    /**
     * Tests if the force calculation (barnes hut) works,
     * for this some random celestial bodies are created
     * and the barnes hut algorithm is compared to the n^2 algorithm
     *
     * @param numberOfMeasurements    Defines how many measurements shall be created
     * @param numberOfCelestialBodies With how many celestial bodies the calculation is done
     * @return Returns true if force calculation works, false otherwise
     */
    public boolean testForceCalculation(int numberOfMeasurements, int numberOfCelestialBodies) {
        double cubeLength = Constants.AU;
        boolean isCorrect = true;

        GenerateSimulationData genData = new GenerateSimulationData(numberOfCelestialBodies, SimulationType.CLUSTER, cubeLength, 100);

        CelestialBody[] bodies = genData.generateAsArray();

        Cube cube = new Cube(new Vector3(-cubeLength / 2, -cubeLength / 2, -cubeLength / 2), cubeLength);
        Octree tree = new Octree(cube);
        Octree newTree = new Octree(cube);

        double totalError = 0;
        int totalNumber = 0;

        for (CelestialBody body : bodies) {
            tree.addToOctree(body.clone());
        }

        Vector3[] forceOnBody = new Vector3[bodies.length];

        for (int i = 0; i < numberOfMeasurements; i++) {

            // for each body (with index i): compute the total force exerted on it.
            for (int k = 0; k < bodies.length; k++) {
                forceOnBody[k] = new Vector3(); // begin with zero
                for (int j = 0; j < bodies.length; j++) {
                    if (k == j) continue;
                    Vector3 forceToAdd = bodies[k].gravitationalForce(bodies[j]);
                    forceOnBody[k] = forceOnBody[k].plus(forceToAdd);
                }
            }

            for (int k = 0; k < bodies.length; k++) {
                bodies[k].move(forceOnBody[k]);
            }

            newTree = new Octree(cube);
            tree.calculateForcesOnAllBodies(tree, newTree);
            tree = newTree;

            ListNode curNode = tree.getListOfBodies().getHead();
            while (curNode != null) {
                CelestialBody treeBody = curNode.getBody();
                CelestialBody arrayBody = null;
                for (int arrayIndex = 0; arrayIndex < bodies.length; arrayIndex++) {
                    if (treeBody.getName().equals(bodies[arrayIndex].getName())) {
                        arrayBody = bodies[arrayIndex];
                    }
                }

                if (arrayBody == null) {
                    isCorrect = false;
                } else {
                    double factor = treeBody.getPosition().distanceToCenter() / arrayBody.getPosition().distanceToCenter();

                    double error = Math.abs(1 - factor) * 100;

                    totalError += error;
                    totalNumber++;
                }

                curNode = curNode.getNext();
            }

            tree = new Octree(cube);
            newTree = new Octree(cube);
            for (CelestialBody body : bodies) {
                tree.addToOctree(body.clone());
            }
        }

        this.averageCalculationError = totalError / totalNumber;
        if (this.averageCalculationError > 5) {
            isCorrect = false;
        }
        return isCorrect;
    }
}
