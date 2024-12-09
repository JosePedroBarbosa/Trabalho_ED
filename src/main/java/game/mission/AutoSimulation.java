package game.mission;

import dataStructures.implementations.ArrayUnorderedList;
import dataStructures.implementations.Network;
import game.character.Enemy;
import game.character.Entity;
import game.character.Player;
import game.data.ImportData;
import game.exceptions.EmptyBackPackException;
import game.map.Room;
import game.settings.GameSettings;

import java.util.Random;

/**
 * Class responsible for simulating an automatic mission execution.
 * It handles player decisions, enemy movements, and interactions.
 */
public class AutoSimulation extends Simulation {

    private static Network<Room> currentNetwork;

    /**
     * Executes the auto-simulation of the game based on the given mission.
     * The simulation ends when the mission is completed or the player dies.
     *
     * @param mission the mission to be simulated.
     */
    public static void autoSimulation(Mission mission){
        ImportData importer = new ImportData();
        importer.importCurrentMissionData();

        currentNetwork = mission.getMissionMap().getMap();

        boolean gameOver = false;

        while(!gameOver){
            updateCurrentNetwork(mission);
            gameOver = playerTurn(mission);

            if(!mission.getPlayer().isAlive()){
                System.out.println("Mission Failed, The Player Died");
                gameOver = true;
            }
        }

        System.out.println("Player health: " + mission.getPlayer().getLife() + "HP");
    }

    /**
     * Updates the edge weights of the mission's network graph based on room conditions.
     *
     * @param mission the mission.
     */
    private static void updateCurrentNetwork(Mission mission){
        ArrayUnorderedList<Room> missionRooms = currentNetwork.getVertices();

        for(int i = 1; i < missionRooms.size(); i++){
            Room currentRoom = missionRooms.getByIndex(i - 1);
            for(int j = 0; j < missionRooms.size(); j++){
                Room nextRoom = missionRooms.getByIndex(j);
                //If has connection to the room
                if(currentNetwork.getEdgeWeight(currentRoom, nextRoom) != Double.POSITIVE_INFINITY){
                    double edgeWeigth = calculateEdgeWeigth(currentRoom, nextRoom);
                    currentNetwork.addEdge(currentRoom, nextRoom, edgeWeigth);
                }
            }
        }
    }

    /**
     * Calculates the weight of an edge between two rooms based on the number and power of enemies in the next room.
     *
     * @param currentRoom the current room.
     * @param nextRoom    the next room.
     * @return the calculated edge weight.
     */
    public static double calculateEdgeWeigth(Room currentRoom, Room nextRoom){
        double weight = 0;

        if(nextRoom.hasEnemies()){
            for(Enemy enemy : nextRoom.getEnemies()){
                if(enemy != null && enemy.isAlive()){
                    weight += enemy.getPower();
                }
            }
        }else {
            weight = 0; //default value
        }

        return weight;
    }

    /**
     * Handles the player's turn, determining the best action and executing it.
     *
     * @param mission the current mission.
     * @return true if the game is over, false otherwise.
     */
    private static boolean playerTurn(Mission mission){
        int bestChoice = getBestPlayerAction(mission);
        switch(bestChoice) {
            case 1:
                Room currentRoom = mission.getPlayer().getCurrentRoom();
                Room nextRoom;
                if(currentRoom == null){
                    nextRoom = getBestRoomToMove(mission);
                }else {
                    nextRoom = getBestRoomToMove(mission);
                }
                movePlayerToRoom(mission, nextRoom);

                if (mission.getTarget().getCurrentRoom().equals(nextRoom)) {
                    if (nextRoom.hasEnemies()) {
                        handleScenario5(mission);
                    } else {
                        handleScenario6(mission);
                    }
                }else{
                    if (!mission.getPlayer().getCurrentRoom().getEnemies().isEmpty()) {
                        handleScenario1(mission);
                    }else{
                        handleScenario2(mission);
                    }
                }
                break;
            case 2:
                handleScenario4(mission);
                break;
            case 0:
                if (mission.getTarget().isPickedUp()) {
                    System.out.println("To Cruz left the building with the Target. Mission completed!");
                } else {
                    System.out.println("To Cruz left the building without the Target. Mission failed!");
                }
                return true;
            default:
                System.out.println("ERROR");
                break;
        }

        return false;
    }

    /**
     * Determines the best action for the player to take based on the current game state.
     *
     * @param mission the current mission.
     * @return an integer representing the action to take:
     * 0 - Exit the building.
     * 1 - Move to a new room.
     * 2 - Use a health kit.
     */
    public static int getBestPlayerAction(Mission mission){
        Player player = mission.getPlayer();
        //If the player has the target and is near to an entrance/exit he should leave
        if(mission.getTarget().isPickedUp() && player.getCurrentRoom().isEntranceAndExit()){
            return 0;
        }
        //If the player has low life he should use a kit
        if(player.getLife() < 50 && player.getBackpack().getBackpackSize() > 0){
            return 2;
        }

        //Otherwise he moves to a room
        return 1;
    }

    /**
     * Handles Scenario 1: The player enters a room with enemies and initiates a confrontation.
     * - The player attacks all enemies in the room, dealing damage simultaneously.
     * - If enemies remain alive, they counter-attack the player and remain in the room,
     *   while other enemies in the building move randomly.
     *
     * @param mission the current mission containing the player and enemies.
     */
    public static void handleScenario1(Mission mission){
        System.out.println("The player encountered enemies in the room. Confrontation Started");
        confrontation(mission.getPlayer());
        enemiesRandomMove(mission, filterEnemiesNotInPlayerRoom(mission));
    }

    /**
     * Handles Scenario 2: The player enters a room without enemies.
     * - All Enemies Move Randomly
     *
     * @param mission the current mission containing the player and enemies.
     */
    public static void handleScenario2(Mission mission){
        enemiesRandomMove(mission, mission.getEnemies());
    }

    /**
     * Handles Scenario 3: The Enemy enters a room with the Player and initiates a confrontation.
     * - The Enemy attacks the player in the room, dealing damage.
     * - If the player remain alive, they counter-attack the enemy
     *
     * @param mission the current mission containing the player and enemies.
     * @param enemy the enemy that attacks the player
     */
    public static void handleScenario3(Mission mission, Enemy enemy){
        System.out.println("The enemy encountered the player in the room. Confrontation Started");
        confrontation(enemy);
        enemiesRandomMove(mission, filterEnemiesNotInPlayerRoom(mission));
    }

    /**
     * Handles Scenario 4: The Player uses a health kit.
     * - The Player ends is turn.
     * - All Enemies move Randomly
     * - If the Player does not have items to use the turn is canceled
     *
     * @param mission the current mission containing the player and enemies.
     */
    public static void handleScenario4(Mission mission) {
        try {
            mission.getPlayer().useItem();
            enemiesRandomMove(mission, mission.getEnemies());
        } catch (EmptyBackPackException e) {
            System.out.println("No Items In The BackPack");
        }
    }

    /**
     * Handles Scenario 5: The player enters a room with the target but found enemies in the room and initiates a confrontation.
     * - The player attacks all enemies in the room, dealing damage simultaneously.
     * - If enemies remain alive, they counter-attack the player and remain in the room,
     *   while other enemies in the building move randomly.
     * - When the confrontation ends if the player remain alive he secure the target
     *
     * @param mission the current mission containing the player and enemies.
     */
    private static void handleScenario5(Mission mission) {
        System.out.println("You found the target but there are enemies in the room. Confrontation Started.");
        confrontation(mission.getPlayer());
        handleTarget(mission);
        enemiesRandomMove(mission, mission.getEnemies());
    }

    /**
     * Handles Scenario 6: The player enters a room with the target and are not enemies in the room.
     * - The player secure the target.
     *
     * @param mission the current mission containing the player and enemies.
     */
    private static void handleScenario6(Mission mission) {
        System.out.println("You found the target.");
        handleTarget(mission);
    }

    /**
     * If the player is in the same room as the target, the player secure the target.
     *
     * @param mission the current mission containing the player and enemies.
     */
    private static void handleTarget(Mission mission) {
        if (mission.getPlayer().getCurrentRoom().equals(mission.getTarget().getCurrentRoom())) {
            mission.getTarget().setPickedUp(true);
            System.out.println("Target Secured.");
        }else{
            System.out.println("The Target Are Not In The Room");
        }
    }

    /**
     * Determines the best room for the player to move into, minimizing potential damage.
     * Considers the player's current state and mission objectives (target or exit).
     *
     * @param mission the current mission containing the player and rooms.
     * @return the next room the player should move to.
     */
    public static Room getBestRoomToMove(Mission mission){
        ArrayUnorderedList<Room> bestPath;

        //If the player is outside the building
        if(mission.getPlayer().getCurrentRoom() == null){
            return getBestEntranceToMove(mission);
        }
        //If the player has secured the target he should go to an exit
        if(mission.getTarget().isPickedUp()){
            bestPath = getBestPathToExit(mission);
        }else{ //Otherwise he goes to the target
            bestPath = getBestPathToTarget(mission);
        }

        //returns the next room he should go
        return bestPath.getByIndex(1);
    }

    /**
     * Finds the best entrance for the player to move toward the target room.
     *
     * @param mission the current mission containing the player and the map.
     * @return the best entrance/exit room for the player.
     */
    public static Room getBestEntranceToMove(Mission mission) {
        return getBestRoom(mission.getEntriesAndExits(), mission.getTarget().getCurrentRoom());
    }

    /**
     * Calculates the best path for the player to reach the target's room.
     *
     * @param mission the current mission containing the player and the target.
     * @return a list of rooms representing the best path to the target.
     */
    public static ArrayUnorderedList<Room> getBestPathToTarget(Mission mission) {
        return currentNetwork.shortestPath(mission.getPlayer().getCurrentRoom(), mission.getTarget().getCurrentRoom());
    }

    /**
     * Calculates the best path for the player to reach the nearest exit room.
     *
     * @param mission the current mission containing the player and rooms.
     * @return a list of rooms representing the best path to an exit.
     */
    public static ArrayUnorderedList<Room> getBestPathToExit(Mission mission) {
        Room bestExit = getBestRoom(mission.getEntriesAndExits(), mission.getPlayer().getCurrentRoom());

        return bestExit != null ? currentNetwork.shortestPath(mission.getPlayer().getCurrentRoom(), bestExit) : null;
    }


    /**
     * Finds the best room from a list of rooms based on the shortest path to a target room.
     *
     * @param rooms the list of candidate rooms.
     * @param target the target room to evaluate against.
     * @return the best room with the shortest path to the target.
     */
    private static Room getBestRoom(ArrayUnorderedList<Room> rooms, Room target) {
        if (rooms == null || target == null) {
            return null;
        }

        Room bestRoom = null;
        double shortestWeight = Double.POSITIVE_INFINITY;

        for (Room room : rooms) {
            if (room != null) {
                ArrayUnorderedList<Room> path = currentNetwork.shortestPath(room, target);
                double pathWeight = calculatePathWeight(path);
                if (!path.isEmpty() && pathWeight < shortestWeight) {
                    shortestWeight = pathWeight;
                    bestRoom = room;
                }
            }
        }
        return bestRoom;
    }

    /**
     * Calculates the total weight of a path between rooms.
     *
     * @param path the list of rooms representing the path.
     * @return the total weight of the path.
     */
    private static double calculatePathWeight(ArrayUnorderedList<Room> path) {
        double totalWeight = 0;

        for (int i = 1; i < path.size(); i++) {
            totalWeight += currentNetwork.getEdgeWeight(path.getByIndex(i - 1), path.getByIndex(i));
        }

        return totalWeight;
    }

    /**
     * Moves enemies randomly throughout the building.
     * Enemies may move multiple times, and if an enemy enters the player's room, a confrontation is triggered.
     *
     * @param mission the current mission containing the enemies and rooms.
     * @param enemies the list of enemies to move.
     */
    public static void enemiesRandomMove(Mission mission, ArrayUnorderedList<Enemy> enemies){
        Random rand = new Random();
        int maxEnemyMoves = GameSettings.getMaxEnemyMoves();

        for (Enemy enemy : enemies) {
            if(enemy != null && enemy.isAlive()){
                Room currentRoom = enemy.getCurrentRoom();
                ArrayUnorderedList<Room> neighbours = mission.getMissionMap().getMap().getNeighbours(currentRoom);

                //random between 1 and maxEnemyMoves
                int moves = rand.nextInt(maxEnemyMoves) + 1;

                for (int i = 0; i < moves; i++) {
                    if (neighbours.isEmpty()) {
                        break;
                    }

                    Room nextRoom = neighbours.getByIndex(rand.nextInt(neighbours.size()));
                    moveEnemyToRoom(mission, enemy, nextRoom);

                    //if the nextRoom have the Player the enemy has priority to attack
                    if(nextRoom.hasPlayer()){
                        System.out.println("Enemy " + enemy.getName() + " entered the player's room!");
                        handleScenario3(mission, enemy);
                        break;
                    }

                    neighbours = mission.getMissionMap().getMap().getNeighbours(nextRoom);
                }
            }
        }
    }

    /**
     * Handles a confrontation based on the priority entity.
     * @param priorityEntity the entity with priority (Player or Enemy).
     */
    private static void confrontation(Entity priorityEntity) {
        if (priorityEntity instanceof Player) {
            // If the player has the priority
            attackSequence((Player) priorityEntity);
        } else if (priorityEntity instanceof Enemy) {
            // If the enemy has the priority
            attackSequence((Enemy) priorityEntity);
        }else {
            throw new IllegalArgumentException("Invalid priority entity.");
        }
    }

    /**
     * Handles the attack sequence during a confrontation when the Player has priority.
     * The Player may attack or use an item depending on their life and backpack availability.
     *
     * @param player the Player engaging in the confrontation.
     */
    public static void attackSequence(Player player){
        boolean confrontationEnd = false;

        while(!confrontationEnd){
            boolean usedItem = getBestChoice(player.getBackpack().getBackpackSize() > 0, player.getLife());

            if (!usedItem) {
                player.attack();
            } else {
                try {
                    player.useItem();
                    System.out.println("Player used an item and skipped the attack.");
                } catch (EmptyBackPackException e) {
                    System.out.println("No items available in the backpack.");
                    continue;
                }
            }

            //attack all enemies in the room.
            for(Enemy enemy : player.getCurrentRoom().getEnemies()){
                if(enemy != null){
                    if (enemy.isAlive()) {
                        enemy.attack();
                    }
                }
            }

            if(!player.isAlive()){
                System.out.println("The Player Died, The Confrontation Ended");
                confrontationEnd = true;
            }else if(player.getCurrentRoom().getEnemies().isEmpty()){
                System.out.println("All Enemies In The Room Died, The Confrontation Ended");
                confrontationEnd = true;
            }
        }
    }


    /**
     * Handles the attack sequence during a confrontation when the Enemy has priority.
     * The Enemy attacks the Player, and the Player retaliates if still alive.
     *
     * @param enemy the Enemy engaging in the confrontation.
     */
    public static void attackSequence(Enemy enemy){
        boolean confrontationEnd = false;

        while(!confrontationEnd){
            Player playerInTheRoom = enemy.getCurrentRoom().getPlayer();

            if (playerInTheRoom == null) {
                System.out.println("No Player To Attack, Confrontation Ended");
                break;
            }

            System.out.println("Enemy " + enemy.getName() + " is attacking...");
            enemy.attack();

            if (!playerInTheRoom.isAlive()) {
                System.out.println("The Player Died, The Confrontation Ended");
                break;
            }

            boolean usedItem = getBestChoice(playerInTheRoom.getBackpack().getBackpackSize() > 0, playerInTheRoom.getLife());

            if (!usedItem) {
                playerInTheRoom.attack(enemy);
            } else {
                try {
                    playerInTheRoom.useItem();
                    System.out.println("Player used an item and skipped the attack.");
                } catch (EmptyBackPackException e) {
                    System.out.println("No items available in the backpack.");
                    continue;
                }
            }

            if (!enemy.isAlive()) {
                System.out.println("The Enemy Died, The Confrontation Ended");
                break;
            }

            if (playerInTheRoom.getCurrentRoom().getEnemies().isEmpty()) {
                System.out.println("All Enemies In The Room Died, The Confrontation Ended");
                break;
            }

        }
    }

    /**
     * Determines the best choice for the Player during an attack sequence.
     * If the Player has items and low health, they prioritize using an item.
     *
     * @param hasItems true if the Player has items available in their backpack.
     * @param playerLife the current life of the Player.
     * @return true if the Player should use an item, false otherwise.
     */
    private static boolean getBestChoice(boolean hasItems, int playerLife) {
        if(hasItems && playerLife < 50){
            return true;
        }
        return false;
    }

}