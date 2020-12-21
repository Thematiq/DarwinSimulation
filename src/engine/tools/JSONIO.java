package engine.tools;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

/**
 * Class containing static IO methods
 * @author Mateusz Praski
 */
public class JSONIO {

    /**
     * Writes stats object into json-like file
     * @param a Stats object
     * @param filename File path
     * @return true if saved successfully
     */
    public static boolean writeStats(Stats a, String filename) {
        JSONObject jo = new JSONObject();
        jo.put("from", a.from);
        jo.put("to", a.to);
        jo.put("mean living animals", a.living);
        jo.put("mean vegetation", a.vegetation);
        jo.put("mean dead animals", a.dead);
        jo.put("mean energy", a.meanEnergy);
        jo.put("mean children", a.meanChildren);
        jo.put("mean lifespan", a.lifespan);
        jo.put("dominating genome", a.dominating.toString());
        try (Writer writer = new FileWriter(filename)) {
            writer.write(jo.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Read params from file
     * @param filename filename
     * @return Parameters from JSON file
     */
    public static Parameters readParameters(String filename) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        FileReader reader = new FileReader(filename);
        JSONObject table = (JSONObject) parser.parse(reader);
        int width = ((Long) table.get("width")).intValue();
        int height = ((Long) table.get("height")).intValue();
        int startEnergy = ((Long) table.get("startEnergy")).intValue();
        int moveEnergy = ((Long) table.get("moveEnergy")).intValue();
        int plantEnergy = ((Long) table.get("plantEnergy")).intValue();
        int startingAnimals = ((Long) table.get("startingAnimals")).intValue();
        float jungleRatio = ((Double) table.get("jungleRatio")).floatValue();
        return new Parameters(
                width, height, startEnergy, moveEnergy,
                plantEnergy, jungleRatio, startingAnimals
        );
    }
}
