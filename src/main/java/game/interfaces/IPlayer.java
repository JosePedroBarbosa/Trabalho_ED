package game.interfaces;

import game.character.Enemy;
import game.exceptions.EmptyBackPackException;
import game.items.Backpack;
import game.items.Item;

/**
 * The IPlayer interface defines the basic contract for any player in the game.
 */
public interface IPlayer extends IEntity {
    public void attack();
    public void attack(Enemy target);
    public void useItem() throws EmptyBackPackException;

    public void collectItem(Item item);

    public Backpack getBackpack();
    
}
