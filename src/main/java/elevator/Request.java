package elevator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class Request {
    private final Integer from;
    private final Integer to;
    private final Direction direction;

    private Request(Integer from, Integer to) {
        this.from = from;
        this.to = to;
        direction = ElevatorUtills.getDirection(to,from);
    }

    public static Request of(Integer from, Integer to) {
        return new Request(from, to);
    }
}
