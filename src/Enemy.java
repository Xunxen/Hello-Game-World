import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.Random;

/**
* Enemy management class
* @author Xunxen Xyto
* @see Player
*/
@SuppressWarnings("unused")
class Enemy extends Player{

    private int v, r;
    private int Bx, By;
    private int captured;
    private Random ran;
    
    public static final int IMMOBILE=0;
    public static final int PATHETIC=1;
    public static final int WEAK=2;
    public static final int EASY=3;
    public static final int NORMAL=4;
    public static final int HARD=5;
    public static final int IMPOSSIBLE=6;
    private int a, b, err;

    /**
    * Initializes this enemy with supplied dimensions, insets, and difficulty.
    * @param d
    *  Dimensions of the play area.
    * @param i
    *  insets of the play area.
    * @param diff
    *  AI difficulty to use.
    * @see Player#constructor(Dimension, Insets) Player
    */
    Enemy(Dimension d, Insets i, int diff){
    
        super(d,i,1);
        err=b-a;
        ran=new Random();
        x=r+ran.nextInt(d.width-2*r);
        y=r+ran.nextInt(d.height-2*r);
        Bx=d.width;
        By=d.height;
        captured=0;
        
        r=6;
        v=diff;
        
    
    }
    
    void paint(Graphics g){
    
        g.setColor(Color.red);
        g.fillOval(x-r,y-r,2*r,2*r);
    
    }
    
    /**
    * Performs a movement and attack operation based on this enemy's difficulty
    * @param targetX
    *  The X-coordinate of this enemy's target.
    * @param targetY
    *  The Y-coordinate of this enemy's target.
    * @param targetR
    *  The radius of this enemy's target.
    * @see #easyAI(int,int,int) easyAI
    * @see #hardAI(int,int,int) hardAI
    * @see #immobileAI(int,int,int) immobileAI
    * @see #impossibleAI(int,int,int) impossibleAI
    * @see #normalAI(int,int,int) normalAI
    * @see #patheticAI(int,int,int) patheticAI
    * @see #weakAI(int,int,int) weakAI
    */
    public Bullet AI(int targetX, int targetY, int targetR){
        
        if(v==IMMOBILE)return immobileAI(targetX, targetY, targetR);
        else if(v==PATHETIC)return patheticAI(targetX, targetY, targetR);
        else if(v==WEAK)return weakAI(targetX, targetY, targetR);
        else if(v==EASY)return easyAI(targetX, targetY, targetR);
        else if(v==NORMAL)return normalAI(targetX, targetY, targetR);
        else if(v==HARD)return hardAI(targetX, targetY, targetR);
        else return impossibleAI(targetX, targetY, targetR);
   
    }

    private Bullet easyAI(int targetX, int targetY, int targetR){

        setFireState(true);
        double xChange=ran.nextDouble();
    	double yChange=ran.nextDouble();
    	if(!upDown()&&!downDown()){
    		
    		if(yChange<0.15) downPress();
    		else if(yChange>0.85) upPress();
    		
    	}
    	else if(upDown()&&(yChange<0.01||(getY()==getR()+i.top&&yChange<0.9))){
    		upRelease();
    		downPress();
    	}
    	else if(upDown()&&yChange<0.1) upRelease();
    	else if(downDown()&&(yChange<0.01||(getY()==By-getR()+i.bottom&&yChange<0.9))){
    		downRelease();
    		upPress();
    	}
    	else if(downDown()&&yChange<0.1) downRelease();
    	
    	if(!leftDown()&&!rightDown()){
    		
    		if(xChange<0.15) rightPress();
    		else if(xChange>0.85) leftPress();
    		
    	}
    	else if(leftDown()&&(xChange<0.01||(getX()==getR()+i.left&&xChange<0.9))){
    		leftRelease();
    		rightPress();
    	}
    	else if(leftDown()&&xChange<0.1) leftRelease();
    	else if(rightDown()&&(xChange<0.01||(getX()==Bx-getR()+i.right&&xChange<0.9))){
    		rightRelease();
    		leftPress();
    	}
    	else if(rightDown()&&xChange<0.1) rightRelease();
    	
    	if(fireReady()) return fire(targetX, targetY);
    	else return null;

    }
    
    private Bullet hardAI(int targetX, int targetY, int targetR){

    	stopX();
    	stopY();
        if(getX()>targetX) leftPress();
        else if(getX()<targetX) rightPress();
        if(getY()>targetY) upPress();
        else if(getY()<targetY) downPress();
        return null;

    }

    private Bullet immobileAI(int targetX, int targetY, int targetR){

    	return null;

    }

    private Bullet impossibleAI(int targetX, int targetY, int targetR){

        setFireState(true);
    	stopX();
    	stopY();
        if(getX()>targetX) leftPress();
        else if(getX()<targetX) rightPress();
        if(getY()>targetY) upPress();
        else if(getY()<targetY) downPress();
        if(fireReady()) return fire(targetX, targetY);
        return null;

    }
    
    private Bullet normalAI(int targetX, int targetY, int targetR){

    	setFireState(true);
    	double xChange=ran.nextDouble();
    	double yChange=ran.nextDouble();
    	if(!upDown()&&!downDown()){
    		
    		if(yChange<0.1) downPress();
    		else if(yChange>0.9) upPress();
    		
    	}
    	else if(upDown()&&(yChange<0.01||(getY()==getR()+i.top&&yChange<0.9))){
    		upRelease();
    		downPress();
    	}
    	else if(upDown()&&yChange>0.8&&targetY>getY()){
    		upRelease();
    		downPress();
    	}
    	else if(upDown()&&yChange<0.1) upRelease();
    	else if(downDown()&&(yChange<0.01||(getY()==By-getR()+i.bottom&&yChange<0.9))){
    		downRelease();
    		upPress();
    	}
    	else if(downDown()&&yChange>0.8&&targetY<getY()){
    		downRelease();
    		upPress();
    	}
    	else if(downDown()&&yChange<0.1) downRelease();
    	
    	if(!leftDown()&&!rightDown()){
    		
    		if(xChange<0.1) rightPress();
    		else if(xChange>0.9) leftPress();
    		
    	}
    	else if(leftDown()&&(xChange<0.01||(getX()==getR()+i.left&&xChange<0.9))){
    		leftRelease();
    		rightPress();
    	}
    	else if(leftDown()&&xChange>0.8&&targetX>getX()){
    		leftRelease();
    		rightPress();
    	}
    	else if(leftDown()&&xChange<0.1) leftRelease();
    	else if(rightDown()&&(xChange<0.01||(getX()==Bx-getR()+i.right&&xChange<0.9))){
    		rightRelease();
    		leftPress();
    	}
    	else if(rightDown()&&xChange>0.8&&targetX<getX()){
    		rightRelease();
    		leftPress();
    	}
    	else if(rightDown()&&xChange<0.1) rightRelease();
    	if(fireReady())return fire(targetX, targetY);
        else return null;

    }

    private Bullet patheticAI(int targetX, int targetY, int targetR){

    	setFireState(true);
    	if(fireReady())return fire(targetX, targetY);
        else return null;

    }

    private Bullet weakAI(int targetX, int targetY, int targetR){

    	double xChange=ran.nextDouble();
    	double yChange=ran.nextDouble();
    	if(!upDown()&&!downDown()){
    		
    		if(yChange<0.15) downPress();
    		else if(yChange>0.85) upPress();
    		
    	}
    	else if(upDown()&&(yChange<0.01||(getY()==getR()+i.top&&yChange<0.9))){
    		upRelease();
    		downPress();
    	}
    	else if(upDown()&&yChange<0.1) upRelease();
    	else if(downDown()&&(yChange<0.01||(getY()==By-getR()+i.bottom&&yChange<0.9))){
    		downRelease();
    		upPress();
    	}
    	else if(downDown()&&yChange<0.1) downRelease();
    	
    	if(!leftDown()&&!rightDown()){
    		
    		if(xChange<0.15) rightPress();
    		else if(xChange>0.85) leftPress();
    		
    	}
    	else if(leftDown()&&(xChange<0.01||(getX()==getR()+i.left&&xChange<0.9))){
    		leftRelease();
    		rightPress();
    	}
    	else if(leftDown()&&xChange<0.1) leftRelease();
    	else if(rightDown()&&(xChange<0.01||(getX()==Bx-getR()+i.right&&xChange<0.9))){
    		rightRelease();
    		leftPress();
    	}
    	else if(rightDown()&&xChange<0.1) rightRelease();
    	return null;

    }

    /**
    * Set this enemy to a new random location within the play area.
    */
    void reset(){
    
        x=r+ran.nextInt(Bx-2*r);
        y=r+ran.nextInt(By-2*r);
    
    }
    
    /**
    * Change the dimensions of this enemy's play area.
    * @param d
    *  Dimensions to resize the play area to.
    * @param in
    *  Insets of the new play area.
	 * @see Player#resize(Dimension, Insets) Player.resize
    */
    void resize(Dimension d, Insets in){
    
        super.resize(d,in);
        Bx=d.width;
        By=d.height;
        i=(Insets)in.clone();
    
    }
    
    /**
    * Get the value for this enemy's difficulty.
    * @return Difficulty value of this enemy, from 0 to 6.
    */
    public int getDifficulty(){
    
        return v;
    
    }
    
    /**
    * set this enemy's difficulty value.
    * @param d
    *  difficulty value to set this enemy to.
    * @see #EASY
    * @see #HARD
    * @see #IMMOBILE
    * @see #IMPOSSIBLE
    * @see #NORMAL
    * @see #PATHETIC
    * @see #WEAK
    */
    public void setDifficulty(int d){
    
        v=d;
    
    }

}
