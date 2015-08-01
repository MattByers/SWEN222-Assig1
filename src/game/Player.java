package game;

import game.cards.PersonCard;
import game.squares.Square;

import java.util.ArrayList;

public class Player {
	
	private int playerNum;
	private PersonCard identity;
	private ArrayList<Card> hand = new ArrayList<Card>();
	private Square location;
	private int diceRoll;
	
	public Player(PersonCard identity, int playerNum){
		this.identity = identity;
		this.playerNum = playerNum;
	}
	
	public void giveCard (Card card){
		this.hand.add(card);
	}
	
	public void rollDice(){
		this.diceRoll = (int)(Math.random() * 6) + (int)(Math.random() * 6);
		System.out.println("You rolled a " + this.diceRoll);
	}

	public void takeTurn() {
		System.out.printf("Player %d (%S), it is your turn!", this.playerNum, this.identity.getName());
		System.out.println("WARNING: Others players look away now");
	}
}
