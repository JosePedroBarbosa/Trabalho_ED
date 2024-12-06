package game.data;

import dataStructures.implementations.ArrayUnorderedList;
import game.map.Room;

/**
 * Represents the results of a simulation, including details about the mission,
 * the remaining health of the player, the success status, and the route taken during the simulation.
 * This class implements Comparable to allow comparison of results based on remaining health.
 */
public class SimulationResults implements Comparable<SimulationResults> {
    /**
     * The code of the mission simulated.
     */
    private String missionCode;

    /**
     * The version of the mission simulated.
     */
    private int version;

    /**
     * The remaining health of the player after the simulation.
     */
    private int remainingHealth;

    /**
     * Indicates whether the simulation was successful.
     */
    private boolean simulationSuccess;

    /**
     * The route taken during the simulation, represented as a list of rooms.
     */
    private ArrayUnorderedList<Room> simulationRoute;

    /**
     * Constructs a new instance of SimulationResults.
     *
     * @param missionCode      The code of the mission.
     * @param version          The version of the mission.
     * @param remainingHealth  The remaining health of the player.
     * @param simulationSuccess Indicates whether the simulation was successful.
     * @param route            The route taken during the simulation.
     */
    public SimulationResults(String missionCode, int version, int remainingHealth, boolean simulationSuccess, ArrayUnorderedList<Room> route){
        this.missionCode = missionCode;
        this.version = version;
        this.remainingHealth = remainingHealth;
        this.simulationSuccess = simulationSuccess;
        this.simulationRoute = route;
    }

    /**
     * Returns a string representation of the simulation results.
     *
     * @return A string representation of the results, including mission details, health, success status, and route.
     */
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

    /**
     * Compares this simulation result to another based on remaining health.
     * A higher remaining health indicates a better simulation result.
     *
     * @param simulationResults The other simulation result to compare against.
     * @return A negative integer, zero, or a positive integer.
     */
    @Override
    public int compareTo(SimulationResults simulationResults) {
        return simulationResults.remainingHealth - this.remainingHealth;
    }
}