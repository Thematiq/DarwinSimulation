package app.windows;

import engine.handlers.Simulation;
import engine.objects.Animal;
import engine.observers.IObserverAnimalStatistics;
import engine.observers.IObserverSimulationStatistics;
import engine.tools.*;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.net.URISyntaxException;
import java.util.regex.Pattern;

/**
 * Abstract class for simulation controllers
 * Handlers map drawing
 * @author Mateusz Praski
 */
public abstract class AbstractSimulatorController implements IObserverSimulationStatistics, IObserverAnimalStatistics {
    final Pattern numericPattern = Pattern.compile("^([1-9][0-9]*)?$");
    final Pattern filenamePattern = Pattern.compile("^([^<>:;,?\"*|/]+)?$");
    final Image animal = new Image(getClass().getResource("resources/animal.png").toURI().toString());
    final Image flower = new Image(getClass().getResource("resources/flower.png").toURI().toString());
    final String stillLiving = "Haven't died yet";
    final String watcherEnded = "Didn't die during watching";
    final String watchingRunning = "Watching / Please stop simulation to choose new animal";
    final String watchingPaused = "Watching / Click on map to choose new animal";
    final String paused = "Click on map to choose new animal";
    final String running = "Please stop simulation to choose new animal";
    final String gathering = "Waiting for the end of the period";
    final String saved = "Saved";
    final boolean grid = false;
    final int dayLength = 200;

    Parameters params;
    boolean drawDominant = false;
    int cellSize;

    protected AbstractSimulatorController() throws URISyntaxException { }

    Change numericChange(Change change) {
        if (numericPattern.matcher(change.getControlNewText()).matches()) {
            return change;
        } else {
            return null;
        }
    }

    Change filenameChange(Change change) {
        if (filenamePattern.matcher(change.getControlNewText()).matches()) {
            return change;
        } else {
            return null;
        }
    }

    /**
     * Prepares window with given parameters
     * @param param Simulation parameters
     */
    public abstract void initSimulation(Parameters param);

    /**
     * IObserverSimulationStatistics handler
     * @param caller Calling statistician
     */
    public abstract void update(SimulationStatistician caller);


    /**
     * IObserverAnimalStatistic handler
     * @param caller calling statistician
     */
    public abstract void newData(AnimalStatistician caller);

    /**
     * Draws map
     * @param sim Simulation object
     * @param can Canvas
     * @param dom Dominating genome in the simulation
     */
    void draw(Simulation sim, Canvas can, Genome dom) {
        GraphicsContext gc = can.getGraphicsContext2D();
        this.drawMap(gc, sim);

        Animal an;
        for (int x = 0; x < params.width ; ++x) {
            for (int y = 0; y < params.height; ++y) {
                if (sim.isGrass(new Vector(x, y))) {
                    gc.drawImage(this.flower, this.cellSize * x, this.cellSize * y, this.cellSize, this.cellSize);
                }
                an = sim.animalAt(new Vector(x,y));
                if (an != null) {
                    this.drawAnimal(gc, an, an.getGenes().equals(dom));
                }
            }
        }
        if (this.grid) {
            this.drawGrid(gc, can);
        }
    }

    /**
     * Draws empty map
     * @param gc Graphics context
     * @param sim Simulation object
     */
    void drawMap(GraphicsContext gc, Simulation sim) {
        gc.setFill(Color.DARKORANGE);
        gc.fillRect(0, 0, this.cellSize * params.width, this.cellSize * params.height);
        gc.setFill(Color.DARKGREEN);
        int[] jungleSize = sim.jungleSize();
        for (int i = 0; i < 4; ++i) {
            jungleSize[i] *= this.cellSize;
        }
        gc.fillRect(jungleSize[0], jungleSize[1], jungleSize[2], jungleSize[3]);
        gc.setFill(Color.GREEN);
    }

    /**
     * Draws grid
     * @param gc Graphics context
     * @param can canvas
     */
    void drawGrid(GraphicsContext gc, Canvas can) {
        gc.setLineWidth(1.0);
        gc.setStroke(Color.BLACK);
        for (int i = 0; i < params.width; ++i) {
            gc.strokeLine(this.cellSize * i, 0, this.cellSize * i, can.getHeight());
        }
        for (int i = 0; i < params.height; ++i) {
            gc.strokeLine(0, this.cellSize * i, can.getWidth(), this.cellSize * i);
        }
    }

    /**
     * Draws Animal
     * @param gc Graphics context
     * @param a Animal
     * @param dominant Dominating genome
     */
    void drawAnimal(GraphicsContext gc, Animal a, boolean dominant) {
        gc.save();
        gc.rotate(a.getOrient().getDegree());

        Vector pos = Algebra.rotateVector(a.getPos().mult(this.cellSize), a.getOrient().getDegree(), new Vector(this.cellSize/2, this.cellSize/2));
        gc.drawImage(this.animal, pos.x, pos.y, this.cellSize, this.cellSize);
        double energyPercent = ((double)a.getEnergy() / (this.params.startEnergy * 2));
        if (energyPercent > 1) {
            energyPercent = 1.0;
        }
        this.drawEnergyBar(gc, pos, energyPercent, dominant && this.drawDominant);
        gc.restore();
    }

    /**
     * Draws energy bar
     * @param gc Graphics context
     * @param pos Energy bar position
     * @param energyPercent Percentage of the bar filled
     * @param dominant does this object has dominating genome
     */
    void drawEnergyBar(GraphicsContext gc, Vector pos, double energyPercent, boolean dominant) {
        gc.setLineWidth(8.0);
        gc.setStroke(Color.GREY);
        gc.strokeLine(pos.x, pos.y, pos.x + this.cellSize, pos.y);
        if (dominant) {
            gc.setStroke(Color.RED);
        } else {
            gc.setStroke(Color.BLUE);
        }
        if (energyPercent > 0) {
            gc.strokeLine(pos.x, pos.y, pos.x + (this.cellSize * energyPercent), pos.y);
        }
    }
}
