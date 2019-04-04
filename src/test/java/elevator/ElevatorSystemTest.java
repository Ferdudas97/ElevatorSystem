package elevator;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


public class ElevatorSystemTest {
    private ElevatorSystemImpl system;


    @Test
    public void simulationTest1() {
        val elevators = Stream.iterate(1, i -> i + 1)
                .limit(5)
                .map(i -> Elevator.of(i, 0))
                .collect(Collectors.toList());
        this.system = ElevatorSystemImpl.of(elevators);

        system.pickUp(3, 1);
        system.pickUp(2, 1);

        assertRoadEquals(Set.of(2, 3), 5);
        doSystemStep(3);
        assertRoadEquals(Set.of(), 5);
        doSystemStep(1);

        system.pickUp(1, 1);
        system.pickUp(9, -1);

        assertRoadEquals(Set.of(1), 4);
        assertRoadEquals(Set.of(9), 5);
        doSystemStep(6);

        system.pickUp(4, 1);
        system.pickUp(8, 1);
        system.pickUp(8, -1);
        system.pickUp(3, -1);

        assertRoadEquals(Set.of(4), 4);
        assertRoadEquals(Set.of(8), 5);
        assertRoadEquals(Set.of(8), 3);
        assertRoadEquals(Set.of(3), 2);

        doSystemStep(3);
        assertRoadEquals(Set.of(), 5);
        assertRoadEquals(Set.of(), 4);
        assertRoadEquals(Set.of(8), 3);
        assertRoadEquals(Set.of(), 2);

        system.pickUp(6, 1);
        system.pickUp(8, -1);
        system.pickUp(4, -1);
        system.pickUp(3, 1);

        assertRoadEquals(Set.of(8), 3);
        assertRoadEquals(Set.of(6), 5);
        assertRoadEquals(Set.of(8), 4);
        assertRoadEquals(Set.of(3), 1);
        assertRoadEquals(Set.of(4), 2);
        doSystemStep(3);
        assertRoadEquals(Set.of(), 5);
        assertRoadEquals(Set.of(8), 3);
    }

    @Test
    public void simulationTest2() {
        val elevators = List.of(Elevator.of(0, 10),
                Elevator.of(1, 4),
                Elevator.of(2, 0),
                Elevator.of(3, 15),
                Elevator.of(4, 7));
        this.system = ElevatorSystemImpl.of(elevators);

        system.pickUp(8, 1);
        system.pickUp(9, -1);
        system.pickUp(4, 1);

        assertRoadEquals(Set.of(8), 4);
        assertRoadEquals(Set.of(9), 0);
        assertRoadEquals(Set.of(), 1);
        doSystemStep(1);
        assertRoadEquals(Set.of(), 4);
        assertRoadEquals(Set.of(), 0);

        system.pickUp(4, -1);
        system.pickUp(3, -1);
        system.pickUp(1, -1);
        system.pickUp(10, -1);
        system.pickUp(7, 1);

        assertRoadEquals(Set.of(3), 1);
        assertRoadEquals(Set.of(1), 2);
        assertRoadEquals(Set.of(10), 0);

    }

    @Test
    public void simulationTest3() {
        val elevators = List.of(Elevator.of(0, 5));
        this.system = ElevatorSystemImpl.of(elevators);

        system.pickUp(6, 1);
        system.pickUp(8, 1);
        system.pickUp(3, -1);
        system.pickUp(4, -1);
        system.pickUp(2, -1);

        assertRoadEquals(Set.of(6, 8), 0);
        assertEquals(3, system.getUnassignedRequests().size());
        doSystemStep(3);
        assertRoadEquals(Set.of(4, 3, 2), 0);

    }

    @Test
    public void simulationTest4() {
        val elevators = List.of(Elevator.of(0, 0));
        this.system = ElevatorSystemImpl.of(elevators);

        system.pickUp(5, 1);
        system.pickUp(9, 1);
        system.pickUp(8, -1);

        assertRoadEquals(Set.of(5, 9), 0);
        assertEquals(1, system.getUnassignedRequests().size());
        doSystemStep(9);
        assertRoadEquals(Set.of(8), 0);
        doSystemStep(1);
        assertRoadEquals(Set.of(), 0);
    }

    private void assertRoadEquals(final Set<Integer> expectedRoad,
                                  final int id) {
        val actualRoad = new HashSet<Integer>(system.getElevatorList()
                .stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .map(Elevator::getRoad)
                .get());

        assertEquals(expectedRoad, actualRoad);

    }


    private void doSystemStep(final int n) {
        Stream.iterate(0, i -> i++)
                .limit(n)
                .forEach(i -> system.step());
    }
}
