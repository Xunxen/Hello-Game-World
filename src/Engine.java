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
        OPTION,
        GAMEOVER

    };
    private States state,prevState;
    //The following fields are protected to allow inheritance.
    protected GameMenu mainMenu;
    protected GameMenu pauseMenu;
    protected GameMenu optionsMenu;
    protected GameMenu gameOverMenu;
    
    private static final String[] DIFFICULTIES={"Immobile","Pathetic","Weak","Easy","Normal","Hard","Impossible"};

    Engine(){
        
        Difficulty=Enemy.NORMAL;
        mainMenu=new GameMenu(new Methods(){
        	public void run(int i){
        		switch(i){
        		case 0:
        			prevState=state;
                    state=States.RUNNING;
                    mainMenu.first();
        		break;
        		case 1:
        			prevState=state;
                    state=States.OPTION;
                    mainMenu.first();
        		break;
        		case 2:
        			System.exit(0);
        		break;
        		default:
        			
        		break;
        		}
        	}
        },"Play","Options","Quit");
        pauseMenu=new GameMenu(new Methods(){
        	public void run(int i){
        		switch(i){
        		case 0:
        			prevState=state;
        	        state=States.RUNNING;
        	        pauseMenu.first();
        		break;
        		case 1:
        			prevState=state;
        	        state=States.OPTION;
        	        pauseMenu.first();
        	        repaint();
        		break;
        		case 2:
        			points=0;
        	        e=new LinkedList<Enemy>();
        	        for(int j=0;j<eCount;j++)
        	            e.append(new Enemy(getSize(),getInsets(),Difficulty));
        	        eB=new LinkedList<Bullet>();
        	 
        	        pB=new LinkedList<Bullet>();
        	        p.reset();
        	        prevState=state;
        	        state=States.RUNNING;
        	        pauseMenu.first();
        		break;
        		case 3:
        			System.exit(0);
        		break;
        		default:
        			
        		break;
        		}
        	}
        },"Resume","Options","Restart","Quit");
        eCount=5;
        optionsMenu=new GameMenu(new Methods(){
        	public void run(int i){
        		switch(i){
        		case 0:
        			e.first();
                    while(e.getData()!=null){
                        e.getData().setDifficulty(Difficulty);
                        e.next();
                    }
        		break;
        		case 1:
        			
        		break;
        		case 2:
        			if(prevState==States.MENU&&eCount!=e.getSize()){

                    	e=new LinkedList<Enemy>();
                        for(int j=0;j<eCount;j++){
                            e.append(new Enemy(getSize(),getInsets(),Difficulty));
                        }

                    }
                    States temp=state;
                    state=prevState;
                    prevState=temp;
                    optionsMenu.first();
        		break;
        		default:
        			
        		break;
        		}
        	}
        },DIFFICULTIES[Difficulty],"Enemies: "+eCount,"Finish");
        gameOverMenu=new GameMenu(new Methods(){
        	public void run(int i){
        		switch(i){
        		case 0:
        			points=0;
        	        e=new LinkedList<Enemy>();
        	        for(int j=0;j<eCount;j++)
        	            e.append(new Enemy(getSize(),getInsets(),Difficulty));
        	        eB=new LinkedList<Bullet>();
        	 
        	        pB=new LinkedList<Bullet>();
        	        p.reset();
        	        prevState=state;
        	        state=States.RUNNING;
        	        pauseMenu.first();
        		break;
        		case 1:
        			prevState=state;
        			state=States.MENU;
        			gameOverMenu.first();
        		break;
        		case 2:
        			System.exit(0);
        		break;
        		default:
        			
        		break;
        		}
        	}
        },"Restart","Main Menu","Quit");
        
        state=States.MENU;
        
        setSize(800,600);
        setBackground(Color.black);
        setFont(new Font("Arial",Font.PLAIN,18));
        setLocation(240,100);
        setVisible(true);
        
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        e=new LinkedList<Enemy>();
        for(int i=0;i<eCount;i++){
        	e.append(new Enemy(getSize(), getInsets(), Difficulty));
        }
                
        //Should add a separate class for these to make engine not need to be inherited.
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
        
        p=new Player(getSize(),getInsets(),3);
        
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
                if((temp.getX()-temp.getR()<pB.getData().getX()+pB.getData().getR()&&
                temp.getX()+temp.getR()>pB.getData().getX()-pB.getData().getR())&&
                (temp.getY()-temp.getR()<pB.getData().getY()+pB.getData().getR()&&
                temp.getY()+temp.getR()>pB.getData().getY()-pB.getData().getR())){

                    ++points;
                    pB.remove();
                    e.remove();
                    if(e.getSize()==0){
                    	prevState=state;
                    	state=States.GAMEOVER;
                    }
                    break;

                }else e.next();

            }

        pB.next();
          
        }

        eB.first();
        while(eB.getData()!=null){

            eB.getData().move();
            
            if((p.getX()-p.getR()<eB.getData().getX()+eB.getData().getR()&&
                p.getX()+p.getR()>eB.getData().getX()-eB.getData().getR())&&
                (p.getY()-p.getR()<eB.getData().getY()+eB.getData().getR()&&
                p.getY()+p.getR()>eB.getData().getY()-eB.getData().getR())){

                        eB.remove();
                        p.die();
                        if(p.getLives()<0){
                        	prevState=state;
                        	state=States.GAMEOVER;
                        }

                    }else e.next();

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
            //This needs to be removed in favor of centralizing the AI to the enemy class
                    x=p.getX();
                    y=p.getY();
                    time=System.nanoTime();
            
                }

                Bullet temp=e.getData().AI(x,y,p.getR());
                e.getData().move();
                if(temp!=null) eB.append(temp);

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

            if(state==States.MENU) mainMenu.draw(dbmg);
                else if(state==States.PAUSED) pauseMenu.draw(dbmg);
                else if(state==States.OPTION) optionsMenu.draw(dbmg);
                else if(state==States.GAMEOVER) gameOverMenu.draw(dbmg);
            g.drawImage(dbmi,a-60,b-70,this);

        }
    
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

            if(ke.getKeyCode()==KeyEvent.VK_S){

                pauseMenu.next();
                repaint();

            }
            if(ke.getKeyCode()==KeyEvent.VK_W){

                pauseMenu.prev();
                repaint();

            }
            if(ke.getKeyCode()==KeyEvent.VK_ESCAPE){

                pauseMenu.menuActions.run(0);

            }
            if(ke.getKeyCode()==KeyEvent.VK_ENTER){

                pauseMenu.run();

            }

        }
        else if(state==States.OPTION){

            if(ke.getKeyCode()==KeyEvent.VK_ESCAPE){

                optionsMenu.menuActions.run(2);
                repaint();

            }
            if(ke.getKeyCode()==KeyEvent.VK_W){

                optionsMenu.prev();
                repaint();

            }
            if(ke.getKeyCode()==KeyEvent.VK_S){

                optionsMenu.next();
                repaint();

            }
            if(ke.getKeyCode()==KeyEvent.VK_A&&optionsMenu.getIndex()==0&&Difficulty>Enemy.IMMOBILE){

                Difficulty--;
                optionsMenu.setItems(DIFFICULTIES[Difficulty],"Enemies: "+eCount,"Finish");
                repaint();

            }
            if(ke.getKeyCode()==KeyEvent.VK_D&&optionsMenu.getIndex()==0&&Difficulty<Enemy.IMPOSSIBLE){

                ++Difficulty;
                optionsMenu.setItems(DIFFICULTIES[Difficulty],"Enemies: "+eCount,"Finish");
                repaint();

            }

            if(ke.getKeyCode()==KeyEvent.VK_A&&optionsMenu.getIndex()==1&&eCount>1){

                --eCount;
                optionsMenu.setItems(DIFFICULTIES[Difficulty],"Enemies: "+eCount,"Finish");
                repaint();

            }

            if(ke.getKeyCode()==KeyEvent.VK_D&&optionsMenu.getIndex()==1&&eCount<100){

                ++eCount;
                optionsMenu.setItems(DIFFICULTIES[Difficulty],"Enemies: "+eCount,"Finish");
                repaint();

            }

            if(ke.getKeyCode()==KeyEvent.VK_ENTER){

            	optionsMenu.run();
                repaint();

            }

        }
        else if(state==States.MENU){

            if(ke.getKeyCode()==KeyEvent.VK_S){

            	mainMenu.next();
                repaint();
                
            }

            if(ke.getKeyCode()==KeyEvent.VK_W){

                mainMenu.prev();
                repaint();

            }
            if(ke.getKeyCode()==KeyEvent.VK_ENTER){

                if(mainMenu.getIndex()==0){

                    prevState=state;
                    state=States.RUNNING;
                    mainMenu.first();

                }
                else if(mainMenu.getIndex()==1){

                    prevState=state;
                    state=States.OPTION;
                    mainMenu.first();
                    repaint();

                }
                else System.exit(0);

            }

        }
        else if(state==States.GAMEOVER){
        	
        	
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
