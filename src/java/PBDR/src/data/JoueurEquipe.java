package data;

public class JoueurEquipe {
	
	private long id;
	private String nom;
	private String poste;
	private long equipe_id;
	
	public JoueurEquipe() {
		
	}
	
	public JoueurEquipe(long id, String nom, String poste, long equipe_id) {
		this.id = id;
		this.nom = nom;
		this.poste = poste;
		this.equipe_id = equipe_id;
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

	public String getPoste() {
		return poste;
	}

	public void setPoste(String poste) {
		this.poste = poste;
	}

	public long getEquipe_id() {
		return equipe_id;
	}

	public void setEquipe_id(long equipe_id) {
		this.equipe_id = equipe_id;
	}

}
