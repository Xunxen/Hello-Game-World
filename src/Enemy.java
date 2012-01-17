import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.Random;

class Enemy extends Player{

    int v, r;
    int Bx, By;
    int captured;
    Random ran;
    
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
    //weak easy normal hard impossible.
    * @see #easyAI(int,int,int) easyAI
    * @see #hardAI(int,int,int) hardAI
    * @see #immobileAI(int,int,int) immobileAI
    * @see #impossibleAI(int,int,int) impossibleAI
    * @see #normalAI(int,int,int) normalAI
    * @see #patheticAI(int,int,int) patheticAI
    * @see #weakAI(int,int,int) weakAI
    */
    void AI(int targetX, int targetY, int targetR){
        
                stopX();
        if(targetX>x+targetR) rightPress();
        else if(targetX<x-targetR) leftPress();
        
                stopY();
        if(targetY>y+targetR) downPress();
        else if(targetY<y-targetR) upPress();
    
    }
    
    /**
    * Set this enemy to a new random location within the play area.
    * @param - none
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
