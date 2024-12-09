package game.data;


import dataStructures.exceptions.EmptyCollectionException;
import dataStructures.implementations.ArrayOrderedList;
import dataStructures.implementations.ArrayUnorderedList;

import game.character.Enemy;

import game.character.Player;
import game.items.HealthKit;
import game.items.Item;

import game.items.Shield;
import game.map.Map;
import game.map.Room;
import game.map.Target;

import game.mission.Mission;
import game.settings.GameSettings;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The ImportData class handles the importing of game and mission data from JSON files.
 * It reads settings, mission details, and simulation results to initialize game components like rooms, enemies, items, etc.
 */
public class ImportData {
    // Paths to the JSON files
    private static String currentMissionSettingsPath = ".\\src\\missions\\Mission1.json";
    private static String gameSettingsPath = ".\\src\\settings\\GameSettings.json";
    private static String simulationResultsPath = ".\\src\\exportedSimulations\\ManualSimulations.json";

    /**
     * Imports the current mission data from the predefined path.
     */
    public void importCurrentMissionData() {
        importMissionData(currentMissionSettingsPath);
    }

    /**
     * Imports mission data from the specified JSON file path.
     * This method reads the mission configuration and sets up the map, rooms, enemies, items, and target.
     *
     * @param missionPath the path to the mission JSON file
     */
    public void importMissionData(String missionPath){
        JSONParser parser = new JSONParser();

        try {
            String jsonText = Files.readString(Path.of(missionPath));
            JSONObject missionJSON = (JSONObject) parser.parse(jsonText);

            String code = (String) missionJSON.get("cod-missao");
            int version = ((Long) missionJSON.get("versao")).intValue();
            ArrayUnorderedList<Room> divisoes = jsonToRooms((JSONArray) missionJSON.get("edificio"));
            ArrayUnorderedList<Room> entriesAndExits = jsonToRooms(divisoes, (JSONArray) missionJSON.get("entradas-saidas"));
            ArrayUnorderedList<Enemy> enemies = jsonToEnemies((JSONArray) missionJSON.get("inimigos"), divisoes);
            Target target = jsonToTarget((JSONObject) missionJSON.get("alvo"), divisoes);
            ArrayUnorderedList<Item> items = jsonToItems((JSONArray) missionJSON.get("itens"), divisoes);

            for(Room room : entriesAndExits){
                room.setEntranceAndExit();
            }

            Map map = new Map();

            for(Room room : divisoes) {
                map.insertRoom(room);
            }
            importConnectionsFromJson(map, divisoes, (JSONArray) missionJSON.get("ligacoes"));

            //create a new player named "To Cruz" and is position is set to a default position
            Player player = new Player("To Cruz");

            Mission.initialize(code, version, player, target, enemies, items, map, entriesAndExits);
            currentMissionSettingsPath = missionPath;

            Path path = Paths.get(missionPath);
            String fileName = path.getFileName().toString();
            String fileNameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
            MissionDisplay.setCurrentMissionImage(fileNameWithoutExtension);

            System.out.println("mission " + code + " imported successfully!");
        } catch (Exception e) {
            System.out.println("An error occurred while importing mission data");
            //System.out.println("*** ERROR: " + e.getMessage() + " ***");
            e.printStackTrace();
        }

    }

    /**
     * Imports game settings from a JSON file and updates the game settings.
     */
    public void importGameSettingsData(){
        JSONParser parser = new JSONParser();

        try {
            String jsonText = Files.readString(Path.of(gameSettingsPath));
            JSONObject settings = (JSONObject) parser.parse(jsonText);

            int maxBackpackItems = ((Long) settings.get("maxBackpackItems")).intValue();
            int maxEnemyMove = ((Long) settings.get("maxEnemyMove")).intValue();
            int playerPower = ((Long) settings.get("playerPower")).intValue();
            int initialCharacterHealth = ((Long) settings.get("initialCharacterHealth")).intValue();

            GameSettings.setMaxBackpackCapacity(maxBackpackItems);
            GameSettings.setPlayerPower(playerPower);
            GameSettings.setMaxEnemyMoves(maxEnemyMove);
            GameSettings.setInitialCharacterHealth(initialCharacterHealth);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Converts a target JSON object to a Target object.
     *
     * @param targetJson the JSON representation of the target
     * @param rooms the list of available rooms
     * @return the Target object
     */
    private Target jsonToTarget(JSONObject targetJson, ArrayUnorderedList<Room> rooms) {
        Room room = getRoom(rooms, (String) targetJson.get("divisao"));
        String type = (String) targetJson.get("tipo");

        Target target = new Target(room, type);
        return new Target(room, type);
    }

    /**
     * Converts a JSON array of rooms to a list of Room objects.
     *
     * @param roomsJson the JSON array of room names
     * @return the list of Room objects
     */
    private ArrayUnorderedList<Room> jsonToRooms(JSONArray roomsJson){
        ArrayUnorderedList<Room> rooms = new ArrayUnorderedList<>(roomsJson.size());

        for(int i = 0; i < roomsJson.size(); i++) {
            rooms.addToRear(new Room((String) roomsJson.get(i)));
        }
        return rooms;
    }

    /**
     * Converts a JSON array of rooms to a list of Room objects, using an existing list of rooms for reference.
     *
     * @param rooms the list of existing rooms
     * @param roomsJson the JSON array of room names
     * @return the new list of Room objects
     */
    private ArrayUnorderedList<Room> jsonToRooms(ArrayUnorderedList<Room> rooms, JSONArray roomsJson){
        ArrayUnorderedList<Room> newRooms = new ArrayUnorderedList<>(roomsJson.size());

        for(int i = 0; i < roomsJson.size(); i++) {
            newRooms.addToRear(getRoom(rooms, (String) roomsJson.get(i)));
        }
        return newRooms;
    }

    /**
     * Imports connections between rooms from a JSON array and adds them to the map.
     *
     * @param map the game map
     * @param rooms the list of rooms
     * @param connectionsJson the JSON array of connections between rooms
     */
    private void importConnectionsFromJson(Map map, ArrayUnorderedList<Room> rooms, JSONArray connectionsJson) {
        for(int i = 0; i < connectionsJson.size(); i++) {
            JSONArray connection = (JSONArray) connectionsJson.get(i);

            Room room1 = getRoom(rooms, (String) connection.get(0));
            Room room2 = getRoom(rooms, (String) connection.get(1));

            map.insertConnection(room1, room2);
        }

    }

    /**
     * Retrieves a Room object by its name from the list of rooms.
     *
     * @param rooms the list of rooms
     * @param name the name of the room
     * @return the Room object with the specified name, or null if not found
     */
    private Room getRoom(ArrayUnorderedList<Room> rooms, String name) {
        for(Room room : rooms) {
            if(room.getName().equals(name)) {
                return room;
            }
        }
        return null;
    }

    /**
     * Converts a JSON array of items to a list of Item objects.
     *
     * @param itemsJSON the JSON array of items
     * @param rooms the list of rooms where items are placed
     * @return the list of Item objects
     */
    private ArrayUnorderedList<Item> jsonToItems(JSONArray itemsJSON, ArrayUnorderedList<Room> rooms){
        ArrayUnorderedList<Item> items = new ArrayUnorderedList<>(itemsJSON.size());

        for(int i = 0; i < itemsJSON.size(); i++) {
            items.addToRear(jsonToItem((JSONObject) itemsJSON.get(i), rooms));
        }

        return items;
    }

    /**
     * Converts a JSON object to an Item object (either HealthKit or Shield).
     *
     * @param itemJSON the JSON representation of the item
     * @param rooms the list of rooms where the item is placed
     * @return the Item object
     */
    private Item jsonToItem(JSONObject itemJSON, ArrayUnorderedList<Room> rooms){
        Room room = getRoom(rooms, (String) itemJSON.get("divisao"));

        Item item;

        if (itemJSON.containsKey("pontos-recuperados")) {
            item = new HealthKit(((Long) itemJSON.get("pontos-recuperados")).intValue(), room);
        }else{
            item = new Shield(((Long) itemJSON.get("pontos-extra")).intValue(), room);
        }

        room.addItem(item);
        return item;
    }


    /**
     * Converts a JSON array of enemies to a list of Enemy objects.
     *
     * @param enemiesJson the JSON array of enemies
     * @param rooms the list of rooms where enemies are placed
     * @return the list of Enemy objects
     */
    private ArrayUnorderedList<Enemy> jsonToEnemies(JSONArray enemiesJson, ArrayUnorderedList<Room> rooms){
        ArrayUnorderedList<Enemy> enemies = new ArrayUnorderedList<>(enemiesJson.size());

        for(int i = 0; i < enemiesJson.size(); i++) {
            enemies.addToRear(this.jsonToEnemy((JSONObject) enemiesJson.get(i), rooms));
        }
        return enemies;
    }

    /**
     * Converts a JSON object to an Enemy object.
     *
     * @param enemyJson the JSON representation of the enemy
     * @param rooms the list of rooms where the enemy is located
     * @return the Enemy object
     */
    private Enemy jsonToEnemy(JSONObject enemyJson, ArrayUnorderedList<Room> rooms){
        String name = (String) enemyJson.get("nome");
        int power = ((Long) enemyJson.get("poder")).intValue();
        String roomName = (String) enemyJson.get("divisao");
        Room room = getRoom(rooms, roomName);

        Enemy enemy = new Enemy(name, power, room);
        room.addEnemy(enemy);
        return enemy;
    }

    /**
     * Imports simulation codes from the simulation results JSON file.
     *
     * @return a list of mission codes found in the simulation results
     */
    public static ArrayUnorderedList<String> importSimulationCodesResults(){
        JSONParser parser = new JSONParser();
        ArrayUnorderedList<String> missionCodes = new ArrayUnorderedList<>();

        try {
            String jsonText = Files.readString(Path.of(simulationResultsPath));
            JSONArray simulationsJSON = (JSONArray) parser.parse(jsonText);

            for(int i = 0; i < simulationsJSON.size(); i++) {
                JSONObject object = (JSONObject) simulationsJSON.get(i);
                String code = (String) object.get("cod-missao");

                try{
                    if(!missionCodes.contains(code)){
                        missionCodes.addToRear(code);
                    }
                }catch (EmptyCollectionException ex){
                    missionCodes.addToRear(code);
                }
            }
        } catch (Exception e) {
            System.out.println("An error occurred while importing simulation codes");
            System.out.println("*** ERROR: " + e.getMessage() + " ***");
        }

        return missionCodes;
    }

    /**
     * Imports simulation results for a given mission code from the simulation results JSON file.
     *
     * @param selectedCode the mission code for which simulation results are to be imported
     * @return a list of SimulationResults objects corresponding to the selected mission code
     */
    public static ArrayOrderedList<SimulationResults> importSimulationResultsByCode(String selectedCode){
        ArrayOrderedList<SimulationResults> results = new ArrayOrderedList<>();

        JSONParser parser = new JSONParser();

        try {
            String jsonText = Files.readString(Path.of(simulationResultsPath));
            JSONArray simulationsJSON = (JSONArray) parser.parse(jsonText);

            for(int i = 0; i < simulationsJSON.size(); i++) {
                JSONObject object = (JSONObject) simulationsJSON.get(i);
                String code = (String) object.get("cod-missao");

                if(code.equals(selectedCode)){
                    results.add(jsonToSimulationResults(object));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("An error occurred while importing simulation codes");
            System.out.println("*** ERROR: " + e.getMessage() + " ***");
        }

        return results;
    }

    /**
     * Converts a JSON object to a SimulationResults object.
     *
     * @param object the JSON representation of the simulation result
     * @return the SimulationResults object
     */
    public static SimulationResults jsonToSimulationResults(JSONObject object){
        String codeMission = (String) object.get("cod-missao");
        int version = ((Long) object.get("versao")).intValue();
        int remainingHealth = ((Long) object.get("pontos-vida-restantes")).intValue();
        boolean simulationSuccess = (Boolean) object.get("simulation-success");
        ArrayUnorderedList<Room> rooms = new ArrayUnorderedList<>();

        JSONArray roomsJson = (JSONArray) object.get("trajeto");
        for(int i = 0; i < roomsJson.size(); i++){
            String roomName = (String) roomsJson.get(i);
            rooms.addToRear(new Room(roomName));
        }

        return new SimulationResults(codeMission, version, remainingHealth, simulationSuccess, rooms);
    }

}