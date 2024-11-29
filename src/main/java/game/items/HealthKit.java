package game.items;

public class HealthKit extends Item {

    private int recoveredPoints;

    public HealthKit(int recoveredPoints) {
        super("Health Kit");
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
