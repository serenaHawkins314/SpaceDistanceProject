import java.net.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.*;
import javafx.scene.paint.*;
import javafx.geometry.*;
import javafx.scene.image.*;

import java.io.*;

import java.util.*;
import java.text.*;
import java.io.*;
import java.lang.*;
import javafx.application.*;
import javafx.event.*;
import javafx.stage.*;
import javafx.scene.canvas.*;
import javafx.scene.paint.*;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.animation.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import java.net.*;
import javafx.geometry.*;


public class Main extends Application
{
   //root flowpane
   FlowPane fp;
   
   //create canvas
   Canvas theCanvas = new Canvas(600,600);
   
   //this is the value of the ticks which increases while a button is being pressed
   static int ticks = 0;
   
   //make a new drawable object but make it a player because polymorphism (i think)
   DrawableObject thePlayer = new TestObject(300,300);
   
   //test
   //DrawableObject mines1 = new Mine(200,200);
   
   //arraylist of mines
   ArrayList<DrawableObject> mines = new ArrayList<DrawableObject>();
  
   //previous grid positions of the player
   int prevgridx = 3;
   int prevgridy = 3; 
        
   //make a random number 
   Random random = new Random();
   
   //make a boolean to check if the game is over 
   boolean gameOver = false;
   
   //make the score
   double score;
   
   //make the highScore
   double highScore = getHighScore();
   
   //make the booleans for when each button is pressed 
   static boolean wPressed = false;
   static boolean aPressed = false;
   static boolean sPressed = false;
   static boolean dPressed = false;
   
   public void start(Stage stage)
   {
      //initialize root
      fp = new FlowPane();
      
      //add canvas to root
      fp.getChildren().add(theCanvas);
      
      //create GraphicsContext
      gc = theCanvas.getGraphicsContext2D();
      
      //get high score from the file
      highScore = getHighScore();
      
      //call drawBackground method
      drawBackground(300,300,gc);
      
      //scene
      Scene scene = new Scene(fp, 600, 600);
      stage.setScene(scene);
      stage.setTitle("Project :)");
      stage.show();
      
      //request focus
      theCanvas.requestFocus();
      
      //create the animation handler and call it to animate the program
      AnimationHandler ta = new AnimationHandler();
      ta.start();
      
      //create the key listener 
      fp.setOnKeyPressed(new KeyListenerDown());
      
      fp.setOnKeyReleased(new KeyListenerUp());
   }
   
   //declare GraphicsContext object
   GraphicsContext gc;
   
   //background image
   Image background = new Image("stars.png");
   Image overlay = new Image("starsoverlay.png");
   Random backgroundRand = new Random();
   
   //this piece of code doesn't need to be modified
   public void drawBackground(float playerx, float playery, GraphicsContext gc)
   {
	   //re-scale player position to make the background move slower. 
      playerx*=.1;
      playery*=.1;
   
	   //figuring out the tile's position.
      float x = (playerx) / 400;
      float y = (playery) / 400;
      
      int xi = (int) x;
      int yi = (int) y;
      
	   //draw a certain amount of the tiled images
      for(int i=xi-3;i<xi+3;i++)
      {
         for(int j=yi-3;j<yi+3;j++)
         {
            gc.drawImage(background,-playerx+i*400,-playery+j*400);
         }
      }
      
	   //below repeats with an overlay image
      playerx*=2f;
      playery*=2f;
   
      x = (playerx) / 400;
      y = (playery) / 400;
      
      xi = (int) x;
      yi = (int) y;
      
      for(int i=xi-3;i<xi+3;i++)
      {
         for(int j=yi-3;j<yi+3;j++)
         {
            gc.drawImage(overlay,-playerx+i*400,-playery+j*400);
         }
      }
   }
   
   public class AnimationHandler extends AnimationTimer
   {
      public void handle(long currentTimeInNanoSeconds) 
      {
         gc.clearRect(0,0,600,600);
                  
         //USE THIS CALL ONCE YOU HAVE A PLAYER
         drawBackground(thePlayer.getX(),thePlayer.getY(),gc); 
         
         //draw the score
         score =  thePlayer.distance(thePlayer);
         gc.setFill(Color.WHITE);
         gc.fillText("Current Score: " + score, 50, 50);
                  
         //draw the high score
         gc.setFill(Color.WHITE);
         gc.fillText("High Score: " + highScore, 50, 100);
         
         //if gameOver is false then draw the player, that way when the game ends it wont draw the player anymore
         if (!gameOver)
         {
   	      //example calls of draw - this should be the player's call for draw
            thePlayer.draw(300,300,gc,true); //all other objects will use false in the parameter.
         }
         
         //example call of a draw where m is a non-player object. Note that you are passing the player's position in and not m's position.
         //mines.draw(thePlayer.getX(),thePlayer.getY(),gc,false);   
          
         //set current grids
         int cgridx = ((int)thePlayer.getX())/100;
         int cgridy = ((int)thePlayer.getY())/100;

 
         //if the current grid is not the previous grid/if it moves to a new grid
         if (cgridx != prevgridx || cgridy != prevgridy)
         {
            //then call mines grid method to make the mines
            grid(cgridx,cgridy);
            
         }
         
         //use a for loop to loop through the arrayList of mines and draw each one
         for (int i=0; i<mines.size(); i++)
         {
            mines.get(i).draw(thePlayer.getX(),thePlayer.getY(),gc,false);
         }
         
         //use a for loop to loop through the list of mines and see if the player hit any of them
         for (int i=0; i<mines.size(); i++)
         {
            //create current distance from each mine
            int distanceFromMine = (int) (Math.sqrt((thePlayer.getX()-mines.get(i).getX())*(thePlayer.getX()-mines.get(i).getX()) + (thePlayer.getY()-mines.get(i).getY())*(thePlayer.getY()-mines.get(i).getY())));
            
            //check if the distance is less than 20 units
            if (distanceFromMine <= 20)
            {
               //make gameOver true
               gameOver = true; 
               
               //remove the mine that was hit from the screen
               mines.remove(i);
               
               //the player is automatically removed from the screen because it is only drawn if gameover is false
                              
               //exit the for loop
               break;
            }
         }
         
         if (gameOver)
         {    
            //use a try catch statement to create a file and add the high score each time to the file
            try
            {
               //create the file
               FileOutputStream fos = new FileOutputStream("highScores.txt", false);
               
               //create the printwriter to edit the file
               PrintWriter pw = new PrintWriter(fos);
               
               //make highScore equal to current score
               if (score > highScore)
               {  
                  highScore = score;
               }
               
               //print the current score in the file 
               pw.println(highScore);
               
               //close the file
               pw.close();
            } 
            catch (FileNotFoundException fnfe)
            {
               System.out.println("File not found!");
            }
            
            highScore = getHighScore();
            
            //now freeze the program
            return;
         }
         
         //call act (so it happens every frame) and add to ticks
         ticks++;
         thePlayer.act(ticks, aPressed, wPressed, sPressed, dPressed);
         
         //store current grid as previous
         prevgridx = cgridx;
         prevgridy = cgridy;
      }   
      
   }
   
   //implement listener for when WASD keys are released
   public class KeyListenerUp implements EventHandler<KeyEvent>
   {
      public void handle(KeyEvent event)
      {      
         //if the WASD keys are released, make it's pressed variable false
         if (event.getCode() == KeyCode.A)
         {
            //make aPressed false
            aPressed = false;
         }
         if (event.getCode() == KeyCode.W)
         {
            //make aPressed false
            wPressed = false;
         }
         if (event.getCode() == KeyCode.S)
         {
            //make aPressed false
            sPressed = false;
         }
         if (event.getCode() == KeyCode.D)
         {
            //make aPressed false
            dPressed = false;
         }
      }
   }
   
   //implement listener for the WASD keys
   public class KeyListenerDown implements EventHandler<KeyEvent>
   {
      public void handle(KeyEvent event)
      {      
         //if any of the WASD keys are pressed then make the pressed variable true
         if (event.getCode() == KeyCode.A)
         {
            //make a pressed true
            aPressed = true;
         }
         if (event.getCode() == KeyCode.W)
         {
            //make w pressed true
            wPressed = true;
         }
         if (event.getCode() == KeyCode.S)
         {
            //make s pressed true
            sPressed = true;
         }
         if (event.getCode() == KeyCode.D)
         {
            //make d pressed true
            dPressed = true;
         }
      }
   }
  
   public void minesGridMethod(int cgridx_, int cgridy_)
   {
      //the canvas is 600x600
      
      //generate random numbers for the chances and random locations within the grid
      int randomX = random.nextInt(100) + 1;
      int randomY = random.nextInt(100) + 1;
      int randomNum = random.nextInt(3) + 1;
      
      //the xCoordinate is the same as the grid
      int xCord =  cgridx_;
      int yCord = cgridy_;
      
      //calculate the max number of mines
      int maxMines = (int) (Math.sqrt(((xCord*100)-300)*((xCord*100)-300) + ((yCord*100)-300)*((yCord*100)-300)))/1000;
      
      //loop through only as many mines that can be created in that grid
      for (int i=0; i<maxMines; i++)
      {
         //1/3 chance of a mine
         if (randomNum == 1)
         {
             //create the mine
             Mine tempMine = new Mine((xCord*100+randomX), (yCord*100+randomY));
             
             //add the mine to the arraylist of mines
             mines.add(tempMine);
         }
         
      }
      
      for (int i=0; i<mines.size(); i++)
      {
         //create distance from mine
         int distanceFromMine = (int) (Math.sqrt((thePlayer.getX()-mines.get(i).getX())*(thePlayer.getX()-mines.get(i).getX()) + (thePlayer.getY()-mines.get(i).getY())*(thePlayer.getY()-mines.get(i).getY())));
               
         //if the distance from the mine is greater than 800 then remove the mine
         if (distanceFromMine >= 800)
         {
            //remove the mine
            mines.remove(i);
         }
      }
   }
   
   public void grid(int cgridx, int cgridy)
   {  
      //set the grid values
      int yPos = cgridy-5;
      int xPos = cgridx-5;
      
      //looping through top of the grid
      for (int i=0; i<10; i++)
      {
         //stay in top row
         yPos = cgridy-5;
         
         //move through each square in the top grid
         xPos = cgridx-5+i;
         
         //create the mine in that square
         minesGridMethod(xPos, yPos);
      }
      
      //looping through the right of the grid
      for (int i=0; i<9; i++)
      {
         //stay in right row
         xPos = cgridx+4;
         
         //move through each square in the right grid
         yPos = cgridy+5-i;
         
         //create a mine in that square
         minesGridMethod(xPos, yPos);
      }
      
      //looping through the bottom of the grid
      for (int i=0; i<9; i++)
      {
         //stay in bottom row
         yPos = cgridy+4;
         
         //move through each square in the bottom grid
         xPos = cgridx-5+i;
         
         //create a mine in that square
         minesGridMethod(xPos, yPos);
      }
      
      //looping through the left of the grid
      for (int i=0; i<8; i++)
      {
         //stay in left row
         xPos = cgridx-5;
         
         //move through each square of the left grid
         yPos = cgridy-5+i;
         
         //create a mine in that square
         minesGridMethod(xPos, yPos);
      }
   }
   
   public double getHighScore()
   {  
      //draw the high score from the file
      try
      {
         //create scanner
         Scanner scan = new Scanner(new File("highScores.txt")); 
         
         //if the file has no scores in it then return 0
         if (!scan.hasNextDouble())
         {
            return 0;
         }
         
         //get the highScore from the file
         return (scan.nextDouble());
      }
      catch (FileNotFoundException e)
      {
         System.out.println("file not found!");
      }

      return 0;
   }

   public static void main(String[] args)
   {
      launch(args);
   }
}

