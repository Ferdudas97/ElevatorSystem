package elevator;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ElevatorTests {


    @Test
    void pickUpTest() {
        val elevator = Elevator.of(1, 0);
        elevator.pickup(2, 5);
        assertEquals(2, elevator.getCurrentTarget());
        assertEquals(List.of(2), elevator.getRoad());
        elevator.pickup(3, 4);
        assertEquals(List.of(3,2), elevator.getRoad());
        elevator.pickup(6, 8);
        assertEquals(List.of(6,3,2), elevator.getRoad());
    }

    @Test
    void stepTest() {
        val elevator = Elevator.of(1, 0);
        elevator.pickup(2, 5);
        doElevatorSteps(elevator, 2);
        assertEquals(2, elevator.getCurrentLevel());
        assertEquals(List.of(5), elevator.getRoad());

        doElevatorSteps(elevator, 3);
        assertEquals(List.of(), elevator.getRoad());
        assertEquals(5, elevator.getCurrentLevel());

        doElevatorSteps(elevator, 4);
        assertEquals(List.of(), elevator.getRoad());
        assertEquals(5, elevator.getCurrentLevel());
        elevator.pickup(4, 2);
        assertEquals(List.of(4), elevator.getRoad());
        assertEquals(5, elevator.getCurrentLevel());
        doElevatorSteps(elevator, 1);
        assertEquals(4, elevator.getCurrentLevel());
        assertEquals(List.of(2), elevator.getRoad());


    }


    private void doElevatorSteps(final Elevator elevator, final int n) {
        Stream.iterate(0, i -> i++)
                .limit(n)
                .forEach(i -> elevator.step());
    }

}
