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
    private Integer currentLevel;
    private Direction direction = NONE;
    private Set<Request> requests = new HashSet<>();
    private boolean isGointInOppositeSite = false;
    private Integer targetFloor;

    private Elevator(Integer id, Integer currentLevel) {
        this.id = id;
        this.currentLevel = currentLevel;
        this.targetFloor = currentLevel;
    }

    public Set<Integer> getRoad() {
        return requests.stream()
                .map(Request::getFrom)
                .collect(Collectors.toSet());
    }

    public void update(final int currentFloor, final int targetFloor) {
        clearData();
        this.currentLevel = currentFloor;
        pickup(Request.of(targetFloor, Direction.of(targetFloor - currentFloor)));
    }

    public Integer getTargetFloor() {
        if (direction.equals(NONE)) return currentLevel;
        else return targetFloor;
    }

    public static Elevator of(final Integer id, final Integer currentLevel) {
        return new Elevator(id, currentLevel);
    }

    public void pickup(final Request request) {
        if (!request.getDirection().equals(direction) && !direction.isNone()) return;
        if (request.getFrom().equals(currentLevel)) return;
        setTargetFloor(request);
        setDirection(request);
        if (!request.getFrom().equals(currentLevel)) requests.add(request);

    }

    private void setDirection(final Request request) {
        if (direction.isNone()) {
            if (request.getDirection().isUp() && currentLevel > request.getFrom()) {
                direction = DOWN;
                isGointInOppositeSite = true;
            } else if (request.getDirection().isDown() && currentLevel < request.getFrom()) {
                direction = UP;
                isGointInOppositeSite = true;
            } else direction = request.getDirection();
        }

    }

    private void setTargetFloor(final Request request) {
        if (direction.isUp()) {
            if (isGointInOppositeSite) {
                if (request.getFrom() < targetFloor) targetFloor = request.getFrom();
            } else if (request.getFrom() > targetFloor) targetFloor = request.getFrom();
        } else if (direction.isDown()) {
            if (isGointInOppositeSite) {
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
        if (!direction.isNone() && !isGointInOppositeSite) {
            if (isBeetwenCurrentFloorAndTargetFloor(request)) return MAX_PRIORITY;
            else if (direction.equals(request.getDirection()))
                return MAX_PRIORITY - distance(request.getFrom(), targetFloor);
        }
        if (isGointInOppositeSite) return -distance(request.getFrom(), targetFloor);
        return MAX_PRIORITY - distance(request.getFrom(), currentLevel);
    }

    private void removeCompletedRequest() {
        if (isGointInOppositeSite) requests.remove(Request.of(currentLevel, direction.negative()));
        else requests.remove(Request.of(currentLevel, direction));

    }

    private void onCompleteAllRequests() {
        direction = NONE;
        targetFloor = currentLevel;
        isGointInOppositeSite = false;
        notifyObserver();
    }


    private void move() {
        if (direction.isUp()) ++currentLevel;
        else if (direction.isDown()) --currentLevel;
    }


    private Integer distance(final int x, final int y) {
        return x - y > 0 ? x - y : y - x;
    }

    public boolean canHandleThisRequest(final Request request) {
        if (isGointInOppositeSite) return canHandleItWhenGoingInOppositeSide(request);
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
        if (request.getDirection().isUp() && currentLevel <= request.getFrom() && request.getFrom() <= targetFloor)
            return true;
        else if (request.getDirection().isDown() && currentLevel >= request.getFrom() && request.getFrom() >= targetFloor)
            return true;
        else return false;
    }

    private void clearData() {
        requests.clear();
        direction = NONE;
        targetFloor = 0;
        currentLevel = 0;
    }
}
