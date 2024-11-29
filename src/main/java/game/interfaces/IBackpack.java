package game.interfaces;

import game.exceptions.EmptyBackPackException;
import game.exceptions.FullBackPackException;

import game.items.Item;

public interface IBackpack {

    public Item useItem() throws EmptyBackPackException;

    public void addItem(Item item) throws FullBackPackException;
}