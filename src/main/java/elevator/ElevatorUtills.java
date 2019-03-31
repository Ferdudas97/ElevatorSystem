package elevator;

import static elevator.Direction.*;

public class ElevatorUtills {


    public static Direction getDirection(final Integer targetLevel, final Integer currentLevel) {
        if (targetLevel > currentLevel) return UP;
        else if (targetLevel < currentLevel) return DOWN;
        else return NONE;

    }

}
