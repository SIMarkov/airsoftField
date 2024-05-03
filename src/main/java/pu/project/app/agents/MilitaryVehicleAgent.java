package pu.project.app.agents;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import pu.project.app.map.MapModel;
import pu.project.app.ontology.OntologyLoader;

public class MilitaryVehicleAgent {
    private OWLOntology ontology;
    private MapModel model;

    private int milSUV1X;
    private int milSUV1Y;
    private int milSUV2X;
    private int milSUV2Y;
    private int milSUV3X;
    private int milSUV3Y;
    private int milSUV4X;
    private int milSUV4Y;
    private int milAPC1X;
    private int milAPC1Y;
    private int milAPC2X;
    private int milAPC2Y;
    private int milAPC3X;
    private int milAPC3Y;
    private int milAPC4X;
    private int milAPC4Y;

    private List<int[]> occupiedCoordinates = new ArrayList<>();

    // Constructor for MilitaryVehicleAgent
    public MilitaryVehicleAgent(OWLOntology ontology, MapModel model) {
        this.ontology = ontology;
        this.model = model;
        placeMilitaryVehicles();
    }

    // Method to assign objects to the agent
    public void assignObjectsToAgent() {
        OWLDataFactory dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();

        OWLClass agentClass = dataFactory.getOWLClass(IRI.create("file://ontology/airsoftField.owx#MilitaryVehicleAgent"));

        OWLNamedIndividual agentInstance = dataFactory
                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField#militaryVehicleAgentInstance"));
        OWLClassAssertionAxiom classAssertion = dataFactory.getOWLClassAssertionAxiom(agentClass, agentInstance);

        OWLNamedIndividual milSUV1 = dataFactory
                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#MilitarySUV1"));
        OWLNamedIndividual milSUV2 = dataFactory
                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#MilitarySUV2"));
        OWLNamedIndividual milSUV3 = dataFactory
                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#MilitarySUV3"));
        OWLNamedIndividual milSUV4 = dataFactory
                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#MilitarySUV4"));
        OWLNamedIndividual milAPC1 = dataFactory
                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#ArmoredPersonnelCarrier1"));
        OWLNamedIndividual milAPC2 = dataFactory
                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#ArmoredPersonnelCarrier2"));
        OWLNamedIndividual milAPC3 = dataFactory
                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#ArmoredPersonnelCarrier3"));
        OWLNamedIndividual milAPC4 = dataFactory
                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#ArmoredPersonnelCarrier4"));
        OWLObjectProperty belongsToAgentProperty = dataFactory
                .getOWLObjectProperty(IRI.create("file://ontology/airsoftField.owx#belongsToAgent"));

        OWLObjectPropertyAssertionAxiom assertion1 = dataFactory
                .getOWLObjectPropertyAssertionAxiom(belongsToAgentProperty, milSUV1, agentInstance);
        OWLObjectPropertyAssertionAxiom assertion2 = dataFactory
                .getOWLObjectPropertyAssertionAxiom(belongsToAgentProperty, milSUV2, agentInstance);
        OWLObjectPropertyAssertionAxiom assertion3 = dataFactory
                .getOWLObjectPropertyAssertionAxiom(belongsToAgentProperty, milSUV3, agentInstance);
        OWLObjectPropertyAssertionAxiom assertion4 = dataFactory
                .getOWLObjectPropertyAssertionAxiom(belongsToAgentProperty, milSUV4, agentInstance);
        OWLObjectPropertyAssertionAxiom assertion5 = dataFactory
                .getOWLObjectPropertyAssertionAxiom(belongsToAgentProperty, milAPC1, agentInstance);
        OWLObjectPropertyAssertionAxiom assertion6 = dataFactory
                .getOWLObjectPropertyAssertionAxiom(belongsToAgentProperty, milAPC2, agentInstance);
        OWLObjectPropertyAssertionAxiom assertion7 = dataFactory
                .getOWLObjectPropertyAssertionAxiom(belongsToAgentProperty, milAPC3, agentInstance);
        OWLObjectPropertyAssertionAxiom assertion8 = dataFactory
                .getOWLObjectPropertyAssertionAxiom(belongsToAgentProperty, milAPC4, agentInstance);
        ontology.getOWLOntologyManager().addAxiom(ontology, classAssertion);
        ontology.getOWLOntologyManager().addAxiom(ontology, assertion1);
        ontology.getOWLOntologyManager().addAxiom(ontology, assertion2);
        ontology.getOWLOntologyManager().addAxiom(ontology, assertion3);
        ontology.getOWLOntologyManager().addAxiom(ontology, assertion4);
        ontology.getOWLOntologyManager().addAxiom(ontology, assertion5);
        ontology.getOWLOntologyManager().addAxiom(ontology, assertion6);
        ontology.getOWLOntologyManager().addAxiom(ontology, assertion7);
        ontology.getOWLOntologyManager().addAxiom(ontology, assertion8);
    }

    // Method to place military vehicles
    public void placeMilitaryVehicles() {
        BaseRespawnAgent baseAgent = new BaseRespawnAgent(ontology, model);
        int[] alphaBaseCoordinates = baseAgent.getAlphaBaseCoordinates();
        int[] bravoBaseCoordinates = baseAgent.getBravoBaseCoordinates();

        int[] militaryVehicle1Coordinates = generateMilitaryVehicleCoordinates(alphaBaseCoordinates[0],
                alphaBaseCoordinates[1]);
        milSUV1X = militaryVehicle1Coordinates[0];
        milSUV1Y = militaryVehicle1Coordinates[1];

        int[] militaryVehicle2Coordinates = generateMilitaryVehicleCoordinates(alphaBaseCoordinates[0],
                alphaBaseCoordinates[1]);
        milSUV2X = militaryVehicle2Coordinates[0];
        milSUV2Y = militaryVehicle2Coordinates[1];

        int[] militaryVehicle3Coordinates = generateMilitaryVehicleCoordinates(bravoBaseCoordinates[0],
                bravoBaseCoordinates[1]);
        milSUV3X = militaryVehicle3Coordinates[0];
        milSUV3Y = militaryVehicle3Coordinates[1];

        int[] militaryVehicle4Coordinates = generateMilitaryVehicleCoordinates(bravoBaseCoordinates[0],
                bravoBaseCoordinates[1]);
        milSUV4X = militaryVehicle4Coordinates[0];
        milSUV4Y = militaryVehicle4Coordinates[1];

        int[] apc1Coordinates = generateMilitaryVehicleCoordinates(alphaBaseCoordinates[0], alphaBaseCoordinates[1]);
        milAPC1X = apc1Coordinates[0];
        milAPC1Y = apc1Coordinates[1];

        int[] apc2Coordinates = generateMilitaryVehicleCoordinates(alphaBaseCoordinates[0], alphaBaseCoordinates[1]);
        milAPC2X = apc2Coordinates[0];
        milAPC2Y = apc2Coordinates[1];

        int[] apc3Coordinates = generateMilitaryVehicleCoordinates(bravoBaseCoordinates[0], bravoBaseCoordinates[1]);
        milAPC3X = apc3Coordinates[0];
        milAPC3Y = apc3Coordinates[1];

        int[] apc4Coordinates = generateMilitaryVehicleCoordinates(bravoBaseCoordinates[0], bravoBaseCoordinates[1]);
        milAPC4X = apc4Coordinates[0];
        milAPC4Y = apc4Coordinates[1];
    }

    // Method to generate military vehicle coordinates
    private int[] generateMilitaryVehicleCoordinates(int baseX, int baseY) {
        List<int[]> freeCoordinates = new ArrayList<>();
        for (int x = baseX - 1; x <= baseX + 1; x++) {
            for (int y = baseY - 1; y <= baseY + 1; y++) {
                if (isValidPosition(x, y) && !isOccupied(x, y)) {
                    freeCoordinates.add(new int[] { x, y });
                }
            }
        }

        if (freeCoordinates.isEmpty()) {
            Random random = new Random();
            int randomX, randomY;

            do {
                randomX = baseX + random.nextInt(5) - 2;
                randomY = baseY + random.nextInt(5) - 2;
            } while (!isValidPosition(randomX, randomY) || isOccupied(randomX, randomY));

            randomX = Math.max(0, Math.min(randomX, model.getWidth() - 1));
            randomY = Math.max(0, Math.min(randomY, model.getHeight() - 1));

            addOccupiedCoordinates(randomX, randomY);

            return new int[] { randomX, randomY };
        }

        Random random = new Random();
        int index = random.nextInt(freeCoordinates.size());
        int[] newCoordinates = freeCoordinates.get(index);

        addOccupiedCoordinates(newCoordinates[0], newCoordinates[1]);

        return newCoordinates;
    }

    // Method to check if position is valid
    private boolean isValidPosition(int x, int y) {
        if (x < 0 || x >= model.getWidth() || y < 0 || y >= model.getHeight()) {
            return false;
        }

        if ((x == 0 && (y == 0 || y == 2)) ||
                (x == 7 && (y == 10 || y == 12)) ||
                (x == 8 && y == 10) ||
                (x == 9 && (y == 10 || y == 12))) {
            return false;
        }

        BuildingManager buildingManager = new BuildingManager(ontology, model);
        int[][] forbiddenBuildingCoordinates = {
                buildingManager.getBuilding1Coordinates(),
                buildingManager.getBuilding2Coordinates(),
                buildingManager.getBuilding3Coordinates(),
                buildingManager.getBuilding6Coordinates(),
                buildingManager.getBuilding7Coordinates(),
                buildingManager.getBuilding9Coordinates()
        };
        for (int[] forbiddenCoordinate : forbiddenBuildingCoordinates) {
            if (x == forbiddenCoordinate[0] && y == forbiddenCoordinate[1]) {
                return false;
            }
        }

        return true;
    }

    // Method to check if position is occupied
    private boolean isOccupied(int x, int y) {
        for (int[] coord : occupiedCoordinates) {
            if (coord[0] == x && coord[1] == y) {
                return true;
            }
        }
        return false;
    }

    // Method to add occupied coordinates
    private void addOccupiedCoordinates(int x, int y) {
        occupiedCoordinates.add(new int[] { x, y });
    }

    // Method to decide
    public void decide() {
        int[] newCoordinates1 = generateMilitaryVehicleCoordinates(milSUV1X, milSUV1Y);
        int[] newCoordinates2 = generateMilitaryVehicleCoordinates(milSUV2X, milSUV2Y);
        int[] newCoordinates3 = generateMilitaryVehicleCoordinates(milSUV3X, milSUV3Y);
        int[] newCoordinates4 = generateMilitaryVehicleCoordinates(milSUV4X, milSUV4Y);
        int[] newCoordinates5 = generateMilitaryVehicleCoordinates(milAPC1X, milAPC1Y);
        int[] newCoordinates6 = generateMilitaryVehicleCoordinates(milAPC2X, milAPC2Y);
        int[] newCoordinates7 = generateMilitaryVehicleCoordinates(milAPC3X, milAPC3Y);
        int[] newCoordinates8 = generateMilitaryVehicleCoordinates(milAPC4X, milAPC4Y);

        milSUV1X = newCoordinates1[0];
        milSUV1Y = newCoordinates1[1];
        milSUV2X = newCoordinates2[0];
        milSUV2Y = newCoordinates2[1];
        milSUV3X = newCoordinates3[0];
        milSUV3Y = newCoordinates3[1];
        milSUV4X = newCoordinates4[0];
        milSUV4Y = newCoordinates4[1];
        milAPC1X = newCoordinates5[0];
        milAPC1Y = newCoordinates5[1];
        milAPC2X = newCoordinates6[0];
        milAPC2Y = newCoordinates6[1];
        milAPC3X = newCoordinates7[0];
        milAPC3Y = newCoordinates7[1];
        milAPC4X = newCoordinates8[0];
        milAPC4Y = newCoordinates8[1];

    }

    public int[] getMilitaryVehicle1Coordinates() {
        return new int[] { milSUV1X, milSUV1Y };
    }

    public int[] getMilitaryVehicle2Coordinates() {
        return new int[] { milSUV2X, milSUV2Y };
    }

    public int[] getMilitaryVehicle3Coordinates() {
        return new int[] { milSUV3X, milSUV3Y };
    }

    public int[] getMilitaryVehicle4Coordinates() {
        return new int[] { milSUV4X, milSUV4Y };
    }

    public int[] getMilitaryVehicle5Coordinates() {
        return new int[] { milAPC1X, milAPC1Y };
    }

    public int[] getMilitaryVehicle6Coordinates() {
        return new int[] { milAPC2X, milAPC2Y };
    }

    public int[] getMilitaryVehicle7Coordinates() {
        return new int[] { milAPC3X, milAPC3Y };
    }

    public int[] getMilitaryVehicle8Coordinates() {
        return new int[] { milAPC4X, milAPC4Y };
    }

    public void printMilitarySUVCoordinates() {
        System.out.println("Military SUV 1: " + milSUV1X + ", " + milSUV1Y);
        System.out.println("Military SUV 2: " + milSUV2X + ", " + milSUV2Y);
        System.out.println("Military SUV 3: " + milSUV3X + ", " + milSUV3Y);
        System.out.println("Military SUV 4: " + milSUV4X + ", " + milSUV4Y);
    }

    public void printMilitaryAPCCoordinates() {
        System.out.println("Military APC 1: " + milAPC1X + ", " + milAPC1Y);
        System.out.println("Military APC 2: " + milAPC2X + ", " + milAPC2Y);
        System.out.println("Military APC 3: " + milAPC3X + ", " + milAPC3Y);
        System.out.println("Military APC 4: " + milAPC4X + ", " + milAPC4Y);
    }

    public static void main(String[] args) {
        String ontologyFilePath = "ontology/airsoftField.owx";
        OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
        try {
            File ontologyFile = new File(ontologyFilePath);
            OWLOntology ontology = ontologyManager.loadOntologyFromOntologyDocument(ontologyFile);

            if (ontology != null) {

                System.out.println("Loaded ontology: " + ontology.getOntologyID().getOntologyIRI().toString());

                OntologyLoader.visualizeObjectsForScenarios(ontology);
                MapModel model = new MapModel(ontologyFilePath, ontology);
                MilitaryVehicleAgent agent = new MilitaryVehicleAgent(ontology, model);
                agent.assignObjectsToAgent();

                System.out.println("Objects assigned to militaryvehicle agent:");
                OntologyLoader.visualizeObjectsForScenarios(ontology);
            } else {
                System.err.println("Error: Ontology not loaded.");
            }
        } catch (OWLOntologyCreationException e) {
            System.err.println("Error loading ontology: " + e.getMessage());
        }
    }
}