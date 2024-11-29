package game.character;

import game.items.Item;
import game.items.HealthKit;
import game.items.Shield;

import game.exceptions.FullBackPackException;

import game.items.Backpack;
import game.map.Room;

import game.interfaces.IPlayer;

public class Player extends Character implements IPlayer {

    private Backpack backpack;

    public Player(String name, int power, Room currentRoom) {
        super(name, power, currentRoom);
        this.backpack = new Backpack();
    }

    @Override
    public void atack() {
        for (Enemy enemy : this.currentRoom.getEnemies()) {
            enemy.takeDamage(this.power);
            if(!enemy.isAlive()){
                this.currentRoom.removeEnemy(enemy);
                System.out.println("Enemy " + enemy.getName() + " Died");
            }
        }
    }

    @Override
    public void collectItem(Item item) {
        if (item instanceof Shield) {
            this.health += item.getGivenPoints();
            this.currentRoom.removeItem(item);
            System.out.println("Shield collected! Extra health added: " + item.getGivenPoints());
        } else if (item instanceof HealthKit) {
            try {
                this.backpack.addItem(item);
                this.currentRoom.removeItem(item);
                System.out.println("HealthKit collected! Stored in backpack.");
            } catch (FullBackPackException ex) {
                System.out.println("Backpack is full, yhe HealthKit has not been collected");
            }

        }
    }

}
