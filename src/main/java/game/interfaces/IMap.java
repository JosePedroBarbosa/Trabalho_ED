/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package game.interfaces;

import game.map.Room;

public interface IMap {

    public void insertRoom(Room room);

    public void insertConnection(Room room1, Room room2);

}
