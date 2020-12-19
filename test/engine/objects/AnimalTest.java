package engine.objects;

import engine.observers.MockIObserverKilled;
import engine.observers.MockIObserverPositionChanged;
import engine.tools.Parameters;
import engine.tools.Vector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for Animal class
 * @author Mateusz Praski
 */
public class AnimalTest {
    private final Vector startingVector = new Vector(3, 3);
    private final Vector endingVector = new Vector(1, 1);
    private final Parameters params = new Parameters(
            10, 10, 100, 1,
            30, (float)0.5, 10
    );

    private Animal testSubject;

    @BeforeEach
    public void animalSpawner() {
        testSubject = new Animal(startingVector, params);
    }

    @Test
    public void moveTest() {
        MockIObserverPositionChanged test = new MockIObserverPositionChanged();
        testSubject.addPositionObserver(test);
        testSubject.move(endingVector);
        assertEquals(test.getValue(), 1);
        assertEquals(testSubject.getPos(), endingVector);
        assertEquals(testSubject.getLifespan(), 1);
        assertEquals(testSubject.getEnergy(), params.startEnergy - params.moveEnergy);
    }

    @Test
    public void killTest() {
        MockIObserverKilled test = new MockIObserverKilled();
        testSubject.addKilledObserver(test);
        testSubject.kill();
        assertEquals(test.getValue(), 1);
    }

    @Test
    public void energyTest() {
        testSubject.eat(10);
        assertEquals(testSubject.getEnergy(), 10 + params.startEnergy);
        testSubject.loseEnergy(5);
        assertEquals(testSubject.getEnergy(), 5 + params.startEnergy);
    }
}
