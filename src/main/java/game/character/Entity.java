package game.character;

import game.settings.GameSettings;
import game.map.Room;
import game.interfaces.IEntity;

public abstract class Entity implements IEntity {

    private static int nextId = 0;
    private final int id;
    private String name;
    protected int health;
    protected final int power;
    protected Room currentRoom;

    public Entity(String name, int power, Room currentRoom) {
        this.id = nextId++;
        this.name = name;
        this.health = GameSettings.getInitialCharacterHealth();
        this.power = power;
        this.currentRoom = currentRoom;
    }

    public Entity(String name, int power) {
        this.id = nextId++;
        this.name = name;
        this.health = GameSettings.getInitialCharacterHealth();
        this.power = power;
        this.currentRoom = null;
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
    public void setCurrentRoom(Room nextRoom) {
        if (nextRoom == null) {
            throw new IllegalArgumentException("The Element Cant Be Null");
        }
        this.currentRoom = nextRoom;
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
