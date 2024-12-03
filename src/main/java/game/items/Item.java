package game.items;

import game.interfaces.IItem;
import game.map.Room;

public abstract class Item implements IItem {

    protected String type;
    protected Room currentRoom;
    protected boolean pickedUp;

    public Item(String type, Room currentRoom) {
        this.type = type;
        this.currentRoom = currentRoom;
        this.pickedUp = false;
    }

    @Override
    public String getType() {
        return this.type;
    }

    public Room getCurrentRoom(){
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        if (currentRoom == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }
        this.currentRoom = currentRoom;
    }

    public boolean isPickedUp() {
        return pickedUp;
    }

    public void setPickedUp(boolean pickedUp) {
        this.pickedUp = pickedUp;
    }
}
