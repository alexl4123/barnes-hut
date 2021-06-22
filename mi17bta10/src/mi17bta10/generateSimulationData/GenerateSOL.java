package mi17bta10.generateSimulationData;

import mi17bta10.simulation.*;

/**
 * This class generates an octree that contains all planets of
 * out solar system and the sun.
 */
public class GenerateSOL extends GenerateSuperClass {

    /**
     * edgeLengthOfSimulatedArea - The length of a side in the octree
     */
    private final double edgeLengthOfSimulatedArea;

    /**
     * simulationSpeedInSecondsCalculated - A constant that specifies how many seconds are calculated in a calc-move cycle
     */
    private final int simulationSpeedInSecondsCalculated;

    /**
     * Constructor
     *
     * @param edgeLengthOfSimulatedArea          The length of a side in the octree
     * @param simulationSpeedInSecondsCalculated How many seconds are calculated in a calc-move cycle
     */
    protected GenerateSOL(double edgeLengthOfSimulatedArea, int simulationSpeedInSecondsCalculated) {
        this.edgeLengthOfSimulatedArea = edgeLengthOfSimulatedArea;
        this.simulationSpeedInSecondsCalculated = simulationSpeedInSecondsCalculated;
    }

    /**
     * Generates octree with all planets of our solar system and the sun.
     *
     * @param edgeLengthOfSimulatedArea The length of a side in the octree
     * @param leftDownCornerOffset      Coordinates of bottom-left corner
     * @param numberOfCelestialBodies   How many celestial bodies shall be created in this simulation
     * @return Octree with the planets of our solar system and the sun
     */
    @Override
    protected Octree generateAsTree(double edgeLengthOfSimulatedArea, Vector3 leftDownCornerOffset, int numberOfCelestialBodies) {
        Cube cube = new Cube(new Vector3(-this.edgeLengthOfSimulatedArea / 2, -this.edgeLengthOfSimulatedArea / 2, -this.edgeLengthOfSimulatedArea / 2), this.edgeLengthOfSimulatedArea);
        Octree tree = new Octree(cube);

        for (CelestialBody body : generateAsArray(cube, leftDownCornerOffset, numberOfCelestialBodies)) {
            tree.addToOctree(body);
        }
        return tree;
    }

    /**
     * Generates array with all planets of our solar system and the sun.
     *
     * @param cube                    Defines the location and size of the octree-node
     * @param offset                  Coordinates of bottom-left corner
     * @param numberOfCelestialBodies How many celestial bodies shall be created in this simulation
     * @return Array with the planets of our solar system and the sun
     */
    @Override
    protected CelestialBody[] generateAsArray(Cube cube, Vector3 offset, int numberOfCelestialBodies) {
        Vector3 sunPosition = new Vector3(0, 0, 0);
        Vector3 sunCurrentMovement = new Vector3(0, 0, 0); // sun is the reference point and assumed not to move.
        CelestialBody sun = new CelestialBody("Sol", 1.989e30, 696340e3, sunPosition, sunCurrentMovement, StdDraw.YELLOW, this.simulationSpeedInSecondsCalculated);

        Vector3 earthPosition = new Vector3(148e9, 0, 0); //x-coordinate is minimal distance to sun in meters.
        Vector3 earthCurrentMovement = new Vector3(0, 29.29e3, 0); //y-orbital speed in meters per second (at minimal distance).
        CelestialBody earth = new CelestialBody("Earth", 5.972e24, 6371e3, earthPosition, earthCurrentMovement, StdDraw.BLUE, this.simulationSpeedInSecondsCalculated);

        Vector3 mercuryPosition = new Vector3(-46.0e9, 0, 0); // arbitrary initialisation: position opposite to the earth with maximal distance.
        Vector3 mercuryCurrentMovement = new Vector3(0, -47.87e3, 0); // viewing from z direction movement is counter-clockwise
        CelestialBody mercury = new CelestialBody("Mercury", 3.301e23, 2439.7e3, mercuryPosition, mercuryCurrentMovement, StdDraw.RED, this.simulationSpeedInSecondsCalculated);

        Vector3 marsPosition = new Vector3(-205.0e9, 0, 0); // arbitrary initialisation: position opposite to the earth with maximal distance.
        Vector3 marsCurrentMovement = new Vector3(0, -24.1e3, 0); // viewing from z direction movement is counter-clockwise
        CelestialBody mars = new CelestialBody("Mars", 6.39e23, 3389.5e3, marsPosition, marsCurrentMovement, StdDraw.BOOK_RED, this.simulationSpeedInSecondsCalculated);

        Vector3 venusPosition = new Vector3(-107.0e9, 0, 0); // arbitrary initialisation: position opposite to the earth with maximal distance.
        Vector3 venusCurrentMovement = new Vector3(0, -35e3, 0); // viewing from z direction movement is counter-clockwise
        CelestialBody venus = new CelestialBody("Venus", 4.876e24, 6051.8e3, venusPosition, venusCurrentMovement, StdDraw.PRINCETON_ORANGE, this.simulationSpeedInSecondsCalculated);

        Vector3 jupiterPosition = new Vector3(-741.0e9, 0, 0); // arbitrary initialisation: position opposite to the earth with maximal distance.
        Vector3 jupiterCurrentMovement = new Vector3(0, -13.1e3, 0); // viewing from z direction movement is counter-clockwise
        CelestialBody jupiter = new CelestialBody("Jupiter", 1.898e27, 69911e3, jupiterPosition, jupiterCurrentMovement, StdDraw.LIGHT_GRAY, this.simulationSpeedInSecondsCalculated);

        Vector3 saturnPosition = new Vector3(-135.0e10, 0, 0); // arbitrary initialisation: position opposite to the earth with maximal distance.
        Vector3 saturnCurrentMovement = new Vector3(0, -9.6e3, 0); // viewing from z direction movement is counter-clockwise
        CelestialBody saturn = new CelestialBody("Saturn", 5.683e26, 58232e3, saturnPosition, saturnCurrentMovement, StdDraw.DARK_GRAY, this.simulationSpeedInSecondsCalculated);

        Vector3 uranusPosition = new Vector3(-275.0e10, 0, 0); // arbitrary initialisation: position opposite to the earth with maximal distance.
        Vector3 uranusCurrentMovement = new Vector3(0, -6.8e3, 0); // viewing from z direction movement is counter-clockwise
        CelestialBody uranus = new CelestialBody("Uranus", 8.681e25, 25362e3, uranusPosition, uranusCurrentMovement, StdDraw.BOOK_LIGHT_BLUE, this.simulationSpeedInSecondsCalculated);

        Vector3 neptunePosition = new Vector3(-445.0e10, 0, 0); // arbitrary initialisation: position opposite to the earth with maximal distance.
        Vector3 neptuneCurrentMovement = new Vector3(0, -5.4e3, 0); // viewing from z direction movement is counter-clockwise
        CelestialBody neptune = new CelestialBody("Neptune", 1.024e26, 24622e3, neptunePosition, neptuneCurrentMovement, StdDraw.BOOK_BLUE, this.simulationSpeedInSecondsCalculated);

        CelestialBody[] bodies = new CelestialBody[]{sun, mercury, venus, earth, mars, jupiter, saturn, uranus, neptune};
        return bodies;
    }
}
