package engine.observers;

import engine.objects.Animal;
import engine.tools.Statistician;

public interface IObserverStatistics {
    void update(Statistician caller);
}
