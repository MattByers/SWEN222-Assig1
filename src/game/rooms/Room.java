package game.rooms;

import game.squares.*;

import java.util.ArrayList;

public abstract class Room {

	private final String roomName;
	private ArrayList<RoomSquare> squareList = new ArrayList<>();
	private Room stairs = null;
	private ArrayList<DoorSquare> doorList = new ArrayList<>();

	public Room(String name){
		this.roomName = name;
	}
	
	//Getters
	public String getName(){return this.roomName;}
	public ArrayList<RoomSquare> getSquareList(){return this.squareList;}
	public ArrayList<DoorSquare> getDoorList(){return this.doorList;}
	public Room getStairs(){return this.stairs;}

	//Setters
	public void addSquare(RoomSquare s){
		this.squareList.add(s);
	}

	public void addDoor(DoorSquare door){
		this.doorList.add(door);
	}
	
	public void addStairs(Room stairs){
		this.stairs = stairs;
	}
}
