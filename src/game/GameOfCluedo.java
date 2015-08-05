package game;

import game.cards.*;
import game.cards.person.*;
import game.cards.rooms.*;
import game.cards.item.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class GameOfCluedo {
	
	protected boolean playing;
	
	private int numPlayers;
	private ArrayList<Player> players;
	private ArrayList<Player> retiredPlayers;
	
	private ArrayList<ItemCard> itemCards;
	private ArrayList<RoomCard> roomCards;
	private ArrayList<PersonCard> personCards;

	private ArrayList<Card> deck;
	private ArrayList<Card> envelope;
	
	private Board board;
	
	private Scanner input = new Scanner(System.in);
	
	public GameOfCluedo (){
		System.out.println("Welcome to Cluedo!");
		System.out.println("WARNING: THIS GAME IS TRASH!");
		
		//Keeps looping until a number between 3 and 6 is entered
		int numPlayers = 0;
		while(numPlayers < 3 || numPlayers > 6){
			System.out.print("Please enter your desired number of players (3-6): ");
			try{
				numPlayers = Integer.parseInt(input.nextLine());
			}catch(NumberFormatException e){
				numPlayers = 0;
			}
		}
		this.numPlayers = numPlayers;
		
		this.board = new Board();
		
		initCards();
		addPlayers();
		initEnvelope();
		createDeck();
		dealCards();
		initCards();
		
		this.playing = true;
		startGame();
	}
	
	public ArrayList<Card> getEnvelope() {
		return envelope;
	}
	/**
	 * Continuously loops through the list of players that are in the game currently, adding any players to be retired to a new list
	 * then retiring them (move them from the player list, to retiredPlayer list). Sets a winner if only one players lasts.
	 */
	private void startGame() {
		ArrayList<Player> toRetire = new ArrayList<Player>();
		while(playing){
			for(Player p : this.players){
				if(toRetire.size() >= this.numPlayers - 1 || !this.playing) continue;
				if(p.isRetired()) {
					toRetire.add(p);
					continue;
				}
				p.takeTurn(this.input);
			}
			
			while(toRetire.size() != 0){
				retirePlayer(toRetire.remove(0));
			}
			
			if(this.players.size() <= 1) {
				Player winner = this.players.get(0);
				System.out.println("All players other have been eliminated. Player " + winner.getPlayerNum() + ", you are the winner! \n");
				break;
			}
			
		}
		System.out.println("Game Over!");
	}
	/**
	 * Joins all three decks (player, item and person cards) into one deck to be dealt.
	 */
	private void createDeck() {
		this.deck = new ArrayList<Card>();
		for(int i = 0; i < 8; i++){
			this.deck.add(this.roomCards.get(i));
			if(i < 5){
				this.deck.add(this.personCards.get(i));
				this.deck.add(this.itemCards.get(i));
			}
		}		
	}
	
	/**
	 * Takes a random card from all three decks to be placed into the envelope ( the list of solutions cards).
	 */
	private void initEnvelope() {
		this.envelope = new ArrayList<Card>();
		this.envelope.add(getRandomPersonCard());
		this.envelope.add(getRandomItemCard());
		this.envelope.add(getRandomRoomCard());
	}
	
	/**
	 * Shuffles the full deck (minus the envelope cards) and deals them to players one at a time,
	 * first few players may get more cards. 
	 */
	private void dealCards() {
		Collections.shuffle(this.deck);
		for(int pos = 0; !this.deck.isEmpty(); pos++){
			if(pos >= numPlayers) pos = 0;
			this.players.get(pos).giveCard(this.deck.remove(0));
		}
	}

	/**
	 * Initialises the three decks with all the cards.
	 */
	private void initCards() {
		
		this.personCards = new ArrayList<PersonCard>();
		this.itemCards = new ArrayList<ItemCard>();
		this.roomCards = new ArrayList<RoomCard>();
		
		this.personCards.add(new MissScarlett());
		this.personCards.add(new ColonelMustard());
		this.personCards.add(new MrsWhite());
		this.personCards.add(new ReverandGreen());
		this.personCards.add(new MrsPeacock());
		this.personCards.add(new ProfessorPlum());
		
		this.itemCards.add(new LeadPipe());
		this.itemCards.add(new Spanner());
		this.itemCards.add(new Revolver());
		this.itemCards.add(new Rope());
		this.itemCards.add(new CandleStick());
		this.itemCards.add(new Dagger());
		
		this.roomCards.add(new Ballroom());
		this.roomCards.add(new BilliardRoom());
		this.roomCards.add(new Conservatory());
		this.roomCards.add(new DiningRoom());
		this.roomCards.add(new Hall());
		this.roomCards.add(new Kitchen());
		this.roomCards.add(new Library());
		this.roomCards.add(new Lounge());
		this.roomCards.add(new Study());
	}

	/**
	 * Creates a new players depending on number of Players given at the start, 
	 * and picks a card from the player cards to assign that person an identity.
	 */
	private void addPlayers(){
		this.players = new ArrayList<Player>();
		this.retiredPlayers = new ArrayList<Player>();
		
		for(int i = 0; i < this.numPlayers; i++){
			this.players.add(new Player(i+1, this.board, this));
		}
	}
	
	/**
	 * Removes a random person card from the personCard list.
	 * @return a random personCard.
	 */
	private PersonCard getRandomPersonCard() {
		Collections.shuffle(this.personCards);
		return this.personCards.remove(0);
	}
	
	/**
	 * Removes a random item card from the itemCard list.
	 * @return a random itemCard.
	 */
	private ItemCard getRandomItemCard() {
		Collections.shuffle(this.itemCards);
		return this.itemCards.remove(0);
	}
	
	/**
	 * Removes a random room card from the roomCard list.
	 * @return a random roomCard.
	 */
	private RoomCard getRandomRoomCard() {
		Collections.shuffle(this.roomCards);
		return this.roomCards.remove(0);
	}
	
	public int getNumPlayers() {
		return numPlayers;
	}

	public ArrayList<RoomCard> getRoomCards() {
		return roomCards;
	}

	public ArrayList<ItemCard> getItemCards() {
		return itemCards;
	}

	public ArrayList<PersonCard> getPersonCards() {
		return personCards;
	}
	
	/**
	 * moves player from players to retired players
	 * @param person to be retired.
	 */
	public void retirePlayer(Player p){
		this.players.remove(p);
		this.retiredPlayers.add(p);
	}
	
	/**
	 * Checks a suggestion against the hands of all players except the player that made the suggestion, to check if it
	 * is a valid suggestion or not.
	 * @param room suggested
	 * @param person suggested
	 * @param item suggested
	 * @param player who made the suggestion
	 * @return false if suggestion was not refused, true if it was refused.
	 */
	public boolean checkSuggestion(RoomCard room, PersonCard person,
			ItemCard item, Player player) {
		for(Player p : this.players){
			if(p.equals(player)) continue;
			for(Card c : p.getHand()){
				if(c.equals(room) || c.equals(item) || c.equals(person))
					return true;
			}
		}
		return false;
		
	}
	
	/**
	 * Prints lines to the console to hide the earlier moves.
	 */
	public static void clearConsole(){
		for(int i = 0; i < 1000; i++){
			System.out.println("");
		}
	}
	
	public static void main(String [] args){
		new GameOfCluedo();
	}

	
	
	
}
