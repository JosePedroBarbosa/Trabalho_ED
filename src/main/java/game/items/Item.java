package game.items;

import game.interfaces.IItem;

public abstract class Item implements IItem {

    protected String type;
    // private Room currentRoom;

    public Item(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return this.type;
    }

    /*
    public Room getCurrentRoom(){
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom){
        if(currentRoom == null){
            throw new IllegalArgumentException("Room cannot be null");
        }
        this.currentRoom = currentRoom;
    }
     */
}
