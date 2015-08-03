package game.squares;

import game.Player;

public class Square {
	private int XLoc;
	private int YLoc;
	private char ID;
	private Player player = null;

	public Square(int x, int y, char ID){
		this.XLoc = x;
		this.YLoc = y;
		this.ID = ID;
	}		
	
	//Getters	
	public int getX(){return this.XLoc;}
	public int getY(){return this.YLoc;}
	public char getID(){return this.ID;}
	public Player getPlayer(){return this.player;}
	
	//Setters
	public void setPlayer(Player p){this.player = p;}
}

