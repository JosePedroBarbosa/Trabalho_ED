package game.character;

import implementations.Game;
import game.map.Room;
import game.interfaces.ICharacter;

public abstract class Character implements ICharacter {

    private static int nextId = 0;
    private final int id;
    private String name;
    protected int health;
    protected final int power;
    protected Room currentRoom;

    public Character(String name, int power, Room currentRoom) {
        this.id = nextId++;
        this.name = name;
        this.health = Game.getMaxCharacterLife();
        this.power = power;
        this.currentRoom = currentRoom;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Room getCurrentRoom() {
        return currentRoom;
    }

    @Override
    public void setCurrentRoom(Room currentRoom) {
        if (currentRoom == null) {
            throw new IllegalArgumentException("The Element Cant Be Null");
        }
        this.currentRoom = currentRoom;

    }

    @Override
    public boolean isAlive() {
        return this.health > 0;
    }

    @Override
    public int getLife() {
        return this.health;
    }

    @Override
    public void takeDamage(int damage) {
        if (damage >= 0) {
            this.health -= damage;
        }
    }

    @Override
    public int getPower() {
        return this.power;
    }

}
