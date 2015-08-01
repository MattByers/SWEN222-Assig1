package game;

public abstract class Card {
	
	private String name = null;
	
	public Card(String name){
		this.name = name;
	}

	public String getName(){
		return this.name;
	}
}
