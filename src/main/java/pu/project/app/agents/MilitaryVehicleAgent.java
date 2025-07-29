package pu.project.app.agents;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import org.semanticweb.owlapi.model.OWLOntology;

import pu.project.app.map.MapModel;

public class MilitaryVehicleAgent extends Agent {
    private OWLOntology ontology;
    private MapModel model;
    private CountDownLatch doneLatch;
    private int scenario;
    
    private int milSUV1X = -1, milSUV1Y = -1;
    private int milSUV2X = -1, milSUV2Y = -1;
    private int milSUV3X = -1, milSUV3Y = -1;
    private int milSUV4X = -1, milSUV4Y = -1;
    private int milAPC1X = -1, milAPC1Y = -1;
    private int milAPC2X = -1, milAPC2Y = -1;
    private int milAPC3X = -1, milAPC3Y = -1;
    private int milAPC4X = -1, milAPC4Y = -1;
    
    private List<int[]> occupiedCoordinates = new ArrayList<>();
    private List<int[]> forbiddenBuildingCoordinates = new ArrayList<>();

    public MilitaryVehicleAgent() {
    }

    @Override
    protected void setup() {
        Object[] args = getArguments();
        if (args != null && args.length == 4) {
            ontology = (OWLOntology) args[0];
            model = (MapModel) args[1];
            doneLatch = (CountDownLatch) args[2];
            scenario = (int) args[3];
            
            if (model != null) {
                BuildingManager buildingManager = new BuildingManager(ontology, model);
                forbiddenBuildingCoordinates.add(buildingManager.getBuilding1Coordinates());
                forbiddenBuildingCoordinates.add(buildingManager.getBuilding2Coordinates());
                forbiddenBuildingCoordinates.add(buildingManager.getBuilding3Coordinates());
                forbiddenBuildingCoordinates.add(buildingManager.getBuilding6Coordinates());
                forbiddenBuildingCoordinates.add(buildingManager.getBuilding7Coordinates());
                forbiddenBuildingCoordinates.add(buildingManager.getBuilding9Coordinates());
            }
        }
        addBehaviour(new PlaceVehiclesBehaviour());
    }

    private class PlaceVehiclesBehaviour extends OneShotBehaviour {
        @Override
        public void action() {
            placeMilitaryVehicles();
            updateModelWithVehicles();
            doneLatch.countDown();
        }
    }

    private void placeMilitaryVehicles() {
        if (scenario == 1) return;
        
        int[] alphaBase = findBaseCoordinates(MapModel.AlphaBase);
        int[] bravoBase = findBaseCoordinates(MapModel.BravoBase);
        
        if (scenario == 2) {
            placeVehiclesForBase(alphaBase, true, 2, 0); // 2 SUVs, 0 APCs
            placeVehiclesForBase(bravoBase, false, 2, 0); // 2 SUVs, 0 APCs
        } else if (scenario == 3) {
            placeVehiclesForBase(alphaBase, true, 2, 2); // 2 SUVs, 2 APCs
            placeVehiclesForBase(bravoBase, false, 2, 2); // 2 SUVs, 2 APCs
        }
    }
      private void placeVehiclesForBase(int[] baseCoords, boolean isAlpha, int suvCount, int apcCount) {
        for (int i = 0; i < suvCount; i++) {
            int[] coords = generateMilitaryVehicleCoordinates(baseCoords[0], baseCoords[1], isAlpha);
            if (isAlpha) {
                if (i == 0) {
                    milSUV1X = coords[0]; milSUV1Y = coords[1];
                } else {
                    milSUV2X = coords[0]; milSUV2Y = coords[1];
                }
            } else {
                if (i == 0) {
                    milSUV3X = coords[0]; milSUV3Y = coords[1];
                } else {
                    milSUV4X = coords[0]; milSUV4Y = coords[1];
                }
            }
        }
        
        for (int i = 0; i < apcCount; i++) {
            int[] coords = generateMilitaryVehicleCoordinates(baseCoords[0], baseCoords[1], isAlpha);
            if (isAlpha) {
                if (i == 0) {
                    milAPC1X = coords[0]; milAPC1Y = coords[1];
                } else {
                    milAPC2X = coords[0]; milAPC2Y = coords[1];
                }
            } else {
                if (i == 0) {
                    milAPC3X = coords[0]; milAPC3Y = coords[1];
                } else {
                    milAPC4X = coords[0]; milAPC4Y = coords[1];
                }
            }
        }
    }

    private int[] findBaseCoordinates(int baseType) {
        if (model == null) return new int[]{-1, -1}; 
        
        for (int x = 0; x < model.getWidth(); x++) {
            for (int y = 0; y < model.getHeight(); y++) {
                if (model.get(x, y) == baseType) {
                    return new int[]{x, y};
                }
            }
        }
        return new int[]{-1, -1};
    }
    private int[] generateMilitaryVehicleCoordinates(int baseX, int baseY, boolean isAlpha) {
        List<int[]> freeCoordinates = new ArrayList<>();
        
        for (int x = baseX - 1; x <= baseX + 1; x++) {
            for (int y = baseY - 1; y <= baseY + 1; y++) {
                if (isValidPosition(x, y, isAlpha) && !isOccupied(x, y)) {
                    freeCoordinates.add(new int[]{x, y});
                }
            }
        }

        if (freeCoordinates.isEmpty()) {
            Random random = new Random();
            int randomX, randomY;
            int attempts = 0;
            do {
                randomX = baseX + random.nextInt(5) - 2;
                randomY = baseY + random.nextInt(5) - 2;
                attempts++;
            } while ((!isValidPosition(randomX, randomY, isAlpha) || isOccupied(randomX, randomY)) 
                    && attempts < 50);
            
            randomX = Math.max(0, Math.min(randomX, model.getWidth() - 1));
            randomY = Math.max(0, Math.min(randomY, model.getHeight() - 1));
            
            addOccupiedCoordinates(randomX, randomY);
            return new int[]{randomX, randomY};
        }

        Random random = new Random();
        int[] coords = freeCoordinates.get(random.nextInt(freeCoordinates.size()));
        addOccupiedCoordinates(coords[0], coords[1]);
        return coords;
    }

    private boolean isValidPosition(int x, int y, boolean isAlpha) {
        if (x < 0 || x >= model.getWidth() || y < 0 || y >= model.getHeight()) {
            return false;
        }
        
        if (model.get(x, y) != MapModel.EMPTY) {
            return false;
        }
        
        for (int[] building : forbiddenBuildingCoordinates) {
            if (x == building[0] && y == building[1]) {
                return false;
            }
        }
        
        return true;
    }
    private void updateModelWithVehicles() {
        if (milSUV1X != -1) model.placeObject(milSUV1X, milSUV1Y, MapModel.MilitarySUV1);
        if (milSUV2X != -1) model.placeObject(milSUV2X, milSUV2Y, MapModel.MilitarySUV2);
        if (milSUV3X != -1) model.placeObject(milSUV3X, milSUV3Y, MapModel.MilitarySUV3);
        if (milSUV4X != -1) model.placeObject(milSUV4X, milSUV4Y, MapModel.MilitarySUV4);
        if (milAPC1X != -1) model.placeObject(milAPC1X, milAPC1Y, MapModel.MilitaryAPC1);
        if (milAPC2X != -1) model.placeObject(milAPC2X, milAPC2Y, MapModel.MilitaryAPC2);
        if (milAPC3X != -1) model.placeObject(milAPC3X, milAPC3Y, MapModel.MilitaryAPC3);
        if (milAPC4X != -1) model.placeObject(milAPC4X, milAPC4Y, MapModel.MilitaryAPC4);
    }

    private boolean isOccupied(int x, int y) {
        for (int[] coord : occupiedCoordinates) {
            if (coord[0] == x && coord[1] == y) {
                return true;
            }
        }
        return false;
    }

    private void addOccupiedCoordinates(int x, int y) {
        occupiedCoordinates.add(new int[]{x, y});
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
}

