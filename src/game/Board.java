package game;

import game.squares.*;

import java.io.File;


import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Board {
	private static final int width = 24;
	private static final int height = 25;
	public Square[][] board = new Square[width][height];

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
	}

	private Square[][] parseBoard(Scanner sc) {
		Square[][] returnBoard;

		returnBoard = parseSquares(sc);

		return returnBoard;
	}

	private Square[][] parseSquares(Scanner sc) {
		//This looks like aids! Please HALP! Teach me to regex LOL
		String next;
		Square[][] board = new Square[width][height];
		final Pattern numbers = Pattern.compile("[0-9]+");

		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				next = sc.next();
				if(next.equals(" ")){
					next = sc.next();
				}

				switch(next){
				case "#": board[i][j] = new Wall(i,j);
				break;
				case "K": board[i][j] = new Room(i,j,'K', "Kitchen");
				break;
				case "-": board[i][j] = new Walkway(i,j);
				break;
				case "B": board[i][j] = new Room(i,j,'B', "Ballroom");
				break;
				case "C":board[i][j] = new Room(i,j,'C', "Conservatory");
				break;
				case "D":board[i][j] = new Room(i,j,'D', "Dining Room");
				break;
				case "G":board[i][j] = new Room(i,j,'G', "Billiard Room");
				break;
				case "L":board[i][j] = new Room(i,j,'L', "Library");
				break;
				case "T":board[i][j] = new Room(i,j,'T', "Lounge");
				break;
				case "H":board[i][j] = new Room(i,j,'H', "Hall");
				break;
				case "S":board[i][j] = new Room(i,j,'S', "Study");
				break;
				case "<":board[i][j] = new Door(i,j, '<');
				break;
				case ">":board[i][j] = new Door(i,j,'>');
				break;
				case "v":board[i][j] = new Door(i,j,'v');
				break;
				case "^":board[i][j] = new Door(i,j,'^');
				break;
				default: board[i][j] = new PlayerSpawn(i,j,'6');
				break;
				}
			}
		}
		return board;
	}

	public void printToConsole(){
		for(int i = 0; i < 24; i++){
			for (int j = 0; j < 25; i++){
				System.out.print(board[i][j].getID());
			}
			System.out.println();
		}
	}	

	public static void main(String[] args){
		new Board();
	}
}
