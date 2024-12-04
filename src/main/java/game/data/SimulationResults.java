package game.data;

import dataStructures.implementations.ArrayUnorderedList;
import game.map.Room;

public class SimulationResults implements Comparable<SimulationResults> {
    private String missionCode;
    private int version;
    private int remainingHealth;
    private boolean simulationSuccess;
    private ArrayUnorderedList<Room> simulationRoute;

    public SimulationResults(String missionCode, int version, int remainingHealth, boolean simulationSuccess, ArrayUnorderedList<Room> route){
        this.missionCode = missionCode;
        this.version = version;
        this.remainingHealth = remainingHealth;
        this.simulationSuccess = simulationSuccess;
        this.simulationRoute = route;
    }

    @Override
    public String toString(){
        String s = "";

        s += "Mission Code: " + missionCode + "\n";
        s += "Version: " + version + "\n";
        s += "Remaining Health: " + remainingHealth + "\n";
        s += "Simulation Success: " + simulationSuccess + "\n";

        s += "Route: \n";
        for(Room room : simulationRoute){
            if(room != null){
                s += "    -> " + room.getName() + "\n";
            }
        }

        return s;
    }

    @Override
    public int compareTo(SimulationResults simulationResults) {
        return simulationResults.remainingHealth - this.remainingHealth;
    }
}
