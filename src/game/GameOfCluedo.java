package game;

import game.cards.*;
import game.cards.person.*;
import game.cards.rooms.*;
import game.cards.item.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class GameOfCluedo {
	
	protected boolean playing;
	protected Player winner = null;
	
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

	private void startGame() {
		while(playing){
			for(Player p : this.players){
				p.takeTurn(this.input);
			}
		}
		System.out.println("Game Over!");
	}

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

	private void initEnvelope() {
		this.envelope = new ArrayList<Card>();
		this.envelope.add(getRandomPersonCard());
		this.envelope.add(getRandomItemCard());
		this.envelope.add(getRandomRoomCard());
	}

	private void dealCards() {
		Collections.shuffle(this.deck);
		for(int pos = 0; !this.deck.isEmpty(); pos++){
			if(pos >= numPlayers) pos = 0;
			this.players.get(pos).giveCard(this.deck.remove(0));
		}
	}

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

	private void addPlayers(){
		this.players = new ArrayList<Player>();
		this.retiredPlayers = new ArrayList<Player>();
		
		for(int i = 0; i < this.numPlayers; i++){
			Collections.shuffle(this.personCards);
			PersonCard identity = this.personCards.get(0);
			this.players.add(new Player(identity, i+1, this.board, this));
		}
	}
	
	private PersonCard getRandomPersonCard() {
		Collections.shuffle(this.personCards);
		return this.personCards.remove(0);
	}
	
	private ItemCard getRandomItemCard() {
		Collections.shuffle(this.itemCards);
		return this.itemCards.remove(0);
	}
	
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
	
	public void retirePlayer(Player p){
		this.players.remove(p);
		this.retiredPlayers.add(p);
	}
	
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
	
	public static void clearConsole(){
		for(int i = 0; i < 1000; i++){
			System.out.println("");
		}
	}
	
	public static void main(String [] args){
		new GameOfCluedo();
	}

	
	
	
}
