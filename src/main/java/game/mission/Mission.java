package game.mission;

import game.character.Player;
import game.exceptions.NoMissionInstantiated;
import game.interfaces.IMission;
import game.map.Target;
import game.map.Room;
import game.map.Map;
import game.character.Enemy;
import game.items.Item;
import dataStructures.implementations.ArrayUnorderedList;

/**
 * Represents a mission in the game.
 * The Mission class follows the Singleton design pattern, ensuring that only one instance of a mission
 * exists during runtime.
 */
public class Mission implements IMission {
    private String missionCode;
    private int version;
    private Player player;
    private Target target;
    private ArrayUnorderedList<Enemy> enemies;
    private ArrayUnorderedList<Item> items;
    private Map missionMap;
    private ArrayUnorderedList<Room> entriesAndExits;

    private static Mission instance; // Unique Instance (Singleton)

    /**
     * The constructor is private to enforce the Singleton design pattern, ensuring that only one instance
     * of the Mission class can exist at a time, and the data can only be initialized through the `initialize()` method.
     *
     * @param missionCode The unique identifier for the mission.
     * @param version The version of the mission.
     * @param player The player associated with this mission.
     * @param target The target the player must achieve in this mission.
     * @param enemies The list of enemies in the mission.
     * @param items The list of items available in the mission.
     * @param missionMap The map of the mission.
     * @param entriesAndExits The list of rooms that are the entries and exits for the mission.
     */
    private Mission(String missionCode, int version, Player player, Target target, ArrayUnorderedList<Enemy> enemies, ArrayUnorderedList<Item> items, Map missionMap, ArrayUnorderedList<Room> entriesAndExits) {
        this.missionCode = missionCode;
        this.version = version;
        this.player = player;
        this.target = target;
        this.enemies = enemies;
        this.items = items;
        this.missionMap = missionMap;
        this.entriesAndExits = entriesAndExits;
    }

    /**
     * Initializes or updates the singleton instance of the Mission class with the provided data.
     *
     * @param missionCode The unique identifier for the mission.
     * @param version The version of the mission.
     * @param player The player associated with this mission.
     * @param target The target the player must achieve in this mission.
     * @param enemies The list of enemies in the mission.
     * @param items The list of items available in the mission.
     * @param missionMap The map of the mission.
     * @param entriesAndExits The list of rooms that are the entries and exits for the mission.
     */
    public static void initialize(String missionCode, int version, Player player, Target target, ArrayUnorderedList<Enemy> enemies, ArrayUnorderedList<Item> items, Map missionMap, ArrayUnorderedList<Room> entriesAndExits) {
        if (instance == null) {
            instance = new Mission(missionCode, version, player, target, enemies, items, missionMap, entriesAndExits);
        } else {
            instance.missionCode = missionCode;
            instance.version = version;
            instance.player = player;
            instance.target = target;
            instance.enemies = enemies;
            instance.items = items;
            instance.missionMap = missionMap;
            instance.entriesAndExits = entriesAndExits;
        }
    }

    /**
     * Returns the single instance of the Mission.
     *
     * @return The instance of the Mission class.
     * @throws NoMissionInstantiated if the Mission instance has not been initialized yet.
     */
    public static Mission getInstance() throws NoMissionInstantiated{
        if (instance == null) {
            throw new NoMissionInstantiated("Mission is not initialized. Call initialize() first.");
        }
        return instance;
    }

    /**
     * Returns the mission code
     * @return The mission code
     */
    public String getMissionCode() {
        return missionCode;
    }

    /**
     * Sets the mission code
     * @param missionCode the new mission code
     */
    public void setMissionCode(String missionCode) {
        this.missionCode = missionCode;
    }

    /**
     * Returns the version number of the mission
     * @return the version number of the mission
     */
    public int getVersion() {
        return version;
    }

    /**
     * Sets the version number of the mission
     * @param version the new version number of the mission
     */
    public void setVersion(int version) {
        this.version = version;
    }

    /**
     * Returns the player associated with this mission.
     * @return the player associated with this mission.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the player for the mission.
     * @param player the new player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Returns the list of items in the mission.
     * @return the list of items in the mission.
     */
    public ArrayUnorderedList<Item> getItems() {
        return items;
    }

    /**
     * Sets the list of items for the mission.
     * @param items the list of items to set.
     */
    public void setItems(ArrayUnorderedList<Item> items) {
        this.items = items;
    }

    /**
     * Returns the target of the mission.
     * @return the target of the mission.
     */
    public Target getTarget() {
        return target;
    }

    /**
     * Sets the target for the mission.
     * @param target the target to set.
     */
    public void setTarget(Target target) {
        this.target = target;
    }

    /**
     * Returns the list of enemies in the mission.
     * @return the list of enemies in the mission.
     */
    public ArrayUnorderedList<Enemy> getEnemies() {
        return enemies;
    }

    /**
     * Sets the list of enemies for the mission.
     * @param enemies the list of enemies to set.
     */
    public void setEnemies(ArrayUnorderedList<Enemy> enemies) {
        this.enemies = enemies;
    }

    /**
     * Returns the mission map.
     * @return the mission map.
     */
    public Map getMissionMap() {
        return missionMap;
    }

    /**
     * Sets the mission map.
     * @param missionMap the mission map to set.
     */
    public void setMissionMap(Map missionMap) {
        this.missionMap = missionMap;
    }

    /**
     * Returns the list of rooms for entry and exit points.
     * @return the list of rooms for entry and exit points.
     */
    public ArrayUnorderedList<Room> getEntriesAndExits() {
        return entriesAndExits;
    }

    /**
     * Sets the list of rooms for entry and exit points.
     * @param entriesAndExits the list of rooms to set.
     */
    public void setEntriesAndExits(ArrayUnorderedList<Room> entriesAndExits) {
        this.entriesAndExits = entriesAndExits;
    }

}