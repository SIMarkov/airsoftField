package pu.project.app.agents;

import java.io.File;
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

public class FlagAgent {
    private OWLOntology ontology;
    private MapModel model;

    private int flag1X;
    private int flag1Y;
    private int flag2X;
    private int flag2Y;
    private int flag3X;
    private int flag3Y;

    // Constructor initializing the flag agent with ontology and map model
    public FlagAgent(OWLOntology ontology, MapModel model) {
        this.ontology = ontology;
        this.model = model;
        placeFlags();
    }

    // Method to assign flag objects to the agent
    public void assignObjectsToAgent() {
        OWLDataFactory dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();

        OWLClass agentClass = dataFactory.getOWLClass(IRI.create("file://ontology/airsoftField.owx#FlagAgent"));

        OWLNamedIndividual agentInstance = dataFactory
                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#flagAgentInstance"));
        OWLClassAssertionAxiom classAssertion = dataFactory.getOWLClassAssertionAxiom(agentClass, agentInstance);

        OWLNamedIndividual flag1 = dataFactory.getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#Flag1"));
        OWLNamedIndividual flag2 = dataFactory.getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#Flag2"));
        OWLNamedIndividual flag3 = dataFactory.getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#Flag3"));
        OWLObjectProperty belongsToAgentProperty = dataFactory
                .getOWLObjectProperty(IRI.create("file://ontology/airsoftField.owx#belongsToAgent"));

        OWLObjectPropertyAssertionAxiom assertion1 = dataFactory
                .getOWLObjectPropertyAssertionAxiom(belongsToAgentProperty, flag1, agentInstance);
        OWLObjectPropertyAssertionAxiom assertion2 = dataFactory
                .getOWLObjectPropertyAssertionAxiom(belongsToAgentProperty, flag2, agentInstance);
        OWLObjectPropertyAssertionAxiom assertion3 = dataFactory
                .getOWLObjectPropertyAssertionAxiom(belongsToAgentProperty, flag3, agentInstance);
        ontology.getOWLOntologyManager().addAxiom(ontology, classAssertion);
        ontology.getOWLOntologyManager().addAxiom(ontology, assertion1);
        ontology.getOWLOntologyManager().addAxiom(ontology, assertion2);
        ontology.getOWLOntologyManager().addAxiom(ontology, assertion3);
    }

    // Method to place flags on the map
    public void placeFlags() {
        BuildingManager buildingAgent = new BuildingManager(ontology, model);
        int[] building4Coordinates = buildingAgent.getBuilding4Coordinates();
        int[] building14Coordinates = buildingAgent.getBuilding14Coordinates();
        int[] building15Coordinates = buildingAgent.getBuilding15Coordinates();

        flag1X = building4Coordinates[0];
        flag1Y = building4Coordinates[1];

        flag2X = 5;
        flag2Y = 7;

        int[][] possiblePositions = { { 12, 7 }, { building14Coordinates[0], building14Coordinates[1] },
                { building15Coordinates[0], building15Coordinates[1] } };

        int decision = decideFlag3Location();
        flag3X = possiblePositions[decision][0];
        flag3Y = possiblePositions[decision][1];
    }

    // Method to decide the location of the third flag randomly
    private int decideFlag3Location() {
        Random random = new Random();
        return random.nextInt(3);
    }

    public int[] getFlag1Coordinates() {
        return new int[] { flag1X, flag1Y };
    }

    public int[] getFlag2Coordinates() {
        return new int[] { flag2X, flag2Y };
    }

    public int[] getFlag3Coordinates() {
        return new int[] { flag3X, flag3Y };
    }

    // Method to print flag coordinates
    public void printFlagCoordinates() {
        System.out.println("Flag 1: " + flag1X + ", " + flag1Y);
        System.out.println("Flag 2: " + flag2X + ", " + flag2Y);
        System.out.println("Flag 3: " + flag3X + ", " + flag3Y);
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
                FlagAgent agent = new FlagAgent(ontology, model);
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
