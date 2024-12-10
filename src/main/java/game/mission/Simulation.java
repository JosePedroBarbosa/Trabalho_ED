package game.mission;

import dataStructures.implementations.ArrayUnorderedList;
import game.character.Enemy;
import game.character.Player;
import game.items.Item;
import game.map.Room;

/**
 * Provides utility methods for simulating gameplay mechanics such as moving players and enemies
 * between rooms, and filtering enemies based on their locations relative to the player.
 */
public class Simulation {

    /**
     * Moves the player to a specified room.
     *
     * @param mission the mission
     * @param room the target room to move the player to
     * @throws IllegalArgumentException if the mission or target room is null
     */
    protected static void movePlayerToRoom(Mission mission, Room room){
        if (mission == null || room == null) {
            throw new IllegalArgumentException("Mission or room cannot be null.");
        }

        Player player = mission.getPlayer();
        Room currentRoom = player.getCurrentRoom();

        if (currentRoom != null) {
            currentRoom.removePlayer();
        }
        player.setCurrentRoom(room);
        player.getCurrentRoom().setPlayer(player);

        System.out.println("Player Moved To " + room.getName());


        for(Item item : player.getCurrentRoom().getItems()){
            if(item != null){
                player.collectItem(item);
            }
        }
    }

    /**
     * Moves an enemy to the specified room within the mission.
     * Updates the enemy's current room, removing it from the previous room and adding it to the new room.
     *
     * @param mission the current mission containing the enemy and rooms.
     * @param enemy the enemy to be moved.
     * @param room the target room to move the enemy to.
     * @throws IllegalArgumentException if the mission or room is null.
     */
    protected static void moveEnemyToRoom(Mission mission, Enemy enemy, Room room){
        if (mission == null || room == null) {
            throw new IllegalArgumentException("Mission and room cannot be null.");
        }

        Room currentRoom = enemy.getCurrentRoom();

        if (currentRoom != null) {
            enemy.getCurrentRoom().removeEnemy(enemy);
        }
        enemy.setCurrentRoom(room);
        enemy.getCurrentRoom().addEnemy(enemy);
    }

    /**
     * Filters and returns a list of enemies that are not in the same room as the player.
     *
     * @param mission the current mission containing the player and enemies.
     * @return an unordered list of enemies that are in rooms other than the player's current room.
     */
    protected static ArrayUnorderedList<Enemy> filterEnemiesNotInPlayerRoom(Mission mission) {
        ArrayUnorderedList<Enemy> filteredEnemies = new ArrayUnorderedList<>();
        for (Enemy enemy : mission.getEnemies()) {
            if (enemy != null && !enemy.getCurrentRoom().equals(mission.getPlayer().getCurrentRoom())) {
                filteredEnemies.addToRear(enemy);
            }
        }
        return filteredEnemies;
    }


}
