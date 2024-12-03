package game.character;

import game.map.Room;
import game.interfaces.IEnemy;

public class Enemy extends Entity implements IEnemy {

    public Enemy(String name, int power, Room currentRoom){
        super(name, power, currentRoom);
    }

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
