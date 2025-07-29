package pu.project.app.ontology;

import java.io.File;
import java.util.Set;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

public class OntologyLoader {

    // Method to load an ontology from file
    public static OWLOntology loadOntology(String ontologyFilePath) {
        OWLOntology ontology = null;
        try {
            OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();

            File ontologyFile = new File(ontologyFilePath);
            ontology = ontologyManager.loadOntologyFromOntologyDocument(ontologyFile);

            visualizeObjectsForScenarios(ontology);

        } catch (OWLOntologyCreationException e) {
            System.err.println("Error loading ontology: " + e.getMessage());
        }
        return ontology;
    }

    // Method to visualize objects related to scenario
        @SuppressWarnings("deprecation")
        public static void visualizeObjectsForScenarios(OWLOntology ontology) {
        OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
        OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);
        reasoner.precomputeInferences();
    
        // Извличане на инстанции на клас "Scenario"
        Set<OWLNamedIndividual> scenarios = ontology.getIndividualsInSignature();
        for (OWLNamedIndividual scenario : scenarios) {
            // Проверка дали текущата инстанция е класа "Scenario"
            OWLClass scenarioClass = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClass(IRI.create("file://ontology/airsoftField.owxScenario"));
            if (reasoner.isSatisfiable(scenarioClass)) {
                // Извличане на името на сценария
                OWLClassAssertionAxiom classAssertion = ontology.getOWLOntologyManager().getOWLDataFactory().getOWLClassAssertionAxiom(scenarioClass, scenario);
                OWLNamedIndividual scenarioNameIndividual = (OWLNamedIndividual) classAssertion.getIndividual();
                System.out.println(scenarioNameIndividual.getIRI().getFragment());
                // Извличане на обектите, които са свързани с текущия сценарий
                NodeSet<OWLNamedIndividual> objects = reasoner.getObjectPropertyValues(scenario, ontology.getOWLOntologyManager().getOWLDataFactory().getOWLObjectProperty(IRI.create("file://ontology/airsoftField#belongsToScenario")));
                for (OWLNamedIndividual object : objects.getFlattened()) {
                    System.out.println(" - Object: " + object);
                }
            }
        }
    
        // Затваряне на рийзънъра след използване
        reasoner.dispose();
    }

    public static void main(String[] args) {
        String ontologyFilePath = "ontology/airsoftField.owx";
        loadOntology(ontologyFilePath);
    }
}
