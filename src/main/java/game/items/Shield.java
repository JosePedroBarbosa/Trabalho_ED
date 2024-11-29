package game.items;

public class Shield extends Item {

    private int extraPoints;

    public Shield(int extraPoints) {
        super("Shield");
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
