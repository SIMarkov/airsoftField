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

public class CommCenterAgent {

    private OWLOntology ontology;
    private MapModel model;
    private int commCenterX;
    private int commCenterY;

    // Constructor for CommCenterAgent
    public CommCenterAgent(OWLOntology ontology, MapModel model) {
        this.ontology = ontology;
        this.model = model;
        placeCommCenter();
    }

    // Method to assign objects to the agent
    public void assignObjectsToAgent() {
        OWLDataFactory dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();

        OWLClass agentClass = dataFactory.getOWLClass(IRI.create("file://ontology/airsoftField.owx#CommCenterAgent"));

        OWLNamedIndividual agentInstance = dataFactory
                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#commCenterAgentInstance"));
        OWLClassAssertionAxiom classAssertion = dataFactory.getOWLClassAssertionAxiom(agentClass, agentInstance);

        OWLNamedIndividual commCenter = dataFactory
                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#CommCenter1"));
        OWLObjectProperty belongsToAgentProperty = dataFactory
                .getOWLObjectProperty(IRI.create("file://ontology/airsoftField.owx#belongsToAgent"));
        OWLObjectPropertyAssertionAxiom assertion = dataFactory
                .getOWLObjectPropertyAssertionAxiom(belongsToAgentProperty, commCenter, agentInstance);

        ontology.getOWLOntologyManager().addAxiom(ontology, classAssertion);
        ontology.getOWLOntologyManager().addAxiom(ontology, assertion);
    }

    // Method to place the communication center on the map
    public void placeCommCenter() {
        BuildingManager buildingAgent = new BuildingManager(ontology, model);

        int[] building4Coordinates = buildingAgent.getBuilding4Coordinates();
        int[] building5Coordinates = buildingAgent.getBuilding5Coordinates();
        int[] building11Coordinates = buildingAgent.getBuilding11Coordinates();
        int[] building13Coordinates = buildingAgent.getBuilding13Coordinates();
        int[] building14Coordinates = buildingAgent.getBuilding14Coordinates();

        int randomBuildingIndex = decideCommCenterLocation(building4Coordinates, building5Coordinates,
                building11Coordinates, building13Coordinates, building14Coordinates);
        switch (randomBuildingIndex) {
            case 0:
                commCenterX = building4Coordinates[0];
                commCenterY = building4Coordinates[1];
                break;
            case 1:
                commCenterX = building5Coordinates[0];
                commCenterY = building5Coordinates[1];
                break;
            case 2:
                commCenterX = building11Coordinates[0];
                commCenterY = building11Coordinates[1];
                break;
            case 3:
                commCenterX = building13Coordinates[0];
                commCenterY = building13Coordinates[1];
                break;
            case 4:
                commCenterX = building14Coordinates[0];
                commCenterY = building14Coordinates[1];
                break;
        }
    }

    // Method to decide the location of the communication center
    private int decideCommCenterLocation(int[] buildingCoordinates4, int[] buildingCoordinates5,
            int[] buildingCoordinates11, int[] buildingCoordinates13, int[] buildingCoordinates14) {
        Random random = new Random();
        return random.nextInt(5);
    }

    // Method to get the coordinates of the communication center
    public int[] getCommCenterCoordinates() {
        return new int[] { commCenterX, commCenterY };
    }

    // Method to print the coordinates of the communication center
    public void printCommCenterCoordinates() {
        System.out.println("Comm Center: " + commCenterX + ", " + commCenterY);
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

            CommCenterAgent agent = new CommCenterAgent(ontology, model);
            agent.assignObjectsToAgent();

            System.out.println("Objects assigned to CommCenter agent:");
            OntologyLoader.visualizeObjectsForScenarios(ontology);

        } catch (OWLOntologyCreationException e) {
            System.err.println("Error loading ontology: " + e.getMessage());
        }
    }
}
