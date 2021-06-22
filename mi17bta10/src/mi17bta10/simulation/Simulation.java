package mi17bta10.simulation;

import mi17bta10.generateSimulationData.GenerateSimulationData;
import mi17bta10.generateSimulationData.SimulationType;

/**
 * Specifies what kind of simulation type shall be simulated,
 * and contains the endless loop which is for the move-calc-cycle.
 */
public class Simulation {
    private static Octree root;

    public static void main(String[] args) {

        GenerateSimulationData genData = new GenerateSimulationData(10000, SimulationType.COOL_STUFF);
        double genLength = genData.getEdgeLengthOfSimulatedArea();
        double viewConstant = genData.getViewConstant();

        Cube cube = new Cube(new Vector3(-genLength / 2, -genLength / 2, -genLength / 2), genLength);
        Octree tree = genData.generateAsTree();
        System.out.println("-Generate data complete");

        root = tree;

        StdDraw.setCanvasSize(1000, 1000);
        StdDraw.setXscale(-genLength / 2, genLength / 2);
        StdDraw.setYscale(-genLength / 2, genLength / 2);

        StdDraw.setVisible(true);
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.enableDoubleBuffering();
        StdDraw.show();
        StdDraw.clear(StdDraw.BLACK);
        displayCelestialBodies(tree, false, viewConstant);
        StdDraw.show();
        System.out.println("-Init finished");
        Octree newOctree;
        long calcTimeStart = 0, calcTimeEnd = 0, calcTimeDif = 0;
        while (true) {
            long startTime = System.nanoTime();

            StdDraw.clear(StdDraw.BLACK);
            displayCelestialBodies(tree, false, viewConstant);
            StdDraw.show();
            long endTime = System.nanoTime();
            System.out.println("Print - Display time: " + (endTime - startTime) / 10e6 + ":: - Calc time: " + calcTimeDif / 10e6);

            calcTimeStart = System.nanoTime();
            newOctree = new Octree(cube);
            tree.calculateForcesOnAllBodies(tree, newOctree);
            tree = newOctree;
            calcTimeEnd = System.nanoTime();
            calcTimeDif = calcTimeEnd - calcTimeStart;

        }
    }

    /**
     * Prints names and positions of all celestial bodies in the tree.
     *
     * @param tree Octree with celestial bodies whose names and positions shall be printed
     */
    private static void print(Octree tree) {
        if (tree != null && tree.getValue() != null) {
            System.out.println(tree.getValue().getName() + " " + tree.getValue().getPosition());
        }

        for (int i = 0; i < tree.getChildren().length; i++) {
            if (tree.getChildren()[i] != null) {
                print(tree.getChildren()[i]);
            }
        }
    }

    /**
     * Gives a graphical representation of all celestial bodies in the tree (with or without edges).
     *
     * @param root         Octree with celestial bodies
     * @param drawEdges    A parameter that specifies if edges should be drawn or not
     * @param viewConstant A parameter that specifies how big a celestial body shall be drawn
     */
    private static void displayCelestialBodies(Octree root, boolean drawEdges, double viewConstant) {
        if (drawEdges) {
            root.displayOctree(viewConstant);

        } else {
            List listOfBodies = root.getListOfBodies();
            ListNode currentNode = listOfBodies.getHead();
            CelestialBody currentBody;
            while (currentNode != null) {
                currentBody = currentNode.getBody();
                currentBody.draw(viewConstant);
                currentNode = currentNode.getNext();
            }
        }
    }
}