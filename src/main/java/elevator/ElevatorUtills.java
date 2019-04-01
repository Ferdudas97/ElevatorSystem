package elevator;

import static elevator.Direction.*;

public class ElevatorUtills {

    public static final int MAX_PRIORITY = 1000;
    public static final int MIN_PRIORITY = -1000;

    public static Direction getDirection(final Integer targetLevel, final Integer currentLevel) {
        if (targetLevel > currentLevel) return UP;
        else if (targetLevel < currentLevel) return DOWN;
        else return NONE;

    }

}
