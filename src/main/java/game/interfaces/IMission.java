package game.interfaces;

import dataStructures.implementations.ArrayUnorderedList;
import game.character.Enemy;
import game.character.Player;
import game.items.Item;
import game.map.Map;
import game.map.Room;
import game.map.Target;

public interface IMission {

    /**
     * Returns the mission code
     * @return The mission code
     */
    public String getMissionCode();

    /**
     * Sets the mission code
     * @param missionCode the new mission code
     */
    public void setMissionCode(String missionCode);

    /**
     * Returns the version number of the mission
     * @return the version number of the mission
     */
    public int getVersion();

    /**
     * Sets the version number of the mission
     * @param version the new version number of the mission
     */
    public void setVersion(int version);

    /**
     * Returns the player associated with this mission.
     * @return the player associated with this mission.
     */
    public Player getPlayer();

    /**
     * Sets the player for the mission.
     * @param player the new player
     */
    public void setPlayer(Player player);

    /**
     * Returns the list of items in the mission.
     * @return the list of items in the mission.
     */
    public ArrayUnorderedList<Item> getItems();

    /**
     * Sets the list of items for the mission.
     * @param items the list of items to set.
     */
    public void setItems(ArrayUnorderedList<Item> items);

    /**
     * Returns the target of the mission.
     * @return the target of the mission.
     */
    public Target getTarget();

    /**
     * Sets the target for the mission.
     * @param target the target to set.
     */
    public void setTarget(Target target);

    /**
     * Returns the list of enemies in the mission.
     * @return the list of enemies in the mission.
     */
    public ArrayUnorderedList<Enemy> getEnemies();

    /**
     * Sets the list of enemies for the mission.
     * @param enemies the list of enemies to set.
     */
    public void setEnemies(ArrayUnorderedList<Enemy> enemies);

    /**
     * Returns the mission map.
     * @return the mission map.
     */
    public Map getMissionMap();

    /**
     * Sets the mission map.
     * @param missionMap the mission map to set.
     */
    public void setMissionMap(Map missionMap);

    /**
     * Returns the list of rooms for entry and exit points.
     * @return the list of rooms for entry and exit points.
     */
    public ArrayUnorderedList<Room> getEntriesAndExits();

    /**
     * Sets the list of rooms for entry and exit points.
     * @param entriesAndExits the list of rooms to set.
     */
    public void setEntriesAndExits(ArrayUnorderedList<Room> entriesAndExits);

}
