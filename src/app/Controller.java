package app;

import engine.handlers.Simulation;
import engine.handlers.IObserverNewDay;
import engine.tools.Orientation;
import engine.tools.Parameters;
import engine.tools.Vector;
import engine.objects.Animal;
import engine.tools.Algebra;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable, IObserverNewDay {

    private SimulationController contr;
    private Simulation sim;
    private boolean simulationRunning;
    private final int dayLength = 1000;
    private Parameters params;
    private int cellSize;
    private final Image animal = new Image("file:resources/animal.png");


    @FXML
    private Label labelPopOne;
    @FXML
    private Label labelPopTwo;
    @FXML
    private Label labelDay;
    @FXML
    private Label labelTextTwo;
    @FXML
    private Label labelTextPopTwo;
    @FXML
    private Button buttonStart;
    @FXML
    private Button buttonReset;
    @FXML
    private Button buttonStop;
    @FXML
    private Button buttonNextDay;
    @FXML
    private LineChart<Number, Number> chartOne;
    @FXML
    private LineChart<Number, Number> chartTwo;
    @FXML
    private Canvas canvasSim;

    private void setStatus(boolean status) {
        this.buttonStart.setDisable(status);
        this.buttonStop.setDisable(!status);
        this.buttonNextDay.setDisable(status);
        this.buttonReset.setDisable(status);
    }

    @FXML
    private void startSimulation() {
        this.setStatus(true);
    }

    @FXML
    private void resetSimulation() {

    }

    @FXML
    private void stopSimulation() {
        this.setStatus(false);
    }

    @FXML
    private void nextDaySimulation() { this.sim.nextDay(); }

    void initSingleSimulation() {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        this.params = new Parameters();
        this.init_drawer();
        this.sim = new Simulation(this.params);
        this.sim.addNewDayObserver(this);
        this.setStatus(false);
        this.dayChanged(0, this.sim);

        // Set components for 2nd simulation invisible
        this.labelPopTwo.setVisible(false);
        this.labelTextPopTwo.setVisible(false);
        this.labelTextTwo.setVisible(false);
        this.chartTwo.setVisible(false);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.initSingleSimulation();
    }

    @Override
    public void dayChanged(int day, Simulation caller) {
        this.labelDay.setText(String.valueOf(caller.getDay()));
        this.labelPopOne.setText(String.valueOf(caller.getPopulation()));
        this.run_drawer(this.canvasSim.getGraphicsContext2D());
    }

    void init_drawer() {
        this.cellSize = (int) (this.canvasSim.getWidth() / this.params.width);
    }

    void run_drawer(GraphicsContext gc) {
        // Draw map
        Animal an;
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.DARKORANGE);
        gc.fillRect(0, 0, this.cellSize * params.width, this.cellSize * params.height);
        gc.setFill(Color.DARKGREEN);
        int[] jungleSize = this.sim.jungleSize();
        for(int i = 0; i < 4; ++i) {
            jungleSize[i] *= this.cellSize;
        }
        gc.fillRect(jungleSize[0], jungleSize[1], jungleSize[2], jungleSize[3]);
        gc.setFill(Color.GREEN);

        for(int x = 0; x < params.width ; ++x) {
            for(int y = 0; y < params.height; ++y) {
                if (this.sim.isGrass(new Vector(x, y))) {
                    gc.fillRoundRect(x * this.cellSize, y * this.cellSize, this.cellSize, this.cellSize,
                                     this.cellSize / 2, this.cellSize / 2);
                }
                an = this.sim.animalAt(new Vector(x,y));
                if (an != null) {
                    this.draw_animal(gc, an);
                }
            }
        }

        for(int i = 0; i < params.width; ++i) {
            gc.strokeLine(this.cellSize * i, 0, this.cellSize * i, this.canvasSim.getHeight());
        }
        for(int i = 0; i < params.height; ++i) {
            gc.strokeLine(0, this.cellSize * i, this.canvasSim.getWidth(), this.cellSize * i);
        }
    }

    void draw_animal(GraphicsContext gc, Animal a) {
        gc.save();
        gc.rotate(a.getOrient().getDegree());
        Vector pos = Algebra.rotateVector(a.getPos().mult(this.cellSize), a.getOrient().getDegree(), new Vector(this.cellSize/2, this.cellSize/2));
        gc.drawImage(this.animal, pos.x, pos.y, this.cellSize, this.cellSize);

        gc.restore();
    }
}
