package Squares;

public class Door extends Square{

	private String dir;
	
	public Door(int x, int y, String dir) {
		super(x, y, 'D');
		this.dir = dir;
	}


}
