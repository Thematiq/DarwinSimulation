package engine.tools;

public class Algebra {
    public static Vector rotateVector(Vector pos, double degree, Vector centerVec) {
        pos = pos.add(centerVec);
        // Using rotation matrix equation
        double radians = Math.toRadians(degree);
        return new Vector(
                (int)(pos.x * Math.cos(radians) + pos.y * Math.sin(radians)),
                (int)(pos.y * Math.cos(radians) - pos.x * Math.sin(radians))
        ).add(centerVec.opposite());
    }
}
