package elevator;

import java.util.Set;

public interface ElevatorSystem {
   Set<ElevatorStatus> status();
   void step();
   void update(Integer elevatorId, Integer currentFloor, Integer targetFloor);
   void pickUp(Integer currentFloor, Integer direction);
}
