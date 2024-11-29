package game.character;

import game.map.Room;
import game.interfaces.IEnemy;

public class Enemy extends Character implements IEnemy {

    public Enemy(String name, int power, Room currentRoom){
        super(name, power, currentRoom);
    }

    @Override
    public void atack() {
        if(this.currentRoom.hasPlayer()){
            this.currentRoom.getPlayer().takeDamage(this.power);
        }
    }
}
