package game.map;

import dataStructures.implementations.ArrayUnorderedList;
import game.character.Entity;
import game.character.Enemy;
import game.character.Player;
import game.items.Item;
import game.interfaces.IRoom;

public class Room implements IRoom {
    private String name;
    private ArrayUnorderedList<Enemy> enemies;
    private ArrayUnorderedList<Item> items;
    private Player player;
    private boolean isEntranceAndExit;

    public Room(String name) {
        this.name = name;
        this.enemies = new ArrayUnorderedList<>();
        this.items = new ArrayUnorderedList<>();
        this.player = null;
        this.isEntranceAndExit = false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public ArrayUnorderedList<Enemy> getEnemies() {
        return this.enemies;
    }

    public void removeEnemy(Enemy enemy) {
        this.enemies.remove(enemy);
    }

    public void addEnemy(Enemy enemy) {
        this.enemies.addToRear(enemy);
    }

    @Override
    public ArrayUnorderedList<Item> getItems() {
        return items;
    }

    public void removeItem(Item item) {
        this.items.remove(item);
    }

    public void addItem(Item item) {
        this.items.addToRear(item);
    }

    @Override
    public boolean hasPlayer() {
        return this.player != null;
    }
    
    @Override
    public Player getPlayer() {
        if(!hasPlayer()){
            throw new IllegalArgumentException("No Player In The Room");
        }
        return this.player;
    }

    public void removePlayer() {
        this.player = null;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String toString(){
        return name;
    }

    public boolean hasEnemies(){
        return !this.enemies.isEmpty();
    }

    public void setEntranceAndExit(){
        this.isEntranceAndExit = true;
    }

    public boolean isEntranceAndExit(){
        return this.isEntranceAndExit;
    }

}