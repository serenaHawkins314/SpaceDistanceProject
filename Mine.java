//Serena Hawkins
//Objects and Elementary Data Structures: GIT Space Project

//This is the Mine class that inherits from DrawableObject

//imports
import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import java.util.*;

public class Mine extends DrawableObject
{
   public Mine(float x_, float y_)
   {
      super(x_, y_);
   }
   
   //positions
   private float x;
   private float y;
   
   //double and random for interpolate
   double fraction = 0; 
   
   //drawMe method to draw the mines
   public void drawMe(float mineX, float mineY, GraphicsContext gc_)
   {
      Color red = Color.RED;
      gc_.setFill(Color.BLACK);
      gc_.fillOval(mineX-6,mineY-6,15,15);
      gc_.setFill(Color.GRAY);
      gc_.fillOval(mineX-6,mineY-6,13,13);
      fraction = getFraction(fraction);
      gc_.setFill(red.interpolate(Color.WHITE, fraction));
      gc_.fillOval(mineX-6,mineY-6,12,12);
   }
   
   public static double getFraction(double _fraction)
   {
      if (_fraction < 1.0)
      {
         _fraction += 0.01;
      }
      else
      {
         _fraction = 0;
      }
      
      return _fraction;
   }
   
}