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

public class HelicopterAgent {
    private OWLOntology ontology;
    private MapModel model;
    private int helicopterX;
    private int helicopterY;

    public HelicopterAgent(OWLOntology ontology, MapModel model) {
        this.ontology = ontology;
        this.model = model;
        placeHelicopter();
    }

    // Assigns objects to the helicopter agent
    public void assignObjectsToAgent() {
        OWLDataFactory dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();

        OWLClass agentClass = dataFactory.getOWLClass(IRI.create("file://ontology/airsoftField.owx#HelicopterAgent"));

        OWLNamedIndividual agentInstance = dataFactory
                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#helicopterAgentInstance"));
        OWLClassAssertionAxiom classAssertion = dataFactory.getOWLClassAssertionAxiom(agentClass, agentInstance);

        OWLNamedIndividual helicopter = dataFactory
                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#Helicopter1"));
        OWLObjectProperty belongsToAgentProperty = dataFactory
                .getOWLObjectProperty(IRI.create("file://ontology/airsoftField.owx#belongsToAgent"));
        OWLObjectPropertyAssertionAxiom assertion = dataFactory
                .getOWLObjectPropertyAssertionAxiom(belongsToAgentProperty, helicopter, agentInstance);

        ontology.getOWLOntologyManager().addAxiom(ontology, classAssertion);
        ontology.getOWLOntologyManager().addAxiom(ontology, assertion);
    }

    // Places the helicopter on the map
    public void placeHelicopter() {
        BuildingManager buildingAgent = new BuildingManager(ontology, model);
        int[] building5Coordinates = buildingAgent.getBuilding5Coordinates();

        int[][] possiblePositions = {
                { building5Coordinates[0], building5Coordinates[1] },
                { 12, 7 }, { 13, 7 }, { 5, 7 }
        };

        int decision = decideHelicopterLocation();

        helicopterX = possiblePositions[decision][0];
        helicopterY = possiblePositions[decision][1];
    }

    // Randomly decides the location of the helicopter
    private int decideHelicopterLocation() {
        Random random = new Random();
        return random.nextInt(4);
    }

    // Retrieves the coordinates of the helicopter
    public int[] getHelicopterCoordinates() {
        return new int[] { helicopterX, helicopterY };
    }

    // Prints the coordinates of the helicopter
    public void printHelicopterCoordinates() {
        System.out.println("Helicopter: " + helicopterX + ", " + helicopterY);
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

            HelicopterAgent agent = new HelicopterAgent(ontology, model);
            agent.assignObjectsToAgent();

            System.out.println("Objects assigned to CommCenter agent:");
            OntologyLoader.visualizeObjectsForScenarios(ontology);

        } catch (OWLOntologyCreationException e) {
            System.err.println("Error loading ontology: " + e.getMessage());
        }
    }
}
