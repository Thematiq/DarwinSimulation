package app;

import engine.handlers.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable, IObserverNewDay {

    private SimulationController contr;
    private Simulation sim;
    private boolean simulationRunning;
    private final int dayLength = 1000;

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
        this.sim = new Simulation();
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
    }
}
