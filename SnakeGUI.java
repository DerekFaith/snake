/* Author: Derek Faith
   Date: October 27, 2019
   Background: Initially built to learn what I can do with CS10's DrawingGUI, I made a 'Snake' themed game for fun. It
        works generally how 'Snake' does, but is built from scratch with my own graphics. Enjoy a game the mashes
        your grandmother's wallpaper with a snake that reminds me of cookies 'n creme!
 */

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SnakeGUI extends DrawingGUI { // DrawingGUI is a CS10 provided class
    private LinkedList<beads> snake;
    private static final int width = 800, height = 800;
    // solo represents the individual object the snake is supposed to catch
    private beads solo;
    //pixelWidth measure the length of a snake's segment, not an actual pixel
    private final int pixelWidth = 40;
    // vx and vy determine the direction the snake moves in
    private int vx = 0;
    private int vy = -pixelWidth;
    // the lengths measure the snake's length, just to keep track
    private int length;
    private int maxLength1;
    private int maxLength2;

    private SnakeGUI() {
        super("Snake", width, height);

        solo = new beads(4 * pixelWidth, 4 * pixelWidth);
        // the snake will always be initialized as 5 segments long
        maxLength1 = 5;
        maxLength2 = 5;
        resetSnake();

        startTimer();
    }

    public void handleKeyPress(char k) {
        if (k == 'a') { // move left
            if(snake.get(1).getX() >= snake.get(0).getX()) {
                vx = -pixelWidth;
                vy = 0;
            }
        }
        else if (k == 'd') { // move right
            if(snake.get(1).getX() <= snake.get(0).getX()) {
                vx = pixelWidth;
                vy = 0;
            }
        }
        else if (k == 'w') { // move up
            if(snake.get(1).getY() >= snake.get(0).getY()) {
                vx = 0;
                vy = -pixelWidth;
            }
        }
        else if (k == 's') { // move down
            if (snake.get(1).getY() <= snake.get(0).getY()) {
                vx = 0;
                vy = pixelWidth;
            }
        }
        else if (k==' ') { // custom reset
            resetSnake();
        }
    }

    public void handleTimer() {
        updatePositions();
        checkCollision();
        hitSolo();
        repaint();
    }

    /* updates the position by moving the last segment to the front, making it the new head
    cases for moving through the borders
    */
    private void updatePositions() {
        // cases with hitting boundaries
        if (vx == -pixelWidth && snake.get(0).getX() - pixelWidth < 0) {
            beads pholder = new beads(width - pixelWidth, snake.get(0).getY(), 'l');
            snake.add(0, pholder);
            snake.remove(snake.size() - 1);

        }
        else if (vx == pixelWidth && snake.get(0).getX() + pixelWidth >= width) {
            beads pholder = new beads(0, snake.get(0).getY(), 'r');
            snake.add(0, pholder);
            snake.remove(snake.size() - 1);

        }
        else if (vy == -pixelWidth && snake.get(0).getY() - pixelWidth < 0) {
            beads pholder = new beads(snake.get(0).getX(), height - pixelWidth, 'u');
            snake.add(0, pholder);
            snake.remove(snake.size() - 1);

        }
        else if (vy == pixelWidth && snake.get(0).getY() + pixelWidth >= height) {
            beads pholder = new beads(snake.get(0).getX(), 0, 'd');
            snake.add(0, pholder);
            snake.remove(snake.size() - 1);

        }
        // cases without hitting boundaries
        else if (vx == -pixelWidth){
            beads pholder = new beads(snake.get(0).getX() + vx, snake.get(0).getY() + vy, 'l');
            snake.add(0, pholder);
            snake.remove(snake.size() - 1);
        }
        else if (vx == pixelWidth){
            beads pholder = new beads(snake.get(0).getX() + vx, snake.get(0).getY() + vy, 'r');
            snake.add(0, pholder);
            snake.remove(snake.size() - 1);
        }
        else if (vy == -pixelWidth){
            beads pholder = new beads(snake.get(0).getX() + vx, snake.get(0).getY() + vy, 'u');
            snake.add(0, pholder);
            snake.remove(snake.size() - 1);
        }
        else {
            beads pholder = new beads(snake.get(0).getX() + vx, snake.get(0).getY() + vy, 'd');
            snake.add(0, pholder);
            snake.remove(snake.size() - 1);
        }
    }

    // checks if the snake head collided with any other segment, triggers a reset
    private void checkCollision() {
        for (int i = 4; i < snake.size(); i++) {
            if (snake.get(0).getX() == snake.get(i).getX() && snake.get(0).getY() == snake.get(i).getY()) {
                System.out.println("Oops! Your length was " + length);
                if (maxLength2 > maxLength1) {
                    System.out.println("This is a personal record!!!");
                    maxLength1 = maxLength2;
                }
                else {
                    System.out.println("You record is " + maxLength1);
                }
                resetSnake();
                return;
            }
        }
    }

    // checks if the head has hit the solo segment, grows the snake
    private void hitSolo() {
        if (snake.get(0).getX() == solo.getX() && snake.get(0).getY() == solo.getY()) {
            if (vx == -pixelWidth || vx == pixelWidth) {
                solo.setX(solo.getX() - pixelWidth);
                solo.setUpOrAcross('a');
                snake.add(solo);
            }
            else if (vy == -pixelWidth || vy == pixelWidth) {
                solo.setY(solo.getY() + vy);
                solo.setUpOrAcross('u');
                snake.add(solo);
            }
            while (soloOnSnake()) {
                generateSolo();
            }
            length++;
            if (length > maxLength2) {
                maxLength2 = length;
            }
        }
    }

    // checks to ensure the new solo isn't generated on the snake
    private boolean soloOnSnake() {
        for (int i = 0; i < snake.size(); i++) {
            beads current = snake.get(i);
            if (current.getX() == solo.getX() && current.getY() == solo.getY()) {
                return true;
            }
        }
        return false;
    }

    // returns snake to base case initialization and resets counting
    private void resetSnake() {
        snake = new LinkedList<beads>();
        int halfWidth = ((int)(width/(2 * pixelWidth))) * pixelWidth;
        int halfHeight = ((int)(height/(2 * pixelWidth))) * pixelWidth;
        snake.add(new beads(halfWidth, halfHeight, 'u'));
        snake.add(new beads(halfWidth, halfHeight + pixelWidth, 'u'));
        snake.add(new beads(halfWidth, halfHeight + 2 * pixelWidth, 'u'));
        snake.add(new beads(halfWidth, halfHeight + 3 * pixelWidth, 'u'));
        snake.add(new beads(halfWidth, halfHeight + 4 * pixelWidth, 'u'));
        vx = 0;
        vy = -pixelWidth;
        length = 5;
    }

    // creates a new solo object once hitSolo() occurs
    private void generateSolo() {
        int soloX = ((int)(((width + 1) * Math.random()) / pixelWidth)) * pixelWidth;
        int soloY = ((int)(((height + 1) * Math.random()) / pixelWidth)) * pixelWidth;
        if (soloX <= pixelWidth) {
            soloX = 2 * pixelWidth;
        }
        else if (soloX >= width - (pixelWidth + 1)) {
            soloX = width - (2 * pixelWidth);
        }
        if (soloY <= pixelWidth) {
            soloY = 2 * pixelWidth;
        }
        else if (soloY >= height - (pixelWidth + 1)) {
            soloY = height - (2 * pixelWidth);
        }
        solo = new beads(soloX, soloY);
    }

    // draws the solo as a cute little critter
    private void drawRodent(int x, int y, Graphics g) {
        int mainDiameter = (5*pixelWidth)/5;
        int xCenter = x + mainDiameter/2;
        int yCenter = y + mainDiameter/2;
        // ears
        g.setColor(Color.WHITE);
        g.fillOval(xCenter - (11*mainDiameter)/24, yCenter - (7*mainDiameter)/12, mainDiameter/3, mainDiameter/3);
        g.fillOval(xCenter + mainDiameter/6, yCenter - (7*mainDiameter)/12, mainDiameter/3, mainDiameter/3);
        g.setColor(Color.PINK);
        g.fillOval(xCenter - (5*mainDiameter)/12, yCenter - mainDiameter/2, mainDiameter/4, mainDiameter/4);
        g.fillOval(xCenter + mainDiameter/6, yCenter - mainDiameter/2, mainDiameter/4, mainDiameter/4);
        // main body
        g.setColor(Color.WHITE);
        g.fillOval(xCenter - mainDiameter/2, yCenter - mainDiameter/2, mainDiameter, mainDiameter);
        // nose and eyes
        g.setColor(Color.PINK);
        g.fillRect(xCenter - mainDiameter/12, yCenter + mainDiameter/6, mainDiameter/6, mainDiameter/12);
        g.fillOval(xCenter - (5*mainDiameter)/12, yCenter - mainDiameter/4, mainDiameter/4, mainDiameter/4);
        g.fillOval(xCenter + mainDiameter/6, yCenter - mainDiameter/4, mainDiameter/4, mainDiameter/4);
        g.setColor(Color.BLACK);
        g.fillOval(xCenter - (7*mainDiameter)/24, yCenter - mainDiameter/8, mainDiameter/8, mainDiameter/8);
        g.fillOval(xCenter + mainDiameter/6, yCenter - mainDiameter/8, mainDiameter/8, mainDiameter/8);
        // whiskers
        g.drawLine(xCenter - mainDiameter/12, yCenter + (5*mainDiameter)/24, xCenter - mainDiameter/6, yCenter + (3*mainDiameter)/24);
        g.drawLine(xCenter - mainDiameter/12, yCenter + (5*mainDiameter)/24, xCenter - mainDiameter/6,yCenter + (5*mainDiameter)/24);
        g.drawLine(xCenter - mainDiameter/12, yCenter + (5*mainDiameter)/24,xCenter - mainDiameter/6,yCenter + (7*mainDiameter)/24);
        g.drawLine(xCenter + mainDiameter/12, yCenter + (5*mainDiameter)/24,xCenter + mainDiameter/6, yCenter + (3*mainDiameter)/24);
        g.drawLine(xCenter + mainDiameter/12, yCenter + (5*mainDiameter)/24, xCenter + mainDiameter/6, yCenter + (5*mainDiameter)/24);
        g.drawLine(xCenter + mainDiameter/12, yCenter + (5*mainDiameter)/24, xCenter + mainDiameter/6, yCenter + (7*mainDiameter)/24);
    }

    // draws the head of the snake as a triangle with eyes dependent on direction
    private void drawHead(Graphics g) {
        g.setColor(new Color(30, 30, 30));
        beads head = snake.get(0);
        if (head.getUpOrAcross() == 'l') {
            int xList[] = {(int)head.getX(), (int)(head.getX() + pixelWidth), (int)(head.getX() + pixelWidth)};
            int yList[] = {(int)(head.getY() + pixelWidth/2), (int)head.getY(), (int)(head.getY() + pixelWidth)};
            Polygon p = new Polygon(xList, yList, 3);
            g.fillPolygon(p);
            g.setColor(Color.WHITE);
            g.drawOval((int)head.getX() + (3*pixelWidth)/4, (int)head.getY(), pixelWidth/4, pixelWidth/4);
            g.drawOval((int)head.getX() + (3*pixelWidth)/4, (int)head.getY() + (3*pixelWidth)/4, pixelWidth/4, pixelWidth/4);
        }
        else if (head.getUpOrAcross() == 'r') {
            int xList[] = {(int)head.getX(), (int)head.getX(), (int)(head.getX() + pixelWidth)};
            int yList[] = {(int)(head.getY() + pixelWidth), (int)head.getY(), (int)(head.getY() + pixelWidth/2)};
            Polygon p = new Polygon(xList, yList, 3);
            g.fillPolygon(p);
            g.setColor(Color.WHITE);
            g.drawOval((int)head.getX(), (int)head.getY(), pixelWidth/4, pixelWidth/4);
            g.drawOval((int)head.getX(), (int)head.getY() + (3*pixelWidth)/4, pixelWidth/4, pixelWidth/4);
        }
        else if (head.getUpOrAcross() == 'u') {
            int xList[] = {(int)head.getX(), (int)(head.getX() + pixelWidth), (int)(head.getX() + pixelWidth/2)};
            int yList[] = {(int)(head.getY() + pixelWidth), (int)head.getY() + pixelWidth, (int)head.getY()};
            Polygon p = new Polygon(xList, yList, 3);
            g.fillPolygon(p);
            g.setColor(Color.WHITE);
            g.drawOval((int)head.getX(), (int)head.getY() + (3*pixelWidth)/4, pixelWidth/4, pixelWidth/4);
            g.drawOval((int)head.getX() + (3*pixelWidth)/4, (int)head.getY() + (3*pixelWidth)/4, pixelWidth/4, pixelWidth/4);
        }
        else {
            int xList[] = {(int)head.getX(), (int)(head.getX() + pixelWidth), (int)(head.getX() + pixelWidth/2)};
            int yList[] = {(int)head.getY(), (int)head.getY(), (int)(head.getY() + pixelWidth)};
            Polygon p = new Polygon(xList, yList, 3);
            g.fillPolygon(p);
            g.setColor(Color.WHITE);
            g.drawOval((int)head.getX(), (int)head.getY(), pixelWidth/4, pixelWidth/4);
            g.drawOval((int)head.getX() + (3*pixelWidth)/4, (int)head.getY(), pixelWidth/4, pixelWidth/4);
        }
    }

    // draws the tail of the snake as a triangle dependent on direction
    private void drawTail(Graphics g) {
        g.setColor(new Color(30, 30, 30));
        beads tail = snake.get(snake.size() - 1);
        if (tail.getUpOrAcross() == 'l') {
            int xList[] = {(int)tail.getX(), (int)tail.getX(), (int)(tail.getX() + pixelWidth)};
            int yList[] = {(int)(tail.getY() + pixelWidth), (int)tail.getY(), (int)(tail.getY() + pixelWidth/2)};
            Polygon p = new Polygon(xList, yList, 3);
            g.fillPolygon(p);
        }
        else if (tail.getUpOrAcross() == 'r') {
            int xList[] = {(int)tail.getX(), (int)(tail.getX() + pixelWidth), (int)(tail.getX() + pixelWidth)};
            int yList[] = {(int)(tail.getY() + pixelWidth/2), (int)tail.getY(), (int)(tail.getY() + pixelWidth)};
            Polygon p = new Polygon(xList, yList, 3);
            g.fillPolygon(p);
        }
        else if (tail.getUpOrAcross() == 'u') {
            int xList[] = {(int)tail.getX(), (int)(tail.getX() + pixelWidth), (int)(tail.getX() + pixelWidth/2)};
            int yList[] = {(int)tail.getY(), (int)tail.getY(), (int)(tail.getY() + pixelWidth)};
            Polygon p = new Polygon(xList, yList, 3);
            g.fillPolygon(p);
        }
        else {
            int xList[] = {(int)tail.getX(), (int)(tail.getX() + pixelWidth), (int)(tail.getX() + pixelWidth/2)};
            int yList[] = {(int)(tail.getY() + pixelWidth), (int)tail.getY() + pixelWidth, (int)tail.getY()};
            Polygon p = new Polygon(xList, yList, 3);
            g.fillPolygon(p);
        }
    }

    // draws a shrub with points oriented north, south, east, and west
    private void drawShrubA(int x, int y, Graphics g) {
        g.setColor(new Color(66, 128, 11));
        x = x + pixelWidth/20;
        y = y + pixelWidth/20;
        int xList[] = {x, x + (3*pixelWidth)/8, x + pixelWidth/2, x + (5*pixelWidth)/8, x + pixelWidth, x + (5*pixelWidth)/8, x + pixelWidth/2, x + (3*pixelWidth)/8};
        int yList[] = {y + pixelWidth/2, y + (3*pixelWidth)/8, y, y + (3*pixelWidth)/8, y + pixelWidth/2, y + (5*pixelWidth)/8, y + pixelWidth, y + (5*pixelWidth)/8};
        g.fillPolygon(new Polygon(xList, yList, 8));
        g.setColor(new Color(222, 218, 191));
        g.fillOval(x + (3*pixelWidth)/8, y + (3*pixelWidth)/8, pixelWidth/4, pixelWidth/4);
        g.setColor(new Color(210, 95, 107));
        g.fillOval(x + (15*pixelWidth)/32, y + (15*pixelWidth)/32, pixelWidth/8, pixelWidth/8);
    }

    // draws a shrub with points oriented towards diagonally
    private void drawShrubB(int x, int y, Graphics g) {
        g.setColor(new Color(66, 128, 11));
        x = x + pixelWidth/20;
        y = y + pixelWidth/20;
        int xList[] = {x, x + pixelWidth/2, x + pixelWidth, x + (5*pixelWidth)/8, x + pixelWidth, x + pixelWidth/2, x, x + (3*pixelWidth)/8};
        int yList[] = {y, y + (3*pixelWidth)/8, y, y + pixelWidth/2, y + pixelWidth, y + (5*pixelWidth)/8, y + pixelWidth, y + pixelWidth/2};
        g.fillPolygon(new Polygon(xList, yList, 8));
        g.setColor(new Color(222, 218, 191));
        g.fillOval(x + (3*pixelWidth)/8, y + (3*pixelWidth)/8, pixelWidth/4, pixelWidth/4);
        g.setColor(new Color(210, 95, 107));
        g.fillOval(x + (15*pixelWidth)/32, y + (15*pixelWidth)/32, pixelWidth/8, pixelWidth/8);
    }

    // actually draws the game in a separate window
    public void draw(Graphics g) {
        //g.drawImage(image, 0, 0, null);
        g.setColor(new Color(190, 135, 50));
        g.fillRect(0, 0, width, height);
        Boolean aOrB = true; // used to alternate between shrub styles
        for (int i = 0; i < width; i += pixelWidth) {
            if (aOrB) {
                aOrB = false;
            }
            else {
                aOrB = true;
            }
            for (int j = 0; j < height; j += pixelWidth) {
                if (aOrB) {
                    drawShrubA(i, j, g);
                    aOrB = false;
                }
                else {
                    drawShrubB(i, j, g);
                    aOrB = true;
                }

            }
        }
        drawRodent((int)solo.getX(), (int)solo.getY(), g);
        drawHead(g);
        for (int i = 1; i < snake.size() - 1; i++) { // graphics for segments of snake body
            g.setColor(new Color(225, 208, 200));
            beads current = snake.get(i);
            g.fillRoundRect((int)current.getX(), (int)current.getY(), (pixelWidth), (pixelWidth), pixelWidth/5, pixelWidth/5);
            g.setColor(new Color(30, 30, 30));
            beads before = snake.get(i - 1);
            beads after = snake.get(i + 1);
            // following if, else if, else block places spots of the snake body according to orientation
            if (before.getUpOrAcross() != after.getUpOrAcross()) { // applies spots to corners
                if ((before.getX() < current.getX() && after.getY() > current.getY()) || (after.getX() < current.getX() && before.getY() > current.getY())) {
                    g.fillOval((int)current.getX(), (int)current.getY(), pixelWidth/2, pixelWidth/2);
                    g.fillOval((int)current.getX() + pixelWidth/2, (int)current.getY() + pixelWidth/2, pixelWidth/2, pixelWidth/2);
                }
                else if ((before.getX() > current.getX() && after.getY() < current.getY()) || (after.getX() > current.getX() && before.getY() < current.getY())) {
                    g.fillOval((int)current.getX(), (int)current.getY(), pixelWidth/2, pixelWidth/2);
                    g.fillOval((int)current.getX() + pixelWidth/2, (int)current.getY() + pixelWidth/2, pixelWidth/2, pixelWidth/2);
                }
                else {
                    g.fillOval((int)current.getX(), (int)current.getY() + pixelWidth/2, pixelWidth/2, pixelWidth/2);
                    g.fillOval((int)current.getX() + pixelWidth/2, (int)current.getY(), pixelWidth/2, pixelWidth/2);
                }
            }

            else if (current.getUpOrAcross() == 'l' || current.getUpOrAcross() == 'r') { // across spots
                g.fillOval((int)(current.getX() + pixelWidth/4), (int)current.getY(), pixelWidth/2, pixelWidth/2);
                g.fillOval((int)(current.getX() + pixelWidth/4), (int)(current.getY() + pixelWidth/2), pixelWidth/2, pixelWidth/2);
            }

            else { // lateral spots
                g.fillOval((int)current.getX(), (int)(current.getY() + pixelWidth/4), pixelWidth/2, pixelWidth/2);
                g.fillOval((int)current.getX() + pixelWidth/2, (int)(current.getY() + pixelWidth/4), pixelWidth/2, pixelWidth/2);
            }
        }
        drawTail(g);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SnakeGUI();
            }});
    }
}
