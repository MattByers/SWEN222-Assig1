package game;

import game.cards.PersonCard;
import game.squares.Square;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Player {
	
	private static final String MOVE = "move";
	private static final String SHOW_CARDS = "show cards";
	private static final String END = "end";
	private static final ArrayList<String> ACTION_LIST = new ArrayList<String>(Arrays.asList(MOVE, SHOW_CARDS, END));
	private static final ArrayList<String> DIR_LIST = new ArrayList<String>(Arrays.asList("U", "D", "L", "R"));
	
	private int playerNum;
	private PersonCard identity;
	private ArrayList<Card> hand = new ArrayList<Card>();
	private Square location;
	private int diceRoll;
	private boolean finished;
	private Scanner input;
	private Board board;
	
	public Player(PersonCard identity, int playerNum, Board board){
		this.identity = identity;
		this.playerNum = playerNum;
		this.board = board;
	}
	
	public void giveCard (Card card){
		this.hand.add(card);
	}
	
	public void rollDice(){
		this.diceRoll = (int)(Math.random() * 6) + (int)(Math.random() * 6);
		System.out.println("You rolled a " + this.diceRoll);
	}

	public void takeTurn(Scanner input) {
		this.input = input;
		this.finished = false;
		
		System.out.printf("Player %d (%S), it is your turn!\n", this.playerNum, this.identity.getName());
		System.out.println("WARNING: Others players look away now");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			System.out.println("Exception " + e + " found in sleep.");
		}
		rollDice();
		
		while(!this.finished){
			System.out.print("What would you like to do? Your options are ");
			System.out.println("nothing!");
			movePlayer();
			endTurn();
		}
	}
	
	private void showCards(){
		for(Card c : this.hand){
			System.out.print(c.getName() + ", ");
		}
		System.out.println("");
	}
	
	private void endTurn(){
		this.finished = true;
	}
	
	private void movePlayer(){
		for(this.diceRoll = this.diceRoll; this.diceRoll > 0; this.diceRoll--){
			System.out.printf("You have %d moves left, which direction would you like to move? (L,R,U,D): ", this.diceRoll);
			String direction = input.next();
			if(!DIR_LIST.contains(direction) || this.board.checkMove(this, direction)){
				System.out.println(input + " is and invalid direction. Please try again.");
				continue;
			}
			System.out.println("Too fucking bad");
			this.diceRoll = 0;
		}
	}
}
