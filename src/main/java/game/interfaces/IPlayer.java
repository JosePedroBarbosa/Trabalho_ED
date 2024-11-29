package game.interfaces;

import game.items.Item;

/**
 * The IPlayer interface defines the basic contract for any player in the game.
 */
public interface IPlayer extends ICharacter {
    
    public void collectItem(Item item);
    
}
