package data;

public class Buteur {
	
	private long id;
	private String nom;
	private int nbButs;
	private long matchf_id;
	private long joueureq_id;
	
	public Buteur() {
		
	}
	
	public Buteur(long id, String nom, int nbButs, long matchf_id, long joueureq_id) {
		this.id = id;
		this.nom = nom;
		this.nbButs = nbButs;
		this.matchf_id = matchf_id;
		this.joueureq_id = joueureq_id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getNbButs() {
		return nbButs;
	}

	public void setNbButs(int nbButs) {
		this.nbButs = nbButs;
	}	

	public long getMatchf_id() {
		return matchf_id;
	}

	public void setMatchf_id(long matchf_id) {
		this.matchf_id = matchf_id;
	}

	public long getJoueureq_id() {
		return joueureq_id;
	}

	public void setJoueureq_id(long joueureq_id) {
		this.joueureq_id = joueureq_id;
	}
	
}
