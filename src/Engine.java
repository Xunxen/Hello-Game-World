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
import java.util.Vector;

class Engine extends JFrame implements Runnable, KeyListener, MouseListener, MouseMotionListener{

    Player p;
    Enemy[] e;
    Image dbi,dbmi;
    Graphics dbg,dbmg;
    Thread t;
    int x,y,mx,my;
    long time;
    int points,eCount;
    int Difficulty;
    LinkedList<Bullet> pB;
    private enum States{

        MENU,
        RUNNING,
        PAUSED,
        OPTION

    };
    private States state,prevState;
    int menuIndex,pauseIndex,optionIndex;
    private static final String[] MENUOPTIONS={"Play","Options","Quit"};
    private static final String[] PAUSEOPTIONS={"Resume","Options","Restart","Quit"};
    private static final String[] OPTIONS={"","Enemies: ","Finish"};
    private static final String[] DIFFICULTIES={"Immobile","Pathetic","Weak","Easy","Normal","Hard","Impossible"};

    Engine(){
        
        Difficulty=Enemy.PATHETIC;
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
        eCount=20;
        e=new Enemy[eCount];
                
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        addComponentListener(new ComponentAdapter(){
        
            public void componentResized(ComponentEvent ce){
            
                p.resize(getSize(),getInsets());
                for(int i=0;i<e.length;i++)
                    if(e[i]!=null)
                        e[i].resize(getSize(),getInsets());
                dbi=createImage(getSize().width,getSize().height);
                dbg=dbi.getGraphics();
            
            }
        
        });
        
        p=new Player(getSize(),getInsets());
        for(int i=0;i<e.length;i++)
            e[i]=new Enemy(getSize(),getInsets(),Difficulty);
        
          pB=new LinkedList<Bullet>();
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
                t.sleep(20);
            }catch(InterruptedException e){}

            Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
        }

    }

    private void drawMenu(Graphics g){

            g.setColor(Color.black);
            g.fillRect(0,0,120,140);
            if(menuIndex==0) g.setColor(Color.white);
            else g.setColor(Color.gray);
            g.drawString(MENUOPTIONS[0],10,20);
            if(menuIndex==1) g.setColor(Color.white);
            else g.setColor(Color.gray);
            g.drawString(MENUOPTIONS[1],10,50);
            if(menuIndex==2) g.setColor(Color.white);
            else g.setColor(Color.gray);
            g.drawString(MENUOPTIONS[2],10,80);

    }

    private void drawPauseMenu(Graphics g){

            g.setColor(Color.black);
            g.fillRect(0,0,120,140);
            if(pauseIndex==0) g.setColor(Color.white);
            else g.setColor(Color.gray);
            g.drawString(PAUSEOPTIONS[0],10,20);
            if(pauseIndex==1) g.setColor(Color.white);
            else g.setColor(Color.gray);
            g.drawString(PAUSEOPTIONS[1],10,50);
            if(pauseIndex==2) g.setColor(Color.white);
            else g.setColor(Color.gray);
            g.drawString(PAUSEOPTIONS[2],10,80);
            if(pauseIndex==3) g.setColor(Color.white);
            else g.setColor(Color.gray);
            g.drawString(PAUSEOPTIONS[3],10,110);

        }

    private void drawOptions(Graphics g){

            g.setColor(Color.black);
            g.fillRect(0,0,120,140);
            g.setColor(Color.white);
            if(optionIndex==0) g.setColor(Color.white);
            else g.setColor(Color.gray);
            g.drawString(DIFFICULTIES[Difficulty],10,20);
            if(optionIndex==1) g.setColor(Color.white);
            else g.setColor(Color.gray);
            g.drawString(OPTIONS[1]+eCount,10,50);
            if(optionIndex==2) g.setColor(Color.white);
            else g.setColor(Color.gray);
            g.drawString(OPTIONS[2],10,80);

        }

    void managePlayer(){

        p.move();
        if(p.fireReady())
            pB.append(p.fire(mx,my));

    }

    void manageBullets(){

        pB.first();
        while(pB.getData()!=null){
          
            pB.getData().move();
            for(int i=0;i<e.length;i++){
                
                if((e[i].getX()-e[i].getR()<pB.getData().getX()+pB.getData().getR()&&
                e[i].getX()+e[i].getR()>pB.getData().getX()-pB.getData().getR())&&
                (e[i].getY()-e[i].getR()<pB.getData().getY()+pB.getData().getR()&&
                e[i].getY()+e[i].getR()>pB.getData().getY()-pB.getData().getR())){

                    ++points;
                    pB.remove();
                    e[i].reset();

                }

            }

        pB.next();
          
        }

    }

    void manageEnemies(){

        for(int i=0;i<e.length;i++){

            if(Difficulty>Enemy.IMMOBILE){
            
                if(System.nanoTime()-time>1000000000L*(Enemy.IMPOSSIBLE-e[i].getDifficulty())/10.0){
            
                    x=p.getX();
                    y=p.getY();
                    time=System.nanoTime();
            
                }

                e[i].AI(x,y,p.getR());
                e[i].move();

            }
            
            if((e[i].getX()-e[i].getR()<p.getX()+p.getR()&&
                e[i].getX()+e[i].getR()>p.getX()-p.getR())&&
                (e[i].getY()-e[i].getR()<p.getY()+p.getR()&&
                e[i].getY()+e[i].getR()>p.getY()-p.getR())){

                p.reset();
                e[i].reset();
                
            }
                
        }

    }

    private void runState(){
    
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

        managePlayer();
        manageBullets();
        manageEnemies();
             
        repaint();
            
        try{
            t.sleep(20);
        }catch(InterruptedException e){}
        
        Thread.currentThread().setPriority(Thread.NORM_PRIORITY);

    }

    public void paint(Graphics g){
    
        if(state==States.RUNNING){

            if(g==null) return;
            g.setColor(Color.gray);
            g.drawString("Defeated "+points+" enemies.",10+getInsets().left,28+getInsets().top);
            p.paint(g);
            for(int i=0;i<e.length;i++)
                e[i].paint(g);
            pB.first();
            while(pB.getData()!=null){
                pB.getData().paint(g);
                pB.next();
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
                &&pauseIndex<PAUSEOPTIONS.length-1){

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
                    e=new Enemy[eCount];
                    for(int i=0;i<e.length;i++)
                        e[i]=new Enemy(getSize(),getInsets(),Difficulty);
                    pB=new LinkedList<Bullet>();
                    p.reset();
                    prevState=state;
                    state=States.RUNNING;
                    pauseIndex=0;

                }
                else System.exit(0);

            }

        }
        else if(state==States.OPTION){

            if(ke.getKeyCode()==KeyEvent.VK_ESCAPE){

                if(prevState==States.MENU&&eCount!=e.length){

                    e=new Enemy[eCount];
                    for(int i=0;i<e.length;i++)
                        e[i]=new Enemy(getSize(),getInsets(),Difficulty);

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
            if(ke.getKeyCode()==KeyEvent.VK_S&&optionIndex<OPTIONS.length-1){

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

                for(int i=0;i<e.length;i++){
                    e[i].setDifficulty(Difficulty);
                }

                if(prevState==States.MENU&&eCount!=e.length){

                    e=new Enemy[eCount];
                    for(int i=0;i<e.length;i++)
                        e[i]=new Enemy(getSize(),getInsets(),Difficulty);

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
                &&menuIndex<MENUOPTIONS.length-1){

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
