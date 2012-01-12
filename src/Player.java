import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Insets;

class Player{

    protected int x,y;
    int r,v;
    boolean left, right, up, down;
    boolean firing;
    int Bx, By;
    Insets i;

    Player(Dimension d,Insets insets){
    
        left=false;
        right=false;
        up=false;
        down=false;
          
            firing=false;

        Bx=d.width;
        By=d.height;
        x=Bx/2;
        y=By/2;
        
        r=5;
        v=5;
        
        i=(Insets)insets.clone();
    
    }
    
    synchronized void upPress(){
    
        up=true;
    
    }
    
    synchronized void downPress(){
    
        down=true;
    
    }
    
    synchronized void leftPress(){
    
        left=true;
    
    }
    
    synchronized void rightPress(){
    
        right=true;
    
    }
    
    synchronized void downRelease(){
    
        down=false;
    
    }
    
    synchronized void upRelease(){
    
        up=false;
    
    }
    
    synchronized void rightRelease(){
    
        right=false;
    
    }
    
    synchronized void leftRelease(){
    
        left=false;
    
    }

    synchronized void stopX(){

        left=false;
        right=false;

    }

    synchronized void stopY(){

        up=false;
        down=false;

    }
    
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
    
    int getX(){
    
        return x;
    
    }
    
    int getY(){
    
        return y;
    
    }
    
    int getR(){
    
        return r;
        
    }
    
    void paint(Graphics g){
    
        g.setColor(Color.white);
        g.fillOval(x-r,y-r,2*r,2*r);
    
    }
    
    void resize(Dimension d,Insets in){
    
        Bx=d.width;
        By=d.height;
        i=(Insets)in.clone();
    
    }
    
    void reset(){
    
        x=Bx/2;
        y=By/2;
    
    }
     
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
    * Checks if the player is firing
    * @param - none
    * @return boolean value denoting whether or not the player is firing.
    */
    public boolean getFireState(){
    
        return firing;
    
    }

}
