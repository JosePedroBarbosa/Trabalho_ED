package game.data;


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

public class ImportData {
    private static String defaultMissionSettingsPath = ".\\src\\missions\\Mission1.json";
    private static String gameSettingsPath = ".\\src\\settings\\GameSettings.json";

    public void importDefaultMissionData() {
        importMissionData(defaultMissionSettingsPath);
    }

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
            MissionDisplay.setCurrentMissionImage("Mission1");
            System.out.println("mission " + code + " imported");
        } catch (Exception e) {
            System.out.println("An error occurred while importing mission data");
            System.out.println("*** ERROR: " + e.getMessage() + " ***");
        }
    }

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
    /*
    TARGET
    */
    private Target jsonToTarget(JSONObject targetJson, ArrayUnorderedList<Room> rooms) {
        Room room = getRoom(rooms, (String) targetJson.get("divisao"));
        String type = (String) targetJson.get("tipo");

        Target target = new Target(room, type);
        return new Target(room, type);
    }

    /*
    ROOMS
    */
    private ArrayUnorderedList<Room> jsonToRooms(JSONArray roomsJson){
        ArrayUnorderedList<Room> rooms = new ArrayUnorderedList<>(roomsJson.size());

        for(int i = 0; i < roomsJson.size(); i++) {
            rooms.addToRear(new Room((String) roomsJson.get(i)));
        }
        return rooms;
    }

    private ArrayUnorderedList<Room> jsonToRooms(ArrayUnorderedList<Room> rooms, JSONArray roomsJson){
        ArrayUnorderedList<Room> newRooms = new ArrayUnorderedList<>(roomsJson.size());

        for(int i = 0; i < roomsJson.size(); i++) {
            newRooms.addToRear(getRoom(rooms, (String) roomsJson.get(i)));
        }
        return newRooms;
    }

    private void importConnectionsFromJson(Map map, ArrayUnorderedList<Room> rooms, JSONArray connectionsJson) {
        for(int i = 0; i < connectionsJson.size(); i++) {
            JSONArray connection = (JSONArray) connectionsJson.get(i);

            Room room1 = getRoom(rooms, (String) connection.get(0));
            Room room2 = getRoom(rooms, (String) connection.get(1));

            map.insertConnection(room1, room2);
        }

    }

    private Room getRoom(ArrayUnorderedList<Room> rooms, String name) {
        for(Room room : rooms) {
            if(room.getName().equals(name)) {
                return room;
            }
        }
        return null;
    }

    /*
    ITEMS
     */

    /*
    ALTERAR AS ROOMS (CHAMAR O METODO GETROOM())
     */
    private ArrayUnorderedList<Item> jsonToItems(JSONArray itemsJSON, ArrayUnorderedList<Room> rooms){
        ArrayUnorderedList<Item> items = new ArrayUnorderedList<>(itemsJSON.size());

        for(int i = 0; i < itemsJSON.size(); i++) {
            items.addToRear(jsonToItem((JSONObject) itemsJSON.get(i), rooms));
        }

        return items;
    }

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

    /*
    ENEMIES
    */

    private ArrayUnorderedList<Enemy> jsonToEnemies(JSONArray enemiesJson, ArrayUnorderedList<Room> rooms){
        ArrayUnorderedList<Enemy> enemies = new ArrayUnorderedList<>(enemiesJson.size());

        for(int i = 0; i < enemiesJson.size(); i++) {
            enemies.addToRear(this.jsonToEnemy((JSONObject) enemiesJson.get(i), rooms));
        }
        return enemies;
    }

    private Enemy jsonToEnemy(JSONObject enemyJson, ArrayUnorderedList<Room> rooms){
        String name = (String) enemyJson.get("nome");
        int power = ((Long) enemyJson.get("poder")).intValue();
        String roomName = (String) enemyJson.get("divisao");
        Room room = getRoom(rooms, roomName);

        Enemy enemy = new Enemy(name, power, room);
        room.addEnemy(enemy);
        return enemy;
    }

}