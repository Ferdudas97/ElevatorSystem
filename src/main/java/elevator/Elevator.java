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

    private Elevator(Integer id, Integer currentLevel) {
        this.id = id;
        this.currentLevel = currentLevel;
    }

    public Integer getTargetFloor() {
        if (direction.equals(NONE)) return currentLevel;
        else if(direction.equals(UP)) return road.stream().mapToInt(Integer::intValue).max().orElse(currentLevel);
        else if(direction.equals(DOWN)) return road.stream().mapToInt(Integer::intValue).min().orElse(currentLevel);
        else return currentLevel;
    }

    public static Elevator of(final Integer id, final Integer currentLevel) {
        return new Elevator(id, currentLevel);
    }

    public void pickup(final Request request) {
        if (direction.equals(NONE)){
            if(request.getDirection().equals(UP) && currentLevel>request.getFrom()) direction = DOWN;
            if (request.getDirection().equals(DOWN) && currentLevel<request.getFrom()) direction = UP;
            else direction = request.getDirection();
        }
        if (currentLevel.equals(request.getFrom())){
            road.add(request.getTo());
        } else {
            road.add(request.getFrom());
            road.add(request.getTo());
        }
    }

    public void pickup(final int from, final int to) {
        pickup(Request.of(from,to));
    }

    public void step() {

        move();
        road.remove(currentLevel);
        if (road.size() == 0) direction = NONE;
    }

    private void move() {
        if (direction.equals(UP)) ++currentLevel;
        else if (direction.equals(DOWN)) --currentLevel;
    }

    public Integer priority(final Request request) {
        if (request.getDirection().equals(direction) && request.getDirection().equals(UP)) {
            if (currentLevel<= request.getFrom() && request.getTo()<=getTargetFloor()){
                return ElevatorUtills.MAX_PRIORITY;
            }
            else if (currentLevel<= request.getFrom() && !(request.getTo()<=getTargetFloor())) {
                return ElevatorUtills.MAX_PRIORITY -(request.getTo() - getTargetFloor());
            }
            else return ElevatorUtills.MIN_PRIORITY + getTargetFloor() - request.getFrom();
        }
        else if (request.getDirection().equals(direction) && request.getDirection().equals(DOWN)) {
            if (currentLevel>= request.getFrom() && request.getTo()>=getTargetFloor()){
                return ElevatorUtills.MAX_PRIORITY;
            }
            else if (currentLevel>= request.getFrom() && !(request.getTo()>=getTargetFloor())) {
                return ElevatorUtills.MAX_PRIORITY  - (getTargetFloor() - request.getTo());
            }
            else return ElevatorUtills.MIN_PRIORITY - getTargetFloor() + request.getFrom();
        }
        if (direction.equals(NONE)) return -Math.abs(request.getFrom() - currentLevel);
        return ElevatorUtills.MIN_PRIORITY + Math.abs(request.getFrom() - getTargetFloor()) ;
    }
}
