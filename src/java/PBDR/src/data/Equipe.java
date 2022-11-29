package data;

public class Equipe {
	
	private long id;
	private String nom;
	private int note;
	private int classement_id;
	
	public Equipe() {
		
	}
	
	public Equipe(long id, String nom, int note, int classement_id) {
		this.id = id;
		this.nom = nom;
		this.note = note;
		this.classement_id = classement_id;
	}

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getNote() {
		return note;
	}

	public void setNote(int note) {
		this.note = note;
	}

	public int getClassement_id() {
		return classement_id;
	}

	public void setClassement_id(int classement_id) {
		this.classement_id = classement_id;
	}

}
