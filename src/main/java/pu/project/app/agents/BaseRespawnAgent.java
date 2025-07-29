package pu.project.app.agents;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.semanticweb.owlapi.model.OWLOntology;

import pu.project.app.map.MapModel;

public class BaseRespawnAgent extends Agent {

    private transient OWLOntology ontology;
    private transient MapModel model;
  private CountDownLatch doneLatch;  //
    private int alphaBaseX, alphaBaseY;
    private int bravoBaseX, bravoBaseY;
    private int alphaRespawnPointX, alphaRespawnPointY;
    private int bravoRespawnPointX, bravoRespawnPointY;
    
    @Override
    protected void setup() {
        Object[] args = getArguments();
        if (args != null && args.length == 3) {
            ontology = (OWLOntology) args[0];
            model = (MapModel) args[1];
            doneLatch = (CountDownLatch) args[2]; // Get latch from arguments
        }
        addBehaviour(new SetupRespawnPointsBehaviour());
    }

    private class SetupRespawnPointsBehaviour extends OneShotBehaviour {
        @Override
        public void action() {
            determineBaseAndRespawnPositions();
            model.placeObject(alphaBaseX, alphaBaseY, MapModel.AlphaBase);
            model.placeObject(bravoBaseX, bravoBaseY, MapModel.BravoBase);
            model.placeObject(alphaRespawnPointX, alphaRespawnPointY, MapModel.AlphaRespawnPoint);
            model.placeObject(bravoRespawnPointX, bravoRespawnPointY, MapModel.BravoRespawnPoint);
            printBaseRespawnCoordinates();
            doneLatch.countDown();
        }
    }
    private void determineBaseAndRespawnPositions() {
        BuildingManager buildingAgent = new BuildingManager(ontology, model);

        int[] building1 = buildingAgent.getBuilding1Coordinates();
        int[] building2 = buildingAgent.getBuilding2Coordinates();
        int[] building3 = buildingAgent.getBuilding3Coordinates();

        int[] building6 = buildingAgent.getBuilding6Coordinates();
        int[] building7 = buildingAgent.getBuilding7Coordinates();
        int[] building9 = buildingAgent.getBuilding9Coordinates();

        int alphaBaseIndex = decideBaseLocation(building1, building2, building3);
        switch (alphaBaseIndex) {
            case 0:
                alphaBaseX = building1[0];
                alphaBaseY = building1[1];
                break;
            case 1:
                alphaBaseX = building2[0];
                alphaBaseY = building2[1];
                break;
            case 2:
                alphaBaseX = building3[0];
                alphaBaseY = building3[1];
                break;
        }
        int[] alphaRespawn = decideRespawnPointLocation(alphaBaseIndex, building1, building2, building3);
        alphaRespawnPointX = alphaRespawn[0];
        alphaRespawnPointY = alphaRespawn[1];

        int bravoBaseIndex = decideBaseLocation(building6, building7, building9);
        switch (bravoBaseIndex) {
            case 0:
                bravoBaseX = building6[0];
                bravoBaseY = building6[1];
                break;
            case 1:
                bravoBaseX = building7[0];
                bravoBaseY = building7[1];
                break;
            case 2:
                bravoBaseX = building9[0];
                bravoBaseY = building9[1];
                break;
        }
        int[] bravoRespawn = decideRespawnPointLocation(bravoBaseIndex, building6, building7, building9);
        bravoRespawnPointX = bravoRespawn[0];
        bravoRespawnPointY = bravoRespawn[1];
    }

    private int decideBaseLocation(int[] b1, int[] b2, int[] b3) {
        Random random = new Random();
        return random.nextInt(3);
    }

    private int[] decideRespawnPointLocation(int baseIndex, int[] b1, int[] b2, int[] b3) {
        int[] chosenBuilding = null;
        int[] respawnPoint = new int[2];

        switch (baseIndex) {
            case 0: chosenBuilding = b1; break;
            case 1: chosenBuilding = b2; break;
            case 2: chosenBuilding = b3; break;
            default: chosenBuilding = b1;
        }

        if (chosenBuilding == b1) {
            respawnPoint[0] = b2[0];
            respawnPoint[1] = b2[1];
        } else if (chosenBuilding == b2) {
            respawnPoint[0] = b3[0];
            respawnPoint[1] = b3[1];
        } else {
            respawnPoint[0] = b1[0];
            respawnPoint[1] = b1[1];
        }

        return respawnPoint;
    }

    public void printBaseRespawnCoordinates() {
        System.out.println("Alpha Base Coordinates: (" + alphaBaseX + ", " + alphaBaseY + ")");
        System.out.println("Bravo Base Coordinates: (" + bravoBaseX + ", " + bravoBaseY + ")");
        System.out.println("Alpha Respawn Point Coordinates: (" + alphaRespawnPointX + ", " + alphaRespawnPointY + ")");
        System.out.println("Bravo Respawn Point Coordinates: (" + bravoRespawnPointX + ", " + bravoRespawnPointY + ")");
    }

    public int[] getAlphaBaseCoordinates() {
        return new int[]{alphaBaseX, alphaBaseY};
    }
    public int[] getBravoBaseCoordinates() {
        return new int[]{bravoBaseX, bravoBaseY};
    }
    public int[] getAlphaRespawnPointCoordinates() {
        return new int[]{alphaRespawnPointX, alphaRespawnPointY};
    }
    public int[] getBravoRespawnPointCoordinates() {
        return new int[]{bravoRespawnPointX, bravoRespawnPointY};
    }
}
