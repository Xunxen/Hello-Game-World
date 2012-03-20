import java.awt.Graphics;
import java.awt.Color;

/**
* Bullet data class
* @author Xunxen Xyto
*/
@SuppressWarnings("unused")
class Bullet{

    //initial x,y position, target x,y position, radius
    private int Bx, By, Tx, Ty, r;
	private int type; //will be used later for seeking missiles and alternate sprites.
    private int lifespan; //How many frames the bullet should last for. -1 lasts until impact.
    private int x, y; //Current position.
    private int a, b; //Gradient values
    private int err;
    private static final int v=8;//velocity
    
    /**
    * Initialize a new bullet with the specified parameters.
    * @param initX
    *  X coordinate of initial position.
    * @param initY
    *  Y coordinate of initial position.
    * @param targetX
    *  X coordinate of target position.
    * @param targetY
    *  Y coordinate of target position.
    * @param initR
    *  Initial bullet radius.
    * @param type
    *  Type of bullet to create.
    * @param life
    *  lifespan of bullet in frames.
    */
    public Bullet(int initX, int initY, int targetX, int targetY, int initR, int type, int life){
    
        Bx=initX;
        By=initY;
        Tx=targetX;
        Ty=targetY;
        r=initR;
        lifespan=life;
        type=0;
        x=Bx;
        y=By;
        a=Math.abs(Tx-Bx);
        b=Math.abs(Ty-By);
        err=b-a;
    
    }
    
    /**
    * moves the bullet position closer to the target position using Bresenham's Algorithm.
    * @postcondition
    *  Bullet has moved further along the line described by (Bx, By) and (Tx, Ty).
    */
    public void move(){

        for(int i=0;i<v;i++){
            int e2=2*err;
            if(e2>-b){
            
                err-=b;
                x+=(Bx<Tx)?1:-1;
            
            }
            if(e2<a){
            
                err+=a;
                y+=(By<Ty)?1:-1;
            
            }
        }
    
    }
    
    /**
    * Gets this bullets x coordinate.
    * @return X coordinate of this bullet
    */
    public int getX(){
    
        return x;
    
    }
    
    /**
    * Gets this bullets x coordinate.
    * @return X coordinate of this bullet
    */
    public int getY(){
    
        return y;
    
    }
    
    /**
    * Gets the radius of this bullet.
    * @return Radius of this bullet.
    */
    public int getR(){
    
        return r;
        
    }
    
    void paint(Graphics g){
    
        g.setColor(Color.blue);
        g.fillOval(x-r,y-r,2*r,2*r);
    
    }

}
