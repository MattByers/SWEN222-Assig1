package game;

public abstract class Card {
	
	private String name = null;
	
	public Card(String name){
		this.name = name;
	}

	public String getName(){
		return this.name;
	}
	
	public boolean equals(Object o){
		if(o.getClass() == this.getClass()) return true;
		return false;
	}
}
