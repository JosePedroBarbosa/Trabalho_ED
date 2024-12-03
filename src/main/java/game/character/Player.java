package game.character;

import dataStructures.implementations.ArrayUnorderedList;
import game.exceptions.EmptyBackPackException;
import game.items.Item;
import game.items.HealthKit;
import game.items.Shield;

import game.exceptions.FullBackPackException;

import game.items.Backpack;
import game.map.Room;

import game.interfaces.IPlayer;
import game.settings.GameSettings;

public class Player extends Entity implements IPlayer {

    private Backpack backpack;

    public Player(String name, Room currentRoom) {
        super(name, GameSettings.getPlayerPower(), currentRoom);
        this.backpack = new Backpack();
    }

    public Player(String name) {
        super(name, GameSettings.getPlayerPower());
        this.backpack = new Backpack();
    }

    @Override
    public void attack() {
        ArrayUnorderedList<Enemy> copyEnemies = new ArrayUnorderedList<>(this.currentRoom.getEnemies().size());

        for(Enemy enemy : this.getCurrentRoom().getEnemies()){
            if(enemy != null){
                copyEnemies.addToRear(enemy);
            }
        }

        for (Enemy enemy : copyEnemies) {
            if(enemy != null){
                enemy.takeDamage(GameSettings.getPlayerPower());
                System.out.println("Player attacked Enemy " + enemy.getName() + ", causing " + this.power + " damage.");

                if(!enemy.isAlive()){
                    this.currentRoom.removeEnemy(enemy);
                    System.out.println("Enemy " + enemy.getName() + " Died");
                }
            }
        }
    }

    public void attack(Enemy target) {
        if (this.currentRoom.getEnemies().contains(target)) {
            int damageToEnemy = GameSettings.getPlayerPower();
            target.takeDamage(damageToEnemy);
            System.out.println("Player attacked Enemy " + target.getName() + ", causing " + damageToEnemy + " damage.");

            if (!target.isAlive()) {
                this.currentRoom.removeEnemy(target);
                System.out.println("Enemy " + target.getName() + " died!");
            }
        } else {
            System.out.println("Enemy " + target.getName() + " is not in the same room.");
        }
    }

    public void useItem() throws EmptyBackPackException {
        Item item = backpack.useBackpackItem();

        int newHealth = this.health + item.getGivenPoints();

        //limit for the player health
        if((newHealth) > 100){
            this.health = 100;
        } else {
            this.health = newHealth;
        }

        System.out.println("Player used item: " + item.getType() + " and got health: " + this.health);

    }

    @Override
    public void collectItem(Item item) {
        if (item instanceof Shield) {
            this.health += item.getGivenPoints();
            this.currentRoom.removeItem(item);
            item.setPickedUp(true);
            System.out.println("Shield collected! Extra health added: " + item.getGivenPoints());
        } else if (item instanceof HealthKit) {
            try {
                this.backpack.addItem(item);
                this.currentRoom.removeItem(item);
                item.setPickedUp(true);
                System.out.println("HealthKit collected! Stored in backpack.");
            } catch (FullBackPackException ex) {
                System.out.println("Backpack is full, the HealthKit has not been collected");
            }

        }
    }

    public Backpack getBackpack(){
        return backpack;
    }

}
