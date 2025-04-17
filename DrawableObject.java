import javafx.scene.paint.*;
import javafx.scene.canvas.*;

public abstract class DrawableObject
{
   public DrawableObject(float x, float y)
   {
      this.x = x;
      this.y = y;
      
      //save original positions
      originalX = x; 
      originalY = y;
   }
   
   //original positions
   private float originalX;
   private float originalY;

   //positions
   private float x;
   private float y;
   
   //make the xforce and the yforce for movement
   static double xForce = 0;
   static double yForce = 0;
   
   //takes the position of the player and calls draw me with appropriate positions
   public void draw(float playerx, float playery, GraphicsContext gc, boolean isPlayer)
   {
      //the 300,300 places the player at 300,300, if you want to change it you will have to modify it here
      
      if(isPlayer)
         drawMe(playerx,playery,gc);
      else
         drawMe(-playerx+300+x,-playery+300+y,gc);
   }
   
   //this method you implement for each object you want to draw. Act as if the thing you want to draw is at x,y.
   //NOTE: DO NOT CALL DRAWME YOURSELF. Let the the "draw" method do it for you. I take care of the math in that method for a reason.
   public abstract void drawMe(float x, float y, GraphicsContext gc);
   
   public void act(int ticks_, boolean _aPressed, boolean _wPressed, boolean _sPressed, boolean _dPressed)
   {
      //if w is pressed
      if (_wPressed)
      {  
         //if the ticks is divisible by itself
         if (ticks_%ticks_==0)
         { 
            //subtract .1 to the yForce
            yForce-=0.1;
               
            //if the yForce gets to be less than -5, then set it back to -5
            if (yForce <= -5)
            {
               //set it back to -5
               yForce = -5;
            } 
         }  
      }
      
      //if A is pressed 
      if (_aPressed)
      { 
         //if the ticks is divisible by itself
         if (ticks_%ticks_==0)
         { 
            //subtract 1 from the xForce
            xForce-=0.1;
               
            //if the xForce gets to be less than -5, set it back to -5
            if (xForce <= -5)
            {
               xForce = -5;
            }
        }
      }
      
      //if S is pressed
      if (_sPressed)
      {  
         //if the ticks is divisible by itself
         if (ticks_%ticks_==0)
         { 
            //add .1 to the yForce
            yForce+=0.1;
               
            //if the yForce gets to be greater than 5, then set it back to 5
            if (yForce >= 5)
            {
               //set it back to 5
               yForce = 5;
            }
         }   

      }
      
      //if d is pressed
      if (_dPressed)
      { 
         //if the ticks is divisible by itself
         if (ticks_%ticks_==0)
         { 
            //add .1 from the xForce
            xForce+=0.1;
            
            //if the xForce gets to be greater than 5, set it back to 5
            if (xForce >= 5)
            {
               xForce = 5;
            }
         }
      }
   
      
      //while the horizontal keys are not pressed
      if (_aPressed==false && _dPressed==false)
      {
         //if the force less than 0 then to slow down, you need to move it closer to 0, so addition
         //if the force is greater than 0 then to slow down, you need to move it closer to 0, so subtraction
         //do this for every tick
         if (ticks_%ticks_==0)
         {
            if (xForce < 0)
            {
               xForce += 0.025;
            }
            else if (xForce > 0)
            {
               xForce -= 0.025;
            }
         }
           
         //if the force is -0.25<force<0.25 then make it 0
         if (xForce > -0.25 && xForce < 0.25)
         {
            xForce = 0;
         }
      } 
      
      //if none of the vertical keys are pressed 
      if (_wPressed==false && _sPressed==false)
      {
            //if the force less than 0 then to slow down, you need to move it closer to 0, so addition
            //if the force is greater than 0 then to slow down, you need to move it closer to 0, so subtraction
            //do this for every tick
            if (ticks_%ticks_==0)
            {
               if (yForce < 0)
               {
                  yForce += 0.025;
               }
               else if (yForce > 0)
               {
                  yForce -= 0.025;
               }
            }
              
            //if the force is -0.25<force<0.25 then make it 0
            if (yForce > -0.25 && yForce < 0.25)
            {
               yForce = 0;
            }   
      }
            
      //add to the yPosition based on the yForce
      y += yForce;
         
      //add to the xPosition based on the xForce
      x += xForce;  
      //System.out.println("x = " + x);    
   }
   
   public float getX(){return x;}
   public float getY(){return y;}
   public void setX(float x_){x = x_;}
   public void setY(float y_){y = y_;}
   
   public double distance(DrawableObject other)
   {
      //System.out.println("distance: " + (Math.sqrt((other.x-originalX)*(other.x-originalX) + (other.y-originalY)*(other.y-originalY))));
      return (Math.sqrt((other.x-originalX)*(other.x-originalX) + (other.y-originalY)*(other.y-originalY)));
   }
}
