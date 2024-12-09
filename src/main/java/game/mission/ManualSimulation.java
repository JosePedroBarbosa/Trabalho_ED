package game.mission;

import dataStructures.implementations.ArrayUnorderedList;
import game.character.Enemy;
import game.character.Entity;
import game.character.Player;
import game.data.ExportData;
import game.data.ImportData;
import game.data.MissionDisplay;
import game.exceptions.EmptyBackPackException;
import game.exceptions.NoMissionInstantiated;
import game.items.HealthKit;
import game.items.Item;
import game.map.Room;
import game.settings.GameSettings;

import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

/**
 * Class responsible for simulating a manual mission execution.
 * The player can choose their movements and actions while the program handles game logic.
 */
public class ManualSimulation extends Simulation{

    /**
     * The status of the mission: true if successful, false otherwise.
     */
    private static boolean missionSuccess;

    /**
     * The route taken by the player during the simulation.
     */
    private static ArrayUnorderedList<Room> simulationRoute;

    /**
     * Simulates the mission gameplay manually by allowing the player to interact with the mission.
     *
     * @param mission the current mission to simulate.
     * @param scanner the input scanner for reading player inputs.
     * @throws NoMissionInstantiated if no mission has been instantiated.
     */
    public static void manualSimulation(Mission mission, Scanner scanner) throws NoMissionInstantiated {
        ImportData importer = new ImportData();
        importer.importCurrentMissionData();

        boolean gameOver = false;
        simulationRoute = new ArrayUnorderedList<>();

        while(!gameOver){
            gameOver = playerTurn(mission, scanner);

            if(!mission.getPlayer().isAlive()){
                System.out.println("Mission Failed, The Player Died");
                missionSuccess = false;
                gameOver = true;
            }
        }

        ExportData.saveManualSimulationToJson(mission, simulationRoute, missionSuccess);
    }


    /**
     * Prints the available rooms with a specified message.
     *
     * @param rooms   the rooms to display.
     * @param message the message to display before the list of rooms.
     */
    public static void printRooms(ArrayUnorderedList<Room> rooms, String message){
        System.out.println(message);
        for(int i = 0; i < rooms.size(); i++){
            System.out.println("   " + (i + 1) + ". " + rooms.getByIndex(i));
        }
        System.out.println();
    }

    /**
     * Allows the user to select a room from a list of available rooms.
     *
     * @param rooms   the list of available rooms.
     * @param message the prompt message for the user.
     * @param scanner the input scanner for reading player inputs.
     * @return the selected room.
     */
    public static Room selectRoom(ArrayUnorderedList<Room> rooms, String message, Scanner scanner){
        printRooms(rooms, message);

        while(true){
            try{
                System.out.print("Select a room: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                if(choice > 0 && choice <= rooms.size()){
                    return rooms.getByIndex(choice - 1);
                }else{
                    System.out.println("Invalid choice, Try again.");
                }
            }catch(InputMismatchException ex){
                scanner.nextLine();
                System.out.println("Invalid input, Try again.");
            }
        }

    }

    /**
     * Handles the player's turn and the corresponding actions based on their input.
     *
     * @param mission the current mission being played.
     * @param scanner the input scanner for reading player inputs.
     * @return true if the game ends, false otherwise.
     */
    public static boolean playerTurn(Mission mission, Scanner scanner){
        int choice = printOptions(mission, scanner);;

        switch(choice) {
            case 1:
                Room currentRoom = mission.getPlayer().getCurrentRoom();
                Room nextRoom;
                if(currentRoom == null){
                    nextRoom = selectRoom(mission.getEntriesAndExits(), "Available Rooms", scanner);
                }else {
                    nextRoom = selectRoom(mission.getMissionMap().getMap().getNeighbours(mission.getPlayer().getCurrentRoom()), "Available Rooms: ", scanner);
                }
                movePlayerToRoom(mission, nextRoom);
                simulationRoute.addToRear(nextRoom);

                if (mission.getTarget().getCurrentRoom().equals(nextRoom)) {
                    if (nextRoom.hasEnemies()) {
                        handleScenario5(mission, scanner);
                    } else {
                        handleScenario6(mission);
                    }
                }else{
                    if (!mission.getPlayer().getCurrentRoom().getEnemies().isEmpty()) {
                        handleScenario1(mission, scanner);
                    }else{
                        handleScenario2(mission, scanner);
                    }
                }
                break;
            case 2:
                handleScenario4(mission, scanner);
                break;
            case 3:
                printBestPathToHealthKit(mission);
                printBestPathToTarget(mission);
                if(mission.getTarget().isPickedUp()) {
                    //printBestPathToExit(mission);
                }
                break;
            case 4:
                printMissionStatus(mission);
                break;
            case 5:
                MissionDisplay.showImage(scanner);
                break;
            case 6:
                mission.getMissionMap().printMap();
                break;
            case 0:
                if (mission.getTarget().isPickedUp()) {
                    System.out.println("To Cruz left the building with the Target. Mission completed!");
                    missionSuccess = true;
                } else {
                    System.out.println("To Cruz left the building without the Target. Mission failed!");
                    missionSuccess = false;
                }
                return true;
            default:
                System.out.println("Invalid choice, Try again.");
                break;
        }

        return false;
    }

    /**
     * Handles Scenario 1: The player enters a room with enemies and initiates a confrontation.
     * - The player attacks all enemies in the room, dealing damage simultaneously.
     * - If enemies remain alive, they counter-attack the player and remain in the room,
     *   while other enemies in the building move randomly.
     *
     * @param mission the current mission containing the player and enemies.
     */
    public static void handleScenario1(Mission mission, Scanner scanner){
        System.out.println("The player encountered enemies in the room. Confrontation Started");
        confrontation(mission.getPlayer(), scanner);
        enemiesRandomMove(mission, filterEnemiesNotInPlayerRoom(mission), scanner);
    }

    /**
     * Handles Scenario 2: The player enters a room without enemies.
     * - All Enemies Move Randomly
     *
     * @param mission the current mission containing the player and enemies.
     */
    public static void handleScenario2(Mission mission, Scanner scanner){
        enemiesRandomMove(mission, mission.getEnemies(), scanner);
    }

    /**
     * Handles Scenario 3: The Enemy enters a room with the Player and initiates a confrontation.
     * - The Enemy attacks the player in the room, dealing damage.
     * - If the player remain alive, they counter-attack the enemy
     *
     * @param mission the current mission containing the player and enemies.
     * @param enemy the enemy that attacks the player
     */
    public static void handleScenario3(Mission mission, Enemy enemy, Scanner scanner){
        System.out.println("The enemy encountered the player in the room. Confrontation Started");
        confrontation(enemy, scanner);
        enemiesRandomMove(mission, filterEnemiesNotInPlayerRoom(mission), scanner);
    }

    /**
     * Handles Scenario 4: The Player uses a health kit.
     * - The Player ends is turn.
     * - All Enemies move Randomly
     * - If the Player does not have items to use the turn is canceled
     *
     * @param mission the current mission containing the player and enemies.
     */
    public static void handleScenario4(Mission mission, Scanner scanner) {
        try {
            mission.getPlayer().useItem();
            enemiesRandomMove(mission, mission.getEnemies(), scanner);
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
    private static void handleScenario5(Mission mission, Scanner scanner) {
        System.out.println("You found the target but there are enemies in the room. Confrontation Started.");
        confrontation(mission.getPlayer(), scanner);
        handleTarget(mission);
        enemiesRandomMove(mission, mission.getEnemies(), scanner);
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
     * Handles a confrontation based on the priority entity.
     * @param priorityEntity the entity with priority (Player or Enemy).
     * @param scanner the input scanner for player actions.
     */
    private static void confrontation(Entity priorityEntity, Scanner scanner) {
        if (priorityEntity instanceof Player) {
            // If the player has the priority
            attackSequence((Player) priorityEntity, scanner);
        } else if (priorityEntity instanceof Enemy) {
            // If the enemy has the priority
            attackSequence((Enemy) priorityEntity, scanner);
        }else {
            throw new IllegalArgumentException("Invalid priority entity.");
        }
    }

    /**
     * Handles the attack sequence during a confrontation when the Player has priority.
     * The Player may attack or use an item depending on their life and backpack availability.
     *
     * @param player the Player engaging in the confrontation.
     * @param scanner the input scanner for player actions.
     */
    public static void attackSequence(Player player, Scanner scanner){
        boolean confrontationEnd = false;

        while(!confrontationEnd){
            System.out.println("\nConfrontation Options:");
            System.out.println("1 - Attack");
            if (player.getBackpack().getBackpackSize() > 0) {
                System.out.println("2 - Use Item");
            } else {
                System.out.println("2̶ ̶-̶ ̶U̶s̶e̶ ̶I̶t̶e̶m̶");
            }

            int choice = getPlayerChoice(scanner, player.getBackpack().getBackpackSize() > 0);

            if (choice == 1) {
                player.attack();
            } else if (choice == 2) {
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
     * @param scanner the input scanner for player actions.
     */
    public static void attackSequence(Enemy enemy, Scanner scanner){
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

            System.out.println("\nConfrontation Options:");
            System.out.println("1 - Attack");
            if (playerInTheRoom.getBackpack().getBackpackSize() > 0) {
                System.out.println("2 - Use Item");
            } else {
                System.out.println("2̶ ̶-̶ ̶U̶s̶e̶ ̶I̶t̶e̶m̶");
            }

            int choice = getPlayerChoice(scanner, playerInTheRoom.getBackpack().getBackpackSize() > 0);

            if (choice == 1) {
                playerInTheRoom.attack(enemy);
            } else if (choice == 2) {
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
     * Retrieves and validates the player's choice from the menu options.
     *
     * @param scanner the input scanner for user input.
     * @param hasItems a boolean indicating if the player has items available for use.
     * @return the validated choice entered by the player.
     */
    private static int getPlayerChoice(Scanner scanner, boolean hasItems) {
        while (true) {
            try {
                System.out.print("> ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == 1 || (choice == 2 && hasItems)) {
                    return choice;
                } else {
                    System.out.println("Invalid choice. Try again.");
                }
            } catch (InputMismatchException ex) {
                scanner.nextLine();
                System.out.println("Invalid input. Try again.");
            }
        }
    }

    /**
     * Handles random movement for all enemies in the mission.
     * If an enemy moves into the same room as the player, an immediate confrontation occurs.
     *
     * @param mission the mission containing the game state.
     * @param enemies the list of enemies to move.
     * @param scanner the input scanner for player interactions.
     */
    public static void enemiesRandomMove(Mission mission, ArrayUnorderedList<Enemy> enemies, Scanner scanner){
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
                        handleScenario3(mission, enemy, scanner);
                        break;
                    }

                    neighbours = mission.getMissionMap().getMap().getNeighbours(nextRoom);
                }
            }
        }
    }

    /**
     * Prints the shortest path from the player's current room to the mission's target room.
     *
     * @param mission the mission containing the map and player details.
     */
    private static void printBestPathToTarget(Mission mission){
        Iterator it = mission.getMissionMap().getMap().iteratorShortestPath(mission.getPlayer().getCurrentRoom(), mission.getTarget().getCurrentRoom());

        System.out.println("Best path to the target:");
        while(it.hasNext()){
            Object current = it.next();
            if(current != null){
                System.out.println("-> " + current.toString());
            }
        }
        System.out.println();
    }

    /**
     * Finds and prints the shortest path from the player's current room to the nearest health kit in the mission.
     *
     * @param mission the mission containing items and map details.
     */
    private static void printBestPathToHealthKit(Mission mission) {
        ArrayUnorderedList<Item> missionItems = mission.getItems();
        HealthKit targetHealthKit = null;
        int shortestDistance = Integer.MAX_VALUE;
        Room targetRoom = null;

        for (Item item : missionItems) {
            if (item != null && !item.isPickedUp()){
                if(item instanceof HealthKit){
                    HealthKit healthKit = (HealthKit) item;
                    Room healthKitRoom = healthKit.getCurrentRoom();

                    Iterator it = mission.getMissionMap().getMap().iteratorShortestPath(mission.getPlayer().getCurrentRoom(), healthKitRoom);

                    int pathLength = 0;
                    while (it.hasNext()) {
                        Object current = it.next();
                        if (current != null) {
                            pathLength++;
                        }
                    }

                    if (pathLength > 0 && pathLength < shortestDistance) {
                        shortestDistance = pathLength;
                        targetHealthKit = healthKit;
                        targetRoom = healthKitRoom;
                    }
                }
            }
        }

        if (targetHealthKit != null) {
            Iterator it = mission.getMissionMap().getMap().iteratorShortestPath(mission.getPlayer().getCurrentRoom(), targetRoom);

            System.out.println("Best path to the health kit:");
            while (it.hasNext()) {
                Object current = it.next();
                if (current != null) {
                    System.out.println("-> " + current.toString());
                }
            }
        } else {
            System.out.println("No health kit found in the mission.");
        }

        System.out.println();
    }


    /**
     * Prints the current status of the mission, including player health, enemies, and items.
     *
     * @param mission the mission containing the game state.
     */
    private static void printMissionStatus(Mission mission) {
        System.out.println("=== MISSION DETAILS ===");
        System.out.println("Current Location: " + (mission.getPlayer().getCurrentRoom() != null ? mission.getPlayer().getCurrentRoom().getName() : "Outside of the building"));
        System.out.println("Player Health: " + mission.getPlayer().getLife());
        if(mission.getTarget().isPickedUp()){
            System.out.println("Target Secured!");
        }else{
            System.out.println("Target: ");
            System.out.println("    Type: " + mission.getTarget().getType());
            System.out.println("    Location: " + mission.getTarget().getCurrentRoom().getName());
        }

        ArrayUnorderedList<Enemy> enemies = mission.getEnemies();
        if(enemies.isEmpty()){
            System.out.println("All Enemies Annihilated");
        }else{
            System.out.println("Remaining Enemies:");
            for (Enemy enemy : enemies) {
                if (enemy.getLife() > 0) {
                    System.out.println("  - Name: " + enemy.getName() + ", Current Room: " + enemy.getCurrentRoom().getName() + ", Health: " + enemy.getLife());
                }
            }
        }

        ArrayUnorderedList<Item> items = mission.getItems();
        if(items.isEmpty()){
            System.out.println("All Items Picked");
        }else{
            System.out.println("Items in the building: ");
            for(Item item : mission.getItems()) {
                if(!item.isPickedUp()) {
                    System.out.println("  - Name: " + item.getType() + ", Current Room: " + item.getCurrentRoom().getName() + ", Given Health: " + item.getGivenPoints());
                }
            }
        }

        System.out.println("=============================");
    }

    /**
     * Displays the player's options during their turn and validates their choice.
     *
     * @param mission the mission containing the game state.
     * @param scanner the input scanner for player interactions.
     * @return the validated option chosen by the player.
     */
    public static int printOptions(Mission mission, Scanner scanner){

        boolean canUseItem = mission.getPlayer().getBackpack().getBackpackSize() > 0;
        boolean canLeave = mission.getPlayer().getCurrentRoom() != null && mission.getPlayer().getCurrentRoom().isEntranceAndExit();
        boolean availableBestPaths = mission.getPlayer().getCurrentRoom() != null;

        System.out.println("\n================");
        System.out.println("   YOUR TURN    ");
        System.out.println("================\n");

        System.out.println("1 - Move To Room");

        if (canUseItem) {
            System.out.println("2 - Use Item");
        } else {
            System.out.println("2̶ ̶-̶ ̶U̶s̶e̶ ̶I̶t̶e̶m̶");
        }

        if (availableBestPaths) {
            System.out.println("3 - Print Best Paths");
        } else {
            System.out.println("3̶ ̶-̶ ̶P̶r̶i̶n̶t̶ ̶B̶e̶s̶t̶ ̶P̶a̶t̶h̶s̶");
        }
        System.out.println("4 - Print Mission Status");
        System.out.println("5 - Show Map (Image)");
        System.out.println("6 - Show Map (Console)");

        if (canLeave) {
            System.out.println("0 - Leave The Building");
        } else {
            System.out.println("0̶ ̶-̶ ̶L̶e̶a̶v̶e̶ ̶T̶h̶e̶ ̶B̶u̶i̶l̶d̶i̶n̶g̶");
        }

        while(true){
            System.out.print("> ");
            try{
                int choice = scanner.nextInt();
                scanner.nextLine();

                if(choice >= 0 && choice <= 6){
                    if((choice == 2 && !canUseItem) || (choice == 3 && !availableBestPaths) || (choice == 0 && !canLeave)){
                        System.out.println("Invalid Option");
                    }else{
                        return choice;
                    }
                }
            }catch(InputMismatchException ex){
                scanner.nextLine();
                System.out.println("Invalid Option");
            }
        }
    }

}