/**
 * A simple game menu management class.
 * @author Xunxen Xyto
 */

import java.awt.Color;
import java.awt.Graphics;

public class GameMenu{
	
	private String[] items;
	private int index;
	private int count;
	private Color text;
	private Color bg;
	private Color selected;
	public Methods menuActions;
	
	/**
	 * 
	 * Initializes a new game menu with the specified menu options.
	 *  @param entries
	 *    Array of text entries to add to this menu.
	 */
	public GameMenu(Methods a, String... entries){
		
		items=entries.clone();
		index=0;
		count=items.length;
		text=Color.gray;
		bg=Color.black;
		selected=Color.white;
		menuActions=a;
		
		
	}
	
	public void run(){
		menuActions.run(index);
	}
	
	/**
	 * Draws this menu to the given graphics object
	 * @param g 
	 *   The graphics object to draw to.
	 */
	public void draw(Graphics g){
		
		g.setColor(bg);
        g.fillRect(0,0,120,140);
        for(int i=0;i<count;i++){
        	
	        if(index==i) g.setColor(selected);
	        else g.setColor(text);
	        g.drawString(items[i],10,30*i+20);
        
        }
      
		
	}
	
	/**
	 * Moves the index to the next menu option, if there is one.
	 */
	public void next(){
		
		index=(index<count-1)?++index:index;
		
	}
	
	/**
	 * Moves the index to the previous menu option, if there is one.
	 */
	public void prev(){
		
		index=(index>0)?--index:index;
		
	}
	
	/**
	 * Sets the color in which to draw unselected menu text.
	 * @param c Color to draw unselected text in.
	 */
	public void setTextColor(Color c){
		
		text=c;
		
	}
	
	/**
	 * Sets the color in which to draw selected menu text.
	 * @param c Color to draw selected text in.
	 */
	public void setSelectedColor(Color c){
		
		selected=c;
		
	}
	
	/**
	 * Sets the menu bacground color.
	 * @param c Color to draw menu background in.
	 */
	public void setBGColor(Color c){
		
		bg=c;
		
	}
	
	public void first(){
		
		index=0;
		
	}
	
	public int getIndex(){
		
		return index;
		
	}
	
	public void setItems(String... s){
		
		items=s.clone();
		count=items.length;
		
	}
	
}