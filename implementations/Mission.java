package implementations;

import game.map.Target;
import game.map.Room;
import game.map.Map;
import game.character.Enemy;
import game.items.Item;
import dataStructures.implementations.ArrayUnorderedList;

public class Mission {
    private String codMissao;
    private int version;
    private Target target;
    private ArrayUnorderedList<Enemy> enemies;
    private ArrayUnorderedList<Room> entriesAndExits;
    private ArrayUnorderedList<Item> items;
    private Map missionMap;

    public Mission(String codMissao, int version, Target target, Map missionMap){
        this.codMissao = codMissao;
        this.version = version;
        this.target = target;
        this.missionMap = missionMap;
    }

    public String getCodMissao(){
        return codMissao;
    }

    public void setCodMissao(String codMissao){
        this.codMissao = codMissao;
    }

    public int getVersion(){
        return this.version;
    }

    public void setVersion(int version){
        this.version = version;
    }

    public Target getTarget(){
        return target;
    }

    public void setTarget(Target target){
        this.target = target;
    }

    public ArrayUnorderedList<Enemy> getEnemies(){
        return enemies;
    }

    public void setEnemies(ArrayUnorderedList<Enemy> enemies){
        this.enemies = enemies;
    }

    public ArrayUnorderedList<Room> getEntriesAndExits(){
        return entriesAndExits;
    }

    public void setEntriesAndExits(ArrayUnorderedList<Room> entriesAndExits){
        this.entriesAndExits = entriesAndExits;
    }

    public ArrayUnorderedList<Item> getItems(){
        return items;
    }

    public void setItems(ArrayUnorderedList<Item> items){
        this.items = items;
    }

    public Map getMissionMap(){
        return missionMap;
    }

    public void setMissionMap(Map missionMap){
        this.missionMap = missionMap;
    }
}
