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

	Enemy(Dimension d, Insets i, int diff){
	
		super(d,i);
		ran=new Random();
		x=r+ran.nextInt(d.width-2*r);
		y=r+ran.nextInt(d.height-2*r);
		Bx=d.width;
		By=d.height;
		captured=0;
		
		r=5;
		v=diff;
		
	
	}
	
	void paint(Graphics g){
	
		g.setColor(Color.red);
		g.fillOval(x-r,y-r,2*r,2*r);
	
	}
	
	void AI(int targetX, int targetY, int targetR){
		
                stopX();
		if(targetX>x+targetR) rightPress();
		else if(targetX<x-targetR) leftPress();
		
                stopY();
		if(targetY>y+targetR) downPress();
		else if(targetY<y-targetR) upPress();
	
	}
	
	void reset(){
	
		x=r+ran.nextInt(Bx-2*r);
		y=r+ran.nextInt(By-2*r);
	
	}
	
	void resize(Dimension d, Insets in){
	
		super.resize(d,in);
		Bx=d.width;
		By=d.height;
		i=(Insets)in.clone();
	
	}
	
	public int getDifficulty(){
	
		return v;
	
	}
	
	public void setDifficulty(int d){
	
		v=d;
	
	}

}
