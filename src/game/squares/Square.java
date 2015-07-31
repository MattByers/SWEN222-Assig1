package game.squares;

public class Square {
	private int XLoc;
	private int YLoc;
	private char name;

	public Square(int x, int y, char name){
		this.XLoc = x;
		this.YLoc = y;
		this.name = name;
	}		
	
	public int getX(){return this.XLoc;}
	
	public int getY(){return this.YLoc;}
	
	public char getName(){return this.name;}
}

