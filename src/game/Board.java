package game;

import game.squares.Square;
import game.squares.Walkway;

import java.io.File;


import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Board {
	private static final int width = 24;
	private static final int height = 25;
	private Square[][] board = new Square[width][height];
	private Map<Character, Square> squareSetMap = new HashMap<>();

	public Board (){
		String boardFile = "gameBoard.txt";

		try{
			Scanner sc = new Scanner(new File(boardFile));
			this.board = parseBoard(sc);
		}
		catch(FileNotFoundException e){
			System.out.println("No file found with name " + boardFile);
		}
	}

	private Square[][] parseBoard(Scanner sc) {
		Square[][] returnBoard;
		squareSetMap = parseKeys(sc);

		returnBoard = parseSquares(sc);

		return returnBoard;
	}

	private Map<Character, Square> parseKeys(Scanner sc) {
		// TODO Fill this shit
		return null;
	}

	private Square[][] parseSquares(Scanner sc) {
		//This looks like aids! Please HALP! Teach me to regex LOL
		String next;
		Square[][] board = new Square[width][height];

		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				next = sc.next();
				if(next.equals(" ")){
					next = sc.next();
				}
				
				board[i][j] = squareSetMap.get(next);
			}
		}
		return board;
	}


}
