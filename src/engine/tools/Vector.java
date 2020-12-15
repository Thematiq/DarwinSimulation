package engine.tools;

import java.util.Objects;
import java.util.Random;

public class Vector {
    public final int x;
    public final int y;

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Vector randomVector(Random rn, Vector bottomLeft, Vector topRight) {
        return new Vector(rn.nextInt(topRight.x - bottomLeft.x + 1) + bottomLeft.x,
                          rn.nextInt(topRight.y - bottomLeft.y + 1) + bottomLeft.y);
    }

    public Vector opposite() {return new Vector(-this.x, -this.y); }
    public Vector add(Vector other) {
        return new Vector(this.x + other.x, this.y + other.y);
    }

    public Vector mult(int scalar) {return new Vector(this.x * scalar, this.y * scalar); }

    public boolean precedes(Vector other) {
        return this.x <= other.x && this.y <= other.y;
    }

    public boolean follows(Vector other) {
        return this.x >= other.x && this.y >= other.y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return x == vector.x &&
                y == vector.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
}
