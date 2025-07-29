package pu.project.app.agents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import org.semanticweb.owlapi.model.OWLOntology;

import pu.project.app.map.MapModel;

public class CommCenterAgent extends Agent {
    private transient OWLOntology ontology;
    private transient MapModel model;
    private CountDownLatch doneLatch;
    private int commCenterX, commCenterY;
    public CommCenterAgent() {
    }
     @Override
    protected void setup() {
        Object[] args = getArguments();
        if (args != null && args.length == 3) {
            ontology = (OWLOntology) args[0];
            model = (MapModel) args[1];
            doneLatch = (CountDownLatch) args[2];
        }
        addBehaviour(new SetupCommCenterBehaviour());
    }

    private class SetupCommCenterBehaviour extends OneShotBehaviour {
        @Override
        public void action() {
            placeCommCenter();
            model.placeObject(commCenterX, commCenterY, MapModel.CommCenter);
            printCommCenterCoordinates();
            doneLatch.countDown();
        }
    }
    
    public CommCenterAgent(OWLOntology ontology, MapModel model) {
        this.ontology = ontology;
        this.model = model;
        placeCommCenter();
    }

private void placeCommCenter() {
    BuildingManager buildingAgent = new BuildingManager(ontology, model);
    
    List<int[]> possiblePositions = new ArrayList<>();
    possiblePositions.add(buildingAgent.getBuilding4Coordinates());
    possiblePositions.add(buildingAgent.getBuilding5Coordinates());
    possiblePositions.add(buildingAgent.getBuilding11Coordinates());
    possiblePositions.add(buildingAgent.getBuilding13Coordinates());
    possiblePositions.add(buildingAgent.getBuilding14Coordinates());
    
    Collections.shuffle(possiblePositions, new Random());
    
    for (int[] pos : possiblePositions) {
        if (model.get(pos[0], pos[1]) == MapModel.EMPTY) {
            commCenterX = pos[0];
            commCenterY = pos[1];
            model.placeObject(commCenterX, commCenterY, MapModel.CommCenter);
            return;
        }
    }
    
    int[] pos = possiblePositions.get(0);
    commCenterX = pos[0];
    commCenterY = pos[1];
    model.placeObject(commCenterX, commCenterY, MapModel.CommCenter);
}


    public void printCommCenterCoordinates() {
        System.out.println("Comm Center Coordinates: (" + commCenterX + ", " + commCenterY + ")");
    }

    public int[] getCommCenterCoordinates() {
        return new int[]{commCenterX, commCenterY};
    }
}
