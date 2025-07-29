package pu.project.app.map;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.Timer;
import org.semanticweb.owlapi.model.OWLOntology;

import jason.environment.Environment;

import pu.project.app.ontology.OntologyLoader;

public class MapEnvironment extends Environment {

    public static void loadResources() {
        String ontologyFilePath = "ontology/airsoftField.owx";
        OWLOntology ontology = OntologyLoader.loadOntology(ontologyFilePath);

        MapModel model = new MapModel(ontologyFilePath, ontology);

        int rows = 15;
        int cols = 15;
        String backgroundImagePath = "icons/field.jpg";
        System.setProperty("java.awt.headless", "false");
        JFrame frame = new JFrame("Load resources");
        MapGrid gridMap = new MapGrid(rows, cols, backgroundImagePath, model);
        frame.add(gridMap);
        frame.setSize(800, 800);
        frame.setVisible(true);
        int delayInMillis = 1;
        Timer timer = new Timer(delayInMillis, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    public static BufferedImage generateMap(MapModel model) {
    if (model == null) {
        System.err.println("Using fallback MapModel");
        String ontologyFilePath = "ontology/airsoftField.owx";
        OWLOntology ontology = OntologyLoader.loadOntology(ontologyFilePath);
        model = new MapModel(ontologyFilePath, ontology);
    }

    int rows = 15;
    int cols = 15;
    String backgroundImagePath = "icons/field.jpg";
    MapGrid gridMap = new MapGrid(rows, cols, backgroundImagePath, model);
    gridMap.setSize(800, 800);
    BufferedImage generatedImage = createMapImage(gridMap);
    saveImage(generatedImage, "icons/airsoft_field.png");

    return generatedImage;
}

private static BufferedImage createMapImage(MapGrid gridMap) {
    BufferedImage image = new BufferedImage(800, 800, BufferedImage.TYPE_INT_RGB);
    gridMap.paint(image.getGraphics());
    return image;
}

    public static void saveImage(BufferedImage image, String filePath) {
        try {
            ImageIO.write(image, "png", new File(filePath));
            System.out.println("Image successfully saved as " + filePath);
        } catch (IOException e) {
            System.out.println("An error occurred while saving the image: " + e.getMessage());
        }
    }
}
