package pu.project.app.agents;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import org.semanticweb.owlapi.model.OWLOntology;

import pu.project.app.map.MapModel;

public class HelicopterAgent extends Agent {   
    private transient OWLOntology ontology;
    private transient MapModel model;
    private CountDownLatch doneLatch;
    private int helicopterX, helicopterY;
    public HelicopterAgent() {
    }
   @Override
    protected void setup() {
        Object[] args = getArguments();
        if (args != null && args.length == 3) {
            ontology = (OWLOntology) args[0];
            model = (MapModel) args[1];
            doneLatch = (CountDownLatch) args[2];
        }
        addBehaviour(new SetupHelicopterBehaviour());
    }

    private class SetupHelicopterBehaviour extends OneShotBehaviour {
        @Override
        public void action() {
            placeHelicopter();
            model.placeObject(helicopterX, helicopterY, MapModel.Helicopter);
            printHelicopterCoordinates();
            doneLatch.countDown();
        }
    }
    
    public HelicopterAgent(OWLOntology ontology, MapModel model) {
        this.ontology = ontology;
        this.model = model;
        placeHelicopter();
    }

private void placeHelicopter() {
    BuildingManager buildingAgent = new BuildingManager(ontology, model);
    
    List<int[]> possiblePositions = new ArrayList<>();
    possiblePositions.add(buildingAgent.getBuilding5Coordinates());
    possiblePositions.add(new int[]{12, 7});
    possiblePositions.add(new int[]{13, 7});
    possiblePositions.add(new int[]{5, 7});
    
    Collections.shuffle(possiblePositions, new Random());
    
    for (int[] pos : possiblePositions) {
        if (model.get(pos[0], pos[1]) == MapModel.EMPTY) {
            helicopterX = pos[0];
            helicopterY = pos[1];
            model.placeObject(helicopterX, helicopterY, MapModel.Helicopter);
            return;
        }
    }
    
    int[] pos = possiblePositions.get(0);
    helicopterX = pos[0];
    helicopterY = pos[1];
    model.placeObject(helicopterX, helicopterY, MapModel.Helicopter);
}

    public void printHelicopterCoordinates() {
        System.out.println("Helicopter Coordinates: (" + helicopterX + ", " + helicopterY + ")");
    }

    public int[] getHelicopterCoordinates() {
        return new int[]{helicopterX, helicopterY};
    }
}

