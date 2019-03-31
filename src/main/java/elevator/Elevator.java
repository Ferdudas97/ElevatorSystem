package elevator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static elevator.Direction.*;

@Getter
public class Elevator {
    private final Integer id;
    private Integer currentLevel;
    private List<Integer> road = new ArrayList<>();
    private List<Pair<Integer, Integer>> pickups = new ArrayList<>();
//    private Direction direction = NONE;

    private Elevator(Integer id, Integer currentLevel) {
        this.id = id;
        this.currentLevel = currentLevel;
    }

    public static Elevator of(final Integer id, final Integer currentLevel) {
        return new Elevator(id, currentLevel);
    }


    public Integer getTargetLevel() {
        return road.size() > 0 ? pickups.stream().mapToInt(Pair::getSecond).max().orElse(getCurrentTarget()) : currentLevel;
    }

    public Integer getCurrentTarget() {
        return road.size() > 0 ? road.get(0) : currentLevel;
    }

    public void step() {
        move();
        pickups.stream().filter(p -> p.getFirst().equals(currentLevel))
                .map(Pair::getSecond)
                .forEach(this::addToRoad);
        pickups = pickups.stream().filter(p -> !p.getFirst().equals(currentLevel))
                .collect(Collectors.toList());
        road = road.stream()
                .filter(l -> !l.equals(currentLevel))
                .collect(Collectors.toList());
    }

    public void setTargetLevel(final Integer level) {
        if (getCurrentTarget().equals(level)) return;
        road.add(0, level);
    }

    public void pickup(final Integer pickupLevel, final Integer targetLevel) {
        pickups.add(Pair.of(pickupLevel, targetLevel));
        addToRoad(pickupLevel);

    }


    private void addToRoad(final Integer level) {
        if (getDirection().equals(UP) && level > getCurrentTarget()) setTargetLevel(level);
        else if (getDirection().equals(DOWN) && level < getCurrentTarget()) setTargetLevel(level);
        else road.add(level);
    }

    private void move() {
        val direction = getDirection();
        if (direction.equals(UP)) ++currentLevel;
        else if (direction.equals(DOWN)) --currentLevel;
    }

    public Boolean checkIfIsInRoad(final Integer level) {
        if (road.contains(level)) return true;
        else if (getDirection().equals(UP) && currentLevel < level && getTargetLevel() > level) return true;
        else if (getDirection().equals(DOWN) && currentLevel > level && getTargetLevel() < level) return true;
        else return false;
    }

    public Direction getDirection() {
        return ElevatorUtills.getDirection(getCurrentTarget(), currentLevel);
    }
}
