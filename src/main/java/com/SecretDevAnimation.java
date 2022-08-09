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
   Boolean type = false, inverseColor = false, grayScale = false, lighten = false, darken = false;
   int lightAmount = 2, darkAmount = 2;
   String temp = "";
   //AnimationTimer
   AnimationTimer timer;

   //SecretDevAnimation Constructors
   public SecretDevAnimation(GraphicsContext a, Boolean type) {
    this.a = a;
    this.type = type;
   }
   public SecretDevAnimation(GraphicsContext a, String image, Boolean type) {
    this.a = a;
    if (image.endsWith("*inverse*")) {
        inverseColor = true;
        image = image.substring(0, image.length() - 9);
    } else if (image.endsWith("*grayscale*")) {
        grayScale = true;
        image = image.substring(0, image.length() - 11);
    } else if (image.indexOf("*lighten") != -1) {
        lighten = true;
        temp = image.substring(image.indexOf("*lighten") + 8, image.indexOf("*", image.indexOf("*lighten") + 1));
        lightAmount = Integer.parseInt(temp.length() > 0 ? temp : "2");
        image = image.substring(0, image.length() - 9 - String.valueOf(temp.length() > 0 ? lightAmount : "").length());
        if (lightAmount < 2) lightAmount = 2;
    } else if (image.indexOf("*darken") != -1) {
        darken = true;
        temp = image.substring(image.indexOf("*darken") + 7, image.indexOf("*", image.indexOf("*darken") + 1));
        darkAmount = Integer.parseInt(temp.length() > 0 ? temp : "2");
        image = image.substring(0, image.length() - 8 - String.valueOf(temp.length() > 0 ? darkAmount : "").length());
        if (darkAmount < 2) darkAmount = 2;
    }
    this.image = new Image(image);
    this.image = new Image(image, this.image.getWidth() > screenBounds.getWidth() ? screenBounds.getWidth() : this.image.getWidth()
                                , this.image.getHeight() > screenBounds.getHeight() ? screenBounds.getHeight() - 75 : this.image.getHeight() - 75
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
        if (inverseColor) color = new Color(1 - color.getRed(), 1 - color.getGreen(), 1 - color.getBlue(), color.getOpacity());
        if (grayScale) color = color.grayscale();
        if (lighten) color = new Color(Math.pow(color.getRed(), 1.0 / lightAmount), Math.pow(color.getGreen(), 1.0 / lightAmount), Math.pow(color.getBlue(), 1.0 / lightAmount), color.getOpacity());
        if (darken) color = new Color(Math.pow(color.getRed(), darkAmount), Math.pow(color.getGreen(), darkAmount), Math.pow(color.getBlue(), darkAmount), color.getOpacity());
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
