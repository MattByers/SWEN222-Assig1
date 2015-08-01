package game;

import game.cards.PersonCard;
import game.rooms.*;
import game.squares.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Player {
	
	private static final String MOVE = "move";
	private static final String SHOW_CARDS = "show cards";
	private static final String END = "end";
	private static final String STAIRS = "use stairs";
	private static final String SUGGEST = "suggestion";
	private static final String ACCUSE = "accusation";
	private static final String LEAVE_ROOM = "leave room";
	private static final ArrayList<String> NORMAL_ACTIONS = new ArrayList<String>(Arrays.asList(MOVE, SHOW_CARDS, END, ACCUSE));
	private static final ArrayList<String> ROOM_ACTIONS = new ArrayList<String>(Arrays.asList(SUGGEST, LEAVE_ROOM));
	private static final ArrayList<String> DIR_LIST = new ArrayList<String>(Arrays.asList("u", "d", "l", "r"));
	
	private int playerNum;
	private PersonCard identity;
	private ArrayList<Card> hand = new ArrayList<Card>();
	private Square location;
	private Room room;
	private int diceRoll;
	private boolean finished;
	private Scanner input;
	private Board board;
	private ArrayList<String>possibleActions = new ArrayList<String>();
	
	public Player(PersonCard identity, int playerNum, Board board){
		this.identity = identity;
		this.playerNum = playerNum;
		this.board = board;
		this.location = this.board.getSpawnList().get(this.playerNum - 1);
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
		
		this.possibleActions.addAll(NORMAL_ACTIONS);
		if(this.room != null) {
			this.possibleActions.addAll(ROOM_ACTIONS);
			if(this.room.getStairs() != null) this.possibleActions.add(STAIRS);
		}
		
		GameOfCluedo.clearConsole();
		
		System.out.printf("Player %d, it is your turn!\n", this.playerNum);

		rollDice();
		
		while(!this.finished){
			System.out.print("What would you like to do? Your options are: ");
			for(String action : this.possibleActions){
				System.out.print("{" + action + "} ");
			}
			String currentAction = input.nextLine();
			if(!this.possibleActions.contains(currentAction)){
				System.out.println("That is an invalid action. Please try again.");
				continue;
			}
			switch(currentAction){
			case MOVE:
				movePlayer();
				break;
			case SHOW_CARDS:
				showCards();
				break;
			case ACCUSE:
				accusation();
				break;
			case END:
				endTurn();
				break;
			case SUGGEST:
				suggestion();
				break;
			case LEAVE_ROOM:
				leaveRoom();
				break;
			case STAIRS:
				useStairs();
				break;
			default:
				break;
			}
		}
	}
	
	private void leaveRoom() {
		this.location = this.room.getDoorList().get(0);
		this.room = null;
	}

	private void useStairs() {
		// TODO Auto-generated method stub
		
	}

	private void suggestion() {
		// TODO Auto-generated method stub
		
	}

	private void accusation() {
		// TODO Auto-generated method stub
		
	}

	private void showCards(){
		
		System.out.println("WARNING: About to show cards, other players look away now!");
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			System.out.println("Exception " + e + " found in sleep.");
		}
		
		for(Card c : this.hand){
			System.out.print(c.getName() + ", ");
		}
		System.out.println("");
	}
	
	private void endTurn(){
		this.finished = true;
	}
	
	//THIS METHOD IS BROKE TO FUCK
	private void movePlayer(){
		while(this.diceRoll > 0){
			System.out.printf("You have %d moves left, which direction would you like to move? (u,d,l,r): ", this.diceRoll);
			String direction = input.next();
			if(DIR_LIST.contains(direction) && this.board.checkMove(this, direction)){
				//this.location = this.board.playerMove(this, direction);
				if(this.location instanceof DoorSquare){
					this.room = ((DoorSquare)this.location).getRoom();
					this.location = this.room.getSquareList().get(0);
				}
				this.diceRoll--;
			}
			else{
				System.out.println("That is an invalid direction. Please try again.");
				continue;
			}
		}
		this.possibleActions.remove(MOVE);
	}
	
	public Square getLocation(){
		return this.location;
	}
}
