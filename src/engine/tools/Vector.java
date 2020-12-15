package engine.tools;

import java.util.Objects;
import java.util.Random;

/**
 * Class desribing 2 dimensional vector
 * @author Mateusz Praski
 */
public class Vector {
    public final int x;
    public final int y;

    /** Spawns new Vector
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

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

    /**
     * @return opposite vector (-x, -y)
     */
    public Vector opposite() {return new Vector(-this.x, -this.y); }

    /**
     * @param other Second vector
     * @return Sum of the vectors
     */
    public Vector add(Vector other) {
        return new Vector(this.x + other.x, this.y + other.y);
    }

    /**
     * @return new Vector scaled by a given integer
     */
    public Vector mult(int scalar) {return new Vector(this.x * scalar, this.y * scalar); }

    /**
     * @param other Second Vector
     * @return True if Vector has both coordinates smaller than the other
     */
    public boolean precedes(Vector other) {
        return this.x <= other.x && this.y <= other.y;
    }

    /**
     * @param other Second Vector
     * @return True if Vector has both coordinates larger than the other
     */
    public boolean follows(Vector other) {
        return this.x >= other.x && this.y >= other.y;
    }

    /** Wraps Vector within bounds of the (0,0) - border rectangle
     *
     * @param border topRight vertex of the rectangle
     * @return wrapped Vector
     */
    public Vector wrap(Vector border) {
        int new_x;
        int new_y;
        if(this.x > border.x) {
            new_x = this.x - border.x - 1;
        } else if(this.x < 0) {
            new_x = border.x + this.x + 1;
        } else {
            new_x = x;
        }
        if(this.y > border.y) {
            new_y = this.y - border.y - 1;
        } else if(this.y < 0) {
            new_y = border.y + this.y + 1;
        } else {
            new_y = y;
        }
        return new Vector(new_x, new_y);
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
