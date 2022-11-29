package data;

public class Classement {
	
	private long id;
	private int victoire;
	private int defaite;
	private int rang;
	
	public Classement() {
		
	}
	
	public Classement(long id, int victoire, int defaite, int rang) { 
		this.id = id;
		this.victoire = victoire;
		this.defaite = defaite;
		this.rang = rang;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getVictoire() {
		return victoire;
	}

	public void setVictoire(int victoire) {
		this.victoire = victoire;
	}

	public int getDefaite() {
		return defaite;
	}

	public void setDefaite(int defaite) {
		this.defaite = defaite;
	}

	public int getRang() {
		return rang;
	}

	public void setRang(int rang) {
		this.rang = rang;
	}
	
	
}