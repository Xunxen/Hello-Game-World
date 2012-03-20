import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.Font;

/**
* Game engine
* @author Xunxen Xyto
*/
class Engine extends JFrame implements Runnable, KeyListener, MouseListener, MouseMotionListener{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Player p;
    protected LinkedList<Enemy> e;
    private Image dbi,dbmi;
    private Graphics dbg,dbmg;
    private Thread t;
    private int x,y,mx,my;
    private long time;
    private int points,eCount;
    private int Difficulty;
    private LinkedList<Bullet> pB;
    private LinkedList<Bullet> eB;
    private enum States{

        MENU,
        RUNNING,
        PAUSED,
        OPTION

    };
    private States state,prevState;
    int menuIndex,pauseIndex,optionIndex;
    //The following fields are protected to allow inheritance.
    protected String[] menuOptions;
    protected String[] pauseOptions;
    protected String[] options;
    
    private static final String[] DIFFICULTIES={"Immobile","Pathetic","Weak","Easy","Normal","Hard","Impossible"};

    Engine(){
        
        Difficulty=Enemy.PATHETIC;
        menuOptions=new String[3];
        menuOptions[0]="Play";
        menuOptions[1]="Options";
        menuOptions[2]="Quit";
        
        pauseOptions=new String[4];
        pauseOptions[0]="Resume";
        pauseOptions[1]="Options";
        pauseOptions[2]="Restart";
        pauseOptions[3]="Quit";
        
        options=new String[3];
        options[0]=DIFFICULTIES[Difficulty];
        options[1]="Enemies: ";
        options[2]="Finish";
        
        state=States.MENU;
        menuIndex=0;
        pauseIndex=0;
        optionIndex=0;
        
        setSize(800,600);
        setBackground(Color.black);
        setFont(new Font("Arial",Font.PLAIN,18));
        setLocation(240,100);
        setVisible(true);
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        eCount=10;
        e=new LinkedList<Enemy>();
        for(int i=0;i<eCount;i++){
        	e.append(new Enemy(getSize(), getInsets(), Difficulty));
        }
                
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        addComponentListener(new ComponentAdapter(){
        
            public void componentResized(ComponentEvent ce){
            
                p.resize(getSize(),getInsets());
                e.first();
                while(e.getData()!=null){
                    e.getData().resize(getSize(),getInsets());
                    e.next();
                }
                dbi=createImage(getSize().width,getSize().height);
                dbg=dbi.getGraphics();
            
            }
        
        });
        
        p=new Player(getSize(),getInsets());
        
        pB=new LinkedList<Bullet>();
        eB=new LinkedList<Bullet>();
        x=p.getX();
        y=p.getY();
        
        points=0;
        
        t=new Thread(this);
        t.start();
    
    }
    
    public void run(){

        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

        while(true){

            if(state==States.RUNNING) runState();
            else repaint();

            try{
                Thread.sleep(20);
            }catch(InterruptedException e){}

            Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
        }

    }
    /**
    * Draws the main menu to supplied Graphics object.
    * @param g
    *  Graphics object to draw the menu to.
    * @see #drawOptions(Graphics) drawOptions
    * @see #drawPauseMenu(Graphicsw drawPauseMenu
    */
    protected void drawMenu(Graphics g){

            g.setColor(Color.black);
            g.fillRect(0,0,120,140);
            if(menuIndex==0) g.setColor(Color.white);
            else g.setColor(Color.gray);
            g.drawString(menuOptions[0],10,20);
            if(menuIndex==1) g.setColor(Color.white);
            else g.setColor(Color.gray);
            g.drawString(menuOptions[1],10,50);
            if(menuIndex==2) g.setColor(Color.white);
            else g.setColor(Color.gray);
            g.drawString(menuOptions[2],10,80);

    }

    /**
    * Draws the pause menu to supplied Graphics object.
    * @param g
    *  Graphics object to draw the pause menu to.
    * @see #drawMenu(Graphics) drawMenu
    * @see #drawOptions(Grpahics) drawOptions
    */
    protected void drawPauseMenu(Graphics g){

            g.setColor(Color.black);
            g.fillRect(0,0,120,140);
            if(pauseIndex==0) g.setColor(Color.white);
            else g.setColor(Color.gray);
            g.drawString(pauseOptions[0],10,20);
            if(pauseIndex==1) g.setColor(Color.white);
            else g.setColor(Color.gray);
            g.drawString(pauseOptions[1],10,50);
            if(pauseIndex==2) g.setColor(Color.white);
            else g.setColor(Color.gray);
            g.drawString(pauseOptions[2],10,80);
            if(pauseIndex==3) g.setColor(Color.white);
            else g.setColor(Color.gray);
            g.drawString(pauseOptions[3],10,110);

        }

    /**
    * Draws the options menu to supplied Graphics object.
    * @param g
    *  Graphics object to draw the options menu to.
    * @see #drawOptions(Graphics) drawOptions
    * @see #drawPauseMenu(Grpahics) drawPauseMenu
    */
    protected void drawOptions(Graphics g){

            g.setColor(Color.black);
            g.fillRect(0,0,120,140);
            g.setColor(Color.white);
            if(optionIndex==0) g.setColor(Color.white);
            else g.setColor(Color.gray);
            g.drawString(DIFFICULTIES[Difficulty],10,20);
            if(optionIndex==1) g.setColor(Color.white);
            else g.setColor(Color.gray);
            g.drawString(options[1]+eCount,10,50);
            if(optionIndex==2) g.setColor(Color.white);
            else g.setColor(Color.gray);
            g.drawString(options[2],10,80);

        }

    /**
    * Manages player actions.
    */
    protected void managePlayer(){

        p.move();
        if(p.fireReady())
            pB.append(p.fire(mx,my));

    }

    /**
    * Manages bullet movement and collisions.
    */
    void manageBullets(){

        pB.first();
        while(pB.getData()!=null){
          
            pB.getData().move();
            e.first();
            while(e.getData()!=null){
                
            	Enemy temp=e.getData();
            	//try{
            	    if((temp.getX()-temp.getR()<pB.getData().getX()+pB.getData().getR()&&
                    temp.getX()+temp.getR()>pB.getData().getX()-pB.getData().getR())&&
                    (temp.getY()-temp.getR()<pB.getData().getY()+pB.getData().getR()&&
                    temp.getY()+temp.getR()>pB.getData().getY()-pB.getData().getR())){

                        ++points;
                        pB.remove();
                        e.remove();
                        break;

                    }else e.next();
                
            	//}catch(NullPointerException ne){
            		
            		//I don't know what to do here...
            		
            		
            	//}

            }

        pB.next();
          
        }

        eB.first();
        while(eB.getData()!=null){

            eB.getData().move();

            eB.next();

        }

    }

    /**
    * Manages enemy actions.
    */
    protected void manageEnemies(){

    	e.first();
        while(e.getData()!=null){

            if(Difficulty>Enemy.IMMOBILE){
            
                if(System.nanoTime()-time>1000000000L*(Enemy.IMPOSSIBLE-e.getData().getDifficulty())/10.0){
            
                    x=p.getX();
                    y=p.getY();
                    time=System.nanoTime();
            
                }

                e.getData().AI(x,y,p.getR());
                e.getData().move();
                if(e.getData().fireReady()) eB.append(e.getData().fire(x, y));

            }
            
            if((e.getData().getX()-e.getData().getR()<p.getX()+p.getR()&&
                e.getData().getX()+e.getData().getR()>p.getX()-p.getR())&&
                (e.getData().getY()-e.getData().getR()<p.getY()+p.getR()&&
                e.getData().getY()+e.getData().getR()>p.getY()-p.getR())){

                p.reset();
                
            }
            
            e.next();
                
        }
        
        e.first();

    }

    /**
    * Performs game running state actions.
    */
    private void runState(){
    
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

        managePlayer();
        manageBullets();
        manageEnemies();
             
        repaint();
            
        try{
            Thread.sleep(20);
        }catch(InterruptedException e){}
        
        Thread.currentThread().setPriority(Thread.NORM_PRIORITY);

    }

    public void paint(Graphics g){
    
        if(state==States.RUNNING){

            if(g==null) return;
            g.setColor(Color.gray);
            g.drawString("Defeated "+points+" enemies. "+e.getSize()+" Remaining.",10+getInsets().left,28+getInsets().top);
            p.paint(g);
            e.first();
            while(e.getData()!=null){
                e.getData().paint(g);
                e.next();
            }
            pB.first();
            while(pB.getData()!=null){
                pB.getData().paint(g);
                pB.next();
            }
            eB.first();
            while(eB.getData()!=null){

                eB.getData().paint(g);
                eB.next();

            }

        }
            
    }
    
    public void repaint(){
    
        update(getGraphics());
    
    }
    
    public void update(Graphics g){
    
        if(dbi==null){
        
            dbi=createImage(getSize().width,getSize().height);
            dbg=dbi.getGraphics();

            dbg.setColor(getBackground());
            dbg.fillRect(0,0,getSize().width,getSize().height);
        
            g.drawImage(dbi,0,0,this);

        
        }

        if(state==States.RUNNING){

            dbg.setColor(getBackground());
            dbg.fillRect(0,0,getSize().width,getSize().height);
        
            paint(dbg);
        
            g.drawImage(dbi,0,0,this);

        }
        else{

            int a=getWidth()/2;
            int b=getHeight()/2;
            if(dbmi==null){

                dbmi=createImage(120,140);
                dbmg=dbmi.getGraphics();

            }

            if(state==States.MENU) drawMenu(dbmg);
                else if(state==States.PAUSED) drawPauseMenu(dbmg);
                else if(state==States.OPTION) drawOptions(dbmg);
            g.drawImage(dbmi,a-60,b-70,this);

        }
    
    }
    
    /**
    * Performs action indicated by currently selected pause menu option.
    */
    protected void selectPauseOption(){
    
    if(pauseIndex==0){

        prevState=state;
        state=States.RUNNING;
        pauseIndex=0;

    }
    else if(pauseIndex==1){
    
        prevState=state;
        state=States.OPTION;
        pauseIndex=0;
        repaint();

    }
    else if(pauseIndex==2){

        points=0;
        e=new LinkedList<Enemy>();
        for(int i=0;i<eCount;i++)
            e.append(new Enemy(getSize(),getInsets(),Difficulty));
        eB=new LinkedList<Bullet>();
 
        pB=new LinkedList<Bullet>();
        p.reset();
        prevState=state;
        state=States.RUNNING;
        pauseIndex=0;

   }
   else System.exit(0);
    
}
    
    public void keyPressed(KeyEvent ke){
    
        if(state==States.RUNNING){
            if(ke.getKeyCode()==KeyEvent.VK_W) p.upPress();
            if(ke.getKeyCode()==KeyEvent.VK_S) p.downPress();
            if(ke.getKeyCode()==KeyEvent.VK_A) p.leftPress();
            if(ke.getKeyCode()==KeyEvent.VK_D) p.rightPress();
            if(ke.getKeyCode()==KeyEvent.VK_ESCAPE){

                prevState=state;
                state=States.PAUSED;

            }
        }
        else if(state==States.PAUSED){

            if(ke.getKeyCode()==KeyEvent.VK_S
                &&pauseIndex<pauseOptions.length-1){

                pauseIndex++;
                repaint();

            }
            if(ke.getKeyCode()==KeyEvent.VK_W&&pauseIndex>0){

                pauseIndex--;
                repaint();

            }
            if(ke.getKeyCode()==KeyEvent.VK_ESCAPE){

                prevState=state;
                state=States.RUNNING;
                pauseIndex=0;

            }
            if(ke.getKeyCode()==KeyEvent.VK_ENTER){

                selectPauseOption();

            }

        }
        else if(state==States.OPTION){

            if(ke.getKeyCode()==KeyEvent.VK_ESCAPE){

                if(prevState==States.MENU&&eCount!=e.getSize()){

                    e=new LinkedList<Enemy>();
                    for(int i=0;i<eCount;i++)
                        e.append(new Enemy(getSize(),getInsets(),Difficulty));

                }
                States temp=state;
                state=prevState;
                prevState=temp;
                optionIndex=0;
                repaint();

            }
            if(ke.getKeyCode()==KeyEvent.VK_W&&optionIndex>0){

                optionIndex--;
                repaint();

            }
            if(ke.getKeyCode()==KeyEvent.VK_S&&optionIndex<options.length-1){

                optionIndex++;
                repaint();

            }
            if(ke.getKeyCode()==KeyEvent.VK_A&&optionIndex==0&&Difficulty>Enemy.IMMOBILE){

                Difficulty--;
                repaint();

            }
            if(ke.getKeyCode()==KeyEvent.VK_D&&optionIndex==0&&Difficulty<Enemy.IMPOSSIBLE){

                ++Difficulty;
                repaint();

            }

            if(ke.getKeyCode()==KeyEvent.VK_A&&optionIndex==1&&eCount>1){

                --eCount;
                repaint();

            }

            if(ke.getKeyCode()==KeyEvent.VK_D&&optionIndex==1&&eCount<100){

                ++eCount;
                repaint();

            }

            if(ke.getKeyCode()==KeyEvent.VK_ENTER&&optionIndex==2){

            	e.first();
                while(e.getData()!=null){
                    e.getData().setDifficulty(Difficulty);
                    e.next();
                }

                if(prevState==States.MENU&&eCount!=e.getSize()){

                	e=new LinkedList<Enemy>();
                    for(int i=0;i<eCount;i++){
                        e.append(new Enemy(getSize(),getInsets(),Difficulty));
                    }

                }
 
                States temp=state;
                state=prevState;
                prevState=temp;
                optionIndex=0;
                repaint();

            }

        }
        else if(state==States.MENU){

            if(ke.getKeyCode()==KeyEvent.VK_S
                &&menuIndex<menuOptions.length-1){

                ++menuIndex;
                repaint();

            }
            if(ke.getKeyCode()==KeyEvent.VK_W&&menuIndex>0){

                menuIndex--;
                repaint();

            }
            if(ke.getKeyCode()==KeyEvent.VK_ENTER){

                if(menuIndex==0){

                    prevState=state;
                    state=States.RUNNING;
                    menuIndex=0;

                }
                else if(menuIndex==1){

                    prevState=state;
                    state=States.OPTION;
                    menuIndex=0;
                    repaint();

                }
                else System.exit(0);

            }

        }
        
    }
    
    public void keyReleased(KeyEvent e){
    
        if(e.getKeyCode()==KeyEvent.VK_W) p.upRelease();
        if(e.getKeyCode()==KeyEvent.VK_S) p.downRelease();
        if(e.getKeyCode()==KeyEvent.VK_A) p.leftRelease();
        if(e.getKeyCode()==KeyEvent.VK_D) p.rightRelease();
    
    }
    
    public void keyTyped(KeyEvent e){}
    public void mouseClicked(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    
    public void mousePressed(MouseEvent e){
     
        p.setFireState(true);
        mx=e.getX();
        my=e.getY();
     
    }
    public void mouseReleased(MouseEvent e){
     
        p.setFireState(false);
     
    }

    public void mouseMoved(MouseEvent e){}

    public void mouseDragged(MouseEvent e){

        if(p.getFireState()){

            mx=e.getX();
            my=e.getY();

        }

    }

}
