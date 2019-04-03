package elevator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static elevator.Direction.*;
import static org.junit.jupiter.api.Assertions.*;

public class DirectionTest {
    @Test
    public void negativeTest() {
        assertEquals(NONE, NONE.negative());
        assertEquals(DOWN, UP.negative());
        assertEquals(UP, DOWN.negative());
    }

}
