/* Author: Derek Faith
   Date: October 25, 2019
   Purpose: To create a class that can store the necessary data for SnakeGUI.java
 */

import java.awt.*;

public class beads {
    private double x ,y;
    // upOrAcross provides the orientation of the object for direction sensitive shapes (front and end of snake)
    // 'l' = left, 'r' = right, 'u' = up, and 'd' = down
    private char upOrAcross;

    public beads(double x, double y, char direction) {
        this.x = x;
        this.y = y;
        this.upOrAcross = direction;
    }

    public beads(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public char getUpOrAcross() {
        return upOrAcross;
    }

    public void setUpOrAcross(char upOrAcross) {
        this.upOrAcross = upOrAcross;
    }

    public void setX(double newX) {
        this.x = newX;
    }

    public void setY(double newY) {
        this.y = newY;
    }

}
