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
        Predicate<Elevator> filter = e -> e.getDirection().equals(Direction.NONE) ||
                (e.checkIfIsInRoad(targetLevel) || e.checkIfIsInRoad(currentLevel));
        val elevator = elevatorList.stream()
                .filter(filter)
                .reduce((e1, e2) -> getElevatorWithShorterDistance(e1, e2, currentLevel, targetLevel));

        if (elevator.isPresent()) elevator.get().pickup(currentLevel, targetLevel);
        else {
//            elevatorList.stream().filter(e -> e.getTar)
        }
    }

    private Elevator getElevatorWithShorterDistance(final Elevator elevator1,
                                                    final Elevator elevator2,
                                                    final Integer currentLevel,
                                                    final Integer targetLevel) {
        val diff1 = Math.abs(elevator1.getCurrentLevel() - elevator1.getTargetLevel() - currentLevel + targetLevel);
        val diff2 = Math.abs(elevator2.getCurrentLevel() - elevator2.getTargetLevel() - currentLevel + targetLevel);
        return diff1 < diff2 ? elevator1 : elevator2;
    }

}
