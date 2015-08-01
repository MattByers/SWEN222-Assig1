package game.squares;

public class RoomSquare extends Square{
	
	private String roomName;
	
	public RoomSquare(int x, int y, char ID, String name) {
		super(x, y, ID);
		this.roomName = name;
	}

	public String getName(){
		return this.roomName;
	}
}
