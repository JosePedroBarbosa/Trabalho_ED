package game.interfaces;

/**
 * The IItem interface defines the basic contract for any item in the game.
 */
public interface IItem {
    
    public void setPoints(int points);
    public int getGivenPoints();
    public String getType();
    
    //acho que nao é preciso isto
    //public IRoom getCurrentRoom();
    //public void setCurrentRoom(IRoom currentRoom);
}
