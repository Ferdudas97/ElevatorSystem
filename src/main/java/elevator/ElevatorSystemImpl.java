package elevator;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter(value = AccessLevel.PACKAGE)
@AllArgsConstructor(staticName = "of")
public class ElevatorSystemImpl implements ElevatorSystem {

    private final List<Elevator> elevatorList;

    public Set<ElevatorStatus> status() {
//        return elevatorList.stream()
////                .map(e -> ElevatorStatus.of(e.getId(), e.getCurrentLevel(), e.getTargetLevel()))
//                .collect(Collectors.toSet());
        return null;
    }

    public void step() {
        elevatorList.forEach(Elevator::step);
    }

    public void update(Integer elevatorId, Integer currentLevel, Integer targetLevel) {
        elevatorList.get(elevatorId).update(currentLevel,targetLevel);
    }

    public void pickUp(Integer currentLevel, Integer targetLevel) {
        val request = Request.of(currentLevel, targetLevel);
        val elevator = elevatorList.stream()
                .reduce((e1, e2) -> getElevatorWithBiggerPriority(e1,e2,request));

        elevator.ifPresent(elevator1 -> elevator1.pickup(request));
    }

    private Elevator getElevatorWithBiggerPriority(final Elevator elevator1,
                                                   final Elevator elevator2,
                                                   final Request request) {
        return elevator1.priority(request) > elevator2.priority(request) ? elevator1 : elevator2;
    }


    private double distance(final double x1, final double x2, final double y1, final double y2) {
        return x1 - y1 + x2 - y2;
    }
}
