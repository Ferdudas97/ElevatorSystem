package elevator;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.Set;

@Value
@AllArgsConstructor(staticName = "of")
public class ElevatorStatus {
    private final Integer id;
    private final Integer currentLevel;
    private final Integer targetLevel;
    private final Set<Integer> road;
}
