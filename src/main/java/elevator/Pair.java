package elevator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class Pair<K,V> {
    private final K first;
    private final V second;
}
