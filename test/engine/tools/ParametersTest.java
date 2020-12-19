package engine.tools;

import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ParametersTest {
    @Test
    public void fromFileTest() {
        try {
            Parameters fromFile = new Parameters("test/testFile.json");
            Parameters actual = new Parameters(
                    10, 10, 10, 1,
                    10, (float) 0.2, 10
            );
            assertEquals(fromFile, actual);
        } catch (Error e) {
            System.out.println(e.getMessage());
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }
}
