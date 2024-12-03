package game.items;

import game.map.Room;

public class HealthKit extends Item {

    private int recoveredPoints;

    public HealthKit(int recoveredPoints, Room currentRoom) {
        super("Health Kit", currentRoom);
        this.recoveredPoints = recoveredPoints;
    }

    @Override
    public void setPoints(int recoveredPoints) {
        this.recoveredPoints = recoveredPoints;
    }

    @Override
    public int getGivenPoints() {
        return this.recoveredPoints;
    }
}
