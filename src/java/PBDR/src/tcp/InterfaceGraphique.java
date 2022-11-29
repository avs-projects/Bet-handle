package tcp;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import data.Parieur;

public class InterfaceGraphique extends JFrame{

	private static final long serialVersionUID = 1L;
	
	protected JPanel matchsPane = new JPanel();
	protected JPanel boutonPane = new JPanel();
	protected JPanel sBoutonPane = new JPanel();
	protected JPanel gamesPane[] = new JPanel[10];
	protected JPanel playersPane = new JPanel();
	protected JPanel pariJPane = new JPanel();
	protected JPanel pariPane = new JPanel();
	
	protected JButton pariDButton[] = new MyButton[10];
	protected JButton pariEButton[] = new MyButton[10];
	protected JButton pariJDButton = new MyButton("Vote");
	protected JButton pariJEButton = new MyButton("Vote");
	protected JButton voirCompo[] = new MyButton[10];
	protected JButton filtreButton = new MyButton("Appliquer");
	protected JButton pretButton = new MyButton("Pret");
	protected JButton refreshButton = new MyButton("Actualiser");
	protected JButton quitterButton = new MyButton("Quitter");
		
	protected JTextField voteDTField[] = new JTextField[10];
	protected JTextField voteETField[] = new JTextField[10];
	protected JTextField voteparieurDField = new JTextField(10);
	protected JTextField voteparieurEField = new JTextField(10);
	
	protected JLabel equipeDomicileLabel[] = new JLabel [15];
	protected JLabel scoreDomicileLabel[] =  new JLabel [15];
	protected JLabel scoreBarreLabel[] = new JLabel[15];
	protected JLabel scoreExterieurLabel[] =  new JLabel [15];
	protected JLabel equipeExterieurLabel[] =  new JLabel [15];
	protected JLabel etatLabel[] = new JLabel[15];
	protected JLabel idLabel[] = new JLabel[15];
	protected JLabel capitalLabel = new JLabel ("Capital");
	protected JLabel ticketLabel = new JLabel("Ticket Pari");
	
	protected JTextArea textArea = new JTextArea(5, 100);
	protected JComboBox<Integer> box;
	protected Integer [] liste = new Integer[38];
	protected JComboBox<String> sBox;
	protected JComboBox<String> tBox;
	
	protected JTextArea txtOutPut = new JTextArea();
	protected JScrollPane sclPane = new JScrollPane(txtOutPut);
	
	protected JTextArea players = new JTextArea();
	
	private Parieur parieur;
	private int intMatch;
	
	String chaine;

	public InterfaceGraphique(String title, Socket socket) throws IOException, ClassNotFoundException, SQLException {
		
		super(title);
		
		for(int i=0;i<38;i++) {
			liste[i] = i+1;
		}
		
		box = new JComboBox<Integer>(liste);
		sBox = new JComboBox<String>();
		tBox = new JComboBox<String>();
		
		parieur = new Parieur(socket);
				
		parieur.setIg(this);
		
		addPanel(gamesPane);
		
		addLabel(equipeDomicileLabel);
		addLabel(scoreDomicileLabel);
		addLabel(scoreExterieurLabel);
		addLabel(equipeExterieurLabel);
		addLabel(scoreBarreLabel);
		addLabel(etatLabel);
		addLabel(idLabel);
		
		addButton(pariDButton);
		addButton(pariEButton);
		addButton(voirCompo);
		
		addTextField(voteDTField);
		addTextField(voteETField);
				
		requestConnexion();

		requestMatch(1);
		
		initStyle();
		
		initLayout();
		
		initActions();
				
	}
	
	protected void initActions() {
		
		for(int i=0;i<10;i++) {
			pariDButton[i].addActionListener(new ParierMatch(i,true));
		}
		
		for(int i=0;i<10;i++) {
			pariEButton[i].addActionListener(new ParierMatch(i,false));
		}
		
		for(int i=0;i<10;i++) {
			voirCompo[i].addActionListener(new VoirCompo(i));
		}
		
		pariJDButton.addActionListener(new ParierJoueur(true));
		pariJEButton.addActionListener(new ParierJoueur(false));
		
		filtreButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				try {
					
					int journee = box.getSelectedIndex()+1;
					parieur.getFlux_sortie().println(Choix.VOIR);
					parieur.getFlux_sortie().println(journee);
					
					
					chaine = parieur.getFlux_entree().readLine();

					if(chaine.equals("Impossible de trouver les matchs...")) {
						System.err.println("Journee n'existe pas");
					}
					
					else {

					for(int i=0; i<10; i++) {
						
						chaine = parieur.getFlux_entree().readLine();
						equipeDomicileLabel[i].setText(chaine);
						chaine = parieur.getFlux_entree().readLine();
						equipeExterieurLabel[i].setText(chaine);
						chaine = parieur.getFlux_entree().readLine();
						scoreDomicileLabel[i].setText(chaine);
						chaine = parieur.getFlux_entree().readLine();
						scoreExterieurLabel[i].setText(" "+chaine);
						chaine = parieur.getFlux_entree().readLine();
						
						if((Boolean.parseBoolean(chaine))==false) {
							etatLabel[i].setText("NJ");
						}
						else {
							etatLabel[i].setText("J");
						}
						
						chaine = parieur.getFlux_entree().readLine();
						idLabel[i].setText(chaine.toUpperCase());
						parieur.setJournee(journee);
					}	
					}
				} catch (IOException ex) {
					// TODO Auto-generated catch block
					ex.printStackTrace();
				}	
			}
		});
		
		pretButton.addActionListener(new Pret());
		refreshButton.addActionListener(new ActualiserPari());
		quitterButton.addActionListener(new Quitter());
		
	}
	
	protected void initStyle() {
		
		
		for(int i=0;i<10;i++) {
			
			this.voteDTField[i].setPreferredSize(new Dimension(40,40));
			this.voteDTField[i].setBackground(new Color(230,230,230));
			this.voteDTField[i].setBorder(null);
			
			this.voteETField[i].setPreferredSize(new Dimension(40,40));
			this.voteETField[i].setBackground(new Color(230,230,230));
			this.voteETField[i].setBorder(null);		
			
			this.voirCompo[i].setBackground(new Color(17,35,63));
			this.voirCompo[i].setText("Compo");
			
			this.gamesPane[i].setBackground(new Color(247, 247, 248));
			
			this.equipeDomicileLabel[i].setPreferredSize(new Dimension(200,40));
			this.equipeDomicileLabel[i].setHorizontalAlignment(SwingConstants.CENTER);
			
			this.scoreBarreLabel[i].setText("-");
			this.scoreBarreLabel[i].setHorizontalAlignment(SwingConstants.CENTER);
    	
			this.equipeExterieurLabel[i].setHorizontalAlignment(SwingConstants.CENTER);
			this.scoreDomicileLabel[i].setHorizontalAlignment(SwingConstants.CENTER);
			this.scoreExterieurLabel[i].setHorizontalAlignment(SwingConstants.CENTER);

    		this.scoreDomicileLabel[i].setPreferredSize(new Dimension(20,40));
    		this.scoreExterieurLabel[i].setPreferredSize(new Dimension(20,40));
    		this.equipeExterieurLabel[i].setPreferredSize(new Dimension(200,40));
    		this.etatLabel[i].setPreferredSize(new Dimension(30,40));
    		this.etatLabel[i].setHorizontalAlignment(SwingConstants.CENTER);
		
		}
		
		this.playersPane.setBackground(new Color(17,35,63));
		this.players.setBackground(new Color(17,35,63));
		this.players.setForeground(Color.WHITE);
		
		this.boutonPane.setBackground(new Color(17,35,63));
		this.pariPane.setBackground(new Color(17,35,63));
		this.sBoutonPane.setBackground(new Color(17,35,63));
		
		this.capitalLabel.setBackground(new Color(17,35,63));
		this.capitalLabel.setForeground(new Color(205, 251, 10));
		this.capitalLabel.setPreferredSize(new Dimension(500,40));
		this.capitalLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.capitalLabel.setFont(new java.awt.Font(Font.SANS_SERIF,Font.BOLD,15));
		
		this.ticketLabel.setBackground(new Color(17,35,63));
		this.ticketLabel.setForeground(new Color(205, 251, 10));
		this.ticketLabel.setPreferredSize(new Dimension(100,40));
		this.ticketLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.ticketLabel.setFont(new java.awt.Font(Font.SANS_SERIF,Font.BOLD,15));
		
		this.filtreButton.setBackground(new Color(205, 251, 10));
		this.filtreButton.setForeground(new Color(17,35,63));
		this.pretButton.setBackground(new Color(205, 251, 10));
		this.pretButton.setForeground(new Color(17,35,63));
		this.refreshButton.setBackground(new Color(205, 251, 10));
		this.refreshButton.setForeground(new Color(17,35,63));
		
		this.sclPane.setPreferredSize(new Dimension(200,510));
		this.players.setPreferredSize(new Dimension(600,80));
		this.players.setFont(new java.awt.Font(Font.SANS_SERIF,Font.BOLD,13));
		
		this.pariJDButton.setBackground(new Color(205, 251, 10));
		this.pariJDButton.setForeground(new Color(17,35,63));
		this.pariJEButton.setBackground(new Color(205, 251, 10));
		this.pariJEButton.setForeground(new Color(17,35,63));
		
	}
		
	
	protected void initLayout() {
		
		Container contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout());
		contentPane.setBackground(Color.WHITE);
		
		this.boutonPane.add(filtreButton);
		this.boutonPane.add(box);
		this.boutonPane.add(capitalLabel);
		this.boutonPane.add(pretButton);
		this.boutonPane.add(quitterButton);
		this.boutonPane.setLayout(new BoxLayout(boutonPane, BoxLayout.LINE_AXIS));
		
		for(int i=0;i<10;i++) {
			
		this.gamesPane[i].add(pariDButton[i]);
		this.gamesPane[i].add(voteDTField[i]);
		this.gamesPane[i].add(equipeDomicileLabel[i]);
		this.gamesPane[i].add(scoreDomicileLabel[i]);
		this.gamesPane[i].add(scoreBarreLabel[i]);
		this.gamesPane[i].add(scoreExterieurLabel[i]);
		this.gamesPane[i].add(equipeExterieurLabel[i]);
		this.gamesPane[i].add(voirCompo[i]);
		this.gamesPane[i].add(etatLabel[i]);	
		this.gamesPane[i].add(voteETField[i]);
		this.gamesPane[i].add(pariEButton[i]);
		this.gamesPane[i].setLayout(new BoxLayout(gamesPane[i], BoxLayout.LINE_AXIS));
		
		}
		
		this.playersPane.add(players);
		this.playersPane.setLayout(new BoxLayout(playersPane, BoxLayout.LINE_AXIS));
		
		this.pariJPane.add(sBox);
		this.pariJPane.add(pariJDButton);
		this.pariJPane.add(voteparieurDField);
		this.pariJPane.add(tBox);
		this.pariJPane.add(pariJEButton);
		this.pariJPane.add(voteparieurEField);
		this.pariJPane.setLayout(new BoxLayout(pariJPane, BoxLayout.LINE_AXIS));
		
		this.matchsPane.add(boutonPane);
		for(int i=0;i<10;i++) this.matchsPane.add(gamesPane[i]);
		this.matchsPane.add(playersPane);
		this.matchsPane.add(pariJPane);
		this.matchsPane.setLayout(new BoxLayout(matchsPane, BoxLayout.PAGE_AXIS));
		
		this.sBoutonPane.add(ticketLabel);
		this.sBoutonPane.add(refreshButton);
		this.sBoutonPane.setLayout(new BoxLayout(sBoutonPane, BoxLayout.LINE_AXIS));
		this.sBoutonPane.add(Box.createHorizontalGlue());

		this.pariPane.add(sBoutonPane);
		this.pariPane.add(sclPane);
		this.pariPane.setLayout(new BoxLayout(pariPane, BoxLayout.PAGE_AXIS));

		
		this.getContentPane().add(matchsPane);
		this.getContentPane().add(pariPane);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setSize(1300,700);
		setVisible(true);
		
	}
	
	public class Pret implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			parieur.getFlux_sortie().println(Choix.PRET);
			parieur.getFlux_sortie().println(parieur.getId());
			try {
				if(parieur.getFlux_entree().readLine().equals("Joueur inconnu")) {
					System.out.println("Joueur non trouv�...");
				}
				
			}catch (SocketException ex)  { 
				
				System.err.println("CONNEXION INTERROMPUE");
				
			}catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	public class ActualiserPari implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
	
			try {
				sBox.removeAllItems();
				tBox.removeAllItems();
				
				String chaine, eqD, eqE, eqGg, cote, somme, vq, sommegg;
				int capital;
				Boolean bool;
				
				txtOutPut.setText("");
				
				parieur.getFlux_sortie().println(Choix.ACTUALISER);
				parieur.getFlux_sortie().println(parieur.getId());
				
				chaine = parieur.getFlux_entree().readLine();
				capital = Integer.parseInt(chaine);
				parieur.setCapital(capital);
				capitalLabel.setText("Capital : "+parieur.getCapital());

				chaine = parieur.getFlux_entree().readLine();
				int nbPari = Integer.parseInt(chaine);
				
				for(int i=0;i<nbPari;i++) {
					eqD = parieur.getFlux_entree().readLine();
					eqE = parieur.getFlux_entree().readLine();
					eqGg = parieur.getFlux_entree().readLine();
					cote = parieur.getFlux_entree().readLine();
					somme = parieur.getFlux_entree().readLine();
					bool = Boolean.parseBoolean(parieur.getFlux_entree().readLine());
					vq = parieur.getFlux_entree().readLine();
					sommegg = parieur.getFlux_entree().readLine();
					
					txtOutPut.append(eqD +" - " + eqE +"\n"+
					"Gagnant : "+eqGg +"\n"+
					"Cote : "+cote+"\n"+
					"Somme mise : "+somme+"\n");
					if(bool==false) txtOutPut.append("Etat : en cours..." +"\n");
					else txtOutPut.append("Etat : Termin�" +"\n"+
					"Vainqueur : " + vq + "\n"+
					"Somme gagn�e : "+ sommegg + "\n");
					txtOutPut.append("----------------------------------------------\n");
					
				}
				
				chaine = parieur.getFlux_entree().readLine();
				nbPari = Integer.parseInt(chaine);
				
				for(int i=0;i<nbPari;i++) {
					eqD = parieur.getFlux_entree().readLine();
					eqE = parieur.getFlux_entree().readLine();
					eqGg = parieur.getFlux_entree().readLine();
					cote = parieur.getFlux_entree().readLine();
					somme = parieur.getFlux_entree().readLine();
					bool = Boolean.parseBoolean(parieur.getFlux_entree().readLine());
					vq =  parieur.getFlux_entree().readLine();
					sommegg = parieur.getFlux_entree().readLine();
					
					txtOutPut.append(eqD +" - " + eqE +"\n"+
					"Buteur : "+eqGg +"\n"+
					"Cote : "+cote+"\n"+
					"Somme mise : "+somme+"\n");
					if(bool==false) txtOutPut.append("Etat : en cours..." +"\n");
					else txtOutPut.append("Etat : Termin�" +"\n"+
					"Resultat : " + vq + "\n"+
					"Somme gagn�e : "+ sommegg + "\n");
					txtOutPut.append("----------------------------------------------\n");
					
				}

			} catch (SocketException ex)  { 			
				System.err.println("CONNEXION INTERROMPUE");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}
	
	public class Quitter implements ActionListener  {
		public void actionPerformed(ActionEvent e) {
			parieur.getFlux_sortie().println(Choix.CLOSE);
			dispose();
		}
	}
	
	public class VoirCompo implements ActionListener {
		
		private int i;
		
		public VoirCompo(int i) {
			this.i = i;
		}
		public void actionPerformed(ActionEvent e) {
					
			String chaine;

			sBox.removeAllItems();
			tBox.removeAllItems();
			intMatch = i;

			try {
				players.setText("\n");
						
				parieur.getFlux_sortie().println(Choix.VOIRCOMPO);
				parieur.getFlux_sortie().println(equipeDomicileLabel[i].getText());
				parieur.getFlux_sortie().println(equipeExterieurLabel[i].getText());
				players.append(" "+equipeDomicileLabel[i].getText());
				
				chaine = parieur.getFlux_entree().readLine();
				
				if(chaine.equals("Equipe inexistante...")) {
					System.err.println("Equipe inexistante...");
				}
				
				for(int i=0; i<11;i++) {
					chaine = parieur.getFlux_entree().readLine();
					sBox.addItem(chaine);
					players.append(" - " + chaine);
				}
				
				players.append("\n");
				players.append(" "+equipeExterieurLabel[i].getText());

				for(int i=0; i<11;i++) {
					chaine = parieur.getFlux_entree().readLine();
					tBox.addItem(chaine);
					players.append(" - " + chaine);

					}
				} catch (SocketException ex)  { 			
					System.err.println("CONNEXION INTERROMPUE");
				} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
	}
	
	public class ParierMatch implements ActionListener {
		
		private int i;
		private Boolean bool;
		
		public ParierMatch(int i, Boolean bool) {
			this.i = i;
			this.bool = bool;
		}
		
		public void actionPerformed(ActionEvent e) {
						
			try {
				parieur.getFlux_sortie().println(Choix.PARIER);
				
				parieur.getFlux_sortie().println(parieur.getJournee());
					
				String chaine = parieur.getFlux_entree().readLine();

				if(chaine.equals("Impossible de trouver les matchs...")) {
					System.err.println("Journee n'existe pas...");
				}
				
				else if (chaine.equals("Match d�j� jou�...")) {
					System.err.println("Match deja jou�...");
					JOptionPane.showMessageDialog(getContentPane(), "Vous ne pouvez plus parier sur ce match");
				}
				
				else {
					parieur.getFlux_sortie().println(i);
					if((chaine=parieur.getFlux_entree().readLine()).equals("Match non trouv� dans la journ�e... Journ�e entre [0,10]")) {
						System.err.println("Match non trouv�...");
					}
					else {
						if(bool == true) {
							parieur.getFlux_sortie().println(parieur.getId());
							if((chaine=parieur.getFlux_entree().readLine()).equals("Parieur non retrouv� dans la BD...")) {
								System.err.println("Joueur non retrouv�...");
							}
							else {
								parieur.getFlux_sortie().println(equipeDomicileLabel[i].getText());
								if((chaine=parieur.getFlux_entree().readLine()).equals("Equipe non retrouv�e dans la BD...")) {
									System.err.println("Equipe non retrouv�e dans la BD");
								}
								else {
									parieur.getFlux_sortie().println(voteDTField[i].getText());
									if((chaine=parieur.getFlux_entree().readLine()).equals("Argent insuffisant...")) {
										System.err.println("Vous n'avez pas assez d'argent. Rechargez votre compte");
										JOptionPane.showMessageDialog(getContentPane(), "Veuillez rechargez votre compte");
									}
								}
							}
						}
						else {
							parieur.getFlux_sortie().println(parieur.getId());
							if((chaine=parieur.getFlux_entree().readLine()).equals("Parieur non retrouv� dans la BD...")) {
								System.err.println("Joueur non retrouv�...");
							}
							else {
								parieur.getFlux_sortie().println(equipeExterieurLabel[i].getText());
								if((chaine=parieur.getFlux_entree().readLine()).equals("Equipe non retrouv�e dans la BD...")) {
									System.err.println("Equipe non retrouv�e dans la BD");
								}
								else {
									parieur.getFlux_sortie().println(voteETField[i].getText());
									if((chaine=parieur.getFlux_entree().readLine()).equals("Argent insuffisant...")) {
										System.err.println("Vous n'avez pas assez d'argent. Rechargez votre compte");
										JOptionPane.showMessageDialog(getContentPane(), "Veuillez rechargez votre compte");
									}
								}
							}
						}
					}			
				}
			} catch (SocketException ex)  { 			
				System.err.println("CONNEXION INTERROMPUE");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
		}
	}
	
	public class ParierJoueur implements ActionListener {
		
		private Boolean bool;
		
		public ParierJoueur(Boolean bool) {
			this.bool = bool;
		}
		
		public void actionPerformed(ActionEvent e) {
			
			try {
				parieur.getFlux_sortie().println(Choix.PARIERJOUEUR);
				
				parieur.getFlux_sortie().println(parieur.getJournee());
					
				String chaine = parieur.getFlux_entree().readLine();

				if(chaine.equals("Impossible de trouver les matchs...")) {
					System.err.println("Journee n'existe pas...");
				}
				
				else if (chaine.equals("Match d�j� jou�...")) {
					System.err.println("Match deja jou�...");
					JOptionPane.showMessageDialog(getContentPane(), "Vous ne pouvez plus parier sur ce match");
				}
				
				else {
					parieur.getFlux_sortie().println(intMatch);
					if((chaine=parieur.getFlux_entree().readLine()).equals("Match non trouv� dans la journ�e... Journ�e entre [0,10]")) {
						System.err.println("Match non trouv�...");
					}
					else {
						if(bool == true) {
							parieur.getFlux_sortie().println(parieur.getId());
							if((chaine=parieur.getFlux_entree().readLine()).equals("Parieur non retrouv� dans la BD...")) {
								System.err.println("Joueur non retrouv�...");
							}
							else {
								parieur.getFlux_sortie().println(sBox.getSelectedItem());
								if((chaine=parieur.getFlux_entree().readLine()).equals("Joueur de l'�quipe non retrouv�e dans la BD...")) {
									System.err.println("Joueur de l'�quipe non retrouv�e dans la BD");
								}
								else {
									parieur.getFlux_sortie().println(voteparieurDField.getText());
									if((chaine=parieur.getFlux_entree().readLine()).equals("Vous n'avez pas assez d'argent. Rechargez votre compte")) {
										System.err.println("Vous n'avez pas assez d'argent. Rechargez votre compte");
									}
								}
							}
						}
						else {
							parieur.getFlux_sortie().println(parieur.getId());
							if((chaine=parieur.getFlux_entree().readLine()).equals("Parieur non retrouv� dans la BD...")) {
								System.err.println("Joueur non retrouv�...");
							}
							else {
								parieur.getFlux_sortie().println(tBox.getSelectedItem());
								if((chaine=parieur.getFlux_entree().readLine()).equals("Joueur de l'�quipe non retrouv�e dans la BD...")) {
									System.err.println("Joueur de l'�quipe non retrouv�e dans la BD");
								}
								else {
									parieur.getFlux_sortie().println(voteparieurEField.getText());
									if((chaine=parieur.getFlux_entree().readLine()).equals("Vous n'avez pas assez d'argent. Rechargez votre compte")) {
										System.err.println("Vous n'avez pas assez d'argent. Rechargez votre compte");
									}
								}
							}
						}
					}			
				}
			} catch (SocketException ex)  { 			
				System.err.println("CONNEXION INTERROMPUE");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	
		}
	}
	
	public void requestConnexion() throws IOException, ClassNotFoundException, SQLException {
		
		try {
			parieur.getFlux_sortie().println(Choix.ANTHENTIFICATION);

			JTextField idField = new JTextField();
			JTextField mdpField = new JTextField();

			Object[] inputFields = {"Identifiant", idField,
					"Mot de passe", mdpField};
        
			int option = JOptionPane.showConfirmDialog(this, inputFields, "Connexion", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
			Boolean bool = true;
			
			while (option==JOptionPane.OK_OPTION && bool.equals(true)) {
    
				this.parieur.setPseudo(idField.getText());
        		this.parieur.setMdp(mdpField.getText());
        		this.parieur.setPret(false);
        		this.parieur.setJournee(1);
        		
        		this.parieur.getFlux_sortie().println(idField.getText());	
        		this.parieur.getFlux_sortie().println(mdpField.getText());	
        		
        		String entree = this.parieur.getFlux_entree().readLine();
        		
        		if(entree.equals("erreur")) {
        			System.exit(0);
        		}
        		else {
            		this.parieur.setId(Long.parseLong(entree));
            		bool = false;
        		}
			}
			
			if(option==JOptionPane.CLOSED_OPTION) {
    			System.exit(0);
			}
		} catch (SocketException ex)  { 			
			System.err.println("CONNEXION INTERROMPUE");	
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public void requestMatch(int journee) {
		
		try {
			this.parieur.getFlux_sortie().println(Choix.VOIR);
			this.parieur.getFlux_sortie().println(journee);
			
			String chaine = this.parieur.getFlux_entree().readLine();

			if(chaine.equals("Impossible de trouver les matchs...")) {
				System.err.println("Journee n'existe pas");
			}
			else {
				for(int i=0; i<10; i++) {
				
					chaine = this.parieur.getFlux_entree().readLine();
					equipeDomicileLabel[i].setText(chaine);
					chaine = this.parieur.getFlux_entree().readLine();
					equipeExterieurLabel[i].setText(chaine);
					chaine = this.parieur.getFlux_entree().readLine();
					scoreDomicileLabel[i].setText(chaine);
					chaine = this.parieur.getFlux_entree().readLine();
					scoreExterieurLabel[i].setText(" "+chaine);
					chaine = this.parieur.getFlux_entree().readLine();
				
					if((Boolean.parseBoolean(chaine))==false) {
						etatLabel[i].setText("NJ");
					}
					else {
						etatLabel[i].setText("J");
					}
				
					chaine = this.parieur.getFlux_entree().readLine();
					idLabel[i].setText(chaine.toUpperCase());
				}		
			}
		} catch (SocketException ex)  { 			
			System.err.println("CONNEXION INTERROMPUE");	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
	public void addPanel(JPanel[] jpanel) {
		for(int i=0;i<10;i++) {
			jpanel[i] = new JPanel();
		}
	}
	
	public void addLabel(JLabel[] jlabel) {
		for(int i=0;i<10;i++) {
			jlabel[i] = new JLabel();
		}
	}
	
	public void addButton(JButton[] myButton) {
		for(int i=0;i<10;i++) {
			myButton[i] = new MyButton("Vote");
		}
	}
	
	public void addTextField(JTextField[] jtextField) {
		for(int i=0;i<10;i++) {
			jtextField[i] = new JTextField(10);
		}
	}
	
	public static void main(String[] args) {
		// On rentre en param�tres l'adresse IP ainsi que le PORT.
		Socket socket;
		try {
			socket = new Socket("localhost",5000);
			new InterfaceGraphique("Pari", socket);
			
		} catch (ConnectException e) {
			System.err.println("PORT ferm�. Connexion impossible");
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			System.err.println("Erreur lors de la rentr�e du param�tre PORT. PORT incorrect.");
		} catch (UnknownHostException e) {
			System.err.println("Connexion impossible, rentrez une nouvelle adresse IP");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
