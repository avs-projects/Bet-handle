package DAO;

public class DAOFactory {
	
	public static EquipeDAO getEquipeDAO() {
		return new EquipeDAO();
	}
	
	public static MatchDAO getMatchDAO() {
		return new MatchDAO();
	}
	
	public static PariDAO getPariDAO() {
		return new PariDAO();
	}
	
	public static ClassementDAO getClassementDAO() {
		return new ClassementDAO();
	}
	
	public static ButeurDAO getButeurDAO() {
		return new ButeurDAO();
	}
	
	public static JoueurEquipeDAO getJoueurEquipeDAO() {
		return new JoueurEquipeDAO();
	}
	
	public static PariJoueurDAO getPariJoueurDAO() {
		return new PariJoueurDAO();
	}
	
	public static ParieurDAO getParieurDAO() {
		return new ParieurDAO();
	}
}
