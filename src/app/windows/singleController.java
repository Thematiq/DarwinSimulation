package app.windows;

import engine.handlers.Simulation;
import engine.observers.IObserverStatistics;
import engine.tools.Parameters;

import engine.tools.Statistician;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;


public class singleController extends AbstractSimulatorController implements IObserverStatistics {

    private Statistician stat;
    private Simulation sim;
    private XYChart.Series<Number, Number> population;
    private XYChart.Series<Number, Number> vegetation;
    @FXML
    private Label labelPop;
    @FXML
    private Label labelDay;
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
    private Canvas canvasSim;
    @FXML
    private Label currentPopLabel;
    @FXML
    private Label currentVegLabel;
    @FXML
    private Label currentGraveLabel;
    @FXML
    private Label currentEngLabel;
    @FXML
    private Label currentChildLabel;
    @FXML
    private Label currentSpanLabel;
    @FXML
    private Label currentGeneLabel;

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

    @Override
    public void update() {
        this.population.getData().add(new XYChart.Data<>(this.stat.getCurrentDay(), this.stat.getCurrentAnimals()));
        this.vegetation.getData().add(new XYChart.Data<>(this.stat.getCurrentDay(), this.stat.getCurrentVegetation()));

        this.labelDay.setText(String.valueOf(this.stat.getCurrentDay()));
        this.labelPop.setText(String.valueOf(this.stat.getCurrentAnimals()));

        this.currentPopLabel.setText(String.valueOf(this.stat.getCurrentAnimals()));
        this.currentVegLabel.setText(String.valueOf(this.stat.getCurrentVegetation()));
        this.currentGraveLabel.setText(String.valueOf(this.stat.getCurrentGraveyard()));
        this.currentEngLabel.setText(String.valueOf(this.stat.getCurrentEnergy()));
        this.currentSpanLabel.setText(String.valueOf(this.stat.getCurrentLifespan()));

        super.run_drawer(this.sim, this.canvasSim);
    }

    public void dayChanged(int day, Simulation caller) {
        this.population.getData().add(new XYChart.Data<>(caller.getDay(), caller.getPopulation()));
        this.vegetation.getData().add(new XYChart.Data<>(caller.getDay(), caller.getVegetation()));

        this.labelDay.setText(String.valueOf(caller.getDay()));
        this.labelPop.setText(String.valueOf(caller.getPopulation()));

        this.currentPopLabel.setText(String.valueOf(caller.getPopulation()));
        this.currentVegLabel.setText(String.valueOf(caller.getVegetation()));
        this.currentGraveLabel.setText(String.valueOf(caller.getGraveyard()));

        super.run_drawer(caller, this.canvasSim);
    }

    void init_drawer() {
        this.cellSize = (int) (this.canvasSim.getWidth() / this.params.width);
        this.canvasSim.setHeight(this.cellSize * this.params.height);
        this.canvasSim.setWidth(this.cellSize * this.params.width);
    }

    @Override
    public void initSimulation(Parameters param) {
        System.out.println(param);
        super.params = param;
        this.init_chart();
        this.init_drawer();
        this.sim = new Simulation(this.params);
        this.stat = new Statistician(this.sim);
        this.stat.addIObserverStatistics(this);
        // Add timeline
        this.timeline = new Timeline(new KeyFrame(Duration.millis(super.dayLength), e -> {
            this.sim.nextDay();
        }));
        this.timeline.setCycleCount(Animation.INDEFINITE);
        this.setStatus(false);
        this.dayChanged(0, this.sim);
    }
}
