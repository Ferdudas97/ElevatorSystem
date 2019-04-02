package elevator;

public enum Direction {
    NONE,UP,DOWN;

    public Direction negative() {
        if ( this.equals(UP) ) return DOWN;
        else if ( this.equals(DOWN)) return UP;
        else return NONE;
    }
}
