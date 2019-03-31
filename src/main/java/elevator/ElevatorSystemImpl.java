package elevator;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter(value = AccessLevel.PACKAGE)
@AllArgsConstructor(staticName = "of")
public class ElevatorSystemImpl implements ElevatorSystem {

    private final List<Elevator> elevatorList;

    public Set<ElevatorStatus> status() {
        return elevatorList.stream()
                .map(e -> ElevatorStatus.of(e.getId(), e.getCurrentLevel(), e.getTargetLevel()))
                .collect(Collectors.toSet());
    }

    public void step() {
        elevatorList.forEach(Elevator::step);
    }

    public void update(Integer elevatorId, Integer currentLevel, Integer targetLevel) {
        elevatorList.get(elevatorId).setTargetLevel(targetLevel);
    }

    public void pickUp(Integer currentLevel, Integer targetLevel) {
        val direction = ElevatorUtills.getDirection(targetLevel, currentLevel);
        Predicate<Elevator> filter = e -> e.getDirection().equals(Direction.NONE) ||
                ((e.checkIfIsInRoad(targetLevel) || e.checkIfIsInRoad(currentLevel))
                        || direction.equals(e.getDirection()));

        val elevators = elevatorList.stream()
                .filter(filter)
                .collect(Collectors.toList());
        val elevator = elevators.stream()
                .reduce((e1, e2) -> getElevatorWithShorterDistance(e1, e2, currentLevel, targetLevel));

        elevator.ifPresent(elevator1 -> elevator1.pickup(currentLevel, targetLevel));
    }

    private Elevator getElevatorWithShorterDistance(final Elevator elevator1,
                                                    final Elevator elevator2,
                                                    final Integer currentLevel,
                                                    final Integer targetLevel) {
        val diff1 = elevator1.distanceToGo(Pair.of(currentLevel,targetLevel)) ;
        val diff2 = elevator2.distanceToGo(Pair.of(currentLevel,targetLevel));
        return diff1 < diff2 ? elevator1 : elevator2;
    }


    private double distance(final double x1, final double x2, final double y1, final double y2) {
        return x1-y1 +x2-y2;
    }
}
