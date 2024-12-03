package game.data;

import game.settings.GameSettings;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class ExportData {

    private static String gameSettingsPath = ".\\src\\settings\\GameSettings.json";

    public static void saveGameSettingsToJson() {

        JSONObject settings = new JSONObject();
        settings.put("maxBackpackItems", GameSettings.getMaxBackpackCapacity());
        settings.put("maxEnemyMove", GameSettings.getMaxEnemyMoves());
        settings.put("playerPower", GameSettings.getPlayerPower());
        settings.put("initialCharacterHealth", GameSettings.getInitialCharacterHealth());

        try (FileWriter fileWriter = new FileWriter(gameSettingsPath)) {
            fileWriter.write(settings.toJSONString());
            System.out.println("Settings successfully saved to: " + gameSettingsPath);
        } catch (IOException e) {
            System.err.println("Error while saving settings: " + e.getMessage());
        }
    }
}
