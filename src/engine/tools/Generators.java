package engine.tools;

import java.util.Random;

public class Generators {

    /** Creates new Vector inside given rectangle
     *
     * @param rn Java Random object
     * @param bottomLeft Bottom left corner of the rectangle
     * @param topRight Top right corner of the rectangle
     * @return new Vector from (0,0) to position inside the rectangle
     */
    public static Vector randomVector(Random rn, Vector bottomLeft, Vector topRight) {
        return new Vector(rn.nextInt(topRight.x - bottomLeft.x + 1) + bottomLeft.x,
                          rn.nextInt(topRight.y - bottomLeft.y + 1) + bottomLeft.y);
    }
}
