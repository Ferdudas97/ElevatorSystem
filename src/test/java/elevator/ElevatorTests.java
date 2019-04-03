package elevator;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ElevatorTests {


    @Test
    void pickUpTest() {
        val elevator = Elevator.of(1, 0);
        elevator.pickup(2, 1);
        assertEquals(2, elevator.getTargetFloor());
        assertEquals(Set.of(2), elevator.getRoad());
        elevator.pickup(4, 1);
        assertEquals(Set.of(4, 2), elevator.getRoad());
        elevator.pickup(6, -1);
        assertEquals(Set.of(4, 2), elevator.getRoad());
    }

    @Test
    void stepTest() {
        val elevator = Elevator.of(1, 0);
        elevator.pickup(2, 1);
        doElevatorSteps(elevator, 2);
        assertEquals(2, elevator.getCurrentLevel());
        assertEquals(Set.of(), elevator.getRoad());
        doElevatorSteps(elevator, 3);

        assertEquals(Set.of(), elevator.getRoad());
        assertEquals(2, elevator.getCurrentLevel());

        elevator.pickup(4, -1);
        assertEquals(Set.of(4), elevator.getRoad());
        assertEquals(Direction.UP, elevator.getDirection());
        doElevatorSteps(elevator, 2);

        assertEquals(4, elevator.getCurrentLevel());


    }



    private void doElevatorSteps(final Elevator elevator, final int n) {
        Stream.iterate(0, i -> i++)
                .limit(n)
                .forEach(i -> elevator.step());
    }

}
