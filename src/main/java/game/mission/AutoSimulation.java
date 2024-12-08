package game.mission;

import dataStructures.implementations.Network;
import game.data.ImportData;
import game.map.Room;

public class AutoSimulation {

    private static Network<Room> currentNetwork;

    public static void autoSimulation(Mission mission){
        ImportData importer = new ImportData();
        importer.importCurrentMissionData();

        boolean gameOver = false;

        while(!gameOver){
            updateCurrentNetwork(mission);
            //gameOver = playerTurn(mission);

            if(!mission.getPlayer().isAlive()){
                System.out.println("Mission Failed, The Player Died");
                gameOver = true;
            }
        }
    }

    private static void updateCurrentNetwork(Mission mission){
        //LOGICA DE IMPLEMENTACAO DOS PESOS DA NETWORK DE ACORDO COM A MISSAO
    }

    private static void playerTurn(Mission mission){
        //deve usar um kit?
        //se usar o turno acaba e os enimigos movem-se

        //se nao usar um kit entao:
        //qual é o melhor caminho para o target tentando levar o menor dano possivel
        // move para essa room
        //os inimigos movem-se
    }
}
