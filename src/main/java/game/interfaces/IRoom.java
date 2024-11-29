package game.interfaces;

import dataStructures.implementations.ArrayUnorderedList;
import game.character.Enemy;
import game.character.Player;
import game.items.Item;

public interface IRoom {

    public void setName(String name);

    public String getName();

    /*LinkedList<IEnemy>*//*Enemy[]*//*Iterator*//*ArrayList<IEnemy>*/
    public ArrayUnorderedList<Enemy> getEnemies();

    /*LinkedList<IItem>*//*Item[]*//*Iterator*//*ArrayList<IItem>*/
    public ArrayUnorderedList<Item> getItems();

    public boolean hasPlayer();
    
    public Player getPlayer();

    public void confrontation(Character priorityCharacter);
}
