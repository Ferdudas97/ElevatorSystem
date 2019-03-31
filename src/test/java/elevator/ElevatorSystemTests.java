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
    private final ElevatorSystemImpl system;

    ElevatorSystemTests() {
        val elevators = Stream.iterate(0, i -> i++)
                .limit(5)
                .map(i -> Elevator.of(i, 0, new ArrayList<>()))
                .collect(Collectors.toList());
        this.system = ElevatorSystemImpl.of(elevators);
    }

    @Test
    void stepTests() {

        system.pickUp(2, 4);
        system.pickUp(3, 8);
        test(system, List.of(8, 4, 2, 3), 1);
        doSystemStep(system, 4);
        test(system, List.of(8), 1);
        system.pickUp(1, 6);
        system.pickUp(9, 10);
        test(system, List.of(10, 9, 8), 1);
        test(system, List.of(6, 1), 1);
    }

    private void test(final ElevatorSystemImpl system,
                      final List<Integer> expectedRoad,
                      final int expectedCount) {
        var actualCount = getNumberOfElevatorsByRoad(system, expectedRoad);
        assertEquals(expectedCount, actualCount);

    }

    private Long getNumberOfElevatorsByRoad(final ElevatorSystemImpl system, final List<Integer> road) {
        return system.getElevatorList().stream()
                .filter(e -> e.getRoad().equals(road))
                .count();

    }

    private void doSystemStep(final ElevatorSystem system, final int n) {
        Stream.iterate(0, i -> i++)
                .limit(n)
                .forEach(i -> system.step());
    }
}
