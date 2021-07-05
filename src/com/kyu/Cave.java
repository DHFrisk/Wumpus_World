package com.kyu;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Cave {

    private int tiles[][];
    private boolean visible[][];
    public static final int GROUND=0, PIT=1, WUMPUS=2, GOLD=3, WIND=10, STENCH=20, SHINING=30;
    private String path= System.getProperty("user.dir")+"//src//com//kyu";
    public static final int xOffset= 35, yOffset= 35;
    private Image groundImage, blackImage, shiningImage, goldImage, pitImage, stenchImage, windImage, wumpusImage;

    public Cave() throws Exception {
        this.tiles = new int[10][10];
        this.visible = new boolean[10][10];
//        this.tiles[4][4]= PIT;

        FileInputStream inputStream = new FileInputStream(this.path + "//images/ground2.png");
        this.groundImage = new Image(inputStream, 70, 50, false, false);
        inputStream = new FileInputStream(this.path + "//images/black.png");
        this.blackImage = new Image(inputStream, 70, 50, false, false);
        inputStream = new FileInputStream(this.path + "//images/gold.png");
        this.goldImage = new Image(inputStream, 70, 50, false, false);
        inputStream = new FileInputStream(this.path + "//images/shining.png");
        this.shiningImage = new Image(inputStream, 70, 50, false, false);
        inputStream = new FileInputStream(this.path + "//images/blackhole.png");
        this.pitImage = new Image(inputStream, 70, 50, false, false);
        inputStream = new FileInputStream(this.path + "//images/wind.png");
        this.windImage = new Image(inputStream, 70, 50, false, false);
        inputStream = new FileInputStream(this.path + "//images/stench.png");
        this.stenchImage = new Image(inputStream, 70, 50, false, false);
        inputStream = new FileInputStream(this.path + "//images/wumpus.png");
        this.wumpusImage = new Image(inputStream, 70, 60, false, false);

    }

    public void draw(GraphicsContext gc){
        for (int i=0; i< tiles.length; i++){
            for (int j=0; j< tiles[0].length; j++) {
                if(this.tiles[i][j] == GROUND){
                    gc.drawImage(this.groundImage, xOffset+(j*70), yOffset+(i*50));
                }
                if(this.tiles[i][j] == PIT){
                    gc.drawImage(this.pitImage, xOffset+(j*70), yOffset+(i*50));
                }
                if(this.tiles[i][j] == WIND){
                    gc.drawImage(this.windImage, xOffset+(j*70), yOffset+(i*50));
                }
                if(this.tiles[i][j] == GOLD){
                    gc.drawImage(this.goldImage, xOffset+(j*70), yOffset+(i*50));
                }
                if(this.tiles[i][j] == SHINING){
                    gc.drawImage(this.shiningImage, xOffset+(j*70), yOffset+(i*50));
                }
                if(this.tiles[i][j] == WUMPUS){
                    gc.drawImage(this.wumpusImage, xOffset+(j*70), yOffset+(i*50));
                }
                if(this.tiles[i][j] == STENCH){
                    gc.drawImage(this.stenchImage, xOffset+(j*70), yOffset+(i*50));
                }
            }
        }
    }

    public Image getGroundImage() {
        return groundImage;
    }

    public Image getGoldImage() {
        return goldImage;
    }

    public Image getPitImage() {
        return pitImage;
    }

    public Image getWumpusImage() {
        return wumpusImage;
    }

    public void setTile(Location l, int tileID){
        if(isValid(l)){
            tiles[l.getRow()][l.getCol()]= tileID;
            updateTileHints(tileID, l.getRow(), l.getCol());
        }
    }

    public int[][] getTiles(){
        return tiles;
    }

    public int getTileValue(Location l){
        return tiles[l.getRow()][l.getCol()];
    }

    private void updateTileHints(int tileID, int row, int col){
        Location up = new Location(row+1, col);
        Location down= new Location(row-1, col);
        Location left = new Location(row, col-1);
        Location right = new Location(row, col+1);

        if(isValid(up)){
            tiles[up.getRow()][up.getCol()]= tileID*10;
            tiles[down.getRow()][down.getCol()]= tileID*10;
            tiles[left.getRow()][left.getCol()]= tileID*10;
            tiles[right.getRow()][right.getCol()]= tileID*10;
        }
    }

    public boolean isValid(Location l){
        return l.getRow() >= 0 && l.getRow() < tiles.length && l.getCol() >= 0 && l.getCol() < tiles[l.getCol()].length;
    }

    public int evaluateLocation(Location l){
        if (tiles[l.getRow()][l.getCol()] == WIND ){
            return WIND;
        }
        if(tiles[l.getRow()][l.getCol()] == STENCH){
            return STENCH;
        }
        if (tiles[l.getRow()][l.getCol()] == WUMPUS){
            return WUMPUS;
        }
        if(tiles[l.getRow()][l.getCol()] == PIT){
            return PIT;
        }
        if(tiles[l.getRow()][l.getCol()] == SHINING){
            return SHINING;
        }
        if(tiles[l.getRow()][l.getCol()] == GOLD){
            return GOLD;
        }
        else{
            return GROUND;
        }
    }

}
