package game;

import game.cards.ItemCard;
import game.cards.PersonCard;
import game.cards.RoomCard;
import game.rooms.*;
import game.squares.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class Player {
	
	private static final String MOVE = "move";
	private static final String SHOW_CARDS = "show cards";
	private static final String END = "end";
	private static final String STAIRS = "use stairs";
	private static final String SUGGEST = "suggestion";
	private static final String ACCUSE = "accusation";
	private static final String LEAVE_ROOM = "leave room";
	private static final String SHOW_ENVELOPE = "show envelope";
	private static final ArrayList<String> NORMAL_ACTIONS = new ArrayList<String>(Arrays.asList(MOVE, SHOW_CARDS, END, ACCUSE));
	private static final ArrayList<String> ROOM_ACTIONS = new ArrayList<String>(Arrays.asList(SUGGEST, LEAVE_ROOM));
	private static final ArrayList<String> DEBUG = new ArrayList<String>(Arrays.asList(SHOW_ENVELOPE));
	private static final ArrayList<String> DIR_LIST = new ArrayList<String>(Arrays.asList("u", "d", "l", "r"));
	
	private int playerNum;
	private PersonCard identity;
	private ArrayList<Card> hand;
	private Square location;
	private Room room;
	private int diceRoll;
	private boolean finished;
	private boolean retired;
	private Scanner input;
	private GameOfCluedo game;
	private Board board;
	private HashSet<String> possibleActions;
	
	public Player(PersonCard identity, int playerNum, Board board, GameOfCluedo game){
		this.game = game;
		this.board = board;
		this.retired = false;
		
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
	
	/**
	 * Allows the player to take a turn, switching over all their possible actions (actions they are allowed dependent on their scenario)
	 * until they end their turn. Uses scanner as its method for user input.
	 * 
	 * @param Scanner input
	 */
	public void takeTurn(Scanner input) {
		this.input = input;
		this.finished = false;
		this.possibleActions = new HashSet<String>();
		
		this.possibleActions.addAll(NORMAL_ACTIONS);
		if(this.room != null) {
			this.possibleActions.addAll(ROOM_ACTIONS);
			if(this.room.getStairs() != null) this.possibleActions.add(STAIRS);
		}
		
		this.possibleActions.addAll(DEBUG); //Debug tools
		
		GameOfCluedo.clearConsole();
		
		System.out.printf("Player %d, it is your turn!\n", this.playerNum);

		rollDice();
		
		while(!this.finished){
			
			if(this.room != null && !this.possibleActions.contains(LEAVE_ROOM)) {
				this.possibleActions.addAll(ROOM_ACTIONS);
				if(this.room.getStairs() != null) this.possibleActions.add(STAIRS);
			}
			
			System.out.println("");
			System.out.print("What would you like to do? Your options are: \n");
			for(String action : this.possibleActions){
				System.out.print("{" + action + "} \n");
			}
			
			
			String currentAction = input.nextLine();
			if(!this.possibleActions.contains(currentAction)){
				System.out.println("That is an invalid action. Please try again.");
				continue;
			}
			
			System.out.println("\n");
			
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
			case SHOW_ENVELOPE:
				showEnvelope();
				break;
			default:
				break;
			}
		}
	}
	
	/**
	 * Shows the current envelope to the user, method only used for debug purposes, wont be shown to a user unless enabled prior.
	 */
	
	private void showEnvelope() {
		System.out.println("WARNING: About to show the game's envelope, you are either Matt, Jashon or cheating!\n");
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			System.out.println("Exception " + e + " found in sleep.");
		}
		
		for(Card c : this.game.getEnvelope()){
			System.out.println("-" + c.getName());
		}
		System.out.println("");
		
	}
	/**
	 * Random number between 2-12 (i.e game uses two die).
	 */
	private void rollDice(){
		this.diceRoll = (int)(Math.random() * 5) + (int)(Math.random() * 5) + 2;
		this.diceRoll = 20; //DEBUG line
		System.out.println("You rolled a " + this.diceRoll + "!\n");
	}
	
	/**
	 * Asks user input to find out which door they would like to leave through, uses numbers to interface with the user, each door is 
	 * assigned a number from 1 to n.doors
	 */
	private void leaveRoom() {
		boolean freeSquare = false;
		DoorSquare exit = null;
		while(!freeSquare){
			int exitInd = -1;
			while(exitInd < 0 || exitInd > this.room.getDoorList().size() - 1){
				System.out.println("Which door would you like to leave through? (1-n)");
				for(int i = 0; i < this.room.getDoorList().size(); i++){
					System.out.printf("{%c : %d}", this.room.getDoorList().get(i).getID(), i+1);
				}
				try{
					exitInd = Integer.parseInt(this.input.nextLine()) -1;
				}catch(NumberFormatException e){
					exitInd = -1;
				}
			}

			exit = this.room.getDoorList().get(exitInd);
			freeSquare = this.board.leaveRoom(this, exit);
		}
		
		this.location = exit;
		
		System.out.printf("You have left the %s.\n", this.room.getName());
		this.room = null;
		
		this.board.printToConsole();
		
		this.possibleActions.removeAll(ROOM_ACTIONS);
		this.possibleActions.remove(STAIRS);
	}

	/**
	 * Switches the current room to the room joined to this one by stairs.
	 */
	private void useStairs() {
		this.room = this.room.getStairs();
		this.location = this.board.enterRoom(this, this.room);
		this.board.printToConsole();
		System.out.printf("You have used the stairs and are now in the %s.\n", this.room.getName());
	}
	/**
	 * Asks the user which item and person they are suggesting, then calls the game class to check the suggestion (item, person, currrent room)
	 * against the hands of the other players to see if they refuse or accept it.
	 */
	private void suggestion() {
		
		System.out.printf("You are making a suggestion, containing the %s\n", this.room.getName());
		
		int personInd = -1;
		while(personInd < 0 || personInd > 5){
			System.out.println("Who would you like to accuse? Your options are: (1-6)\n");
			for(int i = 0; i < this.game.getPersonCards().size(); i++){
				System.out.printf("{%s : %d}\n", this.game.getPersonCards().get(i).getName(), i+1);
			}
			try{
				personInd = Integer.parseInt(this.input.nextLine()) -1;
			}catch(NumberFormatException e){
				personInd = -1;
			}
		}

		
		int itemInd = -1;
		while(itemInd < 0 || itemInd > 5){
			System.out.println("\nWith which item?? Your options are: (1-6)\n");
			for(int i = 0; i < this.game.getItemCards().size(); i++){
				System.out.printf("{%s : %d}\n", this.game.getItemCards().get(i).getName(), i+1);
			}
			try{
				itemInd = Integer.parseInt(this.input.nextLine()) -1;
			}catch(NumberFormatException e){
				itemInd = -1;
			}
		}
		
		PersonCard person = this.game.getPersonCards().get(personInd);
		ItemCard item = this.game.getItemCards().get(itemInd);
		RoomCard room = null;
		
		for(RoomCard rc: this.game.getRoomCards()){
			if(this.room.getName().equals(rc.getName())) room = rc; 
		}
		
		
		boolean refused = this.game.checkSuggestion(room, person, item, this);
		if(refused) System.out.println("Your suggestion was refused by a player.");
		else System.out.println("No one refused your suggestion.");
		this.possibleActions.remove(SUGGEST);
		
	}
	/**
	 * Like suggestion, but asks for a room as well. It also causes you to lose or win the game depending on the outcome.
	 */
	private void accusation() {
		ArrayList<Card> envelope = this.game.getEnvelope();
		
		System.out.println("\nAre you sure you want to make an accusation?");
		System.out.println("If you are wrong, you will be out of the game! (y to accept, else any key)");
		if(!this.input.nextLine().equals("y")) return;
		
		
		int personInd = -1;
		while(personInd < 0 || personInd > 5){
			System.out.println("\nWho would you like to accuse? Your options are: (1-6)\n");
			for(int i = 0; i < this.game.getPersonCards().size(); i++){
				System.out.printf("{%s : %d}\n", this.game.getPersonCards().get(i).getName(), i+1);
			}
			try{
				personInd = Integer.parseInt(this.input.nextLine()) -1;
			}catch(NumberFormatException e){
				personInd = -1;
			}
		}

		int itemInd = -1;
		while(itemInd < 0 || itemInd > 5){
			System.out.println("\nWith which item?? Your options are: (1-6)\n");
			for(int i = 0; i < this.game.getItemCards().size(); i++){
				System.out.printf("{%s : %d}\n", this.game.getItemCards().get(i).getName(), i+1);
			}
			try{
				itemInd = Integer.parseInt(this.input.nextLine()) -1;
			}catch(NumberFormatException e){
				itemInd = -1;
			}
		}

		
		int roomInd = -1;
		while(roomInd < 0 || roomInd > 8){
			System.out.println("\nIn which room??? Your options are: (1-9)\n");
			for(int i = 0; i < this.game.getRoomCards().size(); i++){
				System.out.printf("{%s : %d}\n", this.game.getRoomCards().get(i).getName(), i+1);
			}
			try{
				roomInd = Integer.parseInt(this.input.nextLine()) -1;
			}catch(NumberFormatException e){
				roomInd = -1;
			}
		}

		
		PersonCard person = this.game.getPersonCards().get(personInd);
		ItemCard item = this.game.getItemCards().get(itemInd);
		RoomCard room = this.game.getRoomCards().get(roomInd);
		
		if(envelope.contains(person) && envelope.contains(item) && envelope.contains(room)){
			System.out.println("\nYour accusation was correct. You win!");
			this.game.winner = this;
			this.game.playing = false;
			this.finished = true;
		}
		else{
			System.out.println("\nYour accusation of was incorrect.");
			System.out.println("You are out of the game!");
			this.finished = true;
			this.retired = true;
		}
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			System.out.println("Exception " + e + " found in sleep.");
		}
		
	}
	
	/**
	 * Shows the hand of the player.
	 */
	private void showCards(){
		
		System.out.println("WARNING: About to show your cards, other players look away now!\n");
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			System.out.println("Exception " + e + " found in sleep.");
		}
		
		for(Card c : this.hand){
			System.out.println("-" + c.getName());
		}
		System.out.println("");
	}
	
	private void endTurn(){
		this.finished = true;
	}
	
	/**
	 * Asks the user to specify a direction,  then checks the validity of the move with board and moves them to the new square if valid. 
	 * This continues until they are out of dice rolls. Will cause the player to enter a room if they move onto a doorway.
	 */
	//THIS METHOD IS BROKE TO FUCK
	private void movePlayer(){
		while(this.diceRoll > 0){
			
			this.board.printToConsole();
			System.out.println("\n");
			System.out.printf("You have %d moves left, which direction would you like to move? (u,d,l,r): ", this.diceRoll);
			String direction = input.nextLine();
			if(DIR_LIST.contains(direction) && this.board.checkMove(this, direction)){
				this.location = this.board.playerMove(this, direction);
				if(this.location instanceof DoorSquare){
					this.room = ((DoorSquare)this.location).getRoom();
					this.location = this.board.enterRoom(this, this.room);
					this.board.printToConsole();
					System.out.printf("\nYou have entered the %s\n", this.room.getName());
					this.diceRoll = 0;
				}
				else this.diceRoll--;
			}
			else{
				System.out.println("\nThat is an invalid direction. Please try again.");
				continue;
			}
		}
		System.out.println("You are out of moves.\n");
		
		this.possibleActions.remove(MOVE);
	}
	
	public Square getLocation(){
		return this.location;
	}
	
	public boolean isRetired(){
		return this.retired;
	}

	public ArrayList<Card> getHand() {
		return this.hand;
	}
}
