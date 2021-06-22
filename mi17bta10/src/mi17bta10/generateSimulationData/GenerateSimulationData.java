package mi17bta10.generateSimulationData;

import mi17bta10.simulation.*;

/**
 * This class generates the celestial bodies necessary for the simulation.
 */
public class GenerateSimulationData {

    /**
     * How many celestial bodies shall be created in this simulation
     */
    private final int numberOfCelestialBodies;

    /**
     * A constant that specifies how many seconds are calculated in a calc-move cycle
     */
    private final int simulationSpeedInSecondsCalculated;

    /**
     * Enum that specifies what kind of simulation shall be generated
     */
    private final SimulationType simulationType;

    /**
     * The length of a side in the octree
     */
    private final double edgeLengthOfSimulatedArea;

    /**
     * A parameter that specifies how big a celestial body shall be drawn
     */
    private final double viewConstant;

    /**
     * Constructor for more customization
     *
     * @param numberOfCelestialBodies            How many celestial bodies shall be created in this simulation
     * @param simulationType                     The type of the simulation (ENUM)
     * @param edgeLengthOfSimulatedCube          The length of a side in the octree
     * @param simulationSpeedInSecondsCalculated How many seconds are calculated in a calc-move cycle
     */
    public GenerateSimulationData(int numberOfCelestialBodies, SimulationType simulationType, double edgeLengthOfSimulatedCube, int simulationSpeedInSecondsCalculated) {
        this.numberOfCelestialBodies = numberOfCelestialBodies;
        this.simulationSpeedInSecondsCalculated = simulationSpeedInSecondsCalculated;
        this.simulationType = simulationType;
        this.edgeLengthOfSimulatedArea = edgeLengthOfSimulatedCube;
        this.viewConstant = this.edgeLengthOfSimulatedArea;
    }

    /**
     * The normal constructor
     *
     * @param numberOfCelestialBodies How many celestial bodies shall be generated
     * @param simulationType          The type of the simulation (ENUM)
     */
    public GenerateSimulationData(int numberOfCelestialBodies, SimulationType simulationType) {
        this.numberOfCelestialBodies = numberOfCelestialBodies;
        this.simulationType = simulationType;

        switch (simulationType) {
            case SOL: {
                this.edgeLengthOfSimulatedArea = 70 * Constants.AU;
                this.viewConstant = this.edgeLengthOfSimulatedArea / 4;
                this.simulationSpeedInSecondsCalculated = 10800; //AKA 3 hours
                break;
            }
            case ASTEROID_BELT: {
                this.edgeLengthOfSimulatedArea = 8 * Constants.AU;
                this.viewConstant = this.edgeLengthOfSimulatedArea / 150;
                this.simulationSpeedInSecondsCalculated = 29600; //a few hours per move cycle
                break;
            }
            case COOL_STUFF: {
                this.edgeLengthOfSimulatedArea = 8 * Constants.AU;
                this.viewConstant = this.edgeLengthOfSimulatedArea / 150;
                this.simulationSpeedInSecondsCalculated = 59200; //a few hours
                break;
            }
            case CLUSTER: {
                this.edgeLengthOfSimulatedArea = 2 * Constants.LIGHT_YEAR;
                this.viewConstant = this.edgeLengthOfSimulatedArea / 200;
                this.simulationSpeedInSecondsCalculated = 100000000; //Much
                break;
            }
            case DISTRIBUTED_CLUSTERS: {
                this.edgeLengthOfSimulatedArea = 5 * Constants.LIGHT_YEAR;
                this.viewConstant = this.edgeLengthOfSimulatedArea / 200;
                this.simulationSpeedInSecondsCalculated = 10000000; //Much
                break;
            }
            default: {
                this.edgeLengthOfSimulatedArea = 0;
                this.viewConstant = 0;
                this.simulationSpeedInSecondsCalculated = 1;
                break;
            }
        }
    }

    /**
     * Generates octree with bodies according to the specific type.
     *
     * @return Octree with bodies according to the specific type.
     */
    public Octree generateAsTree() {
        switch (this.simulationType) {
            case CLUSTER:
                return new GenerateCLUSTER(this.simulationSpeedInSecondsCalculated, this.numberOfCelestialBodies)
                        .generateAsTree(this.edgeLengthOfSimulatedArea, new Vector3(0, 0, 0), this.numberOfCelestialBodies);
            case SOL:
                return new GenerateSOL(this.edgeLengthOfSimulatedArea, this.simulationSpeedInSecondsCalculated)
                        .generateAsTree(this.edgeLengthOfSimulatedArea, new Vector3(0, 0, 0), this.numberOfCelestialBodies);
            case DISTRIBUTED_CLUSTERS:
                return new GenerateDISPERSEDCLUSTERS(this.edgeLengthOfSimulatedArea, this.simulationSpeedInSecondsCalculated, this.numberOfCelestialBodies)
                        .generateAsTree(this.edgeLengthOfSimulatedArea, new Vector3(0, 0, 0), this.numberOfCelestialBodies);
            case ASTEROID_BELT:
                return new GenerateASTEROIDBELT(this.numberOfCelestialBodies, this.simulationSpeedInSecondsCalculated)
                        .generateAsTree(this.edgeLengthOfSimulatedArea, new Vector3(0, 0, 0), this.numberOfCelestialBodies);
            case COOL_STUFF:
                return new GenerateCOOLSTUFF(this.numberOfCelestialBodies, this.simulationSpeedInSecondsCalculated)
                        .generateAsTree(this.edgeLengthOfSimulatedArea, new Vector3(0, 0, 0), this.numberOfCelestialBodies);
            default:
                return null;
        }
    }

    /**
     * Generates array with bodies according to the specific type.
     *
     * @return Array with bodies according to the specific type.
     */
    public CelestialBody[] generateAsArray() {
        switch (this.simulationType) {
            case CLUSTER:
                return new GenerateCLUSTER(this.simulationSpeedInSecondsCalculated, this.numberOfCelestialBodies)
                        .generateAsArray(
                                new Cube(new Vector3(-this.edgeLengthOfSimulatedArea / 2, -this.edgeLengthOfSimulatedArea / 2, -this.edgeLengthOfSimulatedArea / 2), this.edgeLengthOfSimulatedArea),
                                new Vector3(0, 0, 0),
                                this.numberOfCelestialBodies);
            case DISTRIBUTED_CLUSTERS:
                return new GenerateDISPERSEDCLUSTERS(this.edgeLengthOfSimulatedArea, this.simulationSpeedInSecondsCalculated, this.numberOfCelestialBodies)
                        .generateAsArray(
                                new Cube(new Vector3(-this.edgeLengthOfSimulatedArea / 2, -this.edgeLengthOfSimulatedArea / 2, -this.edgeLengthOfSimulatedArea / 2), this.edgeLengthOfSimulatedArea),
                                new Vector3(0, 0, 0),
                                this.numberOfCelestialBodies);
            case SOL:
                return new GenerateSOL(this.edgeLengthOfSimulatedArea, this.simulationSpeedInSecondsCalculated)
                        .generateAsArray(
                                new Cube(new Vector3(-this.edgeLengthOfSimulatedArea / 2, -this.edgeLengthOfSimulatedArea / 2, -this.edgeLengthOfSimulatedArea / 2), this.edgeLengthOfSimulatedArea),
                                new Vector3(0, 0, 0),
                                this.numberOfCelestialBodies);
            case ASTEROID_BELT:
                return new GenerateASTEROIDBELT(this.numberOfCelestialBodies, this.simulationSpeedInSecondsCalculated)
                        .generateAsArray(
                                new Cube(new Vector3(-this.edgeLengthOfSimulatedArea / 2, -this.edgeLengthOfSimulatedArea / 2, -this.edgeLengthOfSimulatedArea / 2), this.edgeLengthOfSimulatedArea),
                                new Vector3(0, 0, 0),
                                this.numberOfCelestialBodies);
            case COOL_STUFF:
                return new GenerateCOOLSTUFF(this.numberOfCelestialBodies, this.simulationSpeedInSecondsCalculated)
                        .generateAsArray(
                                new Cube(new Vector3(-this.edgeLengthOfSimulatedArea / 2, -this.edgeLengthOfSimulatedArea / 2, -this.edgeLengthOfSimulatedArea / 2), this.edgeLengthOfSimulatedArea),
                                new Vector3(0, 0, 0),
                                this.numberOfCelestialBodies);
            default:
                return null;
        }
    }

    /**
     * Returns the length of a side in the octree (edgeLengthOfSimulatedArea).
     *
     * @return edgeLengthOfSimulatedArea
     */
    public double getEdgeLengthOfSimulatedArea() {
        return this.edgeLengthOfSimulatedArea;
    }

    /**
     * Returns a parameter that specifies how big a celestial body shall be drawn (viewConstant).
     *
     * @return viewConstant
     */
    public double getViewConstant() {
        return this.viewConstant;
    }
}
