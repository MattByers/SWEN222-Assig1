package game.rooms;

import game.squares.*;

import java.util.ArrayList;

public class Room {

	private String roomName;
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
	public void addSquare(int x, int y, char ID, String name){
		this.squareList.add(new RoomSquare(x,y,ID,name));
	}

	public void addDoor(int x, int y, char ID){
		this.doorList.add(new DoorSquare(x,y,ID));
	}
	
	public void addName(String name){
		this.roomName = name;
	}
}
