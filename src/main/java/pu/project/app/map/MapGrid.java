package pu.project.app.map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class MapGrid extends JPanel {
    public static final int GRID_SIZE = 15;
    private int rows;
    private int cols;
    private Image backgroundImage;
    private MapModel mapModel;

    public MapGrid(int rows, int cols, String backgroundImagePath, MapModel mapModel) {
        this.rows = rows;
        this.cols = cols;
        try {
            backgroundImage = ImageIO.read(new File(backgroundImagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.mapModel = mapModel;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        int cellWidth = getWidth() / cols;
        int cellHeight = getHeight() / rows;

        g.setColor(Color.BLACK);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                g.drawRect(j * cellWidth, i * cellHeight, cellWidth, cellHeight);
            }
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int object = mapModel.get(j, i);
                if (object != 0) {
                    Image objectImage = null;
                    switch (object) {
                        case MapModel.AlphaBase:
                            objectImage = loadImage("icons/alpha_base.png");
                            break;
                        case MapModel.BravoBase:
                            objectImage = loadImage("icons/bravo_base.png");
                            break;
                        case MapModel.AlphaRespawnPoint:
                            objectImage = loadImage("icons/respawn_point.png");
                            break;
                        case MapModel.BravoRespawnPoint:
                            objectImage = loadImage("icons/respawn_point.png");
                            break;
                        case MapModel.MilitarySUV1:
                            objectImage = loadImage("icons/mil_suv.png");
                            break;
                        case MapModel.MilitarySUV2:
                            objectImage = loadImage("icons/mil_suv.png");
                            break;
                        case MapModel.MilitarySUV3:
                            objectImage = loadImage("icons/mil_suv.png");
                            break;
                        case MapModel.MilitarySUV4:
                            objectImage = loadImage("icons/mil_suv.png");
                            break;
                        case MapModel.MilitaryAPC1:
                            objectImage = loadImage("icons/mil_apc.png");
                            break;
                        case MapModel.MilitaryAPC2:
                            objectImage = loadImage("icons/mil_apc.png");
                            break;
                        case MapModel.MilitaryAPC3:
                            objectImage = loadImage("icons/mil_apc.png");
                            break;
                        case MapModel.MilitaryAPC4:
                            objectImage = loadImage("icons/mil_apc.png");
                            break;
                        case MapModel.CommCenter:
                            objectImage = loadImage("icons/comm_center.png");
                            break;
                        case MapModel.Helicopter:
                            objectImage = loadImage("icons/helicopter.png");
                            break;
                        case MapModel.Flag1:
                            objectImage = loadImage("icons/flag.png");
                            break;
                        case MapModel.Flag2:
                            objectImage = loadImage("icons/flag.png");
                            break;
                        case MapModel.Flag3:
                            objectImage = loadImage("icons/flag.png");
                            break;
                        case MapModel.AlphaMission1:
                            objectImage = loadImage("icons/alpha_mission.png");
                            break;
                        case MapModel.AlphaMission2:
                            objectImage = loadImage("icons/alpha_mission.png");
                            break;
                        case MapModel.BravoMission1:
                            objectImage = loadImage("icons/bravo_mission.png");
                            break;
                        case MapModel.BravoMission2:
                            objectImage = loadImage("icons/bravo_mission.png");
                            break;
                        default:
                            break;
                    }
                    if (objectImage != null) {
                        g.drawImage(objectImage, j * cellWidth, i * cellHeight, cellWidth, cellHeight, this);
                    }
                }
            }
        }
    }

    private Image loadImage(String imagePath) {
        try {
            return ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
