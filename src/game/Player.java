package game;

import game.cards.ItemCard;
import game.cards.PersonCard;
import game.cards.RoomCard;
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
	private ArrayList<Card> hand;
	private Square location;
	private Room room;
	private int diceRoll;
	private boolean finished;
	private Scanner input;
	private GameOfCluedo game;
	private Board board;
	private ArrayList<String> possibleActions;
	
	public Player(PersonCard identity, int playerNum, Board board, GameOfCluedo game){
		this.game = game;
		this.board = board;
		
		this.identity = identity;
		this.playerNum = playerNum;
		
		this.location = this.board.addPlayer(this);
		this.hand = new ArrayList<Card>();
	}
	
	public int getPlayerNum() {
		return playerNum;
	}

	public void giveCard (Card card){
		this.hand.add(card);
	}

	public void takeTurn(Scanner input) {
		this.input = input;
		this.finished = false;
		this.possibleActions = new ArrayList<String>();
		
		this.possibleActions.addAll(NORMAL_ACTIONS);
		if(this.room != null) {
			this.possibleActions.addAll(ROOM_ACTIONS);
			if(this.room.getStairs() != null) this.possibleActions.add(STAIRS);
		}
		
		GameOfCluedo.clearConsole();
		
		System.out.printf("Player %d, it is your turn!\n", this.playerNum);

		rollDice();
		
		while(!this.finished){
			if(this.room != null && ! this.possibleActions.contains(ROOM_ACTIONS)) {
				this.possibleActions.addAll(ROOM_ACTIONS);
				if(this.room.getStairs() != null) this.possibleActions.add(STAIRS);
			}
			
			System.out.print("What would you like to do? Your options are: ");
			for(String action : this.possibleActions){
				System.out.print("{" + action + "} ");
			}
			
			
			String currentAction = input.nextLine();
			System.out.println(currentAction);
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
	
	private void rollDice(){
		this.diceRoll = (int)(Math.random() * 6) + (int)(Math.random() * 6);
		this.diceRoll = 20; //DEBUG line
		System.out.println("You rolled a " + this.diceRoll);
	}
	
	private void leaveRoom() {
		this.location = this.room.getDoorList().get(0);
		this.room = null;
		this.possibleActions.removeAll(ROOM_ACTIONS);
		this.possibleActions.remove(STAIRS);
	}

	private void useStairs() {
		this.room = this.room.getStairs();
		System.out.printf("You have used the stairs and are now in the %s.", this.room.getName());
	}

	private void suggestion() {
		// TODO Auto-generated method stub
		
	}

	private void accusation() {
		System.out.println("Are you sure you want to make an accusation?");
		System.out.println("If you are wrong, you will be out of the game! (Y or N)");
		if(!this.input.nextLine().equals("Y")) return;
		
		System.out.println("Who would you like to accuse? Your options are: (1-6)");
		for(int i = 0; i < this.game.getPersonCards().size(); i++){
			System.out.printf("%s : %d", this.game.getPersonCards().get(i), i+1);
		}
		int personInd = Integer.parseInt(this.input.nextLine());
		
		System.out.println("With which item?? Your options are: (1-6)");
		for(int i = 0; i < this.game.getItemCards().size(); i++){
			System.out.printf("%s : %d", this.game.getItemCards().get(i), i+1);
		}
		int itemInd = Integer.parseInt(this.input.nextLine());
		
		System.out.println("In which room??? Your options are: (1-9)");
		for(int i = 0; i < this.game.getRoomCards().size(); i++){
			System.out.printf("%s : %d", this.game.getRoomCards().get(i), i+1);
		}
		int roomInd = Integer.parseInt(this.input.nextLine());
		
		PersonCard person = this.game.getPersonCards().get(personInd);
		ItemCard item = this.game.getItemCards().get(itemInd);
		RoomCard room = this.game.getRoomCards().get(roomInd);
		
		System.out.printf("Player %d, your accusation of %s, %s, %s was incorrect.");
		System.out.printf("You are out of the game!");
		this.finished = true;
		this.game.retirePlayer(this);
		
	}

	private void showCards(){
		
		System.out.println("WARNING: About to show your cards, other players look away now!");
		
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
			this.board.printToConsole();
			System.out.println("");
			System.out.printf("You have %d moves left, which direction would you like to move? (u,d,l,r): ", this.diceRoll);
			String direction = input.next();
			if(DIR_LIST.contains(direction) && this.board.checkMove(this, direction)){
				this.location = this.board.playerMove(this, direction);
				if(this.location instanceof DoorSquare){
					this.room = ((DoorSquare)this.location).getRoom();
					this.location = this.room.getSquareList().get(this.room.getSquareList().size() / 2);
					System.out.printf("new location: %d, %d\n", this.location.getX(), this.location.getY());
					System.out.printf("You have entered the %s\n", this.room.getName());
					this.board.printToConsole();
					return;
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
