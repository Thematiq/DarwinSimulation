package app;

import engine.handlers.Simulation;
import engine.handlers.IObserverNewDay;
import engine.tools.Parameters;
import engine.tools.Vector;
import engine.objects.Animal;
import engine.tools.Algebra;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;


import java.net.URL;
import java.util.ResourceBundle;

public class singleController implements Initializable, IObserverNewDay {

    private Simulation sim;
    private boolean simulationRunning;
    private final int dayLength = 1000;
    private Parameters params;
    private int cellSize;
    private final Image animal = new Image("file:resources/animal.png");
    private final Image flower = new Image("file:resources/flower.png");
    private final Image icon = new Image("file:resources/icon.png");
    private Timeline timeline;
    private boolean grid = false;
    private XYChart.Series<Number, Number> population;
    private XYChart.Series<Number, Number> vegetation;


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
        if(status) {
            this.timeline.play();
        } else {
            this.timeline.stop();
        }
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

    @FXML
    private void canvasClick(MouseEvent e) {
        System.out.println("Cell X: " + (int)(e.getX() / this.cellSize) + " Cell Y: " + (int)(e.getY() / this.cellSize));
    }

    void init_chart() {
        this.population = new XYChart.Series<>();
        this.vegetation = new XYChart.Series<>();
        this.population.setName("No animals");
        this.vegetation.setName("No plants");
        this.chartOne.getData().addAll(this.vegetation, this.population);
        this.chartOne.setCreateSymbols(false);
    }

    void initSingleSimulation() {
        this.init_chart();
        this.params = new Parameters();
        this.init_drawer();
        this.sim = new Simulation(this.params);
        this.sim.addNewDayObserver(this);
        // Add timeline
        this.timeline = new Timeline(new KeyFrame(Duration.millis(200), e -> {
            this.sim.nextDay();
        }));
        this.timeline.setCycleCount(Animation.INDEFINITE);
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
        this.population.getData().add(new XYChart.Data<>(caller.getDay(), caller.getPopulation()));
        this.vegetation.getData().add(new XYChart.Data<>(caller.getDay(), caller.getVegetation()));
        this.labelDay.setText(String.valueOf(caller.getDay()));
        this.labelPopOne.setText(String.valueOf(caller.getPopulation()));
        this.run_drawer(this.canvasSim.getGraphicsContext2D());
    }

    void init_drawer() {
        this.cellSize = (int) (this.canvasSim.getWidth() / this.params.width);
        this.canvasSim.setHeight(this.cellSize * this.params.height);
        this.canvasSim.setWidth(this.cellSize * this.params.width);
    }

    void run_drawer(GraphicsContext gc) {
        // Draw map
        Animal an;
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
                    gc.drawImage(this.flower, this.cellSize * x, this.cellSize * y, this.cellSize, this.cellSize);
                }
                an = this.sim.animalAt(new Vector(x,y));
                if (an != null) {
                    this.draw_animal(gc, an);
                }
            }
        }

        if (this.grid) {
            gc.setLineWidth(1.0);
            gc.setStroke(Color.BLACK);
            for (int i = 0; i < params.width; ++i) {
                gc.strokeLine(this.cellSize * i, 0, this.cellSize * i, this.canvasSim.getHeight());
            }
            for (int i = 0; i < params.height; ++i) {
                gc.strokeLine(0, this.cellSize * i, this.canvasSim.getWidth(), this.cellSize * i);
            }
        }
    }

    void draw_animal(GraphicsContext gc, Animal a) {
        gc.save();
        gc.rotate(a.getOrient().getDegree());
        Vector pos = Algebra.rotateVector(a.getPos().mult(this.cellSize), a.getOrient().getDegree(), new Vector(this.cellSize/2, this.cellSize/2));
        gc.drawImage(this.animal, pos.x, pos.y, this.cellSize, this.cellSize);
        //Draw Energy bar
        gc.setLineWidth(8.0);
        gc.setStroke(Color.GREY);
        gc.strokeLine(pos.x, pos.y, pos.x + this.cellSize, pos.y);
        double energyPercent = ((double)a.getEnergy() / (this.params.startEnergy * 2));
        if (energyPercent > 1) {
            energyPercent = 1.0;
        }
        gc.setStroke(Color.BLUE);
        if (energyPercent > 0) {
            gc.strokeLine(pos.x, pos.y, pos.x + (this.cellSize * energyPercent), pos.y);
        }
        gc.restore();
    }
}
