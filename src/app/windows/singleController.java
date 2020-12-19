package app.windows;

import engine.handlers.Simulation;
import engine.observers.IObserverSimulationStatistics;
import engine.tools.Parameters;

import engine.tools.SimulationStatistician;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;


public class singleController extends AbstractSimulatorController implements IObserverSimulationStatistics {

    private SimulationStatistician stat;
    private Simulation sim;
    private XYChart.Series<Number, Number> population;
    private Timeline timeline;
    private XYChart.Series<Number, Number> vegetation;
    private ObservableList<PieChart.Data> pieChardData;

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
    private PieChart geneChart;
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

    void initCharts() {
        this.population = new XYChart.Series<>();
        this.vegetation = new XYChart.Series<>();
        this.population.setName("No animals");
        this.vegetation.setName("No plants");
        this.chartOne.getData().addAll(this.vegetation, this.population);
        this.chartOne.setCreateSymbols(false);
        this.pieChardData = FXCollections.observableArrayList(
                new PieChart.Data("0", 0),
                new PieChart.Data("1", 0),
                new PieChart.Data("2", 0),
                new PieChart.Data("3", 0),
                new PieChart.Data("4", 0),
                new PieChart.Data("5", 0),
                new PieChart.Data("6", 0),
                new PieChart.Data("7", 0)
        );
        this.geneChart.setData(this.pieChardData);
    }

    @Override
    public void update(SimulationStatistician caller) {
        this.population.getData().add(new XYChart.Data<>(this.stat.getCurrentDay(), this.stat.getCurrentAnimals()));
        this.vegetation.getData().add(new XYChart.Data<>(this.stat.getCurrentDay(), this.stat.getCurrentVegetation()));

        Integer[] currentPopularity = caller.getGenePopularity();
        for (int i = 0; i < 8; ++i) {
            this.pieChardData.get(i).setPieValue(currentPopularity[i]);
        }

        this.labelDay.setText(String.valueOf(this.stat.getCurrentDay()));
        this.labelPop.setText(String.valueOf(this.stat.getCurrentAnimals()));

        this.currentPopLabel.setText(String.valueOf(this.stat.getCurrentAnimals()));
        this.currentVegLabel.setText(String.valueOf(this.stat.getCurrentVegetation()));
        this.currentGraveLabel.setText(String.valueOf(this.stat.getCurrentGraveyard()));
        this.currentEngLabel.setText(String.valueOf(this.stat.getCurrentEnergy()));
        this.currentSpanLabel.setText(String.valueOf(this.stat.getCurrentLifespan()));
        this.currentChildLabel.setText(String.valueOf(this.stat.getCurrentChildren()));
        this.currentGeneLabel.setText(String.valueOf(this.stat.getCurrentDominant()));

        super.runDrawer(this.sim, this.canvasSim);
    }

    void initDrawer() {
        super.cellSize = (int) (Math.min(this.canvasSim.getWidth(), this.canvasSim.getHeight()) /
                                Math.max(this.params.width, this.params.height));
        this.canvasSim.setHeight(this.cellSize * this.params.height);
        this.canvasSim.setWidth(this.cellSize * this.params.width);
    }

    @Override
    public void initSimulation(Parameters param) {
        super.params = param;
        this.initCharts();
        this.initDrawer();
        this.sim = new Simulation(this.params);
        this.stat = new SimulationStatistician(this.sim);
        this.stat.addIObserverStatistics(this);
        this.sim.addNewAllKillsObserver(this.stat);
        // Add timeline
        this.timeline = new Timeline(new KeyFrame(Duration.millis(super.dayLength), e -> this.sim.nextDay()));
        this.timeline.setCycleCount(Animation.INDEFINITE);
        this.setStatus(false);
        this.sim.zeroDay();
    }
}
