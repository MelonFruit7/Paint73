package com;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class SecretDevAnimation extends PaintApp {
   //Our GraphicsContext from our main class
   GraphicsContext a;
   //Image that will be animated
   Image image = new Image("https://upload.wikimedia.org/wikipedia/en/c/cc/JavaFX_Logo.png");
   //ArrayList of Pixels
   private ArrayList<Pixel> pixels = new ArrayList<Pixel>();
   //Current Index in arraylist
   int pixelIndex = 0;
   //if true use random sort
   Boolean type = false;
   //AnimationTimer
   AnimationTimer timer;

   //SecretDevAnimation Constructors
   public SecretDevAnimation(GraphicsContext a, Boolean type) {
    this.a = a;
    this.type = type;
   }
   public SecretDevAnimation(GraphicsContext a, String image, Boolean type) {
    this.a = a;
    this.image = new Image(image);
    this.image = new Image(image, this.image.getWidth() > screenBounds.getWidth() ? screenBounds.getWidth() : this.image.getWidth()
                                , this.image.getHeight() > screenBounds.getHeight() ? screenBounds.getHeight() - 75 : this.image.getHeight()
                                , true, true);
    this.type = type;
   }

   //Animation method
   public void animate() {

    for (int i = 0; i < image.getHeight(); i++) {
        for (int j = 0; j < image.getWidth(); j++) {
            pixels.add(new Pixel(j, i, image.getPixelReader().getColor(j, i)));
        }
    }
    

    pixels.sort(Comparator.comparing(p -> {
        Point2D center = new Point2D(image.getWidth() / 2, image.getHeight() / 2);

        return center.distance(p.x, p.y);
    }));
    if (type) {
     Collections.shuffle(pixels);
    }

    timer = new AnimationTimer() {

        @Override
        public void handle(long now) {
            onUpdate();  
        }
        
    };
    timer.start();
   }

   //Updates drawing
   private void onUpdate() {
     int amount = 2500;

     while(pixelIndex < pixels.size() && amount > 0) {
        Pixel pixel = pixels.get(pixelIndex);
        Color color = pixel.c;

        //a.setStroke(color);
        //a.strokeOval(pixel.x, pixel.y, 1, 1);
        a.setFill(color);
        a.fillRect(pixel.x, pixel.y, 1, 1); 

        amount--;
        pixelIndex++;
     }

     if (pixelIndex == pixels.size()) {
        timer.stop();
     }
   }

   //Pixel class
   private class Pixel {
     int x,y;
     Color c;
     public Pixel(int x, int y, Color c) {
        this.x = x;
        this.y = y;
        this.c = c;
     }
   }
}
