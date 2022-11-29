package data;

public class PariJoueur extends Pari {
	
	private String buteur;
	private int resultat;
	
	public PariJoueur() {
		
	}
	
	public PariJoueur(long id, float cote, float somme, float somme_gg, boolean joue, long joueur_id, long matchf_id,
			String buteur, int resultat) {
		super(id,cote,somme,somme_gg,joue,joueur_id, matchf_id);
		this.buteur = buteur;
		this.resultat = resultat; 
	}

	public String getButeur() {
		return buteur;
	}

	public void setButeur(String buteur) {
		this.buteur = buteur;
	}

	public int getResultat() {
		return resultat;
	}

	public void setResultat(int resultat) {
		this.resultat = resultat;
	}



	
}
