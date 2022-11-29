package data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import tcp.InterfaceGraphique;

public class Joueur {
	
	private long id;
	private String pseudo;
	private String mdp;
	private float capital;
	private boolean pret;
	private int journee;
	private BufferedReader flux_entree;
	private PrintWriter flux_sortie;
	
	private InterfaceGraphique ig;
	
	public Joueur() {
		
	}

	public Joueur(Socket socket) throws IOException {
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
	
	public Joueur(long id, String pseudo, String mdp, float capital, boolean pret, int journee) {
		this.id = id;
		this.pseudo = pseudo;
		this.mdp = mdp;
		this.capital = capital;
		this.pret = pret;
		this.journee = journee;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public String getMdp() {
		return mdp;
	}

	public void setMdp(String mdp) {
		this.mdp = mdp;
	}

	public float getCapital() {
		return capital;
	}

	public void setCapital(float capital) {
		this.capital = capital;
	}

	public boolean isPret() {
		return pret;
	}

	public void setPret(boolean pret) {
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
