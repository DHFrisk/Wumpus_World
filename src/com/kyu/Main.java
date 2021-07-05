package com.kyu;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.*;
import javafx.scene.image.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main extends Application {

    private String locationValue;
    private Image hunterImage;
    private int mouseX, mouseY;
    private int currentSelectedTile= -1;
    ArrayList<String> keysPressed;
    AnimationTimer animationTimer;
    boolean activate= false;
    Cave cave;
    Hunter hunter;

    public Main() throws Exception {
        cave= new Cave();
        hunter= new Hunter(cave);
        keysPressed= new ArrayList<>();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Group root = new Group();
        Scene scene = new Scene(root);
        Canvas canvas = new Canvas(900, 700);
        final GraphicsContext gc= canvas.getGraphicsContext2D();


        gc.setFont(Font.font ("Verdana", 13));

        Button btnStart = new Button("Start/Iniciar");
        Button btnStop = new Button("Stop/Parar");

        btnStart.setLayoutX(35);
        btnStart.setLayoutY(650);
        btnStop.setLayoutX(150);
        btnStop.setLayoutY(650);

        primaryStage.setScene(scene);
        root.getChildren().add(canvas);
//        root.getChildren().add(btnStart);
//        root.getChildren().add(btnStop);

        primaryStage.setTitle("Proyecto I.A.");
//        drawButtons(scene, primaryStage);

        btnStart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                activate= true;
            }
        });
        btnStop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                activate= false;
            }
        });


        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String keyCode = keyEvent.getCode().toString();
                if( !keysPressed.contains(keyCode)){
                    keysPressed.add(keyCode);
                }
            }
        });

        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mouseX= (int)mouseEvent.getX();
                mouseY= (int)mouseEvent.getY();
                if (mouseX >= 800 && mouseX <= 870 && mouseY >= 70 && mouseY <= 130) {
                    currentSelectedTile= Cave.PIT;
                }
                if (mouseX >= 800 && mouseX <= 870 && mouseY >= 150 && mouseY <= 200) {
                    currentSelectedTile= Cave.GOLD;
                }
                if (mouseX >= 800 && mouseX <= 870 && mouseY >= 230 && mouseY <= 280) {
                    currentSelectedTile= Cave.WUMPUS;
                }
                if (mouseX >= 800 && mouseX <= 870 && mouseY >= 310 && mouseY <= 360) {
                    currentSelectedTile= Cave.GROUND;
                }
//                if (mouseX >= 800 && mouseX <= 870 && mouseY >= 380 && mouseY <= 430) {
//                    currentSelectedTile= ;
//                }
                if (currentSelectedTile != -1) {
                    Location clickL= convertClickToLocation(mouseX, mouseY);

                    if(cave.isValid(clickL)){
                        cave.setTile(clickL, currentSelectedTile);
                        currentSelectedTile= -1;
                    }else{

                    }

                }
            }
        });

        scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mouseX= (int)mouseEvent.getX();
                mouseY= (int)mouseEvent.getY();
            }
        });

        animationTimer= new AnimationTimer() {
            @Override
            public void handle(long l) {
                gc.setFill(Color.AZURE);
                gc.fillRect(0, 0, 900, 700);
                processKeysPressed();
                cave.draw(gc);
                drawToolbar(gc);
                hunter.draw(gc);
                drawInformationPanel(gc, hunter.showCurrentLocation(), doLocationAction(cave.evaluateLocation(hunter.getLocation())));
                if (activate){
                    try {
                        simulateKeyPressed(true);
                        Thread.sleep(100);
                    } catch (AWTException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }else{
                    processKeysPressed();
                }
            }
        };
        animationTimer.start();
        primaryStage.show();
    }

    public void processKeysPressed(){
        for (int i=0; i<keysPressed.size(); i++){
            if(keysPressed.get(i) == "RIGHT"){
                if(hunter.isValidMove(hunter.getLocation(), "RIGHT")){
                    hunter.goRight();
                    hunter.addCurrentLocation(hunter.getLocation(), cave.getTileValue(hunter.getLocation()));
                }else {

                }
                keysPressed.remove(i);
                i--;
            }
            else if(keysPressed.get(i) == "LEFT"){
                if(hunter.isValidMove(hunter.getLocation(), "LEFT")){
                    hunter.goLeft();
                    hunter.addCurrentLocation(hunter.getLocation(), cave.getTileValue(hunter.getLocation()));
                }else {

                }
                keysPressed.remove(i);
                i--;
            }
            else if(keysPressed.get(i) == "UP"){
                if(hunter.isValidMove(hunter.getLocation(), "UP")){
                    hunter.goUp();
                    hunter.addCurrentLocation(hunter.getLocation(), cave.getTileValue(hunter.getLocation()));
                }else {

                }
                keysPressed.remove(i);
                i--;
            }
            else if(keysPressed.get(i) == "DOWN"){
                if(hunter.isValidMove(hunter.getLocation(), "DOWN")){
                    hunter.goDown();
                    hunter.addCurrentLocation(hunter.getLocation(), cave.getTileValue(hunter.getLocation()));
                }else {

                }
                keysPressed.remove(i);
                i--;
            }
            else{
                keysPressed.remove(i);
                i--;
            }
            cave.evaluateLocation(hunter.getLocation());
        }
    }

    public void drawToolbar(GraphicsContext gc){
        gc.setFill(Color.BLACK);
        gc.fillText("Objetos", 800, 50);
        gc.drawImage(cave.getPitImage(), 800, 70);
        gc.drawImage(cave.getGoldImage(), 800, 150);
        gc.drawImage(cave.getWumpusImage(), 800, 230);
        gc.drawImage(cave.getGroundImage(), 800, 310);
        gc.drawImage(this.hunterImage, 765, 380);

        if (currentSelectedTile != -1) {
            if (currentSelectedTile == Cave.PIT) {
                gc.drawImage(cave.getPitImage(), mouseX-35, mouseY-25);
            }
            if (currentSelectedTile == Cave.GOLD) {
                gc.drawImage(cave.getGoldImage(), mouseX-35, mouseY-25);
            }
            if (currentSelectedTile == Cave.WUMPUS) {
                gc.drawImage(cave.getWumpusImage(), mouseX-35, mouseY-25);
            }
            if (currentSelectedTile == Cave.GROUND) {
                gc.drawImage(cave.getGroundImage(), mouseX-35, mouseY-25);
            }

        }
    }

    public void drawInformationPanel(GraphicsContext gc, String location, String currentLocation){
        gc.setFill(Color.BROWN);
        gc.fillText(location, 35, 600);
        gc.fillText(currentLocation, 35, 630);
    }

    public void drawButtons(Scene s, Stage stage){

    }

    public Location convertClickToLocation(int x, int y){
        int row = (y-Cave.yOffset)/50;
        int col = (x-Cave.xOffset)/70;
        //        System.out.println(l);
        return new Location(row, col);

    }

    public static void main(String[] args) {
        launch(args);
    }

    public String doLocationAction(int value){
        if (value == Cave.PIT || value == Cave.WUMPUS){
            String x= hunter.die();
            showFailAlert("HAS PERDIDO", x, ":C :( :c");
            return x;
        }
        if (value == Cave.GOLD){
            String x= hunter.win();
            showSuccessAlert("HAS GANADO", x, ":DDDD");
            return x;
        }
        if (value == Cave.WIND){
            String x= hunter.warning("viento/brisa");
//            showSuccessAlert("CUIDADO", x, ":DDDD");
            return x;
        }
        if (value == Cave.STENCH){
            String x= hunter.warning("hedor");
//            showSuccessAlert("CUIDADO", x, ":DDDD");
            return x;
        }
        if (value == Cave.SHINING){
            String x= hunter.warning("brillo");
//            showSuccessAlert("CUIDADO", x, ":DDDD");
            return x;
        }
        else{
            return "nada";
        }
    }

    public void simulateKeyPressed(boolean option) throws AWTException {
//        Robot rb= new Robot();
        if (option) {
            if (hunter.getRandomDirection().equals("UP")) {
                hunter.evaluateNextMove("UP", cave.getTiles());
//            rb.keyPress(java.awt.event.KeyEvent.VK_UP);
//            rb.keyRelease(java.awt.event.KeyEvent.VK_UP);
            }
            if (hunter.getRandomDirection().equals("DOWN")) {
                hunter.evaluateNextMove("DOWN", cave.getTiles());
//            rb.keyPress(java.awt.event.KeyEvent.VK_DOWN);
//            rb.keyRelease(java.awt.event.KeyEvent.VK_DOWN);
            }
            if (hunter.getRandomDirection().equals("LEFT")) {
                hunter.evaluateNextMove("LEFT", cave.getTiles());
//            rb.keyPress(java.awt.event.KeyEvent.VK_LEFT);
//            rb.keyRelease(java.awt.event.KeyEvent.VK_LEFT);
            }
            if (hunter.getRandomDirection().equals("RIGHT")) {
                hunter.evaluateNextMove("RIGHT", cave.getTiles());
//            rb.keyPress(java.awt.event.KeyEvent.VK_RIGHT);
//            rb.keyRelease(java.awt.event.KeyEvent.VK_RIGHT);
            }
        }
    }

    public void showFailAlert(String title, String headText, String contextText){
        Alert a = new Alert(AlertType.ERROR);
        a.setTitle(title);
        a.setHeaderText(headText);
        a.setContentText(contextText);
        a.show();
    }

    public void showSuccessAlert(String title, String headText, String contextText){
        Alert a = new Alert(AlertType.CONFIRMATION);
        a.setTitle(title);
        a.setHeaderText(headText);
        a.setContentText(contextText);
        a.show();
    }

    public void showWarningAlert(String title, String headText, String contextText){
        Alert a = new Alert(AlertType.WARNING);
        a.setTitle(title);
        a.setHeaderText(headText);
        a.setContentText(contextText);
        a.show();
        a.close();
    }
}
