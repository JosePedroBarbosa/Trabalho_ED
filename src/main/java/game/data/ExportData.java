package game.data;

import dataStructures.implementations.ArrayUnorderedList;
import game.map.Room;
import game.mission.Mission;
import game.settings.GameSettings;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ExportData {
    private static String gameSettingsPath = ".\\src\\settings\\GameSettings.json";
    private static String manualSimulationsPath = ".\\src\\exportedSimulations\\ManualSimulations.json";

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

    public static void saveManualSimulationToJson(Mission mission, ArrayUnorderedList<Room> rooms, boolean success) {
        JSONArray simulationsArray;

        try (FileReader reader = new FileReader(manualSimulationsPath)) {
            simulationsArray = (JSONArray) new JSONParser().parse(reader);
        } catch (Exception e) {
            simulationsArray = new JSONArray();
        }

        JSONObject simulation = new JSONObject();
        simulation.put("cod-missao", mission.getMissionCode());
        simulation.put("versao", mission.getVersion());
        simulation.put("trajeto", convertRoomsToArray(rooms));
        simulation.put("pontos-vida-restantes", mission.getPlayer().getLife());
        simulation.put("simulation-success", success);

        simulationsArray.add(simulation);

        try (FileWriter fileWriter = new FileWriter(manualSimulationsPath)) {
            fileWriter.write(simulationsArray.toJSONString());
            System.out.println("Manual Mission successfully saved to: " + manualSimulationsPath);
        } catch (IOException e) {
            System.err.println("Error while saving manual mission: " + e.getMessage());
        }
    }

    private static JSONArray convertRoomsToArray(ArrayUnorderedList<Room> rooms){
        JSONArray jsonArray = new JSONArray();

        for (Room room : rooms) {
            if(room != null){
                jsonArray.add(room.toString());
            }
        }

        return jsonArray;
    }
}
