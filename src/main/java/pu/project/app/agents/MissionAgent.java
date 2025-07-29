package pu.project.app.agents;

import java.util.Random;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.semanticweb.owlapi.model.OWLOntology;

import pu.project.app.map.MapModel;

public class MissionAgent extends Agent{

    private OWLOntology ontology;
    private MapModel model;
    private FlagAgent flagAgent;
    private CommCenterAgent commCenterAgent;
    private HelicopterAgent helicopterAgent;
    private transient CountDownLatch doneLatch;
    private int scenario;
    private int alphaMission1X;
    private int alphaMission1Y;
    private int alphaMission2X;
    private int alphaMission2Y;
    private int bravoMission1X;
    private int bravoMission1Y;
    private int bravoMission2X;
    private int bravoMission2Y;
    public MissionAgent() {
    }
@Override
    protected void setup() {
        Object[] args = getArguments();
        if (args != null && args.length == 4) {
            ontology = (OWLOntology) args[0];
            model = (MapModel) args[1];
            doneLatch = (CountDownLatch) args[2];
            scenario = (int) args[3];
        }
        addBehaviour(new PlaceMissionsBehaviour());
    }

    private class PlaceMissionsBehaviour extends OneShotBehaviour {
        @Override
        public void action() {
            placeMissions();
            doneLatch.countDown();
        }
    }
    public MissionAgent(OWLOntology ontology, MapModel model, FlagAgent flagAgent,
            CommCenterAgent commCenterAgent, HelicopterAgent helicopterAgent) {
        this.ontology = ontology;
        this.model = model;
        this.flagAgent = flagAgent;
        this.commCenterAgent = commCenterAgent;
        this.helicopterAgent = helicopterAgent;
        placeMissions();
    }

 private void placeMissions() {
    BuildingManager buildingAgent = new BuildingManager(ontology, model);
    List<int[]> candidateBuildings = new ArrayList<>();
    candidateBuildings.add(buildingAgent.getBuilding4Coordinates());
    candidateBuildings.add(buildingAgent.getBuilding5Coordinates());
    candidateBuildings.add(buildingAgent.getBuilding11Coordinates());
    candidateBuildings.add(buildingAgent.getBuilding12Coordinates());
    candidateBuildings.add(buildingAgent.getBuilding13Coordinates());
    candidateBuildings.add(buildingAgent.getBuilding14Coordinates());
    candidateBuildings.add(buildingAgent.getBuilding15Coordinates());

    List<int[]> availableBuildings = new ArrayList<>();
    for (int[] building : candidateBuildings) {
        if (model.get(building[0], building[1]) == MapModel.EMPTY) {
            availableBuildings.add(building);
        }
    }

    Collections.shuffle(availableBuildings, new Random());
    
    int[] alphaMission1 = null, alphaMission2 = null;
    int[] bravoMission1 = null, bravoMission2 = null;
    
    // Assign distinct positions from available buildings
    int index = 0;
    if (availableBuildings.size() > index) alphaMission1 = availableBuildings.get(index++);
    if (availableBuildings.size() > index) alphaMission2 = availableBuildings.get(index++);
    if (availableBuildings.size() > index) bravoMission1 = availableBuildings.get(index++);
    if (availableBuildings.size() > index) bravoMission2 = availableBuildings.get(index++);

    // Place missions only if positions are available
    if (alphaMission1 != null) {
        model.placeObject(alphaMission1[0], alphaMission1[1], MapModel.AlphaMission1);
    }
    if (alphaMission2 != null) {
        model.placeObject(alphaMission2[0], alphaMission2[1], MapModel.AlphaMission2);
    }
    if (bravoMission1 != null) {
        model.placeObject(bravoMission1[0], bravoMission1[1], MapModel.BravoMission1);
    }
    if (bravoMission2 != null) {
        model.placeObject(bravoMission2[0], bravoMission2[1], MapModel.BravoMission2);
    }

    printMissionCoordinates(alphaMission1, alphaMission2, bravoMission1, bravoMission2);
}
    private void printMissionCoordinates(int[] alpha1, int[] alpha2, int[] bravo1, int[] bravo2) {
        System.out.println("Alpha Mission Start: " + formatCoords(alpha1));
        System.out.println("Alpha Mission End: " + formatCoords(alpha2));
        System.out.println("Bravo Mission Start: " + formatCoords(bravo1));
        System.out.println("Bravo Mission End: " + formatCoords(bravo2));
    }

    private String formatCoords(int[] coords) {
        return coords != null ? "(" + coords[0] + ", " + coords[1] + ")" : "N/A";
    }

    public int[] getAlphaMission1Coordinates() {
        return new int[] { alphaMission1X, alphaMission1Y };
    }

    public int[] getAlphaMission2Coordinates() {
        return new int[] { alphaMission2X, alphaMission2Y };
    }

    public int[] getBravoMission1Coordinates() {
        return new int[] { bravoMission1X, bravoMission1Y };
    }

    public int[] getBravoMission2Coordinates() {
        return new int[] { bravoMission2X, bravoMission2Y };
    }

    public void printMissionCoordinates() {
        System.out.println("Alpha Mission Start: " + alphaMission1X + ", " + alphaMission1Y);
        System.out.println("Alpha Mission End: " + alphaMission2X + ", " + alphaMission2Y);
        System.out.println("Bravo Mission Start: " + bravoMission1X + ", " + bravoMission1Y);
        System.out.println("Bravo Mission End: " + bravoMission2X + ", " + bravoMission2Y);
    }
}
