package game.map;

import dataStructures.implementations.ArrayUnorderedList;
import game.character.Enemy;
import game.character.Player;
import game.items.Item;
import game.interfaces.IRoom;

public class Room implements IRoom {

    private String name;
    private ArrayUnorderedList<Enemy> enemies;
    private ArrayUnorderedList<Item> items;
    private Player player;

    public Room(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public ArrayUnorderedList<Enemy> getEnemies() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void removeEnemy(Enemy enemy) {
        this.enemies.remove(enemy);
    }

    @Override
    public ArrayUnorderedList<Item> getItems() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void removeItem(Item item) {
        this.items.remove(item);
    }

    @Override
    public boolean hasPlayer() {
        return this.player == null;
    }
    
    @Override
    public Player getPlayer() {
        if(!hasPlayer()){
            throw new IllegalArgumentException("No Player In The Room");
        }
        return this.player;
    }

    @Override
    public void confrontation(Character priorityCharacter) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
