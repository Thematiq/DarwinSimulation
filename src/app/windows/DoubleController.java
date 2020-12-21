package app.windows;

import engine.handlers.Simulation;
import engine.tools.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.net.URISyntaxException;

public class DoubleController extends AbstractSimulatorController {
    private Simulation simOne;
    private Simulation simTwo;
    private SimulationStatistician statOne;
    private SimulationStatistician statTwo;
    private Timeline timeOne;
    private Timeline timeTwo;
    private Genome domOne = new Genome();
    private Genome domTwo = new Genome();
    private AnimalStatistician localStat;
    int cellSizeOne;
    int cellSizeTwo;
    boolean canGenerate = false;
    boolean generating = false;
    // True if left sim, false if right
    boolean leftSim;
    int from;
    int to;

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
    private Label labelGenerate;
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
    private Button buttonGenerating;
    @FXML
    private Canvas canvasOne;
    @FXML
    private Canvas canvasTwo;
    @FXML
    private GridPane gridCanvasOne;
    @FXML
    private GridPane gridCanvasTwo;

    public DoubleController() throws URISyntaxException { }

    /**
     * Listens to gridCanvasOne resize, changing canvas size to grid size
     */
    private void listenCanvasOneResize(ObservableValue<? extends Number> observableValue, Number number, Number number1) {
        this.canvasOne.setHeight(this.gridCanvasOne.getHeight());
        this.canvasOne.setWidth(this.gridCanvasOne.getWidth());
        this.initDrawer();
        this.draw(this.simOne, this.canvasOne);
    }

    /**
     * Listens to gridCanvasTwo resize, changing canvas size to grid size
     */
    private void listenCanvasTwoResize(ObservableValue<? extends Number> observableValue, Number number, Number number1) {
        this.canvasTwo.setHeight(this.gridCanvasTwo.getHeight());
        this.canvasTwo.setWidth(this.gridCanvasTwo.getWidth());
        this.initDrawer();
        this.draw(this.simTwo, this.canvasTwo);
    }

    /**
     * checks whether both fields are not empty
     */
    private void listenText(Observable observable) {
        this.canGenerate = !this.textStatEpoch.getText().equals("") && !this.textFilename.getText().equals("");
        this.setStatusOne(this.getStatusOne());
        this.setStatusTwo(this.getStatusTwo());
    }

    /**
     * Set simulation one status, blocking proper buttons
     * @param status True if simulation is running
     */
    private void setStatusOne(boolean status) {
        this.buttonNextOne.setDisable(status);
        this.buttonStartOne.setDisable(status);
        this.buttonStopOne.setDisable(!status);
        if (this.radioLeftSim.isSelected()) {
            this.buttonGenerating.setDisable(status || !this.canGenerate || this.generating);
        }
        if (status) {
            this.timeOne.play();
            if (this.watcherRunning() && this.leftSim) {
                this.labelDesc.setText("Left sim" + super.watchingRunning);
            } else if (!this.watcherRunning()){
                this.labelDesc.setText(super.running);
            }
        } else {
            this.timeOne.stop();
            if (this.watcherRunning() && this.leftSim) {
                this.labelDesc.setText("Left sim" + super.watchingPaused);
            } else if (!this.watcherRunning()){
                this.labelDesc.setText(super.paused);
            }
        }
    }

    /**
     * Set simulation two status, blocking proper buttons
     * @param status True if simulation is running
     */
    private void setStatusTwo(boolean status) {
        this.buttonNextTwo.setDisable(status);
        this.buttonStartTwo.setDisable(status);
        this.buttonStopTwo.setDisable(!status);
        if (this.radioRightSim.isSelected()) {
            this.buttonGenerating.setDisable(status || !this.canGenerate || this.generating);
        }
        if (status) {
            this.timeTwo.play();
            if (this.watcherRunning() && !this.leftSim) {
                this.labelDesc.setText("Right sim" + super.watchingRunning);
            } else if (!this.watcherRunning()) {
                this.labelDesc.setText(super.running);
            }
        } else {
            this.timeTwo.stop();
            if (this.watcherRunning() && !this.leftSim) {
                this.labelDesc.setText("Right sim" + super.watchingPaused);
            } else if(!this.watcherRunning()) {
                this.labelDesc.setText(super.paused);
            }
        }
    }

    /**
     * @return Current simulation one status
     */
    boolean getStatusOne() { return this.buttonStartOne.isDisabled(); }

    /**
     * @return Current simulation two status
     */
    boolean getStatusTwo() { return this.buttonStartTwo.isDisabled(); }

    /**
     * Drawing wrapper, changing super class values, depending on a simulation
     * @param sim Desired simulation
     * @param can Desired canvas
     */
    void draw(Simulation sim, Canvas can) {
        Genome dominant;
        if (sim == this.simOne) {
            dominant = this.domOne;
            super.cellSize = this.cellSizeOne;
            super.drawDominant = this.radioDominantOne.isSelected();
        } else {
            dominant = this.domTwo;
            super.cellSize = this.cellSizeTwo;
            super.drawDominant = this.radioDominantTwo.isSelected();
        }
        super.draw(sim, can, dominant);
    }

    /**
     * @return True if animal watcher is running
     */
    boolean watcherRunning() {
        return this.localStat != null && this.localStat.isRunning();
    }

    /**
     * @return Duration of animal watching. -1 if text field is empty
     */
    private int getDuration() {
        if (this.textEpoch.getText().equals("")) {
            return -1;
        } else {
            return Integer.parseInt(this.textEpoch.getText());
        }
    }

    /**
     * buttonGenerate event
     */
    @FXML
    private void newFileStats() {
        this.generating = true;
        this.buttonGenerating.setDisable(true);
        this.labelGenerate.setText(super.gathering);
        this.radioLeftSim.setDisable(true);
        this.radioRightSim.setDisable(true);
        this.labelGenerate.setText(super.gathering);
        if (this.radioLeftSim.isSelected()) {
            this.from = this.simOne.getDay();
        } else {
            this.from = this.simTwo.getDay();
        }
        this.to = Integer.parseInt(this.textStatEpoch.getText()) + this.from;
    }


    /**
     * radio display dominant sim one event
     */
    @FXML
    private void ToggleDisplayDominantOne() {
        this.draw(this.simOne, this.canvasOne);
    }

    /**
     * radio display dominant sim two event
     */
    @FXML
    private void ToggleDisplayDominantTwo() {
        this.draw(this.simTwo, this.canvasTwo);
    }

    /**
     * radio sim one selection event
     */
    @FXML
    private void ToggleSimLeft() {
        this.setStatusOne(this.getStatusOne());
    }

    /**
     * radio sim two selection event
     */
    @FXML
    private void ToggleSimRight() {
        this.setStatusTwo(this.getStatusTwo());
    }

    /**
     * Sim one start button event
     */
    @FXML
    private void startOne() { this.setStatusOne(true); }

    /**
     * Sim one stop button event
     */
    @FXML
    private void stopOne() { this.setStatusOne(false); }

    /**
     * Sim one next day button event
     */
    @FXML
    private void nextOne() { this.simOne.nextDay(); }

    /**
     * Sim two start button event
     */
    @FXML
    private void startTwo() { this.setStatusTwo(true); }

    /**
     * Sim two stop button event
     */
    @FXML
    private void stopTwo() { this.setStatusTwo(false); }

    /**
     * Sim two next day button event
     */
    @FXML
    private void nextTwo() { this.simTwo.nextDay(); }

    /**
     * Click on canvas one event, allowing for choosing an animal
     * @param e Mouse event
     */
    @FXML
    private void canvasOneClicked(MouseEvent e) {
        Vector pos = new Vector((int) (e.getX() / this.cellSizeOne), (int) (e.getY() / this.cellSizeOne));
        int endDate = this.getDuration();
        if (!this.getStatusOne() && this.simOne.animalAt(pos) != null && endDate != -1) {
            this.leftSim = true;
            this.localStat = new AnimalStatistician(this.simOne, this.simOne.animalAt(pos), endDate);
            this.localStat.addIObserverAnimalStatistics(this);
            this.newData(this.localStat);
        }
    }

    /**
     * Click on canvas two event, allowing for choosing an animal
     * @param e Mouse event
     */
    @FXML
    private void canvasTwoClicked(MouseEvent e) {
        Vector pos = new Vector((int) (e.getX() / this.cellSizeTwo), (int) (e.getY() / this.cellSizeTwo));
        int endDate = this.getDuration();
        if (!this.getStatusTwo() && this.simTwo.animalAt(pos) != null && endDate != -1) {
            this.leftSim = false;
            this.localStat = new AnimalStatistician(this.simTwo, this.simTwo.animalAt(pos), endDate);
            this.localStat.addIObserverAnimalStatistics(this);
            this.newData(this.localStat);
        }
    }

    /**
     * Animal statistician handler
     * @param caller calling statistician
     */
    @Override
    public void newData(AnimalStatistician caller) {
        this.labelChildren.setText(String.valueOf(caller.getTotalChildren()));
        this.labelDescendants.setText(String.valueOf(caller.getTotalDescendants()));
        if (caller.hasDied()) {
            this.labelDayOfDeath.setText(String.valueOf(caller.getDeathDay()));
        } else if (this.watcherRunning()) {
            String sim;
            if (this.leftSim) {
                sim = "Left sim: ";
                if (this.getStatusOne()) {
                    this.labelDesc.setText(sim + super.watchingRunning);
                } else {
                    this.labelDesc.setText(sim + super.watchingPaused);
                }
            } else {
                sim = "Right sim: ";
                if (this.getStatusTwo()) {
                    this.labelDesc.setText(sim + super.watchingRunning);
                } else {
                    this.labelDesc.setText(sim + super.watchingPaused);
                }
            }
            this.labelDayOfDeath.setText(super.stillLiving);
        } else {
            if (this.getStatusOne() || this.getStatusTwo()) {
                this.labelDesc.setText(super.running);
            } else {
                this.labelDesc.setText(super.paused);
            }
            this.labelDayOfDeath.setText(super.watcherEnded);
        }
    }

    /**
     * Simulation new day handler
     * @param caller Calling statistician
     */
    @Override
    public void update(SimulationStatistician caller) {
        if (caller.equals(this.statOne)) {
            this.labelPopOneBottom.setText(String.valueOf(caller.getCurrentAnimals()));
            this.labelDayOneBottom.setText(String.valueOf(caller.getCurrentDay()));

            this.labelPopOne.setText(String.valueOf(caller.getCurrentAnimals()));
            this.labelVegOne.setText(String.valueOf(caller.getCurrentVegetation()));
            this.labelEnergyOne.setText(String.valueOf(caller.getCurrentEnergy()));
            this.labelLifespanOne.setText(String.valueOf(caller.getCurrentLifespan()));
            this.labelChildrenOne.setText(String.valueOf(caller.getCurrentChildren()));
            this.labelGeneOne.setText(String.valueOf(caller.getCurrentDominant()));

            this.domOne = caller.getCurrentDominant();
            this.draw(this.simOne, this.canvasOne);
            if (this.generating && this.radioLeftSim.isSelected() && this.to == caller.getCurrentDay()) {
                Stats export = caller.getStats(this.from, this.to);
                this.generating = false;
                JSONIO.writeStats(export, this.textFilename.getText());
                this.setStatusOne(this.getStatusOne());
                this.labelGenerate.setText(super.saved);
                this.radioLeftSim.setDisable(false);
                this.radioRightSim.setDisable(false);
            }
        } else if (caller.equals(this.statTwo)) {
            this.labelPopTwoBottom.setText(String.valueOf(caller.getCurrentAnimals()));
            this.labelDayTwoBottom.setText(String.valueOf(caller.getCurrentDay()));

            this.labelPopTwo.setText(String.valueOf(caller.getCurrentAnimals()));
            this.labelVegTwo.setText(String.valueOf(caller.getCurrentVegetation()));
            this.labelEnergyTwo.setText(String.valueOf(caller.getCurrentEnergy()));
            this.labelLifespanTwo.setText(String.valueOf(caller.getCurrentLifespan()));
            this.labelChildrenTwo.setText(String.valueOf(caller.getCurrentChildren()));
            this.labelGeneTwo.setText(String.valueOf(caller.getCurrentDominant()));

            this.domTwo = caller.getCurrentDominant();
            this.draw(this.simTwo, this.canvasTwo);
            if (this.generating && this.radioRightSim.isSelected() &&this.to == caller.getCurrentDay()) {
                Stats export = caller.getStats(this.from, this.to);
                this.generating = false;
                JSONIO.writeStats(export, this.textFilename.getText());
                this.setStatusTwo(this.getStatusTwo());
                this.labelGenerate.setText(super.saved);
                this.radioLeftSim.setDisable(false);
                this.radioRightSim.setDisable(false);
            }
        }
    }

    /**
     * Initializes canvases sizes
     */
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

    /**
     * Initializes simulation window with parameters
     * @param param Simulation parameters
     */
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

        ToggleGroup g = new ToggleGroup();
        this.radioLeftSim.setToggleGroup(g);
        this.radioRightSim.setToggleGroup(g);
        this.radioLeftSim.setSelected(true);

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
        this.setStatusOne(false);
        this.setStatusTwo(false);

        // Rescale canvas, we can pass nulls as params, because that functions doesn't use them
        this.listenCanvasOneResize(null, null, null);
        this.listenCanvasTwoResize(null, null, null);
        this.simOne.zeroDay();
        this.simTwo.zeroDay();
    }
}
