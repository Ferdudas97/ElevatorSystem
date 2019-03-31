package elevator;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(staticName = "of")
public class ElevatorStatus {
    private final Integer id;
    private final Integer currentLevel;
    private final Integer targetLevel;
}
