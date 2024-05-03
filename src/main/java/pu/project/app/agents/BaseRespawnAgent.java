package pu.project.app.agents;

import java.io.File;
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

public class BaseRespawnAgent {

    private OWLOntology ontology;
    private MapModel model;
    private int alphaBaseX;
    private int bravoBaseX;
    private int alphaBaseY;
    private int bravoBaseY;
    private int alphaRespawnPointX;
    private int bravoRespawnPointX;
    private int alphaRespawnPointY;
    private int bravoRespawnPointY;

    // Constructor
    public BaseRespawnAgent(OWLOntology ontology, MapModel model) {
        this.ontology = ontology;
        this.model = model;
        placeBasesAndRespawnPoints();
    }

    // Assigning objects to agent
    public void assignObjectsToAgent() {
        OWLDataFactory dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();

        OWLNamedIndividual agentInstance = dataFactory
                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#baseRespawnAgentInstance"));

        OWLNamedIndividual alphaBase = dataFactory
                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#AlphaBase"));
        OWLNamedIndividual bravoBase = dataFactory
                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#BravoBase"));
        OWLNamedIndividual alphaRespawnPoint = dataFactory
                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#AlphaRespawnPoint"));
        OWLNamedIndividual bravoRespawnPoint = dataFactory
                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#BravoRespawnPoint"));

        OWLObjectProperty belongsToAgentProperty = dataFactory
                .getOWLObjectProperty(IRI.create("file://ontology/airsoftField.owx#belongsToAgent"));

        OWLObjectPropertyAssertionAxiom assertion1 = dataFactory
                .getOWLObjectPropertyAssertionAxiom(belongsToAgentProperty, alphaBase, agentInstance);
        OWLObjectPropertyAssertionAxiom assertion2 = dataFactory
                .getOWLObjectPropertyAssertionAxiom(belongsToAgentProperty, bravoBase, agentInstance);
        OWLObjectPropertyAssertionAxiom assertion3 = dataFactory
                .getOWLObjectPropertyAssertionAxiom(belongsToAgentProperty, alphaRespawnPoint, agentInstance);
        OWLObjectPropertyAssertionAxiom assertion4 = dataFactory
                .getOWLObjectPropertyAssertionAxiom(belongsToAgentProperty, bravoRespawnPoint, agentInstance);

        ontology.getOWLOntologyManager().addAxiom(ontology, assertion1);
        ontology.getOWLOntologyManager().addAxiom(ontology, assertion2);
        ontology.getOWLOntologyManager().addAxiom(ontology, assertion3);
        ontology.getOWLOntologyManager().addAxiom(ontology, assertion4);
    }

    // Placing bases and respawn points
    public void placeBasesAndRespawnPoints() {
        BuildingManager buildingAgent = new BuildingManager(ontology, model);

        int[] building1Coordinates = buildingAgent.getBuilding1Coordinates();
        int[] building2Coordinates = buildingAgent.getBuilding2Coordinates();
        int[] building3Coordinates = buildingAgent.getBuilding3Coordinates();
        int[] building6Coordinates = buildingAgent.getBuilding6Coordinates();
        int[] building7Coordinates = buildingAgent.getBuilding7Coordinates();
        int[] building9Coordinates = buildingAgent.getBuilding9Coordinates();

        int randomBuildingIndex = decideBaseLocation(building1Coordinates, building2Coordinates, building3Coordinates);
        switch (randomBuildingIndex) {
            case 0:
                alphaBaseX = building1Coordinates[0];
                alphaBaseY = building1Coordinates[1];
                break;
            case 1:
                alphaBaseX = building2Coordinates[0];
                alphaBaseY = building2Coordinates[1];
                break;
            case 2:
                alphaBaseX = building3Coordinates[0];
                alphaBaseY = building3Coordinates[1];
                break;
        }

        int[] alphaRespawnPointCoordinates = decideRespawnPointLocation(randomBuildingIndex, building1Coordinates,
                building2Coordinates, building3Coordinates);

        int randomBuildingIndex2 = decideBravoBaseLocation(building6Coordinates, building7Coordinates,
                building9Coordinates);
        switch (randomBuildingIndex2) {
            case 0:
                bravoBaseX = building6Coordinates[0];
                bravoBaseY = building6Coordinates[1];
                break;
            case 1:
                bravoBaseX = building7Coordinates[0];
                bravoBaseY = building7Coordinates[1];
                break;
            case 2:
                bravoBaseX = building9Coordinates[0];
                bravoBaseY = building9Coordinates[1];
                break;
        }

        int[] bravoRespawnPointCoordinates = decideBravoRespawnPointLocation(randomBuildingIndex2, building6Coordinates,
                building7Coordinates, building9Coordinates);

        alphaRespawnPointX = alphaRespawnPointCoordinates[0];
        alphaRespawnPointY = alphaRespawnPointCoordinates[1];
        bravoRespawnPointX = bravoRespawnPointCoordinates[0];
        bravoRespawnPointY = bravoRespawnPointCoordinates[1];
    }

    // Method to decide Alpha base location
    private int decideBaseLocation(int[] buildingCoordinates1, int[] buildingCoordinates2, int[] buildingCoordinates3) {
        Random random = new Random();
        return random.nextInt(3);
    }

    // Method to decide Alpha respawn point location
    private int[] decideRespawnPointLocation(int baseIndex, int[] buildingCoordinates1, int[] buildingCoordinates2,
            int[] buildingCoordinates3) {
        int[] chosenBuildingCoordinates = null;
        int[] respawnPointCoordinates = new int[2];

        switch (baseIndex) {
            case 0:
                chosenBuildingCoordinates = buildingCoordinates1;
                break;
            case 1:
                chosenBuildingCoordinates = buildingCoordinates2;
                break;
            case 2:
                chosenBuildingCoordinates = buildingCoordinates3;
                break;
        }

        if (chosenBuildingCoordinates == buildingCoordinates1) {
            respawnPointCoordinates[0] = buildingCoordinates2[0];
            respawnPointCoordinates[1] = buildingCoordinates2[1];
        } else if (chosenBuildingCoordinates == buildingCoordinates2) {
            respawnPointCoordinates[0] = buildingCoordinates3[0];
            respawnPointCoordinates[1] = buildingCoordinates3[1];
        } else if (chosenBuildingCoordinates == buildingCoordinates3) {
            respawnPointCoordinates[0] = buildingCoordinates1[0];
            respawnPointCoordinates[1] = buildingCoordinates1[1];
        }

        return respawnPointCoordinates;
    }

    // Method to decide Bravo base location
    private int decideBravoBaseLocation(int[] buildingCoordinates6, int[] buildingCoordinates7,
            int[] buildingCoordinates9) {
        Random random = new Random();
        return random.nextInt(3);
    }

    // Method to decide Bravo respawn point location
    private int[] decideBravoRespawnPointLocation(int baseIndex, int[] buildingCoordinates6, int[] buildingCoordinates7,
            int[] buildingCoordinates9) {
        int[] chosenBuildingCoordinates = null;
        int[] respawnPointCoordinates = new int[2];

        switch (baseIndex) {
            case 0:
                chosenBuildingCoordinates = buildingCoordinates6;
                break;
            case 1:
                chosenBuildingCoordinates = buildingCoordinates7;
                break;
            case 2:
                chosenBuildingCoordinates = buildingCoordinates9;
                break;
        }

        if (chosenBuildingCoordinates == buildingCoordinates6) {
            respawnPointCoordinates[0] = buildingCoordinates7[0];
            respawnPointCoordinates[1] = buildingCoordinates7[1];
        } else if (chosenBuildingCoordinates == buildingCoordinates7) {
            respawnPointCoordinates[0] = buildingCoordinates9[0];
            respawnPointCoordinates[1] = buildingCoordinates9[1];
        } else if (chosenBuildingCoordinates == buildingCoordinates9) {
            respawnPointCoordinates[0] = buildingCoordinates6[0];
            respawnPointCoordinates[1] = buildingCoordinates6[1];
        }

        return respawnPointCoordinates;
    }

    // Method to get Alpha base coordinates
    public int[] getAlphaBaseCoordinates() {
        return new int[] { alphaBaseX, alphaBaseY };
    }

    // Method to get Bravo base coordinates
    public int[] getBravoBaseCoordinates() {
        return new int[] { bravoBaseX, bravoBaseY };
    }

    // Method to get Alpha respawn point coordinates
    public int[] getAlphaRespawnPointCoordinates() {
        return new int[] { alphaRespawnPointX, alphaRespawnPointY };
    }

    // Method to get Bravo respawn point coordinates
    public int[] getBravoRespawnPointCoordinates() {
        return new int[] { bravoRespawnPointX, bravoRespawnPointY };
    }

    public void printBaseRespawnCoordinates() {
        System.out.println("Alpha Base: " + alphaBaseX + ", " + alphaBaseY);
        System.out.println("Bravo Base: " + bravoBaseX + ", " + bravoBaseY);
        System.out.println("Alpha Respawn Point: " + alphaRespawnPointX + ", " + alphaRespawnPointY);
        System.out.println("Bravo Respawn Point: " + bravoRespawnPointX + ", " + bravoRespawnPointY);
    }

    public static void main(String[] args) {
        String ontologyFilePath = "ontology/airsoftField.owx";
        OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
        try {
            File ontologyFile = new File(ontologyFilePath);
            OWLOntology ontology = ontologyManager.loadOntologyFromOntologyDocument(ontologyFile);

            MapModel model = new MapModel(ontologyFilePath, ontology);

            OntologyLoader.visualizeObjectsForScenarios(ontology);

            BaseRespawnAgent agent = new BaseRespawnAgent(ontology, model);
            agent.assignObjectsToAgent();

            System.out.println("Objects assigned to intelligent agent:");
            OntologyLoader.visualizeObjectsForScenarios(ontology);

        } catch (OWLOntologyCreationException e) {
            System.err.println("Error loading ontology: " + e.getMessage());
        }
    }
}