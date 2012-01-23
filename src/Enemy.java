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
    
        super(d,i);
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
    void AI(int targetX, int targetY, int targetR){
        
        easyAI(targetX, targetY, targetR);
        /*if(v==IMMOBILE)immobileAI(targetX, targetY, targetR);
        else if(v==PATHETIC)patheticAI(targetX, targetY, targetR);
        else if(v=WEAK)weakAI(targetX, targetY, targetR);
        else if(v=EASY)easyAI(targetX, targetY, targetR);
        else if(v=NORMAL)normalAI(targetX, targetY, targetR);
        else if(v=HARD)hardAI(targetX, targetY, targetR);
        else if(v=IMPOSSIBLE)impossibleAI(targetX, targetY, targetR);*/
   
    }

    private void easyAI(int targetX, int targetY, int targetR){

        setFireState(true);
        stopX();
        if(targetX>x+targetR) rightPress();
        else if(targetX<x-targetR) leftPress();
        
        stopY();
        if(targetY>y+targetR) downPress();
        else if(targetY<y-targetR) upPress();

    }
    
    private void hardAI(int targetX, int targetY, int targetR){



    }

    private void immobileAI(int targetX, int targetY, int targetR){



    }

    private void impossibleAI(int targetX, int targetY, int targetR){



    }
    
    private void normalAI(int targetX, int targetY, int targetR){



    }

    private void patheticAI(int targetX, int targetY, int targetR){



    }

    private void weakAI(int targetX, int targetY, int targetR){



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
