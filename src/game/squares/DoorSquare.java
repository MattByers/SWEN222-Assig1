package game.squares;

import game.rooms.*;

public class DoorSquare extends Square{

	private Room room = null;
	
	public DoorSquare(int x, int y, char ID) {
		super(x, y, ID);
	}

	public void addRoom(Room room){
		this.room = room;
	}
}
