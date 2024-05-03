package pu.project.app.map;

import org.semanticweb.owlapi.model.OWLOntology;

import jason.environment.grid.GridWorldModel;
import pu.project.app.agents.BaseRespawnAgent;
import pu.project.app.agents.CommCenterAgent;
import pu.project.app.agents.FlagAgent;
import pu.project.app.agents.HelicopterAgent;
import pu.project.app.agents.MilitaryVehicleAgent;
import pu.project.app.agents.MissionAgent;
import pu.project.app.views.field.FieldView;

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

    private final int[][] objects;
    public static final int EMPTY = 0;

    private final BaseRespawnAgent baseRespawnAgent;
    private final MilitaryVehicleAgent militaryVehicleAgent;
    private final CommCenterAgent commCenterAgent;
    private final HelicopterAgent helicopterAgent;
    private final FlagAgent flagAgent;
    private final MissionAgent missionAgent;

    // Constructor to initialize the map model
    public MapModel(String ontologyFilePath, OWLOntology ontology) {
        super(MapGrid.GRID_SIZE, MapGrid.GRID_SIZE, 0);
        objects = new int[MapGrid.GRID_SIZE][MapGrid.GRID_SIZE];
        baseRespawnAgent = new BaseRespawnAgent(ontology, this);
        militaryVehicleAgent = new MilitaryVehicleAgent(ontology, this);
        commCenterAgent = new CommCenterAgent(ontology, this);
        helicopterAgent = new HelicopterAgent(ontology, this);
        flagAgent = new FlagAgent(ontology, this);
        missionAgent = new MissionAgent(ontology, this, flagAgent, commCenterAgent, helicopterAgent);
        decideFromScenario();

        int[] alphaBaseCoordinates = baseRespawnAgent.getAlphaBaseCoordinates();
        int[] bravoBaseCoordinates = baseRespawnAgent.getBravoBaseCoordinates();
        int[] alphaRespawnPointCoordinates = baseRespawnAgent.getAlphaRespawnPointCoordinates();
        int[] bravoRespawnPointCoordinates = baseRespawnAgent.getBravoRespawnPointCoordinates();

        int[] alphaMission1Coordinates = missionAgent.getAlphaMission1Coordinates();
        int[] alphaMission2Coordinates = missionAgent.getAlphaMission2Coordinates();
        int[] bravoMission1Coordinates = missionAgent.getBravoMission1Coordinates();
        int[] bravoMission2Coordinates = missionAgent.getBravoMission2Coordinates();

        objects[alphaBaseCoordinates[0]][alphaBaseCoordinates[1]] = AlphaBase;
        objects[bravoBaseCoordinates[0]][bravoBaseCoordinates[1]] = BravoBase;
        objects[alphaRespawnPointCoordinates[0]][alphaRespawnPointCoordinates[1]] = AlphaRespawnPoint;
        objects[bravoRespawnPointCoordinates[0]][bravoRespawnPointCoordinates[1]] = BravoRespawnPoint;

        objects[alphaMission1Coordinates[0]][alphaMission1Coordinates[1]] = AlphaMission1;
        objects[alphaMission2Coordinates[0]][alphaMission2Coordinates[1]] = AlphaMission2;
        objects[bravoMission1Coordinates[0]][bravoMission1Coordinates[1]] = BravoMission1;
        objects[bravoMission2Coordinates[0]][bravoMission2Coordinates[1]] = BravoMission2;
    }

    // Get the object at a specific grid position
    public int get(int x, int y) {
        return objects[x][y];
    }

    // Method to distribute objects based on the selected scenario
    public void decideFromScenario() {
        FieldView fieldView = FieldView.getInstance();
        if (fieldView != null) {
            int scenario = fieldView.getScenario();

            int[] milSUV1Coordinates = militaryVehicleAgent.getMilitaryVehicle1Coordinates();
            int[] milSUV2Coordinates = militaryVehicleAgent.getMilitaryVehicle2Coordinates();
            int[] milSUV3Coordinates = militaryVehicleAgent.getMilitaryVehicle3Coordinates();
            int[] milSUV4Coordinates = militaryVehicleAgent.getMilitaryVehicle4Coordinates();
            int[] milAPC1Coordinates = militaryVehicleAgent.getMilitaryVehicle5Coordinates();
            int[] milAPC2Coordinates = militaryVehicleAgent.getMilitaryVehicle6Coordinates();
            int[] milAPC3Coordinates = militaryVehicleAgent.getMilitaryVehicle7Coordinates();
            int[] milAPC4Coordinates = militaryVehicleAgent.getMilitaryVehicle8Coordinates();

            int[] commCenterCoordinates = commCenterAgent.getCommCenterCoordinates();

            int[] helicopterCoordinates = helicopterAgent.getHelicopterCoordinates();

            int[] flag1Coordinates = flagAgent.getFlag1Coordinates();
            int[] flag2Coordinates = flagAgent.getFlag2Coordinates();
            int[] flag3Coordinates = flagAgent.getFlag3Coordinates();

            switch (scenario) {
                case 1:
                    objects[commCenterCoordinates[0]][commCenterCoordinates[1]] = CommCenter;
                    break;
                case 2:
                    objects[milSUV1Coordinates[0]][milSUV1Coordinates[1]] = MilitarySUV1;
                    objects[milSUV2Coordinates[0]][milSUV2Coordinates[1]] = MilitarySUV2;
                    objects[milSUV3Coordinates[0]][milSUV3Coordinates[1]] = MilitarySUV3;
                    objects[milSUV4Coordinates[0]][milSUV4Coordinates[1]] = MilitarySUV4;
                    objects[helicopterCoordinates[0]][helicopterCoordinates[1]] = Helicopter;
                    break;
                case 3:
                    objects[milSUV1Coordinates[0]][milSUV1Coordinates[1]] = MilitarySUV1;
                    objects[milSUV2Coordinates[0]][milSUV2Coordinates[1]] = MilitarySUV2;
                    objects[milSUV3Coordinates[0]][milSUV3Coordinates[1]] = MilitarySUV3;
                    objects[milSUV4Coordinates[0]][milSUV4Coordinates[1]] = MilitarySUV4;
                    objects[milAPC1Coordinates[0]][milAPC1Coordinates[1]] = MilitaryAPC1;
                    objects[milAPC2Coordinates[0]][milAPC2Coordinates[1]] = MilitaryAPC2;
                    objects[milAPC3Coordinates[0]][milAPC3Coordinates[1]] = MilitaryAPC3;
                    objects[milAPC4Coordinates[0]][milAPC4Coordinates[1]] = MilitaryAPC4;
                    objects[flag1Coordinates[0]][flag1Coordinates[1]] = Flag1;
                    objects[flag2Coordinates[0]][flag2Coordinates[1]] = Flag2;
                    objects[flag3Coordinates[0]][flag3Coordinates[1]] = Flag3;
                    break;
                default:
                    break;
            }
        } else {
        }
    }
}