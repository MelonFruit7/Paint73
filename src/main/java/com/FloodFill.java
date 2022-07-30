package com;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class FloodFill extends PaintApp {
    //GraphicContext for canvas
    GraphicsContext a;
    //canvas as an image
    WritableImage image;
    //coords
    int x,y;
    //counter for animation
    int counter = 0;
    //AnimationTimer
    AnimationTimer timer;

    public FloodFill(GraphicsContext a, WritableImage image, int x, int y) {
        this.a = a;
        this.image = image;
        this.x = x;
        this.y = y;
    }

    public void fill(int x, int y, Color newColor) {
         Color oldColor = image.getPixelReader().getColor(x, y);
        if (oldColor.equals(newColor)) {
            return;
        } else {
            ArrayList<Integer[]> queue = new ArrayList<Integer[]>();
            queue.add(new Integer[]{x,y});
            while(queue.size() > 0) {
                x = queue.get(queue.size() - 1)[0];
                y = queue.get(queue.size() - 1)[1];
                queue.remove(queue.size() - 1);
                if (x < 0 || x >= screenBounds.getWidth() || y < 0 || y >= screenBounds.getHeight() || !image.getPixelReader().getColor(x, y).equals(oldColor)) {
                    continue;
                } else {
                    image.getPixelWriter().setColor(x, y, newColor);
                    a.strokeOval(x, y, 1, 1);
                    queue.add(new Integer[]{x + 1,y});
                    queue.add(new Integer[]{x - 1,y});
                    queue.add(new Integer[]{x,y + 1});
                    queue.add(new Integer[]{x,y - 1});
                }
            }
        }
    }

    public void animateFill(Color newColor) {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
               fillAnimateHelper(newColor);
            }    
        }; 
        timer.start();
    }
    ArrayList<Integer[]> queue = new ArrayList<Integer[]>();
    Color oldColor;
    private void fillAnimateHelper(Color newColor) {
        if (queue.size() == 0) oldColor = image.getPixelReader().getColor(x, y);
        if (oldColor.equals(newColor)) {
            timer.stop();
            return;
        } else {
            queue.add(new Integer[]{x,y});
            while(queue.size() > 0) {
                x = queue.get(queue.size() - 1)[0];
                y = queue.get(queue.size() - 1)[1];
                if (counter++ == 2500) {
                    counter = 0;
                    return;
                }
                queue.remove(queue.size() - 1);
                if (x < 0 || x >= screenBounds.getWidth() || y < 0 || y >= screenBounds.getHeight() || !image.getPixelReader().getColor(x, y).equals(oldColor)) {
                    continue;
                } else {
                    image.getPixelWriter().setColor(x, y, newColor);
                    a.strokeOval(x, y, 1, 1);
                    queue.add(new Integer[]{x,y + 1});
                    queue.add(new Integer[]{x + 1,y});
                    queue.add(new Integer[]{x - 1,y});
                    queue.add(new Integer[]{x,y - 1});
                }
            }
            timer.stop();
            //No my code's not broken you're broken ;-;
            System.gc();
        }
    }
}
