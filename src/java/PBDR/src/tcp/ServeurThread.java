package tcp;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import DAO.ConnexionPostgreSQL;
import DAO.DAOFactory;
import data.JoueurEquipe;
import data.Match;
import data.PariJoueur;
import data.PariMatch;
import data.Parieur;

public class ServeurThread extends Thread{
	
	private Socket socket = null;
	private Serveur server = null;
	private BufferedReader flux_entree = null;
	private PrintWriter flux_sortie = null;
	private Parieur joueur;
	int numClient;
	int numConnected;
	int numReady;
	int nbMatchs;
	
	Connection connect = ConnexionPostgreSQL.getInstance();
	
	public ServeurThread(Socket socket, Serveur server) {
		this.socket = socket;
		this.server = server;
		try {
			flux_sortie = new PrintWriter(socket.getOutputStream (), true) ;
			flux_entree = new BufferedReader (new InputStreamReader(socket.getInputStream ())) ;
			
			// We list all the sockets in connected mode
			
			synchronized(server.listClients) {
				numClient=server.addClient(socket);
			}
			
		} catch (SocketException e)  { 
			System.err.println("CONNEXION INTERROMPUE");

		} catch (IOException e) {
        	e.printStackTrace();
		}  
		
	}
	
	public void run() {
				    	
    	long id = 0;
    	
		try {
			
			String chaine_entree, chaine_sortie;
			ArrayList<Match> matchs = new ArrayList<Match>();
			ArrayList<PariMatch> paris = new ArrayList <PariMatch>();
			ArrayList<PariJoueur> parisJ = new ArrayList <PariJoueur>();
			int journee;
			
			while(!socket.isClosed()) {
				
				chaine_entree = flux_entree.readLine();
				InetSocketAddress remote = (InetSocketAddress)socket.getRemoteSocketAddress();
		        String debug = "";
		        debug = "Thread : " + Thread.currentThread().getName() + ". ";
		        debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress() +".";
		        debug += " Sur le port : " + remote.getPort() + ".\n";
		        debug += "\t -> Commande re�ue : " + chaine_entree + "\n";
		        System.err.println("\n" + debug);
				
		        switch(chaine_entree) {
		        
		        	case Choix.ANTHENTIFICATION :	
		        		
		        		/*
		        		 * Allows connection between the client and the network via BDD
		        		 */
										
						chaine_entree = flux_entree.readLine();
						String pseudo = chaine_entree;
						chaine_entree = flux_entree.readLine();
						String password = chaine_entree;
						
						/*
						 * Allows password hashing
						 */
						
						MessageDigest m = MessageDigest.getInstance("MD5");
						m.reset();
						m.update(password.getBytes());
						byte[] digest = m.digest();
		    			BigInteger bigInt = new BigInteger(1,digest);
		    			String hashtext = bigInt.toString(16);
		    			while(hashtext.length() < 32 ){
			    			  hashtext = "0"+hashtext;
			    			}
						PreparedStatement statement = null;
						ResultSet resultat = null;
					
						try {
							statement = connect.prepareStatement("SELECT mdp FROM parieur WHERE pseudo ='"+ pseudo + "'");
							resultat = statement.executeQuery();
							joueur = DAOFactory.getParieurDAO().find(pseudo);

							if(resultat.next()) {
								if(joueur.getMdp().equals(hashtext)) flux_sortie.println(joueur.getId());
								else flux_sortie.println("erreur");
							}
							resultat = statement.executeQuery();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					
					case Choix.VOIR :
					
						/*
						 * Allows you to see the matches of the selected day.
						 */
					
						chaine_entree = flux_entree.readLine();
						journee = Integer.parseInt(chaine_entree);

						if(DAOFactory.getMatchDAO().findMaxJournee()<journee) {
							
							flux_sortie.println("Impossible de trouver les matchs...");
							
							debug = "";
						    debug = "Thread : " + Thread.currentThread().getName() + ". ";
						    debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress() +".";
						    debug += " Sur le port : " + remote.getPort() + ".\n";
						    debug += "\t -> Commande re�ue et refus�e... : " + journee + "\n";
						    System.err.println("\n" + debug);
						    
							break;
						}
						
						flux_sortie.println("OK");
					
						matchs = DAOFactory.getMatchDAO().search(journee); 
						
						for(int i=0; i<matchs.size(); i++) {
							chaine_sortie = matchs.get(i).getEquipeD();
							flux_sortie.println(chaine_sortie);
							chaine_sortie = matchs.get(i).getEquipeE();
							flux_sortie.println(chaine_sortie);
							chaine_sortie = Integer.toString(matchs.get(i).getScoreD());
							flux_sortie.println(chaine_sortie);
							chaine_sortie = Integer.toString(matchs.get(i).getScoreE());
							flux_sortie.println(chaine_sortie); 
							chaine_sortie = Boolean.toString(matchs.get(i).isJoue());
							flux_sortie.println(chaine_sortie);
							chaine_sortie = Long.toString(matchs.get(i).getId());
							flux_sortie.println(chaine_sortie);
						}
						
						deBug(debug, remote, Thread.currentThread().getName(), remote.getAddress().getHostAddress(), remote.getPort(), Integer.toString(journee));
					
						break;
					
					case Choix.PARIER :
					
						/*
						 * Allows you to bet on a match.
						 */
					
						journee = Integer.parseInt(flux_entree.readLine());
						
						if(DAOFactory.getMatchDAO().findMaxJournee()<journee) {
							
							flux_sortie.println("Impossible de trouver les matchs...");
							
							debug = "";
						    debug = "Thread : " + Thread.currentThread().getName() + ". ";
						    debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress() +".";
						    debug += " Sur le port : " + remote.getPort() + ".\n";
						    debug += "\t -> Commande re�ue et refus�e... : " + journee + "\n";
						    System.err.println("\n" + debug);
						    
							break;
						}
						
						else if(DAOFactory.getParieurDAO().getJournee()>journee) {
							flux_sortie.println("Match d�j� jou�...");
							
							debug = "";
						    debug = "Thread : " + Thread.currentThread().getName() + ". ";
						    debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress() +".";
						    debug += " Sur le port : " + remote.getPort() + ".\n";
						    debug += "\t -> Commande re�ue et refus�e... : " + journee + "\n";
						    System.err.println("\n" + debug);
						    
							break;
						}
						
						else { 
							
							flux_sortie.println("OK");
						
							matchs = DAOFactory.getMatchDAO().search(journee);

							// If the match has not been played, we continue
					
							chaine_entree = flux_entree.readLine();
							int match = Integer.parseInt(chaine_entree);
							
							if(match<0 && match>=10) {
								flux_sortie.println("Match non trouv� dans la journ�e... Journ�e entre [0,10]");
								debug = "";
							    debug = "Thread : " + Thread.currentThread().getName() + ". ";
							    debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress() +".";
							    debug += " Sur le port : " + remote.getPort() + ".\n";
							    debug += "\t -> Commande re�ue et refus�e... : " + chaine_entree + "\n";
							    System.err.println("\n" + debug);
								break;
							}
							else {
								flux_sortie.println("OK");
							
								chaine_entree = flux_entree.readLine();
								id = Long.parseLong(chaine_entree);
								System.out.println(id);
								if(DAOFactory.getParieurDAO().find(id)==null) {
									flux_sortie.println("Parieur non retrouv� dans la BD...");
									debug = "";
									debug = "Thread : " + Thread.currentThread().getName() + ". ";
									debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress() +".";
									debug += " Sur le port : " + remote.getPort() + ".\n";
									debug += "\t -> Commande re�ue et refus�e... : " + id + "\n";
									System.err.println("\n" + debug);
									break;
								}
								else {		
									flux_sortie.println("OK");

									String equipeGg = flux_entree.readLine();
									chaine_entree = equipeGg;	

									if(DAOFactory.getEquipeDAO().find(chaine_entree)==null) {
										flux_sortie.println("Equipe non retrouv�e dans la BD...");					
										debug = "";
										debug = "Thread : " + Thread.currentThread().getName() + ". ";
										debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress() +".";
										debug += " Sur le port : " + remote.getPort() + ".\n";
										debug += "\t -> Commande re�ue et refus�e... : " + chaine_entree + "\n";
										System.err.println("\n" + debug);
										break;
									}
									else {
									
										flux_sortie.println("OK");									
									
										chaine_entree = flux_entree.readLine();
										int sommeParie = Integer.parseInt(chaine_entree);

										if(DAOFactory.getParieurDAO().find(id).getCapital()<sommeParie) {
											flux_sortie.println("Argent insuffisant...");					
											debug = "";
											debug = "Thread : " + Thread.currentThread().getName() + ". ";
											debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress() +".";
											debug += " Sur le port : " + remote.getPort() + ".\n";
											debug += "\t -> Commande re�ue et refus�e... : " + chaine_entree + "\n";
											System.err.println("\n" + debug);
								    		break;
										}
									
										else {
											
											flux_sortie.println("OK");									
											
											DAOFactory.getParieurDAO().updateCapital(sommeParie, id);
					
											long noteD = DAOFactory.getEquipeDAO().find(matchs.get(match).getEquipeD_id()).getNote();
											long noteE = DAOFactory.getEquipeDAO().find(matchs.get(match).getEquipeE_id()).getNote();

											float coteD = 0;
											float coteE = 0;
					
											if (noteD>noteE) {					
												coteD = (float)((float)(noteD+(noteD-noteE)*5)/(float)(noteD+noteE));
												coteD = (float)(coteD*100);
												coteD = (float)(100/coteD);
												coteE = (float)((float)(noteE-(noteD-noteE)*5)/(float)(noteD+noteE));
												coteE = (float)(coteE*100);
												coteE = (float)(100/coteE);	
											}
					
											else {	
												coteE = (float)((float)(noteE+(noteE-noteD)*5)/(float)(noteD+noteE));
												coteE = (float)(coteE*100);
												coteE = (float)(100/coteE);
												coteD = (float)((float)(noteD-(noteE-noteD)*5)/(float)(noteD+noteE));
												coteD = (float)(coteD*100);
												coteD = (float)(100/coteD);
											}
					
											if(equipeGg.equals(matchs.get(match).getEquipeD())) {
												DAOFactory.getPariDAO().create(new PariMatch(0,coteD,sommeParie,0,false,id,matchs.get(match).getId(),equipeGg,"null"));
											}
											else {
												DAOFactory.getPariDAO().create(new PariMatch(0,coteE,sommeParie,0,false,id,matchs.get(match).getId(),equipeGg,"null"));
											}
										}
									}		
								}
							}
						}
						
						break;
						
					case Choix.PARIERJOUEUR :
					
						/*
						 * Allows you to bet on a player whether or not he scores during a match.
						 */
					
						journee = Integer.parseInt(flux_entree.readLine());
						
						if(DAOFactory.getMatchDAO().findMaxJournee()<journee) {
							
							flux_sortie.println("Impossible de trouver les matchs...");
							
							debug = "";
						    debug = "Thread : " + Thread.currentThread().getName() + ". ";
						    debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress() +".";
						    debug += " Sur le port : " + remote.getPort() + ".\n";
						    debug += "\t -> Commande re�ue et refus�e... : " + journee + "\n";
						    System.err.println("\n" + debug);
						    
							break;
						}
						
						else if(DAOFactory.getParieurDAO().getJournee()>journee) {
							flux_sortie.println("Match d�j� jou�...");
							
							debug = "";
						    debug = "Thread : " + Thread.currentThread().getName() + ". ";
						    debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress() +".";
						    debug += " Sur le port : " + remote.getPort() + ".\n";
						    debug += "\t -> Commande re�ue et refus�e... : " + journee + "\n";
						    System.err.println("\n" + debug);
						    
							break;
						}
						
						else { 
							
							flux_sortie.println("OK");
						
							matchs = DAOFactory.getMatchDAO().search(journee);
					
							chaine_entree = flux_entree.readLine();
							int match = Integer.parseInt(chaine_entree);
							System.out.println(match);

							
							if(match<0 && match>=10) {
								flux_sortie.println("Match non trouv� dans la journ�e... Journ�e entre [0,10]");
								debug = "";
							    debug = "Thread : " + Thread.currentThread().getName() + ". ";
							    debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress() +".";
							    debug += " Sur le port : " + remote.getPort() + ".\n";
							    debug += "\t -> Commande re�ue et refus�e... : " + chaine_entree + "\n";
							    System.err.println("\n" + debug);
								break;
							}
							else {
								flux_sortie.println("OK");
							
								chaine_entree = flux_entree.readLine();
								id = Long.parseLong(chaine_entree);
								System.out.println(id);
								if(DAOFactory.getParieurDAO().find(id)==null) {
									flux_sortie.println("Parieur non retrouv� dans la BD...");
									debug = "";
									debug = "Thread : " + Thread.currentThread().getName() + ". ";
									debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress() +".";
									debug += " Sur le port : " + remote.getPort() + ".\n";
									debug += "\t -> Commande re�ue et refus�e... : " + id + "\n";
									System.err.println("\n" + debug);
									break;
								}
								else {		
									flux_sortie.println("OK");

									String joueurParie = flux_entree.readLine();
									chaine_entree = joueurParie;	
									System.out.println(joueurParie);

									if(DAOFactory.getJoueurEquipeDAO().find(joueurParie)==null) {
										flux_sortie.println("Joueur de l'�quipe non retrouv�e dans la BD...");					
										debug = "";
										debug = "Thread : " + Thread.currentThread().getName() + ". ";
										debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress() +".";
										debug += " Sur le port : " + remote.getPort() + ".\n";
										debug += "\t -> Commande re�ue et refus�e... : " + chaine_entree + "\n";
										System.err.println("\n" + debug);
										break;
									}
									else {
									
										flux_sortie.println("OK");									
									
										chaine_entree = flux_entree.readLine();
										int sommeParie = Integer.parseInt(chaine_entree);
										System.out.println(sommeParie);

										if(DAOFactory.getParieurDAO().find(id).getCapital()<sommeParie) {
											flux_sortie.println("Argent insuffisant...");					
											debug = "";
											debug = "Thread : " + Thread.currentThread().getName() + ". ";
											debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress() +".";
											debug += " Sur le port : " + remote.getPort() + ".\n";
											debug += "\t -> Commande re�ue et refus�e... : " + chaine_entree + "\n";
											System.err.println("\n" + debug);
								    		break;
										}
									
										else {
											
											flux_sortie.println("OK");
						
											DAOFactory.getParieurDAO().updateCapital(sommeParie, id);

											JoueurEquipe joueurEq = DAOFactory.getJoueurEquipeDAO().findPlayer(joueurParie);
											if(joueurEq.getPoste().equals("ATT")) DAOFactory.getPariJoueurDAO().create(new PariJoueur(0,(float)1.5,sommeParie,0,false,id,matchs.get(match).getId(),joueurEq.getNom(),0));
											if(joueurEq.getPoste().equals("MIL")) DAOFactory.getPariJoueurDAO().create(new PariJoueur(0,(float)3.5,sommeParie,0,false,id,matchs.get(match).getId(),joueurEq.getNom(),0));
											if(joueurEq.getPoste().equals("DEF")) DAOFactory.getPariJoueurDAO().create(new PariJoueur(0,(float)6.5,sommeParie,0,false,id,matchs.get(match).getId(),joueurEq.getNom(),0));

										}
										break;
									}
								}
							}			
						}
					
					case Choix.PRET : 
						
						/*
						* Allows you to advance in matches.
						* If all players are "READY", matches will be played.
						 */
					
						chaine_entree = flux_entree.readLine();
						id = Integer.parseInt(chaine_entree);
						
						if(DAOFactory.getParieurDAO().find(id)==null) {
							flux_sortie.println("Joueur inconnu");					
							debug = "";
							debug = "Thread : " + Thread.currentThread().getName() + ". ";
							debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress() +".";
							debug += " Sur le port : " + remote.getPort() + ".\n";
							debug += "\t -> Commande re�ue et refus�e... : " + chaine_entree + "\n";
							System.err.println("\n" + debug);
				    		break;
						}
						else {
							flux_sortie.println("OK");
							synchronized (server.readyPlayers) {
								numReady = server.addRPlayers(DAOFactory.getParieurDAO().find(id));
							}	

							if(server.readyPlayers.size()==server.listClients.size()) {
								journee = DAOFactory.getParieurDAO().getJournee();
								matchs = DAOFactory.getMatchDAO().search(journee);
			
								for(int i=0;i<matchs.size();i++) {
									Match ma = DAOFactory.getMatchDAO().playMatch(matchs.get(i), DAOFactory.getButeurDAO(), DAOFactory.getJoueurEquipeDAO(), DAOFactory.getEquipeDAO());
									DAOFactory.getPariDAO().pariFini(ma);
									DAOFactory.getPariDAO().distriGain(ma);
									DAOFactory.getPariJoueurDAO().pariFini(ma);
									DAOFactory.getPariJoueurDAO().distriGain(ma);
									DAOFactory.getClassementDAO().updateC(ma);
									matchs.set(i, ma);
								}

								int a = server.readyPlayers.size();
								for(int i=0;i<a;i++) {
									synchronized(server.readyPlayers) {
										numReady=server.removeAll();
									}
								}
								DAOFactory.getParieurDAO().incrementJournee();
							}	
							break;
						}
						
					case Choix.ACTUALISER : 
						
						/*
						 * We update the bets as well as the player's money.
						 */
					
						chaine_entree = flux_entree.readLine();
						id = Integer.parseInt(chaine_entree);
						if(DAOFactory.getParieurDAO().find(id)==null) {
							
							flux_sortie.println("Joueur inconnu");					
							debug = "";
							debug = "Thread : " + Thread.currentThread().getName() + ". ";
							debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress() +".";
							debug += " Sur le port : " + remote.getPort() + ".\n";
							debug += "\t -> Commande re�ue et refus�e... : " + chaine_entree + "\n";
							System.err.println("\n" + debug);
				    		break;
						}
						else {
							
							flux_sortie.println(DAOFactory.getParieurDAO().find(id).getCapital());
							paris = DAOFactory.getPariDAO().getParis(id);
							parisJ = DAOFactory.getPariJoueurDAO().getParis(id);
							flux_sortie.println(paris.size());
						
							for(int i=0;i<paris.size();i++) {
								flux_sortie.println(DAOFactory.getMatchDAO().find(paris.get(i).getMatchf_id()).getEquipeD());
								flux_sortie.println(DAOFactory.getMatchDAO().find(paris.get(i).getMatchf_id()).getEquipeE());
								flux_sortie.println(paris.get(i).getVictoire());
								flux_sortie.println(paris.get(i).getCote());
								flux_sortie.println(paris.get(i).getSomme());
								flux_sortie.println(paris.get(i).isJoue());
								flux_sortie.println(paris.get(i).getGagnant());
								flux_sortie.println(paris.get(i).getSomme_gg());
							}

							flux_sortie.println(parisJ.size());

							for(int i=0;i<parisJ.size();i++) {
								flux_sortie.println(DAOFactory.getMatchDAO().find(parisJ.get(i).getMatchf_id()).getEquipeD());
								flux_sortie.println(DAOFactory.getMatchDAO().find(parisJ.get(i).getMatchf_id()).getEquipeE());
								flux_sortie.println(parisJ.get(i).getButeur());
								flux_sortie.println(parisJ.get(i).getCote());
								flux_sortie.println(parisJ.get(i).getSomme());
								flux_sortie.println(parisJ.get(i).isJoue());
								flux_sortie.println(parisJ.get(i).getResultat());
								flux_sortie.println(parisJ.get(i).getSomme_gg());
							}
													
							break;
						}
						
					case Choix.VOIRCOMPO :
						
						/*
						 * Allows you to see the players of each team.
						 * Each team is considered to have only 11 players.
						 */
						
						chaine_entree = flux_entree.readLine();
						String eqD = chaine_entree;
						chaine_entree = flux_entree.readLine();
						String eqE = chaine_entree;
						
						if((DAOFactory.getEquipeDAO().find(eqD)==null) || (DAOFactory.getEquipeDAO().find(eqE)==null)) {
						
							flux_sortie.println("Equipe inexistante pour l'une ou les deux �quipes...");					
							debug = "";
							debug = "Thread : " + Thread.currentThread().getName() + ". ";
							debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress() +".";
							debug += " Sur le port : " + remote.getPort() + ".\n";
							debug += "\t -> Commande re�ue et refus�e... : " + eqD + "\n";
							System.err.println("\n" + debug);
				    		break;
				    		
						}
						
						else {
							
							flux_sortie.println("OK");					
							
							ArrayList<JoueurEquipe> liste = DAOFactory.getJoueurEquipeDAO().find(eqD);
							
							for(int i=0;i<11;i++) {
								flux_sortie.println(liste.get(i).getNom());
							}
						
							liste = DAOFactory.getJoueurEquipeDAO().find(eqE);

							for(int i=0;i<11;i++) {
								flux_sortie.println(liste.get(i).getNom());
							}
													    
							break;
							
						}
					case Choix.CLOSE :
						
						/*
						 * Allows clean closing of the client.
						 */
						
						flux_sortie.close();
						flux_entree.close();
						socket.close();
						break;	
					default :
						flux_sortie.println("Commande Inconnu");
							
					}
		        }
			
				flux_sortie.close();
				flux_entree.close();
				socket.close();
				
			} catch (SocketException e)  {  
				
				synchronized (server.listClients) {
					numClient=server.removeClient(socket);
				}
				
				// We delete ready players from the list as soon as they are disconnected.
				
				synchronized (server.readyPlayers) {
					numReady=server.removeRPlayers(DAOFactory.getParieurDAO().find(id));
				}
				
			
			System.err.println("CONNEXION INTERROMPUE");
			
			} catch (IOException e) {
				
			// Remove the socket connections from the list.
			
			e.printStackTrace();
			} catch (NoSuchAlgorithmException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
	}
	
	public void deBug (String debug, InetSocketAddress remote, String thread, String adress, int port, String commande) {
		debug = "";
	    debug = "Thread : " + thread + ". ";
	    debug += "Demande de l'adresse : " + adress +".";
	    debug += " Sur le port : " + port + ".\n";
	    debug += "\t -> Commande re�ue et accept�e : " + commande + "\n";
	    System.err.println("\n" + debug);

	}
}
