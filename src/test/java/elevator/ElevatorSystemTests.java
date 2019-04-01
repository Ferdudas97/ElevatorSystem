package elevator;

import lombok.val;
import lombok.var;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorSystemTests {
    private ElevatorSystemImpl system;

    ElevatorSystemTests() {
        val elevators = Stream.iterate(0, i -> i++)
                .limit(5)
                .map(i -> Elevator.of(i, 0))
                .collect(Collectors.toList());
        this.system = ElevatorSystemImpl.of(elevators);
    }

    @Test
    void stepTests() {
        val elevators = Stream.iterate(1, i -> i+1)
                .limit(5)
                .map(i -> Elevator.of(i, 0))
                .collect(Collectors.toList());
        this.system = ElevatorSystemImpl.of(elevators);

        system.pickUp(2, 4);
        system.pickUp(3, 8);
        check(Set.of(2,3,4,8), 5);
        doSystemStep(3);
        check(Set.of(4,8), 5);
        doSystemStep(1);
        check(Set.of(8), 5);
        system.pickUp(1, 6);
        system.pickUp(9, 10);
        check(Set.of(1,6), 4);
        check(Set.of(8,9,10), 5);
        doSystemStep(5);
        check(Set.of(6), 4);
        check(Set.of(10), 5);
        doSystemStep(1);
        check(Set.of(), 5);
        check(Set.of(), 4);
        system.pickUp(3, 5);
        system.pickUp(3, 8);
        system.pickUp(9, 5);
        system.pickUp(4, 6);
        check(Set.of(5,9), 5);
        check(Set.of(3,4,5,6,8), 3);
    }

    @Test
    void stepTest2() {
        val elevators = List.of(Elevator.of(0, 10),
                Elevator.of(1, 4),
                Elevator.of(2, 0),
                Elevator.of(3, 15),
                Elevator.of(4, 7));
        this.system = ElevatorSystemImpl.of(elevators);

        system.pickUp(8, 11);
        system.pickUp(9, 10);
        system.pickUp(10, 9);
        check(Set.of(8, 9, 10, 11), 4);
        check(Set.of(9), 0);
        doSystemStep(1);
        check(Set.of(9, 10, 11), 4);
        check(Set.of(), 0);
        system.pickUp(4, 9);
        system.pickUp(3, 4);
        system.pickUp(1, 3);
        system.pickUp(8, 6);
        system.pickUp(7, 3);
        check(Set.of(9), 1);
        check(Set.of(1, 3, 4), 2);
        check(Set.of(8,6,7,3),0);

    }

    private void check(final Set<Integer> expectedRoad,
                       final int id) {
        var actualRoad = system.getElevatorList().stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .map(Elevator::getRoad)
                .orElseThrow(RuntimeException::new);
        assertEquals(expectedRoad, actualRoad);

    }


    private void doSystemStep(final int n) {
        Stream.iterate(0, i -> i++)
                .limit(n)
                .forEach(i -> system.step());
    }
}
