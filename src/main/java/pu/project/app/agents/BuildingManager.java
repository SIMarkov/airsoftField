package pu.project.app.agents;

import java.io.File;

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

public class BuildingManager {

        private OWLOntology ontology;

        private int building1X;
        private int building1Y;
        private int building2X;
        private int building2Y;
        private int building3X;
        private int building3Y;
        private int building4X;
        private int building4Y;
        private int building5X;
        private int building5Y;
        private int building6X;
        private int building6Y;
        private int building7X;
        private int building7Y;
        private int building8X;
        private int building8Y;
        private int building9X;
        private int building9Y;
        private int building10X;
        private int building10Y;
        private int building11X;
        private int building11Y;
        private int building12X;
        private int building12Y;
        private int building13X;
        private int building13Y;
        private int building14X;
        private int building14Y;
        private int building15X;
        private int building15Y;

        // Initializing BuildingManager with ontology and model
        public BuildingManager(OWLOntology ontology, MapModel model) {
                this.ontology = ontology;
                placeBuldings();
        }

        // Assigning objects to agent
        public void assignObjectsToAgent() {
                OWLDataFactory dataFactory = ontology.getOWLOntologyManager().getOWLDataFactory();

                OWLClass agentClass = dataFactory.getOWLClass(IRI.create("file://ontology/airsoftField.owx#BuildingAgent"));

                OWLNamedIndividual agentInstance = dataFactory
                                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#buildingAgentInstance"));
                OWLClassAssertionAxiom classAssertion = dataFactory.getOWLClassAssertionAxiom(agentClass,
                                agentInstance);

                OWLNamedIndividual building1 = dataFactory
                                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#Building1"));
                OWLNamedIndividual building2 = dataFactory
                                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#Building2"));
                OWLNamedIndividual building3 = dataFactory
                                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#Building3"));
                OWLNamedIndividual building4 = dataFactory
                                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#Building4"));
                OWLNamedIndividual building5 = dataFactory
                                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#Building5"));
                OWLNamedIndividual building6 = dataFactory
                                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#Building6"));
                OWLNamedIndividual building7 = dataFactory
                                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#Building7"));
                OWLNamedIndividual building8 = dataFactory
                                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#Building8"));
                OWLNamedIndividual building9 = dataFactory
                                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#Building9"));
                OWLNamedIndividual building10 = dataFactory
                                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#Building10"));
                OWLNamedIndividual building11 = dataFactory
                                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#Building11"));
                OWLNamedIndividual building12 = dataFactory
                                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#Building12"));
                OWLNamedIndividual building13 = dataFactory
                                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#Building13"));
                OWLNamedIndividual building14 = dataFactory
                                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#Building14"));
                OWLNamedIndividual building15 = dataFactory
                                .getOWLNamedIndividual(IRI.create("file://ontology/airsoftField.owx#Building15"));
                OWLObjectProperty belongsToAgentProperty = dataFactory
                                .getOWLObjectProperty(IRI.create("file://ontology/airsoftField.owx#belongsToAgent"));

                OWLObjectPropertyAssertionAxiom assertion1 = dataFactory
                                .getOWLObjectPropertyAssertionAxiom(belongsToAgentProperty, building1, agentInstance);
                OWLObjectPropertyAssertionAxiom assertion2 = dataFactory
                                .getOWLObjectPropertyAssertionAxiom(belongsToAgentProperty, building2, agentInstance);
                OWLObjectPropertyAssertionAxiom assertion3 = dataFactory
                                .getOWLObjectPropertyAssertionAxiom(belongsToAgentProperty, building3, agentInstance);
                OWLObjectPropertyAssertionAxiom assertion4 = dataFactory
                                .getOWLObjectPropertyAssertionAxiom(belongsToAgentProperty, building4, agentInstance);
                OWLObjectPropertyAssertionAxiom assertion5 = dataFactory
                                .getOWLObjectPropertyAssertionAxiom(belongsToAgentProperty, building5, agentInstance);
                OWLObjectPropertyAssertionAxiom assertion6 = dataFactory
                                .getOWLObjectPropertyAssertionAxiom(belongsToAgentProperty, building6, agentInstance);
                OWLObjectPropertyAssertionAxiom assertion7 = dataFactory
                                .getOWLObjectPropertyAssertionAxiom(belongsToAgentProperty, building7, agentInstance);
                OWLObjectPropertyAssertionAxiom assertion8 = dataFactory
                                .getOWLObjectPropertyAssertionAxiom(belongsToAgentProperty, building8, agentInstance);
                OWLObjectPropertyAssertionAxiom assertion9 = dataFactory
                                .getOWLObjectPropertyAssertionAxiom(belongsToAgentProperty, building9, agentInstance);
                OWLObjectPropertyAssertionAxiom assertion10 = dataFactory
                                .getOWLObjectPropertyAssertionAxiom(belongsToAgentProperty, building10, agentInstance);
                OWLObjectPropertyAssertionAxiom assertion11 = dataFactory
                                .getOWLObjectPropertyAssertionAxiom(belongsToAgentProperty, building11, agentInstance);
                OWLObjectPropertyAssertionAxiom assertion12 = dataFactory
                                .getOWLObjectPropertyAssertionAxiom(belongsToAgentProperty, building12, agentInstance);
                OWLObjectPropertyAssertionAxiom assertion13 = dataFactory
                                .getOWLObjectPropertyAssertionAxiom(belongsToAgentProperty, building13, agentInstance);
                OWLObjectPropertyAssertionAxiom assertion14 = dataFactory
                                .getOWLObjectPropertyAssertionAxiom(belongsToAgentProperty, building14, agentInstance);
                OWLObjectPropertyAssertionAxiom assertion15 = dataFactory
                                .getOWLObjectPropertyAssertionAxiom(belongsToAgentProperty, building15, agentInstance);

                ontology.getOWLOntologyManager().addAxiom(ontology, classAssertion);
                ontology.getOWLOntologyManager().addAxiom(ontology, assertion1);
                ontology.getOWLOntologyManager().addAxiom(ontology, assertion2);
                ontology.getOWLOntologyManager().addAxiom(ontology, assertion3);
                ontology.getOWLOntologyManager().addAxiom(ontology, assertion4);
                ontology.getOWLOntologyManager().addAxiom(ontology, assertion5);
                ontology.getOWLOntologyManager().addAxiom(ontology, assertion6);
                ontology.getOWLOntologyManager().addAxiom(ontology, assertion7);
                ontology.getOWLOntologyManager().addAxiom(ontology, assertion8);
                ontology.getOWLOntologyManager().addAxiom(ontology, assertion9);
                ontology.getOWLOntologyManager().addAxiom(ontology, assertion10);
                ontology.getOWLOntologyManager().addAxiom(ontology, assertion11);
                ontology.getOWLOntologyManager().addAxiom(ontology, assertion12);
                ontology.getOWLOntologyManager().addAxiom(ontology, assertion13);
                ontology.getOWLOntologyManager().addAxiom(ontology, assertion14);
                ontology.getOWLOntologyManager().addAxiom(ontology, assertion15);
        }

        // Placing buildings on the map
        public void placeBuldings() {
                building1X = 1;
                building1Y = 0;
                building2X = 0;
                building2Y = 1;
                building3X = 0;
                building3Y = 3;
                building4X = 4;
                building4Y = 9;
                building5X = 5;
                building5Y = 14;
                building6X = 7;
                building6Y = 11;
                building7X = 8;
                building7Y = 11;
                building8X = 9;
                building8Y = 12;
                building9X = 8;
                building9Y = 12;
                building10X = 8;
                building10Y = 10;
                building11X = 11;
                building11Y = 9;
                building12X = 12;
                building12Y = 9;
                building13X = 11;
                building13Y = 8;
                building14X = 11;
                building14Y = 6;
                building15X = 11;
                building15Y = 7;
        }

        public int[] getBuilding1Coordinates() {
                return new int[] { building1X, building1Y };
        }

        public int[] getBuilding2Coordinates() {
                return new int[] { building2X, building2Y };
        }

        public int[] getBuilding3Coordinates() {
                return new int[] { building3X, building3Y };
        }

        public int[] getBuilding4Coordinates() {
                return new int[] { building4X, building4Y };
        }

        public int[] getBuilding5Coordinates() {
                return new int[] { building5X, building5Y };
        }

        public int[] getBuilding6Coordinates() {
                return new int[] { building6X, building6Y };
        }

        public int[] getBuilding7Coordinates() {
                return new int[] { building7X, building7Y };
        }

        public int[] getBuilding8Coordinates() {
                return new int[] { building8X, building8Y };
        }

        public int[] getBuilding9Coordinates() {
                return new int[] { building9X, building9Y };
        }

        public int[] getBuilding10Coordinates() {
                return new int[] { building10X, building10Y };
        }

        public int[] getBuilding11Coordinates() {
                return new int[] { building11X, building11Y };
        }

        public int[] getBuilding12Coordinates() {
                return new int[] { building12X, building12Y };
        }

        public int[] getBuilding13Coordinates() {
                return new int[] { building13X, building13Y };
        }

        public int[] getBuilding14Coordinates() {
                return new int[] { building14X, building14Y };
        }

        public int[] getBuilding15Coordinates() {
                return new int[] { building15X, building15Y };
        }

        public static void main(String[] args) {
                String ontologyFilePath = "ontology/airsoftField.owx";
                OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
                try {
                        File ontologyFile = new File(ontologyFilePath);
                        OWLOntology ontology = ontologyManager.loadOntologyFromOntologyDocument(ontologyFile);
                        MapModel model = new MapModel(ontologyFilePath, ontology);
                        if (ontology != null) {

                                System.out.println("Loaded ontology: "
                                                + ontology.getOntologyID().getOntologyIRI().toString());

                                OntologyLoader.visualizeObjectsForScenarios(ontology);

                                BuildingManager agent = new BuildingManager(ontology, model);
                                agent.assignObjectsToAgent();

                                System.out.println("Objects assigned to building agent:");
                                OntologyLoader.visualizeObjectsForScenarios(ontology);
                        } else {
                                System.err.println("Error: Ontology not loaded.");
                        }
                } catch (OWLOntologyCreationException e) {
                        System.err.println("Error loading ontology: " + e.getMessage());
                }
        }
}
