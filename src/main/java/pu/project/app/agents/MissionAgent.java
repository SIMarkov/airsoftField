package pu.project.app.agents;

import java.io.File;
import java.util.Arrays;
import java.util.Random;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import pu.project.app.map.MapModel;
import pu.project.app.ontology.OntologyLoader;
import pu.project.app.views.field.FieldView;

public class MissionAgent {

    private OWLOntology ontology;
    private MapModel model;
    private FlagAgent flagAgent;
    private CommCenterAgent commCenterAgent;
    private HelicopterAgent helicopterAgent;

    private int alphaMission1X;
    private int alphaMission1Y;
    private int alphaMission2X;
    private int alphaMission2Y;
    private int bravoMission1X;
    private int bravoMission1Y;
    private int bravoMission2X;
    private int bravoMission2Y;

    // Constructor
    public MissionAgent(OWLOntology ontology, MapModel model, FlagAgent flagAgent,
            CommCenterAgent commCenterAgent, HelicopterAgent helicopterAgent) {
        this.ontology = ontology;
        this.model = model;
        this.flagAgent = flagAgent;
        this.commCenterAgent = commCenterAgent;
        this.helicopterAgent = helicopterAgent;
        placeMissions();
    }

    // Assign missions to the agent
    public void assignObjectsToAgent() {
        OWLDataFactory dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();

        OWLNamedIndividual agentInstance = dataFactory
                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#missionAgentInstance"));

        OWLNamedIndividual alphaMission = dataFactory
                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#AlphaSecretMission1"));
        OWLNamedIndividual bravoMission = dataFactory
                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#BravoSecretMission1"));

        OWLObjectProperty belongsToAgentProperty = dataFactory
                .getOWLObjectProperty(IRI.create("file://ontology/airsoftField.owx#belongsToAgent"));

        OWLObjectPropertyAssertionAxiom assertion1 = dataFactory
                .getOWLObjectPropertyAssertionAxiom(belongsToAgentProperty, alphaMission, agentInstance);
        OWLObjectPropertyAssertionAxiom assertion2 = dataFactory
                .getOWLObjectPropertyAssertionAxiom(belongsToAgentProperty, bravoMission, agentInstance);

        ontology.getOWLOntologyManager().addAxiom(ontology, assertion1);
        ontology.getOWLOntologyManager().addAxiom(ontology, assertion2);
    }

    // Place missions on the map
    public void placeMissions() {
        FieldView fieldView = FieldView.getInstance();
        if (fieldView != null) {
            int scenario = fieldView.getScenario();
            BuildingManager buildingAgent = new BuildingManager(ontology, model);
            int[] building4Coordinates = buildingAgent.getBuilding4Coordinates();
            int[] building5Coordinates = buildingAgent.getBuilding5Coordinates();
            int[] building11Coordinates = buildingAgent.getBuilding11Coordinates();
            int[] building12Coordinates = buildingAgent.getBuilding12Coordinates();
            int[] building13Coordinates = buildingAgent.getBuilding13Coordinates();
            int[] building14Coordinates = buildingAgent.getBuilding14Coordinates();
            int[] building15Coordinates = buildingAgent.getBuilding15Coordinates();

            placeAlphaMission(scenario, building4Coordinates, building5Coordinates, building11Coordinates,
                    building12Coordinates, building13Coordinates, building14Coordinates, building15Coordinates);
            placeBravoMission(scenario, building4Coordinates, building5Coordinates, building11Coordinates,
                    building12Coordinates, building13Coordinates, building14Coordinates, building15Coordinates);
        }
    }

    // Check if a mission collides with other objects on the map
    private boolean checkMissionCollides(int x, int y, int scenario) {
        switch (scenario) {
            case 1:
                return (x == commCenterAgent.getCommCenterCoordinates()[0]
                        && y == commCenterAgent.getCommCenterCoordinates()[1]);
            case 2:
                return (x == helicopterAgent.getHelicopterCoordinates()[0]
                        && y == helicopterAgent.getHelicopterCoordinates()[1]);
            case 3:
                return (x == flagAgent.getFlag1Coordinates()[0] && y == flagAgent.getFlag1Coordinates()[1]) ||
                        (x == flagAgent.getFlag2Coordinates()[0] && y == flagAgent.getFlag2Coordinates()[1]) ||
                        (x == flagAgent.getFlag3Coordinates()[0] && y == flagAgent.getFlag3Coordinates()[1]);
            default:
                System.out.println("Invalid scenario");
                return false;
        }
    }

    // Place the Alpha mission on the map
    private void placeAlphaMission(int scenario, int[]... buildingCoordinates) {
        int[] startBuilding;
        int[] endBuilding;
        do {
            startBuilding = decideMissionLocation(buildingCoordinates);
            endBuilding = decideMissionLocation(buildingCoordinates);
        } while (Arrays.equals(startBuilding, endBuilding) ||
                checkMissionCollides(startBuilding[0], startBuilding[1], scenario) ||
                checkMissionCollides(endBuilding[0], endBuilding[1], scenario));

        alphaMission1X = startBuilding[0];
        alphaMission1Y = startBuilding[1];
        alphaMission2X = endBuilding[0];
        alphaMission2Y = endBuilding[1];
    }

    // Place the Bravo mission on the map
    private void placeBravoMission(int scenario, int[]... buildingCoordinates) {
        int[] startBuilding;
        int[] endBuilding;
        do {
            startBuilding = decideMissionLocation(buildingCoordinates);
            endBuilding = decideMissionLocation(buildingCoordinates);
        } while (Arrays.equals(startBuilding, endBuilding) ||
                checkMissionCollides(startBuilding[0], startBuilding[1], scenario) ||
                checkMissionCollides(endBuilding[0], endBuilding[1], scenario) ||
                (startBuilding[0] == alphaMission1X && startBuilding[1] == alphaMission1Y) ||
                (startBuilding[0] == alphaMission2X && startBuilding[1] == alphaMission2Y) ||
                (endBuilding[0] == alphaMission1X && endBuilding[1] == alphaMission1Y) ||
                (endBuilding[0] == alphaMission2X && endBuilding[1] == alphaMission2Y));

        bravoMission1X = startBuilding[0];
        bravoMission1Y = startBuilding[1];
        bravoMission2X = endBuilding[0];
        bravoMission2Y = endBuilding[1];
    }

    // Decide the location of a mission randomly from available building coordinates
    private int[] decideMissionLocation(int[][] buildingCoordinates) {
        Random random = new Random();
        int randomBuildingIndex = random.nextInt(buildingCoordinates.length);
        return buildingCoordinates[randomBuildingIndex];
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

    public static void main(String[] args) {
        String ontologyFilePath = "ontology/airsoftField.owx";
        OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
        try {
            File ontologyFile = new File(ontologyFilePath);
            OWLOntology ontology = ontologyManager.loadOntologyFromOntologyDocument(ontologyFile);

            MapModel model = new MapModel(ontologyFilePath, ontology);

            System.out.println("Loaded ontology: " + ontology.getOntologyID().getOntologyIRI().toString());

            OntologyLoader.visualizeObjectsForScenarios(ontology);

            FlagAgent flagAgent = new FlagAgent(ontology, model);
            flagAgent.assignObjectsToAgent();

            CommCenterAgent commCenterAgent = new CommCenterAgent(ontology, model);
            commCenterAgent.assignObjectsToAgent();

            HelicopterAgent helicopterAgent = new HelicopterAgent(ontology, model);
            helicopterAgent.assignObjectsToAgent();

            MissionAgent agent = new MissionAgent(ontology, model, flagAgent, commCenterAgent, helicopterAgent);
            agent.assignObjectsToAgent();

            System.out.println("Objects assigned to intelligent agent:");
            OntologyLoader.visualizeObjectsForScenarios(ontology);

        } catch (OWLOntologyCreationException e) {
            System.err.println("Error loading ontology: " + e.getMessage());
        }
    }
}
