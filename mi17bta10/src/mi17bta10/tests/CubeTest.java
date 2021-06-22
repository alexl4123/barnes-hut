package mi17bta10.tests;

import mi17bta10.simulation.Cube;
import mi17bta10.simulation.Vector3;

/**
 * Test class for the cube class
 */
public class CubeTest implements TestInterface {

    private boolean isInsideCubeTestWorking;

    @Override
    public void startTests() {
        this.isInsideCubeTestWorking = insideCubeTest();
    }

    @Override
    public void printResults() {
        System.out.println("-" + this.isInsideCubeTestWorking + ":: Does check if inside cube work?");
    }

    private boolean insideCubeTest() {
        boolean right = true;

        Vector3 leftDownCorner = new Vector3(0, 0, 0);
        Cube testCube = new Cube(leftDownCorner, 100);

        Vector3 testVector1 = new Vector3(0, 0, 0);
        Vector3 testVector2 = new Vector3(100, 0, 0);
        Vector3 testVector3 = new Vector3(0, 100, 0);
        Vector3 testVector4 = new Vector3(0, 0, 100);
        Vector3 testVector5 = new Vector3(100, 100, 100);
        Vector3 testVector6 = new Vector3(50, 50, 50);

        if (!testCube.isInsideCube(testVector1)
                || testCube.isInsideCube(testVector2)
                || testCube.isInsideCube(testVector3)
                || testCube.isInsideCube(testVector4)
                || testCube.isInsideCube(testVector5)
                || !testCube.isInsideCube(testVector6)) {
            right = false;
        }
        return right;
    }
}
