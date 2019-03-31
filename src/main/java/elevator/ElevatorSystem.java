package elevator;

import java.util.Map;
import java.util.Set;

public interface ElevatorSystem {
   Set<ElevatorStatus> status();
   void step();
   void update(Integer elevatorId, Integer currentLevel, Integer targetLevel);
   void pickUp(Integer currentLevel, Integer targetLevel);
}
