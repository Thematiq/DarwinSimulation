package app.windows;

import engine.tools.Parameters;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;


public class paramController implements Initializable {
    private boolean single;
    private ToggleGroup simulationGroup;

    @FXML
    private TextField widthText;
    @FXML
    private TextField heightText;
    @FXML
    private TextField energyText;
    @FXML
    private TextField costText;
    @FXML
    private TextField plantText;
    @FXML
    private TextField jungleText;
    @FXML
    private TextField startingText;
    @FXML
    private RadioButton singleRadio;
    @FXML
    private RadioButton doubleRadio;

    private final Pattern numericPattern = Pattern.compile("^([1-9][0-9]*)?$");
    private final Pattern floatingPattern = Pattern.compile("^(0\\.?[0-9]*)?$");

    private Change numericChange(Change change) {
        if (numericPattern.matcher(change.getControlNewText()).matches()) {
            return change;
        } else {
            return null;
        }
    }

    private Change floatingChange(Change change) {
        if (floatingPattern.matcher(change.getControlNewText()).matches()) {
            return change;
        } else {
            return null;
        }
    }

    @FXML
    private void singleSelected() {
        this.single = true;
    }

    @FXML
    private void doubleSelected() {
        this.single = false;
    }

    @FXML
    void startSim() throws IOException {
        if (this.allParams()) {
            if (this.single) {
                this.spawnSimulationWindow("singleSimulation.fxml", this.scrapParams());
            } else {
                this.spawnSimulationWindow("doubleSimulation.fxml", this.scrapParams());
            }
        }
    }

    @FXML
    void readFile() {
        //TODO
        //Read files params to text boxes
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.widthText.setTextFormatter(new TextFormatter<Object>(this::numericChange));
        this.heightText.setTextFormatter(new TextFormatter<Object>(this::numericChange));
        this.energyText.setTextFormatter(new TextFormatter<Object>(this::numericChange));
        this.costText.setTextFormatter(new TextFormatter<Object>(this::numericChange));
        this.plantText.setTextFormatter(new TextFormatter<Object>(this::numericChange));
        this.startingText.setTextFormatter(new TextFormatter<Object>(this::numericChange));
        this.jungleText.setTextFormatter(new TextFormatter<Object>(this::floatingChange));

        this.single = true;
        this.simulationGroup = new ToggleGroup();
        this.singleRadio.setToggleGroup(this.simulationGroup);
        this.doubleRadio.setToggleGroup(this.simulationGroup);
        this.singleRadio.setSelected(true);
    }

    private Parameters scrapParams() {
        int width = Integer.parseInt(this.widthText.getText());
        int height = Integer.parseInt(this.heightText.getText());
        int startEnergy = Integer.parseInt(this.energyText.getText());
        int moveEnergy = Integer.parseInt(this.costText.getText());
        int plantEnergy = Integer.parseInt(this.plantText.getText());
        int startingAnimals = Integer.parseInt(this.startingText.getText());
        float jungleRation = Float.parseFloat(this.jungleText.getText());
        return new Parameters(width, height, startEnergy, moveEnergy, plantEnergy, jungleRation, startingAnimals);
    }

    private boolean allParams() {
        TextField[] textFields = new TextField[]{
                this.widthText, this.heightText, this.energyText, this.costText,
                this.plantText, this.startingText, this.jungleText
        };
        for (TextField t : textFields) {
            if (t.getText().equals("")) {
                return false;
            }
        }
        if (this.jungleText.getText().charAt(this.jungleText.getText().length() - 1) == '.') {
            return false;
        }
        return true;
    }

    private void spawnSimulationWindow(String window, Parameters params) throws IOException {
        Stage currentStage = (Stage) this.startingText.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(window));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        currentStage.setScene(scene);
        AbstractSimulatorController controller = loader.getController();
        controller.initSimulation(params);
    }
}
