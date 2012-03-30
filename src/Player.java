import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Insets;

/**
Player management class
@author Xunxen Xyto
*/
class Player{

    protected int x,y;
    protected int lives;
    private int r,v;
    private boolean left, right, up, down;
    private boolean firing;
    private int rateOfFire;
    private int fireFrame;
    protected int Bx, By;
    protected Insets i;

    /**
    * Creates a new player bounded by the given dimensions and insets.
    * @param d
    *  Dimensions of the play area to stay within.
    * @param insets
    *  Insets of the play area further bounding it.
    *  @param l
    *   Number of lives the player will start with.
    */
    Player(Dimension d,Insets insets,int l){
    
        left=false;
        right=false;
        up=false;
        down=false;
        firing=false;
        Bx=d.width;
        By=d.height;
        x=Bx/2;
        y=By/2;
        lives=3;
        
        r=5;
        v=5;
        
        i=(Insets)insets.clone();

        rateOfFire=5;
        fireFrame=0;
    
    }

    /**
    * Determines if this Player is ready to fire based on fire rate and if it's
    *  Trying to fire.
    * @return boolean for whether or not the player can fire now.
    */
    boolean fireReady(){

       return firing&&(fireFrame++)%rateOfFire==0; 

    }
    
    /**
    * Indicates that this player has requested an upward movement.
    */
    synchronized void upPress(){
    
        up=true;
    
    }
    
    /**
    * Indicates that this player has requested a downward movement.
    */
   synchronized void downPress(){
    
        down=true;
    
    }
    
    /**
    * Indicates that this player has requested a left movement.
    */
    synchronized void leftPress(){
    
        left=true;
    
    }
    
    /**
    * Indicates that this player has requested a right movement.
    */
    synchronized void rightPress(){
    
        right=true;
    
    }

    /**
    * Indicates that this player has stopped requesting a downward movement.
    */
    synchronized void downRelease(){
    
        down=false;
    
    }
 
    /**
    * Indicates that this player has stopped requesting an upward movement.
    */   
    synchronized void upRelease(){
    
        up=false;
    
    }
 
    /**
    * Indicates that this player has stopped requesting a right movement.
    */   
    synchronized void rightRelease(){
    
        right=false;
    
    }
 
    /**
    * Indicates that this player has stopped requesting a left movement.
    */   
    synchronized void leftRelease(){
    
        left=false;
    
    }

    /**
	 * Indicates that this player has stopped requesting all horizontal movement 
	 */
    synchronized void stopX(){

        left=false;
        right=false;

    }

    /**
	 * Indicates that this player has stopped requesting all vertical movement 
	 */
    synchronized void stopY(){

        up=false;
        down=false;

    }
    
	 /**
	 * Performs this player's requested movements if the resulting position will be withing 
	 *  game boundaries.
	 */
    void move(){
    
        if(x>Bx-r-i.right) x=Bx-r-i.right;
        if(y>By-r-i.bottom) y=By-r-i.bottom;
        if(x<r+i.left) x=r+i.left;
        if(y<r+i.top) y=r+i.top;
        
        synchronized(this){
        
            int Vx=0, Vy=0;
            if(left) Vx-=v;
            if(right) Vx+=v;
            if(up) Vy-=v;
            if(down) Vy+=v;

            if(x==Bx-r-i.right&&right) Vx=0;
            if(y==By-r-i.bottom&&down) Vy=0;
            if(x==r+i.left&&left) Vx=0;
            if(y==r+i.top&&up) Vy=0;
            
            x+=Vx;
            y+=Vy;
        
        }
    
    }
    
	 /**
	 * Gets the x coordinate of this player's current position.
	 * @return x coordinate of this player's current position
	 */
    int getX(){
    
        return x;
    
    }

    /**
	 * Gets the y coordinate of this player's current position.
	 * @return y coordinate of this player's current position
	 */
    int getY(){
    
        return y;
    
    }

    /**
	 * Gets this player's radius.
	 * @return radius of this player.
	 */
    int getR(){
    
        return r;
        
    }
    
    void paint(Graphics g){
    
        g.setColor(Color.white);
        g.fillOval(x-r,y-r,2*r,2*r);
    
    }

    /**
	 * Resizes this player's boundaries based on provided dimensions and insets.
	 * @param d
	 *  new dimensions of the game field
	 * @param in
	 *  Insets of the game field to further bound player movement to within view.
	 */
    void resize(Dimension d,Insets in){
    
        Bx=d.width;
        By=d.height;
        i=(Insets)in.clone();
    
    }
    
	 /**
	 * reposition this player at the center of the game field.
	 */
    void reset(){
    
        x=Bx/2;
        y=By/2;
    
    }
    
	 /**
	 * create a bullet with a straight trajectory toward supplied point.
	 * @param Tx
	 *  X coordinate of target's position
	 * @param Ty
	 *  Y coordinate of target's position.
	 * @return A new Bullet object at this player's position moving in a straight line toward
	 *  target position.
	 */
    public Bullet fire(int Tx, int Ty){
     
        return new Bullet(x,y,Tx,Ty,3,0,-1);
     
    }
     
    /**
    * Sets the firing state for this player
    * @param state
    *  The firing state to set the player to.
    */
    public void setFireState(boolean state){

        firing=state;

    }

    /**
    * Gets the firing state for this player
	 * @return true if the player is requesting to fire, otherwise false.
    */
    public boolean getFireState(){

        return firing;

    }
    
    public boolean leftDown(){
    	
    	return left;
    	
    }

    public boolean rightDown(){
    
        return right;
    	
    }
    
    public boolean upDown(){
    	
    	return up;
    	
    }
    
    public boolean downDown(){
    	
    	return down;
    	
    }
    
    public void die(){
    	
    	--lives;
    	reset();
    	
    }
    
    public int getLives(){
    	
    	return lives;
    	
    }
    
}
