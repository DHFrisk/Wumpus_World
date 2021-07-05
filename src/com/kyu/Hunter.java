package com.kyu;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class Hunter extends Brain {

    private String path = System.getProperty("user.dir") + "//src//com//kyu";
    private Location l;
    private Image hunterImage;
    private Cave cave;
    private List<List<Integer>> locations = new ArrayList<List<Integer>>();
    private Map<ArrayList<Integer>, Integer> locationValue;

    public Hunter(Cave cave) throws FileNotFoundException {
        this.cave = cave;
        this.l = new Location(9, 0);
        FileInputStream inputStream = new FileInputStream(this.path + "//images/soldier.png");
        this.hunterImage = new Image(inputStream, 70, 50, false, false);
        locations = new ArrayList<>();
        locationValue = new Hashtable<>();
    }

    public void goRight() {
        setLocation(l.getRow(), l.getCol() + 1);
    }

    public void goLeft() {
        setLocation(l.getRow(), l.getCol() - 1);
    }

    public void goUp() {
        setLocation(l.getRow() - 1, l.getCol());
    }

    public void goDown() {
        setLocation(l.getRow() + 1, l.getCol());
    }

    public Location lookUp() {
        return getLookLocation(l.getRow() - 1, l.getCol());
    }

    public Location lookDown() {
        return getLookLocation(l.getRow() + 1, l.getCol());
    }

    public Location lookLeft() {
        return getLookLocation(l.getRow(), l.getCol() - 1);
    }

    public Location lookRight() {
        return getLookLocation(l.getRow(), l.getCol() + 1);
    }

    public void setLocation(int row, int col) {
        l.setCol(col);
        l.setRow(row);
        addCurrentLocation(getLocation(), cave.getTileValue(getLocation()));
    }

    public Location getLookLocation(int row, int col) {
        return new Location(row, col);
    }

    public void draw(GraphicsContext gc) {
        gc.drawImage(this.hunterImage, l.getCol() * 70 + Cave.xOffset, l.getRow() * 50 + Cave.yOffset);
    }

    public Location getLocation() {
        return l;
    }

    public boolean isValidMove(Location l, String direction) {
        if (direction.equals("UP")) {
            if (l.getRow() > 0) {
                return true;
            } else {
                return false;
            }
        }
        if (direction.equals("DOWN")) {
            if (l.getRow() < 9) {
                return true;
            } else {
                return false;
            }
        }
        if (direction.equals("LEFT")) {
            if (l.getCol() > 0) {
                return true;
            } else {
                return false;
            }
        }
        if (direction.equals("RIGHT")) {
            if (l.getCol() < 9) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public String die() {
        setLocation(9, 0);
        locations.clear();
        locationValue.clear();
        return "¡HAS PERDIDO Y RAMBO HA MUERTO!";
    }

    public String win() {
        setLocation(9, 0);
        locations.clear();
        locationValue.clear();
        return "¡FELICIDADES, GANASTE!";
    }

    public String warning(String w) {
        return "¡Cuidado, hay " + w + " aquí!";
    }

    public String showCurrentLocation() {
        return l.toString();
    }

    /*================================
    BRAIN
    ===============================* */

    public void addCurrentLocation(Location l, int value) {
        ArrayList<Integer> x = new ArrayList<Integer>();
        x.add(l.getRow());
        x.add(l.getCol());
        locations.add(x);
        locationValue.put(x, value);
    }

    public int[] getLastLocation() {
        List<Integer> x = locations.get(locations.size() - 2);
        int row = x.get(0);
        int col = x.get(1);
        return new int[]{row, col};
    }


    public void evaluateNextMove(String direction, int[][] tiles) {
        //FOR EACH POSSIBLE OPTION HUNTER WILL EVALUATE FIRST THE CURRENT POSITION TO KNOW IF HE DISCOVERED WIND
        // OR STENCH, IF NOT HE WILL EVALUATE THE GIVEN DIRECTION TO KNOW IF THERE IS WIND OR STENCH, FINALLY IF NONE
        // OF THE POSSIBILITIES IS THE KNOWN POSITIONS HUNTER WILL JUST GO STRAIGHT FORWARD TO SEE WHAT HE DISCOVERS
        if (direction.equals("UP")) {
            if (isValidMove(lookUp(), "UP")) {
                if (isKnownLocation(getLocation())) {
                    if (getLocationValue(getLocation()) == Cave.STENCH || getLocationValue(getLocation()) == Cave.WIND) {
                        System.out.println("arriba esta mike o un pozo");
                        setLocation(getLastLocation()[0], getLastLocation()[1]);
                    } else {
//                        goUp();
                    }
                }
                if (isKnownLocation(lookUp())) {
                    if (getLocationValue(lookUp()) == Cave.STENCH || getLocationValue(lookUp()) == Cave.WIND) {
                        System.out.println("arriba hay hedor o viento");
//                        addCurrentLocation(getLocation(), cave.getTileValue(getLocation()));
                        setLocation(getLastLocation()[0], getLastLocation()[1]);
                    } else {
//                        goUp();
                    }
                }
                goUp();
            }
        }

        if (direction.equals("DOWN")) {
            if (isValidMove(lookDown(), "DOWN")) {
                if (isKnownLocation(getLocation())) {
                    if (getLocationValue(getLocation()) == Cave.STENCH || getLocationValue(getLocation()) == Cave.WIND) {
                        System.out.println("abajo esta mike o un pozo");
                        setLocation(getLastLocation()[0], getLastLocation()[1]);
                    } else {
//                        goDown();
                    }
                }
                if (isKnownLocation(lookDown())) {
                    if (getLocationValue(lookDown()) == Cave.STENCH || getLocationValue(lookDown()) == Cave.WIND) {
                        System.out.println("abajo hay hedor o viento");
//                        addCurrentLocation(getLocation(), cave.getTileValue(getLocation()));
                        setLocation(getLastLocation()[0], getLastLocation()[1]);
                    } else {
//                        goDown();
                    }
                }
                goDown();
            }
        }

        if (direction.equals("LEFT")) {
            if (isValidMove(lookLeft(), "LEFT")) {
                if (isKnownLocation(getLocation())) {
                    if (getLocationValue(getLocation()) == Cave.STENCH || getLocationValue(getLocation()) == Cave.WIND) {
                        System.out.println("a la izquierda hay hedor o viento");
                        setLocation(getLastLocation()[0], getLastLocation()[1]);
                    } else {
//                        goLeft();
                    }
                }
                if (isKnownLocation(lookLeft())) {
                    if (getLocationValue(lookLeft()) == Cave.STENCH || getLocationValue(lookLeft()) == Cave.WIND) {
                        System.out.println("a la izquierda hay hedor o viento");
//                        addCurrentLocation(getLocation(), cave.getTileValue(getLocation()));
                        setLocation(getLastLocation()[0], getLastLocation()[1]);
                    } else {
//                        goLeft();
                    }
                }
                goLeft();
            }
        }

        if (direction.equals("RIGHT")) {
            if (isValidMove(lookRight(), "RIGHT")) {
                if (isKnownLocation(getLocation())) {
                    if (getLocationValue(getLocation()) == Cave.STENCH || getLocationValue(getLocation()) == Cave.WIND) {
                        System.out.println("a la derecha hay hedor o viento");
                        setLocation(getLastLocation()[0], getLastLocation()[1]);
                    } else {
//                        goRight();
                    }
                }
                if (isKnownLocation(lookRight())) {
                    if (getLocationValue(lookRight()) == Cave.STENCH || getLocationValue(lookRight()) == Cave.WIND) {
                        System.out.println("a la derecha hay hedor o viento");
//                        addCurrentLocation(getLocation(), cave.getTileValue(getLocation()));
                        setLocation(getLastLocation()[0], getLastLocation()[1]);
                    } else {
//                        goRight();
                    }
                }
                goRight();
            }
        }


    }

    public String getRandomDirection() {
        String[] options = new String[]{"UP", "DOWN", "LEFT", "RIGHT"};
        Random r = new Random();
        return options[r.nextInt(4)];
    }

    public boolean isKnownLocation(Location l) {
        ArrayList<Integer> x = new ArrayList<Integer>();
        x.add(l.getRow());
        x.add(l.getCol());
        return locationValue.containsKey(x);
    }

    public int getLocationValue(Location l) {
        ArrayList<Integer> x = new ArrayList<Integer>();
        x.add(l.getRow());
        x.add(l.getCol());
        return locationValue.get(x);
    }

}
