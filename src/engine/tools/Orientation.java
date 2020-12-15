package engine.tools;

public enum Orientation {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;

    public int getInt() {
        return switch (this) {
            case NORTH -> 0;
            case NORTHEAST -> 1;
            case EAST -> 2;
            case SOUTHEAST -> 3;
            case SOUTH -> 4;
            case SOUTHWEST -> 5;
            case WEST -> 6;
            case NORTHWEST -> 7;
        };
    }

    public static Orientation getOrient(int x) {
        return switch(x % 8) {
            case 0 -> NORTH;
            case 1 -> NORTHEAST;
            case 2 -> EAST;
            case 3 -> SOUTHEAST;
            case 4 -> SOUTH;
            case 5 -> SOUTHWEST;
            case 6 -> WEST;
            case 7 -> NORTHWEST;
            default -> NORTH;
        };
    }

    public double getDegree() {
        return switch(this) {
            case NORTH -> 0;
            case NORTHEAST -> 45;
            case EAST -> 90;
            case SOUTHEAST -> 135;
            case SOUTH -> 180;
            case SOUTHWEST -> 225;
            case WEST -> 270;
            case NORTHWEST -> 315;
        };
    }

    public static Orientation translateOrient(Orientation x, Orientation y) {
        return getOrient(x.getInt() + y .getInt());
    }

    public static Orientation translateOrient(Orientation x, int y) {
        return getOrient(x.getInt() + y);
    }
}
