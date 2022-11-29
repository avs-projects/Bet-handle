package data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Date;

import tcp.InterfaceGraphique;

public class Parieur {
	
	private long id;
	private String email;
	private String mdp;
	private String prenom;
	private String nom;
	private String pseudo;
	private int capital;
	private Date naissance;
	private Boolean pret;
	private int journee;
	
	private BufferedReader flux_entree;
	private PrintWriter flux_sortie;
	
	private InterfaceGraphique ig;
	
	public Parieur() {
		
	}
	
	public Parieur(Socket socket) throws IOException {
		try {
			flux_entree = new BufferedReader (
                    new InputStreamReader(socket.getInputStream ())) ;
			flux_sortie = new PrintWriter(socket.getOutputStream (), true) ;
		}
		catch (UnknownHostException e) {
            System.err.println ("Hote inconnu") ;
            flux_sortie.println(id);
            System.exit (1) ;
        } 
	}
	
	public Parieur(long id, String email, String mdp, String prenom, String nom, String pseudo,
			int capital, Date naissance, Boolean pret, int journee) {
		this.id = id;
		this.email = email;
		this.mdp = mdp;
		this.prenom = prenom;
		this.nom = nom;
		this.pseudo = pseudo;
		this.capital = capital;
		this.pret = pret;
		this.naissance = naissance;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMdp() {
		return mdp;
	}

	public void setMdp(String mdp) {
		this.mdp = mdp;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public int getCapital() {
		return capital;
	}

	public void setCapital(int capital) {
		this.capital = capital;
	}

	public Date getNaissance() {
		return naissance;
	}

	public void setNaissance(Date naissance) {
		this.naissance = naissance;
	}

	public Boolean getPret() {
		return pret;
	}

	public void setPret(Boolean pret) {
		this.pret = pret;
	}

	public int getJournee() {
		return journee;
	}

	public void setJournee(int journee) {
		this.journee = journee;
	}

	public BufferedReader getFlux_entree() {
		return flux_entree;
	}

	public void setFlux_entree(BufferedReader flux_entree) {
		this.flux_entree = flux_entree;
	}

	public PrintWriter getFlux_sortie() {
		return flux_sortie;
	}

	public void setFlux_sortie(PrintWriter flux_sortie) {
		this.flux_sortie = flux_sortie;
	}

	public InterfaceGraphique getIg() {
		return ig;
	}

	public void setIg(InterfaceGraphique ig) {
		this.ig = ig;
	}
	
}
