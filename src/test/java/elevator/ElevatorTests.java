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
        elevator.pickup(2, 5);
        assertEquals(5, elevator.getTargetFloor());
        assertEquals(Set.of(2,5), elevator.getRoad());
        elevator.pickup(3, 4);
        assertEquals(Set.of(5,4,3,2), elevator.getRoad());
        elevator.pickup(6, 8);
        assertEquals(Set.of(8,5,6,4,3,2), elevator.getRoad());
    }

    @Test
    void stepTest() {
        val elevator = Elevator.of(1, 0);
        elevator.pickup(2, 5);
        doElevatorSteps(elevator, 2);
        assertEquals(2, elevator.getCurrentLevel());
        assertEquals(Set.of(5), elevator.getRoad());

        doElevatorSteps(elevator, 3);
        assertEquals(Set.of(), elevator.getRoad());
        assertEquals(5, elevator.getCurrentLevel());

        doElevatorSteps(elevator, 4);
        assertEquals(Set.of(), elevator.getRoad());
        assertEquals(5, elevator.getCurrentLevel());
        elevator.pickup(4, 2);
        assertEquals(Set.of(4,2), elevator.getRoad());
        assertEquals(5, elevator.getCurrentLevel());
        doElevatorSteps(elevator, 1);
        assertEquals(4, elevator.getCurrentLevel());
        assertEquals(Set.of(2), elevator.getRoad());


    }


    private void doElevatorSteps(final Elevator elevator, final int n) {
        Stream.iterate(0, i -> i++)
                .limit(n)
                .forEach(i -> elevator.step());
    }

}
