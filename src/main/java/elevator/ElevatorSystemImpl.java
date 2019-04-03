package elevator;

import elevator.observable.Observable;
import elevator.observable.Observer;
import elevator.requests.Request;
import elevator.requests.RequestQueue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.val;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter(value = AccessLevel.PACKAGE)
public class ElevatorSystemImpl implements ElevatorSystem, Observer {

    private final List<Elevator> elevatorList;
    private final RequestQueue unassignedRequests = new RequestQueue();

    private ElevatorSystemImpl(List<Elevator> elevatorList) {
        this.elevatorList = elevatorList;
        elevatorList.forEach(e -> e.subscribeObservable(this));
    }

    public static ElevatorSystemImpl of(List<Elevator> elevatorList) {
        return new ElevatorSystemImpl(elevatorList);
    }

    public Set<ElevatorStatus> status() {
        return elevatorList.stream()
                .map(e -> ElevatorStatus.of(e.getId(), e.getCurrentLevel(), e.getTargetFloor()))
                .collect(Collectors.toSet());
    }

    public void step() {
        elevatorList.forEach(Elevator::step);
    }

    public void update(Integer elevatorId, Integer currentFloor, Integer targetFloor) {
        elevatorList.get(elevatorId).update(currentFloor, targetFloor);
    }

    public void pickUp(Integer currentFloor, Integer direction) {
        val request = Request.of(currentFloor, Direction.of(direction));
        val elevator = elevatorList.stream()
                .filter(e -> e.canHandleThisRequest(request))
                .reduce((e1, e2) -> getElevatorWithBiggerPriority(e1, e2, request));

        elevator.ifPresentOrElse(elevator1 -> elevator1.pickup(request), () -> unassignedRequests.add(request));
    }


    private Elevator getElevatorWithBiggerPriority(final Elevator elevator1,
                                                   final Elevator elevator2,
                                                   final Request request) {
        return elevator1.priority(request) > elevator2.priority(request) ? elevator1 : elevator2;
    }

    @Override
    public void onNotify(Observable observable) {
        if (observable instanceof Elevator) {
            val elevator = (Elevator) observable;
            var request = unassignedRequests.get();
            while (request.isPresent() && elevator.canHandleThisRequest(request.get())) {
                unassignedRequests.pop().ifPresent(elevator::pickup);
                request = unassignedRequests.get();
            }

        }
    }
}
