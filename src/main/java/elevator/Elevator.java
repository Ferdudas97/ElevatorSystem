package elevator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.val;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;

import static elevator.Direction.*;

@Getter
@AllArgsConstructor(staticName = "of")
public class Elevator {
    private final Integer id;
    private Integer currentLevel;
    private List<Integer> road;

    private List<Integer> pickupLevels;
    private List<Integer> targetLevels;

    public Integer getTargetLevel() {
        return road.size() > 0 ? road.get(0) : currentLevel;
    }

    public void step() {
        move();
        road = road.stream().filter(l -> !l.equals(currentLevel))
                .collect(Collectors.toList());
    }

    public void setTargetLevel(final Integer level) {
        if (getTargetLevel().equals(level)) return;
        road.add(0, level);
    }

    public void pickup(final Integer currentLevel, final Integer level) {
        addToRoad(currentLevel);
        addToRoad(level);
    }
    private void addToPickups(final Integer level) {
        if (getDirection().equals(UP) && level > getTargetLevel()) setTargetLevel(level);
        else if (getDirection().equals(DOWN) && level < getTargetLevel()) setTargetLevel(level);
        else road.add(level);
    }
    private void addToRoad(final Integer level,final List<Integer> list) {
        if (getDirection().equals(UP) && level > getTargetLevel()) setTargetLevel(level);
        else if (getDirection().equals(DOWN) && level < getTargetLevel()) pickupLevels(level);
        else pickupLevels.add(level);
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
        return ElevatorUtills.getDirection(getTargetLevel(), currentLevel);
    }
}
