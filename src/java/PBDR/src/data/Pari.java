package data;

public class Pari {
	
	private long id;
	private float cote;
	private float somme;
	private float somme_gg;
	private boolean joue;
	private long id_parieur;
	private long matchf_id;
	
	public Pari() {
		
	}
	
	public Pari(long id, float cote, float somme, float somme_gg, boolean joue,
			long id_parieur, long matchf_id) {
		this.id = id;
		this.cote = cote;
		this.somme = somme;
		this.somme_gg = somme_gg;
		this.joue = joue;
		this.id_parieur = id_parieur;
		this.matchf_id = matchf_id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public float getCote() {
		return cote;
	}

	public void setCote(float cote) {
		this.cote = cote;
	}

	public float getSomme() {
		return somme;
	}

	public void setSomme(float somme) {
		this.somme = somme;
	}

	public float getSomme_gg() {
		return somme_gg;
	}

	public void setSomme_gg(float somme_gg) {
		this.somme_gg = somme_gg;
	}

	public boolean isJoue() {
		return joue;
	}

	public void setJoue(boolean joue) {
		this.joue = joue;
	}

	public long getId_parieur() {
		return id_parieur;
	}

	public void setId_parieur(long id_parieur) {
		this.id_parieur = id_parieur;
	}

	public long getMatchf_id() {
		return matchf_id;
	}

	public void setMatchf_id(long matchf_id) {
		this.matchf_id = matchf_id;
	}
}
