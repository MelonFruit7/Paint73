package com;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Screen;
import javafx.stage.Stage;

//Beware the code is very messy and comments aren't that helpful
public class PaintApp extends Application {
    //coords of paint
    double x, y;

    double slopeX, slopeY, drawSize = 5;
    //keeps check of what color we change the drawsize box to
    int updateCounter = 0;

    //checks if opencircle is on or off, DevMode on or off, flood animation on/off
    boolean keepOpenCircle = false, DevMode = false, floodAnimation = false;

    //Used for the swapAnimation
    AnimationTimer timer;
    //Used for the swapAnimation
    int logger = 0;

    //loops of how many circles placed when drawing with arrow keys
    int movement = 3;

    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
    Canvas canvas = new Canvas(screenBounds.getWidth(),screenBounds.getHeight());
    GraphicsContext abc = canvas.getGraphicsContext2D();

    @Override
    public void start(Stage window) throws Exception {

        window.getIcons().add(new Image("data:image/jpeg;base64,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"));

        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(10,10,10,10));

        GridPane bottomlayout = new GridPane();
        GridPane toplayout = new GridPane();
        toplayout.setHgap(10);
        

     
        //Clear Button
        Button clearButton = new Button("Clear");
        clearButton.setFocusTraversable(false);
        GridPane.setConstraints(clearButton, 0, 0);
        //Eraser CheckBox
        CheckBox eraser = new CheckBox("Eraser");
        eraser.setFocusTraversable(false);
        GridPane.setConstraints(eraser, 1, 0);
        //Drawing size (px) textfield
        TextField sizeField = new TextField();
        sizeField.setPromptText("drawSize");
        sizeField.setFocusTraversable(false);
        GridPane.setConstraints(sizeField, 2, 0);
        //Open Circle
        CheckBox openCircle = new CheckBox("Open Circle");
        openCircle.setFocusTraversable(false);
        GridPane.setConstraints(openCircle, 3, 0);
        //Flood Fill
        CheckBox fillBox = new CheckBox("Flood");
        fillBox.setFocusTraversable(false);
        GridPane.setConstraints(fillBox, 4, 0);
        //Coloring (px) textfield
        TextField colorField = new TextField("#000000");
        colorField.setFocusTraversable(false);
        colorField.setPromptText("HEX COLOR (#334455)");
        GridPane.setConstraints(colorField, 5, 0);
        //Undo Button
        Button undoBtn = new Button("Undo");
        undoBtn.setFocusTraversable(false);
        GridPane.setConstraints(undoBtn, 6, 0);
        //Developer Box
        TextField DevBox = new TextField();
        DevBox.setFocusTraversable(false);
        DevBox.setPromptText("Dev Mode Enabled");
        GridPane.setConstraints(DevBox, 7, 0);
        //My label
        Label credit = new Label("Developed By MelonFruit");
        GridPane.setConstraints(credit, 8, 0);

 

        bottomlayout.getChildren().addAll(canvas);
        layout.setBottom(bottomlayout);
        toplayout.getChildren().addAll(clearButton,eraser,sizeField,openCircle,colorField,undoBtn,fillBox);
        layout.setTop(toplayout);

     
        ArrayList<Image> undoStack = new ArrayList<Image>();
        
        //UNDO previous drawing
        undoBtn.setOnAction(e -> {
          if (undoStack.size() != 0) {
            abc.drawImage(undoStack.remove(undoStack.size() - 1), 0, 0);
          }
        });

          abc.setStroke(Color.BLACK);
          abc.setLineWidth(5);


        //OPEN CIRCLE
        openCircle.setOnAction(e -> {
          if (openCircle.isSelected()) {
            abc.setLineWidth(1);
            keepOpenCircle = true;
          } else {
            abc.setLineWidth(drawSize);
            keepOpenCircle = false;
          }
        });

        //CHANGE SIZE
        sizeField.setOnKeyPressed(e -> {
          if (String.valueOf(e.getCode()).equals("ENTER") && validize(sizeField.getText())) {
              drawSize = Double.parseDouble(sizeField.getText());

              //Sets the width of the stroke and focuses out of the textfield 
              if (!keepOpenCircle) {
                abc.setLineWidth(drawSize);
              } else {
                abc.setLineWidth(1);
              }
              bottomlayout.requestFocus();

              //Changing Border Color of the textfield
              if (updateCounter++ % 2 == 0) {
                sizeField.setStyle("-fx-border-color: blue");
              } else {
                sizeField.setStyle("-fx-border-color: red");
              }
          }
      });

        //CHANGE COLOR
        colorField.setOnKeyPressed(e -> {
            if (String.valueOf(e.getCode()).equals("ENTER") && validHex(colorField.getText())) {
                //Sets the color of the your strokes
                abc.setStroke(Color.web(colorField.getText()));

                //Changing border color of the textfield and focuses out of the textfield
                colorField.setStyle("-fx-border-color: " + colorField.getText());
                bottomlayout.requestFocus();
            }
        });

        //ERASER
        eraser.setOnAction(e -> {
            if (eraser.isSelected()) {
              //Sets the stroke to white to act as an eraser (This assumes the background is always white)
              abc.setStroke(Color.WHITE);
            } else {
              //Checks for a valid hex (valid as well being a hex with a length of 7 or 9 including the #)
              if (validHex(colorField.getText())) {
                abc.setStroke(Color.web(colorField.getText()));
              } else {
                abc.setStroke(Color.BLACK);
              }
            }
        });
        //CLEAR BUTTON
        clearButton.setOnAction(e -> {
            abc.clearRect(0, 0, 5000, 5000);
            undoStack.clear();
            //No my code's not broken you're broken ;-;
            System.gc();
        });


        //Our scene and css file added
        Scene scene = new Scene(layout, 1000, 500);
        scene.setCursor(Cursor.CROSSHAIR);
        scene.getStylesheets().add(getClass().getResource("PaintAppCss.css").toString());

        //SETING A PATH
        bottomlayout.setOnMousePressed(e -> {
            Image image = canvas.snapshot(null, null);
            if (undoStack.size() < 11) {
              undoStack.add(image);
            } else {
              undoStack.remove(0);
              undoStack.add(image);
            }
            //sets X and Y to a position that puts your mouse at the center of your strokes
            x = e.getX() - drawSize / 2.0;
            y = e.getY() - drawSize / 2.0;

            //FLOOD FILL
            if (fillBox.isSelected() && floodAnimation && DevMode) {
              //Set LineWidth to 1 to not interfere with the flood fill
              abc.setLineWidth(1);
    
              WritableImage screen = canvas.snapshot(null, null);
              FloodFill f = new FloodFill(abc, screen,(int)e.getX(),(int)e.getY());
              //calling an animation of floodFill
              f.animateFill((Color)abc.getStroke());
              //No my code's not broken you're broken ;-;
              System.gc();
            } else if (fillBox.isSelected()) {
              //Set LineWidth to 1 to not interfere with the flood fill
              abc.setLineWidth(1);
              
              WritableImage screen = canvas.snapshot(null, null);
              FloodFill f = new FloodFill(abc, screen,(int)e.getX(),(int)e.getY());
              //calling the fill method and passing in exact coords
              f.fill((int)e.getX(), (int) e.getY(), (Color)abc.getStroke());
              //No my code's not broken you're broken ;-;
              System.gc();
            } else {
              //Until we get out of flood mode and start drawing again we will be stuck on open circle, why? because I'm lazy
              if (!openCircle.isSelected()) abc.setLineWidth(drawSize);
              abc.strokeOval(x, y, drawSize, drawSize);
            }
        });

        //DRAWING
        bottomlayout.setOnMouseDragged(e -> {
              /*
              Finds how far you move an X for each 1 Y movement and how far you move a Y for each 1 X movement 

                We abs these values due to me contantly experincing bugs and not being smart enough to figure out something more efficent 
                we end up finding out if we have to subtract or add these values later within the while loop
              */
              slopeX = Math.abs(((e.getX() - drawSize / 2.0) - x) / ((e.getY() - drawSize / 2.0) - y));
              slopeY = Math.abs(((e.getY() - drawSize / 2.0) - y) / ((e.getX() - drawSize / 2.0) - x));
          
           //Be careful here, double check everything if this breaks your entire PC will freeze
           while((int)x != (int)(e.getX() - drawSize / 2.0) || (int)y != (int)(e.getY() - drawSize / 2.0)) {
                /*
                 Since we want to plot a line of circles we want the best looking line with minimum gaps so
                 we find which slope is the greater of the 2 and set it's value to 1 as to minimize the gaps
               public  between placements of oval strokes
                */
                if (slopeX > slopeY) {
                  slopeX = 1;
                } else {
                  slopeY = 1;
                }


                if ((int)x > (int)(e.getX() - drawSize / 2.0)) {
                    abc.strokeOval(x -= slopeX, y, drawSize, drawSize);
                } else if ((int)x < (int)(e.getX() - drawSize / 2.0)) {
                    abc.strokeOval(x += slopeX, y, drawSize, drawSize);
                }

                if ((int)y > (int)(e.getY() - drawSize / 2.0)) {
                    abc.strokeOval(x, y -= slopeY, drawSize, drawSize);
                } else if ((int)y < (int)(e.getY() - drawSize / 2.0 )) {
                    abc.strokeOval(x, y += slopeY, drawSize, drawSize);
                }
            }
          
            //Kinda useless but also somewhat maintains precision so I keep it :)
            x = e.getX() - drawSize / 2.0;
            y = e.getY() - drawSize / 2.0;
            
            abc.strokeOval(x, y, drawSize, drawSize);
        });

       
        KeyCombination DevModeComb = new KeyCodeCombination(KeyCode.SLASH, KeyCombination.ALT_DOWN);
        KeyCombination AnimateComb = new KeyCodeCombination(KeyCode.Y, KeyCombination.ALT_DOWN);
        KeyCombination AnimateFloodComb = new KeyCodeCombination(KeyCode.A, KeyCombination.ALT_DOWN);
        //Secret Dev Combination
        scene.setOnKeyReleased(e -> {
          if (DevModeComb.match(e) && !DevMode) {
            DevMode = true;
            toplayout.getChildren().addAll(DevBox,credit);
          }
          if (DevMode && AnimateComb.match(e)) {
            try {
              SecretDevAnimation dev = new SecretDevAnimation(abc, DevBox.getText(), keepOpenCircle);
              dev.animate();
              dev = null;
              //No my code's not broken you're broken ;-;
              System.gc();
            } catch (Exception invalid) {
              SecretDevAnimation dev = new SecretDevAnimation(abc, keepOpenCircle);
              dev.animate();
              dev = null;
              //No my code's not broken you're broken ;-;
              System.gc();
            }
          } else if (AnimateComb.match(e)) {
             SecretDevAnimation dev = new SecretDevAnimation(abc, keepOpenCircle);
             dev.animate();
             dev = null;
             //No my code's not broken you're broken ;-;
             System.gc();
          }
          //Toggle Flood animation on/off
          if (AnimateFloodComb.match(e)) {
            floodAnimation = !floodAnimation;
          }
        });
        //SWAP ANIMATION
        DevBox.setOnKeyPressed(e -> {
          if (e.getCode() == KeyCode.ENTER && validizeInt(DevBox.getText()) && logger == 0) {
            swapAnimation(Integer.parseInt(DevBox.getText()), abc.getStroke());
            bottomlayout.requestFocus();
          }
        });


        KeyCombination movementPlus = new KeyCodeCombination(KeyCode.P, KeyCombination.SHIFT_DOWN);
        KeyCombination movementMinus = new KeyCodeCombination(KeyCode.M, KeyCombination.SHIFT_DOWN);
        
        //DRAWING with arrow keys
        scene.setOnKeyPressed(e -> {
            if (movementPlus.match(e)) {
              movement++;
            } else if (movementMinus.match(e)) {
              if (movement > 1) {
                movement--;
              }
            }
            switch(String.valueOf(e.getCode())) {
                case "UP":
                  for (int i = 0; i < movement; i++) {
                    y -= 5;
                    abc.strokeOval(x, y, drawSize, drawSize);
                  }
                  break;
                case "RIGHT": 
                  for (int i = 0; i < movement; i++) {
                    x += 5;
                    abc.strokeOval(x, y, drawSize, drawSize);
                  }
                  break;
                case "LEFT":
                  for (int i = 0; i < movement; i++) {
                    x -= 5;
                    abc.strokeOval(x, y, drawSize, drawSize);
                  }
                  break;
                case "DOWN":
                  for (int i = 0; i < movement; i++) {
                    y += 5;
                    abc.strokeOval(x, y, drawSize, drawSize);
                  }
            }
        });
       


        window.setScene(scene);
        window.setTitle("Paint73");
        window.show();
        bottomlayout.requestFocus();


    }

   /**
     * 
     * @param a random String
     * @return Whether the String was a Double or not
     */
    private static boolean validize(String a) {
        try {
            Double.parseDouble(a);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    /**
     * 
     * @param a random String
     * @return Whether the String was a integer or not
     */
    private static boolean validizeInt(String a) {
      try {
          Integer.parseInt(a);
          return true;
      } catch (NumberFormatException e) {
          return false;
      }
  }

    
    /**
     * 
     * @param hex a hex code
     * @return Whether the hex is a vlaid 6/8 number long hex
     */
    private static boolean validHex(String hex) {
        if (hex.length() != 7 && hex.length() != 9) return false;

        hex = hex.toUpperCase();
        for (int i = 1; i < hex.length(); i++) {
            if (!(hex.charAt(i) >= 48 && hex.charAt(i) <= 57) && !(hex.charAt(i) >= 65 && hex.charAt(i) <= 70)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 
     * @param time The time till the animation ends (not based on any real time system)
     * @param current The current Color of your paint so after the animation ends it can reset the color
     * @return Swaps the paints color rapidly for a set time
     */
    public void swapAnimation(int time, Paint current) {

      timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if (logger++ < time) {
             abc.setStroke(Color.rgb((int)(Math.random() * 255)
                                    ,(int)(Math.random() * 255)
                                    ,(int)(Math.random() * 255)));
            } else {
                logger = 0;
                abc.setStroke(current);
                timer.stop();
            }
        }};

        timer.start();
    }
    public static void main(String[] args) {
      
       launch(args); 
    }
}
