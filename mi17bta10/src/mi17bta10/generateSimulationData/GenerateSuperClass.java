package mi17bta10.generateSimulationData;

import mi17bta10.simulation.*;

/**
 * This class generates an asteroid belt of celestial bodies
 * and returns it as octree.
 */

public abstract class GenerateSuperClass {

    protected abstract Octree generateAsTree(double edgeLengthOfSimulatedArea, Vector3 leftDownCornerOffset, int numberOfCelestialBodies);

    protected abstract CelestialBody[] generateAsArray(Cube cube, Vector3 offset, int numberOfCelestialBodies);
}
