package game;

import game.cards.*;
import game.cards.person.*;
import game.cards.rooms.*;
import game.cards.item.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class GameOfCluedo {
	
	private int numPlayers;
	protected ArrayList<Player> players = new ArrayList<Player>();
	
	private ArrayList<ItemCard> itemCards = new ArrayList<ItemCard>();
	private ArrayList<RoomCard> roomCards = new ArrayList<RoomCard>();
	private ArrayList<PersonCard> personCards = new ArrayList<PersonCard>();
	
	private ArrayList<Card> deck = new ArrayList<Card>();
	private ArrayList<Card> envelope = new ArrayList<Card>();
	
	protected Board board;
	
	private Scanner input = new Scanner(System.in);
	
	public GameOfCluedo (){
		System.out.println("Welcome to Cluedo!");
		System.out.println("WARNING: THIS GAME IS TRASH!");
		
		System.out.print("Please enter your desired number of players: ");
		this.numPlayers = Integer.parseInt(input.nextLine());
		this.board = new Board();
		
		initCards();
		addPlayers();
		initEnvelope();
		createDeck();
		dealCards();
		
	}
	
	private void createDeck() {
		for(int i = 0; i < 9; i++){
			this.deck.add(this.roomCards.get(0));
			if(i < 5){
				this.deck.add(this.personCards.get(0));
				this.deck.add(this.itemCards.get(0));
			}
		}		
	}

	private void initEnvelope() {
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
		for(int i = 0; i < this.numPlayers || i < 4; i++){
			Collections.shuffle(this.personCards);
			PersonCard identity = this.personCards.get(0);
			this.players.add(new Player(identity));
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
	
	public static void main(String [] args){
		new GameOfCluedo();
	}
	
	
}