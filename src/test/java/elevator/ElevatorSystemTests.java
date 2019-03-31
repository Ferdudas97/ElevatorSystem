package elevator;

import lombok.val;
import lombok.var;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
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

        system.pickUp(2, 4);
        system.pickUp(3, 8);
        test(List.of(3, 2), 1);
        doSystemStep(3);
        test(List.of(8, 4), 1);
        doSystemStep(1);
        test(List.of(8), 1);
        system.pickUp(1, 6);
        system.pickUp(9, 10);
        test(List.of(1), 1);
        test(List.of(9, 8), 1);
        doSystemStep(5);
        test(List.of(6), 1);
        test(List.of(10), 1);
        doSystemStep(1);
        test(List.of(), 5);
        system.pickUp(3, 5);
        system.pickUp(3, 8);
        system.pickUp(9, 5);
        system.pickUp(4, 6);
        test(List.of(9), 1);
        test(List.of(3, 3), 1);
        test(List.of(4), 1);
        test(List.of(), 2);
    }

    @Test
    void stepTest2() {
        val elevators = List.of(Elevator.of(0, 10),
                Elevator.of(1, 4),
                Elevator.of(2, 0),
                Elevator.of(3, 15),
                Elevator.of(4, 7));
        this.system = ElevatorSystemImpl.of(elevators);

        system.pickUp(8,11);
        system.pickUp(9,10);
        system.pickUp(10,9);
        check(List.of(8),4);
        check(List.of(9,9),0);
        doSystemStep(1);
        check(List.of(10),0);
        check(List.of(11),4);
        system.pickUp(4,9);
        system.pickUp(3,4);
        system.pickUp(1,3);
        system.pickUp(8,6);
        system.pickUp(7,3);
        check(List.of(9,8),1);
        check(List.of(7,3,1),2);

    }

    private void test(final List<Integer> expectedRoad,
                      final int expectedCount) {
        var actualCount = system.getElevatorList().stream()
                .filter(e -> e.getRoad().equals(expectedRoad))
                .count();

        assertEquals(expectedCount, actualCount);

    }

    private void check(final List<Integer> expectedRoad,
                       final int id) {
        var actualRoad = system.getElevatorList().stream().filter(e -> e.getId().equals(id))
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
