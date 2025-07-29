package pu.project.app.map;

import org.semanticweb.owlapi.model.OWLOntology;

import jason.environment.grid.GridWorldModel;

public class MapModel extends GridWorldModel {

    public static final int AlphaBase = 1;
    public static final int BravoBase = 2;
    public static final int AlphaRespawnPoint = 3;
    public static final int BravoRespawnPoint = 4;
    public static final int MilitarySUV1 = 5;
    public static final int MilitarySUV2 = 6;
    public static final int MilitarySUV3 = 7;
    public static final int MilitarySUV4 = 8;
    public static final int MilitaryAPC1 = 9;
    public static final int MilitaryAPC2 = 10;
    public static final int MilitaryAPC3 = 11;
    public static final int MilitaryAPC4 = 12;
    public static final int CommCenter = 13;
    public static final int Helicopter = 14;
    public static final int Flag1 = 15;
    public static final int Flag2 = 16;
    public static final int Flag3 = 17;
    public static final int AlphaMission1 = 18;
    public static final int AlphaMission2 = 19;
    public static final int BravoMission1 = 20;
    public static final int BravoMission2 = 21;

    public final int[][] objects;
    public static final int EMPTY = 0;
    public static final int GRID_SIZE = 15;

    public MapModel(String ontologyFilePath, OWLOntology ontology) {
        super(MapGrid.GRID_SIZE, MapGrid.GRID_SIZE, 0);
        objects = new int[MapGrid.GRID_SIZE][MapGrid.GRID_SIZE];
    }

public synchronized int get(int x, int y) {
    if (objects == null || x < 0 || x >= GRID_SIZE || y < 0 || y >= GRID_SIZE) {
        return EMPTY;
    }
    return objects[x][y];
}
    public synchronized void placeObject(int x, int y, int objectType) {
        objects[x][y] = objectType;
    }
    public void resetGrid() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                objects[i][j] = EMPTY;
            }
        }
        System.out.println("Grid reset complete");
    }
    public void decideFromScenario(int scenario) {
    for (int i = 0; i < GRID_SIZE; i++) {
        for (int j = 0; j < GRID_SIZE; j++) {
            if (objects[i][j] == CommCenter || objects[i][j] == Helicopter || 
                objects[i][j] == Flag1 || objects[i][j] == Flag2 || objects[i][j] == Flag3) {
                objects[i][j] = EMPTY;
            }
        }
    }
    
}
}

