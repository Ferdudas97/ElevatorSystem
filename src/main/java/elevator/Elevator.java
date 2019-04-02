package elevator;

import lombok.Getter;
import lombok.val;

import java.util.*;
import java.util.stream.Collectors;

import static elevator.Direction.*;

@Getter
public class Elevator {
    private final Integer id;
    private Integer currentLevel;
    private Direction direction = NONE;
    private Set<Integer> road = new HashSet<>();
    private boolean isGoingToFirstRequest = false;

    private Elevator(Integer id, Integer currentLevel) {
        this.id = id;
        this.currentLevel = currentLevel;
    }

    public void update(final Integer currentLevel, final int targetLevel) {

    }
    public Integer getTargetFloor() {
        if (direction.equals(NONE)) return currentLevel;
        else if (direction.equals(UP)) return road.stream().mapToInt(Integer::intValue).max().orElse(currentLevel);
        else if (direction.equals(DOWN)) return road.stream().mapToInt(Integer::intValue).min().orElse(currentLevel);
        else return currentLevel;
    }

    public static Elevator of(final Integer id, final Integer currentLevel) {
        return new Elevator(id, currentLevel);
    }

    public void pickup(final Request request) {
        if (direction.equals(NONE)) {
            if (request.getDirection().equals(UP) && currentLevel > request.getFrom()) {
                direction = DOWN;
                isGoingToFirstRequest = true;
            } else if (request.getDirection().equals(DOWN) && currentLevel < request.getFrom()) {
                direction = UP;
                isGoingToFirstRequest = true;
            } else direction = request.getDirection();
        }
        if (currentLevel.equals(request.getFrom())) {
            road.add(request.getTo());
        } else {
            road.add(request.getFrom());
            road.add(request.getTo());
        }
    }

    public void pickup(final int from, final int to) {
        pickup(Request.of(from, to));
    }

    public void step() {

        move();
        if (currentLevel.equals(getTargetFloor()) && isGoingToFirstRequest) {
            isGoingToFirstRequest = false;
            direction = direction.negative();
        }
        if (!isGoingToFirstRequest) road.remove(currentLevel);
        if (road.size() == 0) direction = NONE;
    }

    private void move() {
        if (direction.equals(UP)) ++currentLevel;
        else if (direction.equals(DOWN)) --currentLevel;
    }

    public Integer priority(final Request request) {
        if (isGoingToFirstRequest) return ElevatorUtills.MIN_PRIORITY;
        if (request.getDirection().equals(direction) && request.getDirection().equals(UP)) {
            return priorityWhenGoingUp(request);
        } else if (request.getDirection().equals(direction) && request.getDirection().equals(DOWN)) {
            return priorityWhenGoingDown(request);
        }
        if (direction.equals(NONE)) return ElevatorUtills.MAX_PRIORITY - distance(request.getFrom(), currentLevel) - 1;
        return ElevatorUtills.MIN_PRIORITY + distance(request.getFrom(), getTargetFloor());
    }


    private Integer priorityWhenGoingUp(final Request request) {
        if (currentLevel <= request.getFrom() && request.getTo() <= getTargetFloor()) {
            return ElevatorUtills.MAX_PRIORITY - distance(currentLevel, request.getFrom());
        } else if (currentLevel <= request.getFrom() && !(request.getTo() <= getTargetFloor())) {
            return ElevatorUtills.MAX_PRIORITY - distance(request.getTo(), getTargetFloor());
        } else return ElevatorUtills.MIN_PRIORITY + distance(getTargetFloor(), request.getFrom());
    }

    private Integer priorityWhenGoingDown(final Request request) {
        if (currentLevel >= request.getFrom() && request.getTo() >= getTargetFloor()) {
            return ElevatorUtills.MAX_PRIORITY - distance(currentLevel, request.getFrom());
        } else if (currentLevel >= request.getFrom() && !(request.getTo() >= getTargetFloor())) {
            return ElevatorUtills.MAX_PRIORITY - distance(getTargetFloor(), request.getTo());
        } else return ElevatorUtills.MIN_PRIORITY + distance(getTargetFloor(), request.getFrom());
    }

    private Integer distance(final int from, final int to) {
        return Math.abs(to - from);
    }
}
