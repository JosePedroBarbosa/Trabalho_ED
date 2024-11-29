package game.data;


import dataStructures.implementations.ArrayUnorderedList;

import dataStructures.implementations.Graph;
import game.character.Enemy;

import game.items.HealthKit;
import game.items.Item;

import game.map.Map;
import game.map.Room;
import game.map.Target;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.nio.file.Files;
import java.nio.file.Path;

public class ImportData {

    private static String missionSettingsPath = ".\\settings\\MissionSettings.json";
    private static String gameSettingsPath = ".\\settings\\GameSettings.json";

    public void importMissionData() {

        JSONParser parser = new JSONParser();
        String jsonText = "";

        try {
            jsonText = Files.readString(Path.of(missionSettingsPath));

            System.out.println(jsonText);

            JSONObject mission = (JSONObject) parser.parse(jsonText);

            String code = (String) mission.get("cod-missao");
            int versao = ((Long) mission.get("versao")).intValue();
            ArrayUnorderedList<Room> divisoes = jsonToRooms((JSONArray) mission.get("edificio"));
            ArrayUnorderedList<Room> entradas = jsonToRooms((JSONArray) mission.get("entradas-saidas"));
            ArrayUnorderedList<Enemy> enemies = jsonToEnemies((JSONArray) mission.get("inimigos"));
            Target target = jsonToTarget((JSONObject) mission.get("alvo"));
            ArrayUnorderedList<Item> items = jsonToItems((JSONArray) mission.get("items"));

            //FALTA IMPORTAR AS LIGAÇÕES (Map)
            // criar a missao com tudo o que extraimos

            Map map = new Map();

            for(Room room : divisoes) {
                map.insertRoom(room);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



    }
    /*
    TARGET
    */
    public Target jsonToTarget(JSONObject targetJson){
        Room room = new Room((String) targetJson.get("divisao"));
        String type = (String) targetJson.get("tipo");

        return new Target(room, type);
    }
    /*
    ROOMS
    */
    public ArrayUnorderedList<Room> jsonToRooms(JSONArray roomsJson){
        ArrayUnorderedList<Room> rooms = new ArrayUnorderedList<>(roomsJson.size());

        for(int i = 0; i < roomsJson.size(); i++) {
            rooms.addToRear(new Room((String) roomsJson.get(i)));
        }
        return rooms;
    }

    /*
    ITEMS
     */
    public ArrayUnorderedList<Item> jsonToItems(JSONArray itemsJSON){
        ArrayUnorderedList<Item> items = new ArrayUnorderedList<>(itemsJSON.size());

        for(int i = 0; i < itemsJSON.size(); i++) {
            items.addToRear(jsonToItem((JSONObject) itemsJSON.get(i)));
        }

        return items;
    }

    public Item jsonToItem(JSONObject itemJSON){
        Room room = new Room((String) itemJSON.get("divisao"));
        int points;

        if (itemJSON.containsKey("pontos-recuperados")) {
            return new HealthKit(((Long) itemJSON.get("pontos-recuperados")).intValue());
        }else{
            return new HealthKit(((Long) itemJSON.get("pontos-extra")).intValue());
        }
    }

    /*
    ENEMIES
    */

    public ArrayUnorderedList<Enemy> jsonToEnemies(JSONArray enemiesJson){
        ArrayUnorderedList<Enemy> enemies = new ArrayUnorderedList<>(enemiesJson.size());

        for(int i = 0; i < enemiesJson.size(); i++) {
            enemies.addToRear(this.jsonToEnemy((JSONObject) enemiesJson.get(i)));
        }
        return enemies;
    }

    public Enemy jsonToEnemy(JSONObject enemyJson){
        String name = (String) enemyJson.get("nome");
        int power = ((Long) enemyJson.get("poder")).intValue();
        String roomName = (String) enemyJson.get("divisao");
        Room room = new Room(roomName);

        return new Enemy(name, power, room);
    }

}
