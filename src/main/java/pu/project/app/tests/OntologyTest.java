package pu.project.app.tests;

import java.io.File;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

public class OntologyTest {

    public static void main(String[] args) {
        OntologyTest ontologyAgent = new OntologyTest();
        ontologyAgent.checkOntologyConsistency();
    }

    // Method to check the consistency of the ontology
    public void checkOntologyConsistency() {
        File ontologyFile = new File("ontology/airsoftField.owx");

        OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();

        try {
            OWLOntology ontology = ontologyManager.loadOntologyFromOntologyDocument(ontologyFile);

            OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
            OWLReasoner reasoner = reasonerFactory.createReasoner(ontology);

            boolean isConsistent = reasoner.isConsistent();

            if (isConsistent) {
                System.out.println("The ontology is consistent.");
            } else {
                System.out.println("The ontology is inconsistent.");
                Node<OWLClass> bottomNode = reasoner.getUnsatisfiableClasses();
                System.out.println("Unsatisfiable classes:");
                for (OWLClass cls : bottomNode) {
                    System.out.println("    " + cls);
                }
            }

        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
    }
}
