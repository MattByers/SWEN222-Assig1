package game.squares;

public class Square {
	private int XLoc;
	private int YLoc;
	private char ID;

	public Square(int x, int y, char ID){
		this.XLoc = x;
		this.YLoc = y;
		this.ID = ID;
	}		
	
	public int getX(){return this.XLoc;}
	
	public int getY(){return this.YLoc;}
	
	public char getID(){return this.ID;}
}

