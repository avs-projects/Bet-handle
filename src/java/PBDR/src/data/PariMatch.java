package data;

public class PariMatch extends Pari{
	
	private String victoire;
	private String gagnant;
	
	public PariMatch() {
		
	}
	
	public PariMatch(long id, float cote, float somme, float somme_gg, boolean joue, long joueur_id
			,  long matchf_id, String victoire, String gagnant) {
		super(id,cote,somme,somme_gg,joue,joueur_id, matchf_id);
		this.victoire = victoire;
		this.gagnant = gagnant;
	}

	public String getVictoire() {
		return victoire;
	}

	public void setVictoire(String victoire) {
		this.victoire = victoire;
	}

	public String getGagnant() {
		return gagnant;
	}

	public void setGagnant(String gagnant) {
		this.gagnant = gagnant;
	}
	
}
