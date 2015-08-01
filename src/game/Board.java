package game;

import game.rooms.*;
import game.squares.*;

import java.io.File;


import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Board {
	private static final int width = 24;
	private static final int height = 25;
	public Square[][] board = new Square[width][height];
	private ArrayList<Room> roomList = new ArrayList<>();
	private ArrayList<Square> spawnList = new ArrayList<>();
	private ArrayList<Square> doorList = new ArrayList<>();


	public Board (){
		String boardFile = "src/gameBoard.txt";

		try{
			Scanner sc = new Scanner(new File(boardFile));
			this.board = parseBoard(sc);
		}
		catch(FileNotFoundException e){
			System.out.println("No file found with name " + boardFile);
		}

		printToConsole();
		addAndLinkRooms();
	}

	private Square[][] parseBoard(Scanner sc) {
		Square[][] returnBoard;

		returnBoard = parseSquares(sc);

		return returnBoard;
	}

	private Square[][] parseSquares(Scanner sc) {
		String next;
		Square[][] board = new Square[height][width];

		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				next = sc.next();
				if(next.equals(" ")){
					next = sc.next();
				}

				switch(next){
				case "#": board[i][j] = new WallSquare(i,j);
				break;
				case "K": board[i][j] = new RoomSquare(i,j, 'K', "Kitchen");
				break;
				case "-": board[i][j] = new WalkwaySquare(i,j);
				break;
				case "B": board[i][j] = new RoomSquare(i,j,'B', "Ballroom");
				break;
				case "C":board[i][j] = new RoomSquare(i,j,'C', "Conservatory");
				break;
				case "D":board[i][j] = new RoomSquare(i,j,'D', "Dining Room");
				break;
				case "G":board[i][j] = new RoomSquare(i,j,'G', "Billiard Room");
				break;
				case "L":board[i][j] = new RoomSquare(i,j,'L', "Library");
				break;
				case "T":board[i][j] = new RoomSquare(i,j,'T', "Lounge");
				break;
				case "H":board[i][j] = new RoomSquare(i,j,'H', "Hall");
				break;
				case "S":board[i][j] = new RoomSquare(i,j,'S', "Study");
				break;
				case "<":board[i][j] = new DoorSquare(i,j, '<');
				break;
				case ">":board[i][j] = new DoorSquare(i,j,'>');
				break;
				case "v":board[i][j] = new DoorSquare(i,j,'v');
				break;
				case "^":board[i][j] = new DoorSquare(i,j,'^');
				break;
				default: board[i][j] = new PlayerSpawnSquare(i,j,next.charAt(0));
				break;
				}
			}
		}
		return board;
	}

	public void printToConsole(){
		for(int i = 0; i < height; i++){
			for(int j = 0; j < width; j++){
				System.out.print(board[i][j].getID());
			}
			System.out.println();
		}
	}	

	public void addAndLinkRooms(){		
		//Add all rooms to list
		roomList.add(new Ballroom());
		roomList.add(new DiningRoom());
		roomList.add(new BilliardRoom());
		roomList.add(new Library());
		roomList.add(new Hall());
		roomList.add(new Kitchen());
		roomList.add(new Study());
		roomList.add(new Lounge());
		roomList.add(new Conservatory());
		
		//Link stairs to necesarry rooms
		getRoom("Kitchen").addStairs(getRoom("Study"));
		getRoom("Study").addStairs(getRoom("Kitchen"));
		getRoom("Lounge").addStairs(getRoom("Conservatory"));
		getRoom("Conservatory").addStairs(getRoom("Lounge"));
	}
	
	public Room getRoom(String name){
		for(Room r : roomList){
			if(r.getName().equals(name)){
				return r;
			}
		}
		return null;
	}
	
	public boolean checkMove(Player p, String dir){
		Square current = p.getLocation();
		switch(dir){
		case "u": return (board[current.getX()-1][current.getY()] instanceof DoorSquare) || (board[current.getX()-1][current.getY()] instanceof WalkwaySquare);
		case "d": return (board[current.getX()+1][current.getY()] instanceof DoorSquare) || (board[current.getX()+1][current.getY()] instanceof WalkwaySquare);
		case "l": return (board[current.getX()][current.getY()-1] instanceof DoorSquare) || (board[current.getX()][current.getY()-1] instanceof WalkwaySquare);
		case "r": return (board[current.getX()][current.getY()+1] instanceof DoorSquare) || (board[current.getX()][current.getY()+1] instanceof WalkwaySquare);
		default:
			return false;
		}
	}

	public static void main(String[] args){
		new Board();
	}
}
