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

public class FlagAgent extends Agent{
    private OWLOntology ontology;
    private MapModel model;
    private CountDownLatch doneLatch;
     private int flag1X, flag1Y;
    private int flag2X, flag2Y;
    private int flag3X, flag3Y;
    public FlagAgent() {
    }
    @Override
    protected void setup() {
        Object[] args = getArguments();
        if (args != null && args.length == 3) {
            ontology = (OWLOntology) args[0];
            model = (MapModel) args[1];
            doneLatch = (CountDownLatch) args[2];
        }
        addBehaviour(new SetupFlagsBehaviour());
    }

    private class SetupFlagsBehaviour extends OneShotBehaviour {
        @Override
        public void action() {
            placeFlags();
            model.placeObject(flag1X, flag1Y, MapModel.Flag1);
            model.placeObject(flag2X, flag2Y, MapModel.Flag2);
            model.placeObject(flag3X, flag3Y, MapModel.Flag3);
            printFlagCoordinates();
            doneLatch.countDown();
        }
    }

public void placeFlags() {
    BuildingManager buildingAgent = new BuildingManager(ontology, model);
    
    int[] building4 = buildingAgent.getBuilding4Coordinates();
    flag1X = building4[0];
    flag1Y = building4[1];
    model.placeObject(flag1X, flag1Y, MapModel.Flag1);
    
    flag2X = 5;
    flag2Y = 7;
    model.placeObject(flag2X, flag2Y, MapModel.Flag2);
    
    List<int[]> possibleFlag3Positions = new ArrayList<>();
    possibleFlag3Positions.add(building4); // Building 4
    possibleFlag3Positions.add(buildingAgent.getBuilding14Coordinates());
    possibleFlag3Positions.add(buildingAgent.getBuilding15Coordinates());
    possibleFlag3Positions.add(new int[]{12, 7}); // Additional position
    
    Collections.shuffle(possibleFlag3Positions, new Random());
    
    for (int[] pos : possibleFlag3Positions) {
        if (model.get(pos[0], pos[1]) == MapModel.EMPTY) {
            flag3X = pos[0];
            flag3Y = pos[1];
            model.placeObject(flag3X, flag3Y, MapModel.Flag3);
            return;
        }
    }
    
    int[] pos = possibleFlag3Positions.get(0);
    flag3X = pos[0];
    flag3Y = pos[1];
    model.placeObject(flag3X, flag3Y, MapModel.Flag3);
}
    public FlagAgent(OWLOntology ontology, MapModel model) {
        this.ontology = ontology;
        this.model = model;
        placeFlags();
    }

 public void printFlagCoordinates() {
        System.out.println("Flag 1 Coordinates: (" + flag1X + ", " + flag1Y + ")");
        System.out.println("Flag 2 Coordinates: (" + flag2X + ", " + flag2Y + ")");
        System.out.println("Flag 3 Coordinates: (" + flag3X + ", " + flag3Y + ")");
    }

    public int[] getFlag1Coordinates() {
        return new int[]{flag1X, flag1Y};
    }

    public int[] getFlag2Coordinates() {
        return new int[]{flag2X, flag2Y};
    }

    public int[] getFlag3Coordinates() {
        return new int[]{flag3X, flag3Y};
    }
}


