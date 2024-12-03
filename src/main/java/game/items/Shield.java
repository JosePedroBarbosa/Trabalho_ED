package game.items;

import game.map.Room;

public class Shield extends Item {

    private int extraPoints;

    public Shield(int extraPoints,Room currentRoom) {
        super("Shield", currentRoom);
        this.extraPoints = extraPoints;
    }

    @Override
    public void setPoints(int extraPoints) {
        this.extraPoints = extraPoints;
    }

    @Override
    public int getGivenPoints() {
        return this.extraPoints;
    }
}
