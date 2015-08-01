package game.squares;

public class Room extends Square{
	
	private String roomName;
	
	public Room(int x, int y, char ID, String name) {
		super(x, y, ID);
		this.roomName = name;
	}

	public String getName(){
		return this.roomName;
	}
}
