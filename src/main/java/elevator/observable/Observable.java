package elevator.observable;

import java.util.ArrayList;
import java.util.List;

public abstract class Observable {
    protected List<Observer> observers = new ArrayList<>();

    public void notifyObserver() {
        observers.forEach(o -> o.onNotify(this));
    }

    public void subscribeObservable(Observer observer) {
        observers.add(observer);
    }

    public void unsubscribe(Observer observer) {
        observers.remove(observer);
    }
}
