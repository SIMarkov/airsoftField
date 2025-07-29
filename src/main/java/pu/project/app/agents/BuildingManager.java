package pu.project.app.agents;

import org.semanticweb.owlapi.model.*;

import pu.project.app.map.MapModel;
import java.util.HashMap;
import java.util.Map;
public class BuildingManager {
    private final OWLOntology ontology;
    private final Map<String, int[]> buildingCoordinates = new HashMap<>();
    private final OWLDataFactory df;

    public BuildingManager(OWLOntology ontology, MapModel model) {
        this.ontology = ontology;
        this.df = ontology.getOWLOntologyManager().getOWLDataFactory();
        loadBuildingCoordinates();
    }

    private void loadBuildingCoordinates() {
        for (int i = 1; i <= 15; i++) {
            String buildingName = "Building" + i;
            buildingCoordinates.put(buildingName, getBuildingCoordinates(buildingName));
        }
    }

    private int[] getBuildingCoordinates(String buildingName) {
        // Try different IRI formats
        OWLNamedIndividual building = findBuildingIndividual(buildingName);
        
        int x = getCoordinateValue(building, "hasXCoordinate");
        int y = getCoordinateValue(building, "hasYCoordinate");
        
        return new int[]{x, y};
    }

    private OWLNamedIndividual findBuildingIndividual(String buildingName) {
        // Try with explicit IRI format
        IRI buildingIRI = IRI.create("file://ontology/airsoftField.owx#" + buildingName);
        OWLNamedIndividual building = df.getOWLNamedIndividual(buildingIRI);
        
        // If not found, search by local name
        if (!ontology.containsEntityInSignature(building)) {
            for (OWLNamedIndividual ind : ontology.getIndividualsInSignature()) {
                if (ind.getIRI().getFragment().equals(buildingName)) {
                    return ind;
                }
            }
            throw new RuntimeException("Building not found: " + buildingName);
        }
        return building;
    }

    private int getCoordinateValue(OWLNamedIndividual individual, String propertyName) {
        IRI propertyIRI = IRI.create("file://ontology/airsoftField.owx#" + propertyName);
        OWLDataProperty property = df.getOWLDataProperty(propertyIRI);
        
        for (OWLDataPropertyAssertionAxiom ax : 
             ontology.getDataPropertyAssertionAxioms(individual)) {
            if (ax.getProperty().equals(property)) {
                OWLLiteral value = ax.getObject();
                return value.parseInteger();
            }
        }
        throw new RuntimeException(propertyName + " not found for " + individual.getIRI());
    }

    // Individual building access methods
    public int[] getBuilding1Coordinates() { return buildingCoordinates.get("Building1"); }
    public int[] getBuilding2Coordinates() { return buildingCoordinates.get("Building2"); }
    public int[] getBuilding3Coordinates() { return buildingCoordinates.get("Building3"); }
    public int[] getBuilding4Coordinates() { return buildingCoordinates.get("Building4"); }
    public int[] getBuilding5Coordinates() { return buildingCoordinates.get("Building5"); }
    public int[] getBuilding6Coordinates() { return buildingCoordinates.get("Building6"); }
    public int[] getBuilding7Coordinates() { return buildingCoordinates.get("Building7"); }
    public int[] getBuilding8Coordinates() { return buildingCoordinates.get("Building8"); }
    public int[] getBuilding9Coordinates() { return buildingCoordinates.get("Building9"); }
    public int[] getBuilding10Coordinates() { return buildingCoordinates.get("Building10"); }
    public int[] getBuilding11Coordinates() { return buildingCoordinates.get("Building11"); }
    public int[] getBuilding12Coordinates() { return buildingCoordinates.get("Building12"); }
    public int[] getBuilding13Coordinates() { return buildingCoordinates.get("Building13"); }
    public int[] getBuilding14Coordinates() { return buildingCoordinates.get("Building14"); }
    public int[] getBuilding15Coordinates() { return buildingCoordinates.get("Building15"); }
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

       /*public void placeBuldings() {
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
*/
}
