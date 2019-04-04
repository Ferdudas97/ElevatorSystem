package elevator;

import elevator.observable.Observable;
import elevator.requests.Request;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static elevator.Direction.*;
import static elevator.ElevatorUtills.MAX_PRIORITY;

@Getter
public class Elevator extends Observable {
    private final Integer id;
    private Integer currentFloor;
    private Direction direction = NONE;
    private Set<Request> requests = new HashSet<>();
    private boolean isGoingInOppositeDirection = false;
    private Integer targetFloor;

    private Elevator(Integer id, Integer currentFloor) {
        this.id = id;
        this.currentFloor = currentFloor;
        this.targetFloor = currentFloor;
    }

    public Set<Integer> getRoad() {
        return requests.stream()
                .map(Request::getFrom)
                .collect(Collectors.toSet());
    }

    public void update(final int currentFloor, final int targetFloor) {
        clearData(currentFloor);
        pickup(Request.of(targetFloor, Direction.of(targetFloor - currentFloor)));
    }

    public Integer getTargetFloor() {
        if (direction.equals(NONE)) return currentFloor;
        else return targetFloor;
    }

    public static Elevator of(final Integer id, final Integer currentLevel) {
        return new Elevator(id, currentLevel);
    }

    public void pickup(final Request request) {
        if (!request.getDirection().equals(direction) && !direction.isNone()) return;
        if (request.getFrom().equals(currentFloor)) return;
        setTargetFloor(request);
        setDirection(request);
        if (!request.getFrom().equals(currentFloor)) requests.add(request);

    }

    private void setDirection(final Request request) {
        if (direction.isNone()) {
            if (request.getDirection().isUp() && currentFloor > request.getFrom()) {
                direction = DOWN;
                isGoingInOppositeDirection = true;
            } else if (request.getDirection().isDown() && currentFloor < request.getFrom()) {
                direction = UP;
                isGoingInOppositeDirection = true;
            } else direction = request.getDirection();
        }

    }

    private void setTargetFloor(final Request request) {
        if (direction.isUp()) {
            if (isGoingInOppositeDirection) {
                if (request.getFrom() < targetFloor) targetFloor = request.getFrom();
            } else if (request.getFrom() > targetFloor) targetFloor = request.getFrom();
        } else if (direction.isDown()) {
            if (isGoingInOppositeDirection) {
                if (request.getFrom() > targetFloor) targetFloor = request.getFrom();
            } else if (request.getFrom() < targetFloor) targetFloor = request.getFrom();
        } else targetFloor = request.getFrom();
    }

    public void pickup(final int from, final int direction) {
        pickup(Request.of(from, Direction.of(direction)));
    }

    public void step() {

        move();
        removeCompletedRequest();
        if (requests.size() == 0) {
            onCompleteAllRequests();
        }
    }

    public Integer priority(final Request request) {
        if (!direction.isNone() && !isGoingInOppositeDirection) {
            if (isBeetwenCurrentFloorAndTargetFloor(request)) return MAX_PRIORITY;
            else if (direction.equals(request.getDirection()))
                return MAX_PRIORITY - distance(request.getFrom(), targetFloor);
        }
        if (isGoingInOppositeDirection) return -distance(request.getFrom(), targetFloor);
        return MAX_PRIORITY - distance(request.getFrom(), currentFloor);
    }

    private void removeCompletedRequest() {
        if (isGoingInOppositeDirection) requests.remove(Request.of(currentFloor, direction.negative()));
        else requests.remove(Request.of(currentFloor, direction));

    }

    private void onCompleteAllRequests() {
        direction = NONE;
        targetFloor = currentFloor;
        isGoingInOppositeDirection = false;
        notifyObserver();
    }


    private void move() {
        if (direction.isUp()) ++currentFloor;
        else if (direction.isDown()) --currentFloor;
    }


    private Integer distance(final int x, final int y) {
        return x - y > 0 ? x - y : y - x;
    }

    public boolean canHandleThisRequest(final Request request) {
        if (isGoingInOppositeDirection) return canHandleItWhenGoingInOppositeSide(request);
        if (direction.equals(request.getDirection())) return true;
        if (direction.isNone()) return true;
        return false;
    }

    private boolean canHandleItWhenGoingInOppositeSide(final Request request) {
        if (requests.contains(request)) return true;
        else
            return isBeetwenCurrentFloorAndTargetFloor(Request.of(request.getFrom(),
                    request.getDirection().negative()));
    }

    private boolean isBeetwenCurrentFloorAndTargetFloor(final Request request) {
        if (request.getDirection().isUp() && currentFloor <= request.getFrom() && request.getFrom() <= targetFloor)
            return true;
        else if (request.getDirection().isDown() && currentFloor >= request.getFrom() && request.getFrom() >= targetFloor)
            return true;
        else return false;
    }

    private void clearData(final int currentFloor) {
        requests.clear();
        direction = NONE;
        targetFloor = currentFloor;
        this.currentFloor = currentFloor;
    }
}
