package app.windows;

import engine.handlers.Simulation;
import engine.tools.Parameters;
import engine.tools.Statistician;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class DoubleController extends AbstractSimulatorController {
    private Simulation simOne;
    private Simulation simTwo;
    private Statistician statOne;
    private Statistician statTwo;
    private Timeline timeOne;
    private Timeline timeTwo;

    @FXML
    private Label simOneDay;
    @FXML
    private Label simOnePop;
    @FXML
    private Label simTwoDay;
    @FXML
    private Label simTwoPop;
    @FXML
    private Canvas canvasOne;
    @FXML
    private Canvas canvasTwo;
    @FXML
    private Label popOneLabel;
    @FXML
    private Label vegOneLabel;
    @FXML
    private Label geneOneLabel;
    @FXML
    private Label energyOneLabel;
    @FXML
    private Label lifespanOneLabel;
    @FXML
    private Label childrenOneLabel;
    @FXML
    private Label popTwoLabel;
    @FXML
    private Label vegTwoLabel;
    @FXML
    private Label geneTwoLabel;
    @FXML
    private Label energyTwoLabel;
    @FXML
    private Label lifespanTwoLabel;
    @FXML
    private Label childrenTwoLabel;
    @FXML
    private Button startOneButton;
    @FXML
    private Button stopOneButton;
    @FXML
    private Button nextOneButton;
    @FXML
    private Button startTwoButton;
    @FXML
    private Button stopTwoButton;
    @FXML
    private Button nextTwoButton;

    @FXML
    private void startOne() { this.setSimOneStatus(true); }

    @FXML
    private void stopOne() { this.setSimOneStatus(false); }

    @FXML
    private void nextOne() {

    }

    @FXML
    private void startTwo() { this.setSimTwoStatus(true); }

    @FXML
    private void stopTwo() { this.setSimTwoStatus(false); }

    @FXML
    private void nextTwo() {

    }

    private void setSimOneStatus(boolean status) {
        this.nextOneButton.setDisable(status);
        this.startOneButton.setDisable(status);
        this.stopOneButton.setDisable(!status);
        if (status) {
            this.timeOne.play();
        } else {
            this.timeOne.stop();
        }
    }

    private void setSimTwoStatus(boolean status) {
        this.nextTwoButton.setDisable(status);
        this.startTwoButton.setDisable(status);
        this.stopTwoButton.setDisable(!status);
        if (status) {
            this.timeTwo.play();
        } else {
            this.timeTwo.stop();
        }
    }

    @Override
    public void initSimulation(Parameters param) {
        super.params = param;
        this.initDrawer();
        this.simOne = new Simulation(super.params);
        this.simTwo = new Simulation(super.params);
        this.statOne = new Statistician(this.simOne);
        this.statTwo = new Statistician(this.simTwo);
        this.statOne.addIObserverStatistics(this);
        this.statTwo.addIObserverStatistics(this);
        this.timeOne = new Timeline(new KeyFrame(Duration.millis(super.dayLength), e -> this.simOne.nextDay()));
        this.timeOne.setCycleCount(Animation.INDEFINITE);
        this.timeTwo = new Timeline(new KeyFrame(Duration.millis(super.dayLength), e -> this.simTwo.nextDay()));
        this.timeTwo.setCycleCount(Animation.INDEFINITE);
        this.setSimOneStatus(false);
        this.setSimTwoStatus(false);
        this.simOne.zeroDay();
        this.simTwo.zeroDay();
    }

    void initDrawer() {
        super.cellSize = (int) (Math.min(this.canvasOne.getWidth(), this.canvasOne.getHeight()) / this.params.width);
        this.canvasOne.setWidth(super.cellSize * super.params.width);
        this.canvasOne.setHeight(super.cellSize * super.params.height);
        this.canvasTwo.setWidth(super.cellSize * super.params.width);
        this.canvasTwo.setHeight(super.cellSize * super.params.height);
    }

    @Override
    public void update(Statistician caller) {
        if (caller.equals(this.statOne)) {
            this.simOnePop.setText(String.valueOf(caller.getCurrentAnimals()));
            this.simOneDay.setText(String.valueOf(caller.getCurrentDay()));

            this.popOneLabel.setText(String.valueOf(caller.getCurrentAnimals()));
            this.vegOneLabel.setText(String.valueOf(caller.getCurrentVegetation()));
            this.energyOneLabel.setText(String.valueOf(caller.getCurrentEnergy()));
            this.lifespanOneLabel.setText(String.valueOf(caller.getCurrentLifespan()));

            super.runDrawer(this.simOne, this.canvasOne);
        } else if (caller.equals(this.statTwo)) {
            this.simTwoPop.setText(String.valueOf(caller.getCurrentAnimals()));
            this.simTwoDay.setText(String.valueOf(caller.getCurrentDay()));

            this.popTwoLabel.setText(String.valueOf(caller.getCurrentAnimals()));
            this.vegTwoLabel.setText(String.valueOf(caller.getCurrentVegetation()));
            this.energyTwoLabel.setText(String.valueOf(caller.getCurrentEnergy()));
            this.lifespanTwoLabel.setText(String.valueOf(caller.getCurrentLifespan()));

            super.runDrawer(this.simTwo, this.canvasTwo);
        }
    }
}
