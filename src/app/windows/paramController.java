package app.windows;

import engine.tools.JSONIO;
import engine.tools.Parameters;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextFormatter.Change;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * JavaFX window to input data or load from json file
 * @author Mateusz Praski
 */
public class paramController implements Initializable {
    final String corrupt = "Error, couldn't read file";
    final String error = "Error, not all fields are filled correctly";

    @FXML
    private Label labelError;
    @FXML
    private TextField textWidth;
    @FXML
    private TextField textHeight;
    @FXML
    private TextField textEnergy;
    @FXML
    private TextField textCost;
    @FXML
    private TextField textPlant;
    @FXML
    private TextField textJungle;
    @FXML
    private TextField textStarting;
    @FXML
    private RadioButton radioSingle;
    @FXML
    private RadioButton radioDouble;

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
    void startSim() throws IOException {
        if (this.allParams()) {
            if (this.radioSingle.isSelected()) {
                this.spawnSimulationWindow("singleSimulation.fxml", this.scrapParams());
            } else {
                this.spawnSimulationWindow("doubleSimulation.fxml", this.scrapParams());
            }
        } else {
            this.labelError.setText(this.error);
        }
    }

    @FXML
    void readFile() {
        try {
            Stage currentStage = (Stage) this.textStarting.getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose parameters JSON file");
            fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("JSON files", "*.json"));
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
            String path = fileChooser.showOpenDialog(currentStage).getPath();
            Parameters params = JSONIO.readParameters(path);
            if (params.startingAnimals <= params.width * params.height) {
                if (this.radioSingle.isSelected()) {
                    this.spawnSimulationWindow("singleSimulation.fxml", params);
                } else {
                    this.spawnSimulationWindow("doubleSimulation.fxml", params);
                }
            } else {
                this.labelError.setText(this.corrupt);
            }
        } catch (Error e) {
            System.out.println(e.getMessage());
        } catch (ParseException | IOException | NullPointerException e) {
            this.labelError.setText(this.corrupt);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.textWidth.setTextFormatter(new TextFormatter<>(this::numericChange));
        this.textHeight.setTextFormatter(new TextFormatter<>(this::numericChange));
        this.textEnergy.setTextFormatter(new TextFormatter<>(this::numericChange));
        this.textCost.setTextFormatter(new TextFormatter<>(this::numericChange));
        this.textPlant.setTextFormatter(new TextFormatter<>(this::numericChange));
        this.textStarting.setTextFormatter(new TextFormatter<>(this::numericChange));
        this.textJungle.setTextFormatter(new TextFormatter<>(this::floatingChange));

        ToggleGroup simulationGroup = new ToggleGroup();
        this.radioSingle.setToggleGroup(simulationGroup);
        this.radioDouble.setToggleGroup(simulationGroup);
        this.radioSingle.setSelected(true);
    }

    private Parameters scrapParams() {
        int width = Integer.parseInt(this.textWidth.getText());
        int height = Integer.parseInt(this.textHeight.getText());
        int startEnergy = Integer.parseInt(this.textEnergy.getText());
        int moveEnergy = Integer.parseInt(this.textCost.getText());
        int plantEnergy = Integer.parseInt(this.textPlant.getText());
        int startingAnimals = Integer.parseInt(this.textStarting.getText());
        float jungleRation = Float.parseFloat(this.textJungle.getText());
        return new Parameters(width, height, startEnergy, moveEnergy, plantEnergy, jungleRation, startingAnimals);
    }

    private boolean allParams() {
        TextField[] textFields = new TextField[]{
                this.textWidth, this.textHeight, this.textEnergy, this.textCost,
                this.textPlant, this.textStarting, this.textJungle
        };
        for (TextField t : textFields) {
            if (t.getText().equals("")) {
                return false;
            }
        }
        return this.textJungle.getText().charAt(this.textJungle.getText().length() - 1) != '.' &&
                Integer.parseInt(this.textStarting.getText()) <= Integer.parseInt(this.textWidth.getText()) * Integer.parseInt(this.textHeight.getText());
    }

    /**
     * Changes window
     * @param window path to new window
     * @param params Simulation params
     */
    private void spawnSimulationWindow(String window, Parameters params) throws IOException {
        Stage currentStage = (Stage) this.textStarting.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(window));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        currentStage.setScene(scene);
        AbstractSimulatorController controller = loader.getController();
        controller.initSimulation(params);
    }
}
