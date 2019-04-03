package elevator.requests;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RequestQueue {
    private List<Request> requests = new ArrayList<>();

    public void add(final Request request) {
        if (!requests.contains(request)) requests.add(request);
    }

    public Optional<Request> get() {
        return size() > 0 ? Optional.of(requests.get(0)) : Optional.empty();
    }

    public Optional<Request> pop() {
        return size() > 0 ? Optional.of(requests.remove(0)) : Optional.empty();

    }

    public int size() {
        return requests.size();
    }
}
