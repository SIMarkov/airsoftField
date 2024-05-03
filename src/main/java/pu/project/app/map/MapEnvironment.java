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
import pu.project.app.agents.BaseRespawnAgent;
import pu.project.app.agents.CommCenterAgent;
import pu.project.app.agents.FlagAgent;
import pu.project.app.agents.HelicopterAgent;
import pu.project.app.agents.MilitaryVehicleAgent;
import pu.project.app.agents.MissionAgent;
import pu.project.app.ontology.OntologyLoader;

public class MapEnvironment extends Environment {

    // Method to load resources
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

    // Method to generate a map
    public static BufferedImage generateMap(int scenario) {
        String ontologyFilePath = "ontology/airsoftField.owx";
        OWLOntology ontology = OntologyLoader.loadOntology(ontologyFilePath);
        MapModel model = new MapModel(ontologyFilePath, ontology);
        int rows = 15;
        int cols = 15;
        String backgroundImagePath = "icons/field.jpg";
        System.setProperty("java.awt.headless", "false");
        JFrame frame = new JFrame("Airsoft field");
        MapGrid gridMap = new MapGrid(rows, cols, backgroundImagePath, model);
        frame.add(gridMap);
        frame.setSize(800, 800);
        frame.setVisible(true);
        BufferedImage generatedImage = createMapImage(gridMap);
        saveImage(generatedImage, "icons/airsoft_field.png");

        BaseRespawnAgent baseRespawnAgent = new BaseRespawnAgent(ontology, model);
        HelicopterAgent helicopterAgent = new HelicopterAgent(ontology, model);
        CommCenterAgent commCenterAgent = new CommCenterAgent(ontology, model);
        FlagAgent flagAgent = new FlagAgent(ontology, model);
        MilitaryVehicleAgent militaryVehicleAgent = new MilitaryVehicleAgent(ontology, model);
        MissionAgent missionAgent = new MissionAgent(ontology, model, flagAgent, commCenterAgent, helicopterAgent);

        baseRespawnAgent.printBaseRespawnCoordinates();
        missionAgent.printMissionCoordinates();

        switch (scenario) {
            case 1:
                commCenterAgent.printCommCenterCoordinates();
                break;
            case 2:
                militaryVehicleAgent.printMilitarySUVCoordinates();
                helicopterAgent.printHelicopterCoordinates();
                break;
            case 3:
                militaryVehicleAgent.printMilitarySUVCoordinates();
                militaryVehicleAgent.printMilitaryAPCCoordinates();
                flagAgent.printFlagCoordinates();
                break;
            default:
        }

        int delayInMillis = 1;
        Timer timer = new Timer(delayInMillis, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        timer.setRepeats(false);
        timer.start();

        return generatedImage;
    }

    // Method to create an image of the map
    private static BufferedImage createMapImage(MapGrid gridMap) {
        BufferedImage image = new BufferedImage(gridMap.getWidth(), gridMap.getHeight(), BufferedImage.TYPE_INT_RGB);
        gridMap.paint(image.getGraphics());
        return image;
    }

    // Method to save an image
    public static void saveImage(BufferedImage image, String filePath) {
        try {
            ImageIO.write(image, "png", new File(filePath));
            System.out.println("Image successfully saved as " + filePath);
        } catch (IOException e) {
            System.out.println("An error occurred while saving the image: " + e.getMessage());
        }
    }
}