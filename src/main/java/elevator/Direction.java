package elevator;

public enum Direction {
    NONE, UP, DOWN;

    public Direction negative() {
        if (this.equals(UP)) return DOWN;
        else if (this.equals(DOWN)) return UP;
        else return NONE;
    }

    public static Direction of(final int value) {
        if (value == 0) return NONE;
        else if (value < 0) return DOWN;
        else return UP;
    }

    public int toInt() {
        if (this.equals(UP)) return 1;
        else if (this.equals(DOWN)) return -1;
        else return 0;
    }

    public boolean isUp() {
        return this.equals(UP);
    }

    public boolean isDown() {
        return this.equals(DOWN);
    }

    public boolean isNone() {
        return this.equals(NONE);
    }


}
