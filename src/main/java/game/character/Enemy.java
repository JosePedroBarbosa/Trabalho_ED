package game.character;

import game.map.Room;
import game.interfaces.IEnemy;

/**
 * The Enemy class represents an enemy character in the game.
 */
public class Enemy extends Entity implements IEnemy {
    /**
     * Creates a new instance of an Enemy with the specified name, power, and initial room.
     *
     * @param name        The name of the enemy.
     * @param power       The attack power of the enemy.
     * @param currentRoom The room where the enemy is initially located.
     */
    public Enemy(String name, int power, Room currentRoom){
        super(name, power, currentRoom);
    }

    /**
     * The attack method allows the enemy to attack the player if they are in the same room.
     * It causes damage to the player and checks if the player is defeated.
     * If the player is defeated, a message is printed.
     */
    @Override
    public void attack() {
        if(this.currentRoom.hasPlayer()){
            this.currentRoom.getPlayer().takeDamage(this.power);
            System.out.println("Enemy " + this.getName() + " attacked Player, causing " + this.power + " damage.");

            Player player = this.currentRoom.getPlayer();
            if (!player.isAlive()) {
                System.out.println("Player " + player.getName() + " was defeated.");
            }
        }
    }
}