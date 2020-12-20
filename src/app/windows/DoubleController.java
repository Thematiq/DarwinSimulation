package app.windows;

import engine.handlers.Simulation;
import engine.tools.AnimalStatistician;
import engine.tools.Genome;
import engine.tools.Parameters;
import engine.tools.SimulationStatistician;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class DoubleController extends AbstractSimulatorController {
    private Simulation simOne;
    private Simulation simTwo;
    private SimulationStatistician statOne;
    private SimulationStatistician statTwo;
    private Timeline timeOne;
    private Timeline timeTwo;
    private Genome domOne = new Genome();
    private Genome domTwo = new Genome();
    int cellSizeOne;
    int cellSizeTwo;
    boolean canGenerate = false;

    @FXML
    private Label labelDayOneBottom;
    @FXML
    private Label labelPopOneBottom;
    @FXML
    private Label labelDayTwoBottom;
    @FXML
    private Label labelPopTwoBottom;
    @FXML
    private Label labelPopOne;
    @FXML
    private Label labelVegOne;
    @FXML
    private Label labelGeneOne;
    @FXML
    private Label labelEnergyOne;
    @FXML
    private Label labelLifespanOne;
    @FXML
    private Label labelChildrenOne;
    @FXML
    private Label labelPopTwo;
    @FXML
    private Label labelVegTwo;
    @FXML
    private Label labelGeneTwo;
    @FXML
    private Label labelEnergyTwo;
    @FXML
    private Label labelLifespanTwo;
    @FXML
    private Label labelChildrenTwo;
    @FXML
    private Label labelChildren;
    @FXML
    private Label labelDescendants;
    @FXML
    private Label labelDayOfDeath;
    @FXML
    private Label labelDesc;
    @FXML
    private TextField textEpoch;
    @FXML
    private TextField textStatEpoch;
    @FXML
    private TextField textFilename;
    @FXML
    private RadioButton radioLeftSim;
    @FXML
    private RadioButton radioRightSim;
    @FXML
    private RadioButton radioDominantOne;
    @FXML
    private RadioButton radioDominantTwo;
    @FXML
    private Button buttonStartOne;
    @FXML
    private Button buttonStopOne;
    @FXML
    private Button buttonNextOne;
    @FXML
    private Button buttonStartTwo;
    @FXML
    private Button buttonStopTwo;
    @FXML
    private Button buttonNextTwo;
    @FXML
    private Canvas canvasOne;
    @FXML
    private Canvas canvasTwo;
    @FXML
    private GridPane gridCanvasOne;
    @FXML
    private GridPane gridCanvasTwo;

    void draw(Simulation sim, Canvas can, Genome dom, int cellSize) {
        super.cellSize = cellSize;
        super.draw(sim, can, dom);
    }

    private void listenCanvasOneResize(ObservableValue<? extends Number> observableValue, Number number, Number number1) {
        this.canvasOne.setHeight(this.gridCanvasOne.getHeight());
        this.canvasOne.setWidth(this.gridCanvasOne.getWidth());
        this.initDrawer();
        this.draw(this.simOne, this.canvasOne, this.domOne, this.cellSizeOne);
    }
    private void listenCanvasTwoResize(ObservableValue<? extends Number> observableValue, Number number, Number number1) {
        this.canvasTwo.setHeight(this.gridCanvasTwo.getHeight());
        this.canvasTwo.setWidth(this.gridCanvasTwo.getWidth());
        this.initDrawer();
        this.draw(this.simTwo, this.canvasTwo, this.domTwo, this.cellSizeTwo);
    }

    private void listenText(Observable observable) {
        this.canGenerate = !this.textStatEpoch.getText().equals("") && !this.textFilename.getText().equals("");
    }

    private void setSimOneStatus(boolean status) {
        this.buttonNextOne.setDisable(status);
        this.buttonStartOne.setDisable(status);
        this.buttonStopOne.setDisable(!status);
        if (status) {
            this.timeOne.play();
        } else {
            this.timeOne.stop();
        }
    }

    private void setSimTwoStatus(boolean status) {
        this.buttonNextTwo.setDisable(status);
        this.buttonStartTwo.setDisable(status);
        this.buttonStopTwo.setDisable(!status);
        if (status) {
            this.timeTwo.play();
        } else {
            this.timeTwo.stop();
        }
    }

    @FXML
    private void startOne() { this.setSimOneStatus(true); }

    @FXML
    private void stopOne() { this.setSimOneStatus(false); }

    @FXML
    private void nextOne() { this.simOne.nextDay(); }

    @FXML
    private void startTwo() { this.setSimTwoStatus(true); }

    @FXML
    private void stopTwo() { this.setSimTwoStatus(false); }

    @FXML
    private void nextTwo() { this.simTwo.nextDay(); }

    @Override
    public void newData(AnimalStatistician caller) {

    }

    void initDrawer() {
        this.cellSizeOne = (int) (Math.min(this.canvasOne.getWidth(), this.canvasOne.getHeight()) /
                                Math.max(this.params.width, this.params.height));
        this.cellSizeTwo = (int) (Math.min(this.canvasTwo.getWidth(), this.canvasTwo.getHeight()) /
                Math.max(this.params.width, this.params.height));
        this.canvasOne.setWidth(this.cellSizeOne * super.params.width);
        this.canvasOne.setHeight(this.cellSizeOne * super.params.height);
        this.canvasTwo.setWidth(this.cellSizeTwo * super.params.width);
        this.canvasTwo.setHeight(this.cellSizeTwo * super.params.height);
    }

    @Override
    public void update(SimulationStatistician caller) {
        if (caller.equals(this.statOne)) {
            this.labelPopOneBottom.setText(String.valueOf(caller.getCurrentAnimals()));
            this.labelDayOneBottom.setText(String.valueOf(caller.getCurrentDay()));

            this.labelPopOne.setText(String.valueOf(caller.getCurrentAnimals()));
            this.labelVegOne.setText(String.valueOf(caller.getCurrentVegetation()));
            this.labelEnergyOne.setText(String.valueOf(caller.getCurrentEnergy()));
            this.labelLifespanOne.setText(String.valueOf(caller.getCurrentLifespan()));

            this.domOne = caller.getCurrentDominant();
            this.draw(this.simOne, this.canvasOne, this.domOne, this.cellSizeOne);
        } else if (caller.equals(this.statTwo)) {
            this.labelPopTwoBottom.setText(String.valueOf(caller.getCurrentAnimals()));
            this.labelDayTwoBottom.setText(String.valueOf(caller.getCurrentDay()));

            this.labelPopTwo.setText(String.valueOf(caller.getCurrentAnimals()));
            this.labelVegTwo.setText(String.valueOf(caller.getCurrentVegetation()));
            this.labelEnergyTwo.setText(String.valueOf(caller.getCurrentEnergy()));
            this.labelLifespanTwo.setText(String.valueOf(caller.getCurrentLifespan()));

            this.domTwo = caller.getCurrentDominant();
            this.draw(this.simTwo, this.canvasTwo, this.domTwo, this.cellSizeTwo);
        }
    }

    @Override
    public void initSimulation(Parameters param) {
        super.params = param;
        this.initDrawer();
        this.textEpoch.setTextFormatter(new TextFormatter<>(super::numericChange));
        this.textStatEpoch.setTextFormatter(new TextFormatter<>(super::numericChange));
        this.textFilename.setTextFormatter(new TextFormatter<>(super::filenameChange));
        this.gridCanvasOne.heightProperty().addListener(this::listenCanvasOneResize);
        this.gridCanvasOne.widthProperty().addListener(this::listenCanvasOneResize);
        this.gridCanvasTwo.heightProperty().addListener(this::listenCanvasTwoResize);
        this.gridCanvasTwo.widthProperty().addListener(this::listenCanvasTwoResize);
        this.textFilename.textProperty().addListener(this::listenText);
        this.textStatEpoch.textProperty().addListener(this::listenText);

        this.simOne = new Simulation(super.params);
        this.simTwo = new Simulation(super.params);
        this.statOne = new SimulationStatistician(this.simOne);
        this.statTwo = new SimulationStatistician(this.simTwo);
        this.statOne.addIObserverStatistics(this);
        this.statTwo.addIObserverStatistics(this);
        this.simOne.addNewAllKillsObserver(this.statOne);
        this.simTwo.addNewAllKillsObserver(this.statTwo);

        this.timeOne = new Timeline(new KeyFrame(Duration.millis(super.dayLength), e -> this.simOne.nextDay()));
        this.timeOne.setCycleCount(Animation.INDEFINITE);
        this.timeTwo = new Timeline(new KeyFrame(Duration.millis(super.dayLength), e -> this.simTwo.nextDay()));
        this.timeTwo.setCycleCount(Animation.INDEFINITE);
        this.setSimOneStatus(false);
        this.setSimTwoStatus(false);

        this.simOne.zeroDay();
        this.simTwo.zeroDay();
    }
}
