package game_renderer;

//import game_engine.GameState;
//import game_engine.Unit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel; // .swing.JPanel;

import stratego_engine.GameState;
import stratego_engine.MapTile;

public class StrategoGraphicsPanel extends JPanel {
	
	private GameState gs;
	
	public StrategoGraphicsPanel(GameState gs){
		this.setBackground(Color.BLACK);
	//	this.setLayout(new BorderLayout());
		this.setSize(new Dimension(600,600));
		
		this.gs = gs;
		
		
	}
	
	public void updateGS(GameState gs){
		this.gs = gs;
	}
	
	public void paintComponent(Graphics g) {
        super.paintComponent(g);

      //  int[] map = this.gs.getMap();
        
        MapTile[] map = this.gs.getMap();
        
          
        // draw the game board here
        for(int i=0;i<map.length;i++){
        	
        	// if the tile is occupied, draw the unit
        	if(map[i].isTraversible()){
        		g.setColor(Color.GREEN);
        	}
        	else{
        		g.setColor(Color.BLUE);
        	}
        	
        //	g.fillRect((i%10)*40, (i/10)*60, 40, 60);
        	g.fill3DRect((i%10)*40, (i/10)*40, 40, 40, true);
        	
        }
        
        // draw the game pieces
        for(int i=0;i<map.length;i++){
        	if(map[i].isOccupied()){
        		
        		// IF piece is alive
        		if(this.gs.getPiece(map[i].getOccupyingPiece()).isAlive()){
        			
        		
	        		// if piece belongs to player 1
	        		if(this.gs.getPiece(map[i].getOccupyingPiece()).getPlayerID()==1){
	        			g.setColor(Color.ORANGE);
	        			// if piece is hidden
	        			if(this.gs.getPiece(map[i].getOccupyingPiece()).isHidden()){
		        			g.setColor(Color.RED);
	        			}
	        		}
	        		// if piece belongs to player 2
	        		if(this.gs.getPiece((map[i].getOccupyingPiece())).getPlayerID()==2){
	        			g.setColor(Color.DARK_GRAY);
	        			if(this.gs.getPiece(map[i].getOccupyingPiece()).isHidden()){
		        			g.setColor(Color.GRAY);

	        			}
	        		}
	        		
	        		// draw rect
	            	g.fill3DRect(((i%10)*40)+2, ((i/10)*40)+3, 35, 35, true);

	        		
	        		// write text over it
	        		g.setColor(Color.WHITE);
	        		g.drawString( Integer.valueOf(this.gs.getPiece(map[i].getOccupyingPiece()).getRank()).toString(), ((i%10)*40)+17, ((i/10)*40)+25);

        		}
        	}
        }
        
        /*
        
        int xStart = 30;
        int yStart = 30;
        int gap = 5;
        int length = 40;
	//	g.setColor(Color.RED);

        // Draw game board
        for(int x=0;x<10;x++){
        	for(int y=0;y<10;y++){
        		if(map[(y*10)+x]==1){
        			g.setColor(Color.CYAN);
        		}
        		else{
        			g.setColor(Color.GREEN);
        		}
            	g.fillRect(xStart+ x + (x*(length+gap)) ,yStart + y+(y*(length+gap)), length, length);
        	}
        	
        }
        
        // Draw the Units on the game board.
        ArrayList<Unit> units = gs.getUnits();
    //    System.out.println(units.get(0).getPlayerID());
        
        for(Unit u:units){
        	if(u.getAlive()){
	        	if(u.getPlayerID()==1){
	        		if(u.isHidden()){
	        			g.setColor(Color.BLUE);
	        		}
	        		else{
	            		g.setColor(Color.RED);
	        		}
	        		g.fillOval(xStart + (9-u.getX()) + ((9-u.getX())*(length+gap)), yStart+u.getY()+(u.getY()*(length+gap)), length, length);
	        		g.setColor(Color.WHITE);
	        		if(u.getRank()<=GameState.SERGEANT){
	        			g.drawString(Integer.toString(u.getRank()), xStart+ (length/2)+ (9-u.getX()) + ((9-u.getX())*(length+gap)), yStart+(length/2)+u.getY()+(u.getY()*(length+gap)));

	        		}
	        		// Modify DrawString to have alphabet for special characters
	        		if(u.getRank()==GameState.MINER){
		        		g.drawString("M", xStart+ (length/2)+ (9-u.getX()) + ((9-u.getX())*(length+gap)), yStart+(length/2)+u.getY()+(u.getY()*(length+gap)));

	        		}
	        		if(u.getRank()==GameState.SCOUT){
		        		g.drawString("Sc", xStart+ (length/2)+ (9-u.getX()) + ((9-u.getX())*(length+gap)), yStart+(length/2)+u.getY()+(u.getY()*(length+gap)));

	        		}
	        		if(u.getRank()==GameState.SPY){
		        		g.drawString("Sp", xStart+ (length/2)+ (9-u.getX()) + ((9-u.getX())*(length+gap)), yStart+(length/2)+u.getY()+(u.getY()*(length+gap)));

	        		}
	        		if(u.getRank()==GameState.BOMB){
		        		g.drawString("B", xStart+ (length/2)+ (9-u.getX()) + ((9-u.getX())*(length+gap)), yStart+(length/2)+u.getY()+(u.getY()*(length+gap)));

	        		}
	        		if(u.getRank()==GameState.FLAG){
		        		g.drawString("F", xStart+ (length/2)+ (9-u.getX()) + ((9-u.getX())*(length+gap)), yStart+(length/2)+u.getY()+(u.getY()*(length+gap)));

	        		}

	        	}
	        	if(u.getPlayerID()==2){
	        		if(u.isHidden()){
	        			g.setColor(Color.BLACK);
	        		}
	        		else{
	        			g.setColor(Color.YELLOW);
	        		}
	        		g.fillOval(xStart + (u.getX()) + (u.getX()*(length+gap)), yStart+(9-u.getY())+((9-u.getY())*(length+gap)), length, length);
	        		g.setColor(Color.WHITE);
	        		if(u.getRank()<=GameState.SERGEANT){
	    	        	g.drawString(Integer.toString(u.getRank()), xStart + (length/2)+ (u.getX()) + (u.getX()*(length+gap)), yStart+ (length/2)+(9-u.getY())+((9-u.getY())*(length+gap)));

	        		}
	        		// Modify DrawString to have alphabet for special characters
	        		if(u.getRank()==GameState.MINER){
	    	        	g.drawString("M", xStart + (length/2)+ (u.getX()) + (u.getX()*(length+gap)), yStart+ (length/2)+(9-u.getY())+((9-u.getY())*(length+gap)));

	        		}
	        		if(u.getRank()==GameState.SCOUT){
	    	        	g.drawString("Sc", xStart + (length/2)+ (u.getX()) + (u.getX()*(length+gap)), yStart+ (length/2)+(9-u.getY())+((9-u.getY())*(length+gap)));

	        		}
	        		if(u.getRank()==GameState.SPY){
	    	        	g.drawString("Sp", xStart + (length/2)+ (u.getX()) + (u.getX()*(length+gap)), yStart+ (length/2)+(9-u.getY())+((9-u.getY())*(length+gap)));

	        		}
	        		if(u.getRank()==GameState.BOMB){
	    	        	g.drawString("B", xStart + (length/2)+ (u.getX()) + (u.getX()*(length+gap)), yStart+ (length/2)+(9-u.getY())+((9-u.getY())*(length+gap)));

	        		}
	        		if(u.getRank()==GameState.FLAG){
	    	        	g.drawString("F", xStart + (length/2)+ (u.getX()) + (u.getX()*(length+gap)), yStart+ (length/2)+(9-u.getY())+((9-u.getY())*(length+gap)));

	        		}
	        	//	g.drawString(Integer.toString(u.getRank()), xStart + (length/2)+ (u.getX()) + (u.getX()*(length+gap)), yStart+ (length/2)+(9-u.getY())+((9-u.getY())*(length+gap)));
	
	        		
	        	}
	        	
        	}
        	
        	
        } */
   
    }
	
}