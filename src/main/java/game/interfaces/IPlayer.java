package game.interfaces;

import game.items.Item;

/**
 * The IPlayer interface defines the basic contract for any player in the game.
 */
public interface IPlayer extends IEntity {
    
    public void collectItem(Item item);
    
}
