package elevator.requests;

import elevator.Direction;
import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor(staticName = "of")
@Value
public class Request {
    private final Integer from;
    private final Direction direction;

}
