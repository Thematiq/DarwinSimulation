package app.windows;

import engine.handlers.Simulation;
import engine.observers.IObserverSimulationStatistics;
import engine.tools.*;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.net.URISyntaxException;

/**
 * Controller for single simulation window
 * @author Mateusz Praski
 */
public class singleController extends AbstractSimulatorController implements IObserverSimulationStatistics {
    AnimalStatistician localStat;
    SimulationStatistician stat;
    Simulation sim;
    Genome dominant;
    XYChart.Series<Number, Number> population;
    Timeline timeline;
    XYChart.Series<Number, Number> vegetation;
    ObservableList<PieChart.Data> pieChardData;
    boolean canGenerate = false;
    boolean generating = false;
    int from;
    int to;

    @FXML
    private Label labelPop;
    @FXML
    private Label labelDay;
    @FXML
    private Label labelCurrentPop;
    @FXML
    private Label labelCurrentVeg;
    @FXML
    private Label labelCurrentGrave;
    @FXML
    private Label labelCurrentEnergy;
    @FXML
    private Label labelCurrentChildren;
    @FXML
    private Label labelCurrentSpan;
    @FXML
    private Label labelCurrentGene;
    @FXML
    private Label labelDayOfDeath;
    @FXML
    private Label labelDescendants;
    @FXML
    private Label labelChildren;
    @FXML
    private Label labelDesc;
    @FXML
    private Label labelGenerate;
    @FXML
    private TextField textEpoch;
    @FXML
    private TextField textStatEpoch;
    @FXML
    private TextField textFilename;
    @FXML
    private RadioButton radioDisplayDominant;
    @FXML
    private Button buttonStart;
    @FXML
    private Button buttonStop;
    @FXML
    private Button buttonNextDay;
    @FXML
    private Button buttonGenerate;
    @FXML
    private LineChart<Number, Number> chartOne;
    @FXML
    private PieChart chartGene;
    @FXML
    private Canvas canvasSim;
    @FXML
    private GridPane gridCanvas;

    public singleController() throws URISyntaxException { }

    private void listenCanvasResize(ObservableValue<? extends Number> observableValue, Number number, Number number1) {
        this.canvasSim.setHeight(this.gridCanvas.getHeight());
        this.canvasSim.setWidth(this.gridCanvas.getWidth());
        this.initDrawer();
        super.draw(this.sim, this.canvasSim, this.dominant);
    }

    private void listenText(Observable observable) {
        this.canGenerate = !this.textStatEpoch.getText().equals("") && !this.textFilename.getText().equals("");
        this.buttonGenerate.setDisable(!this.canGenerate);
    }

    private void setStatus(boolean status) {
        this.buttonStart.setDisable(status);
        this.buttonStop.setDisable(!status);
        this.buttonNextDay.setDisable(status);
        this.buttonGenerate.setDisable(status || !this.canGenerate || this.generating);
        if(status) {
            this.timeline.play();
            if (this.watcherRunning()) {
                this.labelDesc.setText(super.watchingRunning);
            } else {
                this.labelDesc.setText(super.Running);
            }
        } else {
            this.timeline.stop();
            if (this.watcherRunning()) {
                this.labelDesc.setText(super.watchingPaused);
            } else {
                this.labelDesc.setText(super.Paused);
            }
        }
    }

    boolean getStatus() {
        return this.buttonStart.isDisabled();
    }

    @FXML
    private void newFileStats() {
        this.generating = true;
        this.buttonGenerate.setDisable(true);
        this.from = this.sim.getDay();
        this.to = Integer.parseInt(this.textStatEpoch.getText()) + this.from;
        this.labelGenerate.setText(super.gathering);
    }

    @FXML
    private void toggleDisplayDominant() {
        super.drawDominant = this.radioDisplayDominant.isSelected();
        super.draw(this.sim, this.canvasSim, this.dominant);
    }

    @FXML
    private void startSimulation() {
        this.setStatus(true);
    }

    @FXML
    private void stopSimulation() {
        this.setStatus(false);
    }

    @FXML
    private void nextDaySimulation() { this.sim.nextDay(); }

    @FXML
    private void canvasClick(MouseEvent e) {
        Vector pos = new Vector((int) (e.getX() / this.cellSize), (int) (e.getY() / this.cellSize));
        int endDate = this.getEndDate();
        if (!this.running() && this.sim.animalAt(pos) != null && endDate != -1) {
            System.out.println(endDate);
            this.localStat = new AnimalStatistician(this.sim, this.sim.animalAt(pos), endDate);
            this.localStat.addIObserverAnimalStatistics(this);
            this.newData(this.localStat);
        }
    }

    boolean running() {
        return this.buttonStart.isDisabled();
    }

    boolean watcherRunning() {
        return this.localStat != null && this.localStat.isRunning();
    }

    private int getEndDate() {
        if (this.textEpoch.getText().equals("")) {
            return -1;
        } else {
            return Integer.parseInt(this.textEpoch.getText());
        }
    }

    private void initCharts() {
        this.population = new XYChart.Series<>();
        this.vegetation = new XYChart.Series<>();
        this.population.setName("Total animals");
        this.vegetation.setName("Total plants");
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
        this.chartGene.setData(this.pieChardData);
    }

    private void initDrawer() {
        super.cellSize = (int) (Math.min(this.canvasSim.getWidth(), this.canvasSim.getHeight()) /
                Math.max(this.params.width, this.params.height));
        this.canvasSim.setHeight(this.cellSize * this.params.height);
        this.canvasSim.setWidth(this.cellSize * this.params.width);
    }

    @Override
    public void newData(AnimalStatistician caller) {
        this.labelChildren.setText(String.valueOf(caller.getTotalChildren()));
        this.labelDescendants.setText(String.valueOf(caller.getTotalDescendants()));
        if (caller.hasDied()) {
            this.labelDayOfDeath.setText(String.valueOf(caller.getDeathDay()));
        } else if(caller.isRunning()){
            if (this.running()) {
                this.labelDesc.setText(super.watchingRunning);
            } else {
                this.labelDesc.setText(super.watchingPaused);
            }
            this.labelDayOfDeath.setText(super.stillLiving);
        } else {
            if (this.running()) {
                this.labelDesc.setText(super.Running);
            } else {
                this.labelDesc.setText(super.Paused);
            }
            this.labelDayOfDeath.setText(super.watcherEnded);
        }
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

        this.labelCurrentPop.setText(String.valueOf(this.stat.getCurrentAnimals()));
        this.labelCurrentVeg.setText(String.valueOf(this.stat.getCurrentVegetation()));
        this.labelCurrentGrave.setText(String.valueOf(this.stat.getCurrentGraveyard()));
        this.labelCurrentEnergy.setText(String.valueOf(this.stat.getCurrentEnergy()));
        this.labelCurrentSpan.setText(String.valueOf(this.stat.getCurrentLifespan()));
        this.labelCurrentChildren.setText(String.valueOf(this.stat.getCurrentChildren()));
        this.labelCurrentGene.setText(String.valueOf(this.stat.getCurrentDominant()));

        this.dominant = this.stat.getCurrentDominant();
        super.draw(this.sim, this.canvasSim, this.dominant);
        if (this.generating && this.to == caller.getCurrentDay()) {
            Stats export = caller.getStats(this.from, this.to);
            this.generating = false;
            JSONIO.writeStats(export, this.textFilename.getText());
            this.setStatus(this.getStatus());
            this.labelGenerate.setText(super.saved);
        }
    }

    @Override
    public void initSimulation(Parameters param) {
        super.params = param;
        this.initCharts();
        this.initDrawer();
        this.textEpoch.setTextFormatter(new TextFormatter<>(super::numericChange));
        this.textStatEpoch.setTextFormatter(new TextFormatter<>(super::numericChange));
        this.textFilename.setTextFormatter(new TextFormatter<>(super::filenameChange));
        this.gridCanvas.widthProperty().addListener(this::listenCanvasResize);
        this.gridCanvas.heightProperty().addListener(this::listenCanvasResize);
        this.textFilename.textProperty().addListener(this::listenText);
        this.textStatEpoch.textProperty().addListener(this::listenText);

        this.sim = new Simulation(this.params);
        this.stat = new SimulationStatistician(this.sim);
        this.stat.addIObserverStatistics(this);
        this.sim.addNewAllKillsObserver(this.stat);

        this.timeline = new Timeline(new KeyFrame(Duration.millis(super.dayLength), e -> this.sim.nextDay()));
        this.timeline.setCycleCount(Animation.INDEFINITE);
        this.setStatus(false);

        this.listenCanvasResize(null, null, null);
        this.sim.zeroDay();
    }
}
