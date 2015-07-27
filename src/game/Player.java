package game;

import game.cards.PersonCard;

import java.util.ArrayList;

public class Player {
	
	private PersonCard identity;
	private ArrayList<Card> hand = new ArrayList<Card>();
	private Square location;
	private int diceRoll;
	
	public Player(PersonCard identity){
		this.identity = identity;
	}
	
	public void giveCard (Card card){
		this.hand.add(card);
	}
	
	public void rollDice(){
		this.diceRoll = (int)(Math.random() * 6) + (int)(Math.random() * 6);
	}
}
