package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import data.Buteur;
import data.Equipe;
import data.JoueurEquipe;
import data.Match;

public class MatchDAO{

    public Connection connect = ConnexionPostgreSQL.getInstance();

	public Match create(Match obj) {
		
		try {
			
			ResultSet result = this.connect.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE
					).executeQuery("SELECT NEXTVAL('matchf_id_seq') as id");
			
			if(result.first()) {
				int id = result.getInt("id");
				PreparedStatement prepare = this.connect.prepareStatement(
						"INSERT INTO matchf (matchf_id, matchf_equipe"
						+ "domicile, matchf_equipe_exterieur, matchf_score_domicile,"
						+ "match_score_exterieur, matchf_joue, matchf_journee,equipe_id_d,equipe_id_e) "
						+ "VALUES(?,?,?,?,?,?,?)");
				
				prepare.setLong(1, id);
				prepare.setString(2, obj.getEquipeD());
				prepare.setString(3, obj.getEquipeE());
				prepare.setInt(4, obj.getScoreD());
				prepare.setInt(5, obj.getScoreE());
				prepare.setBoolean(6, obj.isJoue());
				prepare.setInt(7,  obj.getJournee());
				prepare.setLong(8,obj.getEquipeD_id());
				prepare.setLong(9,obj.getEquipeE_id());
				prepare.executeUpdate();
				obj = this.find(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return obj;
		
	}
	
	public Match find(long id) {
		
		Match mf = new Match();
		
		try {
			ResultSet result = this.connect.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE).executeQuery(
					"SELECT * FROM matchf WHERE matchf_id = " + id);
			
			if(result.first()) 
				mf = new Match (id,
						result.getString("matchf_equipe_domicile"),
						result.getString("matchf_equipe_exterieur"),
						result.getInt("matchf_score_domicile"),
						result.getInt("matchf_score_exterieur"),
						result.getBoolean("matchf_joue"),
						result.getInt("matchf_journee"),
						result.getLong("equipe_id_d"),
						result.getLong("equipe_id_e"));
		} catch (SQLException e) {
            e.printStackTrace();
		}
		return mf;
	}
	
	public Match update(Match obj) {
		try {
            
            this .connect    
                 .createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE, 
                    ResultSet.CONCUR_UPDATABLE
                 ).executeUpdate(
                    "UPDATE matchf SET matchf_equipe_domicile = '" + obj.getEquipeD() + "','"
                    		+ "matchf_equipe_exterieur = "+ obj.getEquipeE() +"','" 
                    		+ "matchf_score_domicile = "+ obj.getScoreD() +"','"
                    		+ "matchf_score_exterieur = "+ obj.getScoreE() + "','"
                    		+ "matchf_joue ="+ obj.isJoue() + "','"
                    		+ "matchf_journee =" + obj.getJournee() + "','"
                    		+ "equipe_id_d = "+ obj.getEquipeD_id() +"','"
                    		+ "equipe_id_e = "+ obj.getEquipeE_id() +
                    " WHERE matchf_id = " + obj.getId()
                 );
        
        obj = this.find(obj.getId());
        
		} catch (SQLException e) {
            e.printStackTrace();
		}
    
    return obj;
	}
	
	public ArrayList<Match> search(int journee) {
		
		ArrayList<Match> matchs = new ArrayList<Match>();
		Match mf = new Match();
		
		try {
			ResultSet result = this.connect.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE).executeQuery(
					"SELECT * FROM matchf WHERE matchf_journee = " + journee + " ORDER BY matchf_id");
			
			while (result.next()) {
				mf = new Match (result.getLong("matchf_id"),
						result.getString("matchf_equipe_domicile"),
						result.getString("matchf_equipe_exterieur"),
						result.getInt("matchf_score_domicile"),
						result.getInt("matchf_score_exterieur"),
						result.getBoolean("matchf_joue"),
						result.getInt("matchf_journee"),
						result.getLong("equipe_id_d"),
						result.getLong("equipe_id_e"));
				matchs.add(mf);
			}
		} catch (SQLException e) {
            e.printStackTrace();
		}
		return matchs;
		
	}
	
	public Match playMatch(Match match, ButeurDAO buteurDAO, JoueurEquipeDAO joueurDAO, EquipeDAO equipeDAO) {

		try {
			
			ResultSet result1 = this.connect.createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE).executeQuery(
						"SELECT equipe_note FROM equipe WHERE equipe_id = " + match.getEquipeD_id());
			ResultSet result2 = this.connect.createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE).executeQuery(
						"SELECT equipe_note FROM equipe WHERE equipe_id = " + match.getEquipeE_id());
			
			int noteD = 0;
			int noteE = 0;
			if(result1.first()) {
				noteD = result1.getInt("equipe_note");
			}
			if(result2.first()) {
				noteE = result2.getInt("equipe_note");
			}
	
			int scoreD = 0;
			int scoreE = 0;

			Equipe equipe;
			JoueurEquipe joueur;
			String buteur;			
				
			for(int i=0; i<90; i++) {
				if((int)(Math.random()*30)==0) {
					if(noteD>noteE) {
						int a = 100*(noteD+(noteD-noteE)*5)
									/((noteD+noteE));
						int b = 100*(noteE-(noteD-noteE)*5)
									/((noteD+noteE));
													
						int random = (int)(Math.random()*a);
						int random2 = (int)(Math.random()*b);
						int random3 = (int)(Math.random()*10);
						
						if(random3<6) buteur = "ATT";
						else if(random3<8) buteur = "MIL";
						else buteur = "DEF";
	
						if(random >= random2 ) {
							scoreD = scoreD+1; 
							equipe = equipeDAO.find(match.getEquipeD_id());
						}
						else {
							scoreE = scoreE+1;
							equipe = equipeDAO.find(match.getEquipeE_id());
						}
					
					}	
					else {
						int a = 100*(noteD-(noteE-noteD)*5)
									/(noteD+noteE);
						int b = 100*(noteE+(noteE-noteD)*5)
									/(noteD+noteE);
							
						int random = (int)(Math.random()*a);
						int random2 = (int)(Math.random()*b);
						int random3 = (int)(Math.random()*10);
						
						if(random3<6) buteur = "ATT";
						else if(random3<8) buteur = "MIL";
						else buteur = "DEF";
							
						if(random >= random2 ) {
							scoreD = scoreD+1;
							equipe = equipeDAO.find(match.getEquipeD_id());
						}
						else {
							scoreE = scoreE+1;
							equipe = equipeDAO.find(match.getEquipeE_id());
							
						}		
						
					}
					
					joueur = joueurDAO.findViaEq(equipe.getId(), buteur);
					int tmp = buteurDAO.count(joueur, match);
					if(tmp==0)
					buteurDAO.create(new Buteur(0,joueur.getNom(),1,match.getId(),joueur.getId()));
					else 
					buteurDAO.update(joueur);

				}
			}
			
			 this .connect    
             .createStatement(
                ResultSet.TYPE_SCROLL_INSENSITIVE, 
                ResultSet.CONCUR_UPDATABLE
             ).executeUpdate(
                "UPDATE matchf SET matchf_score_domicile = '" + scoreD + "',matchf_score_exterieur ='" + scoreE +
                "',matchf_joue = " + true + " WHERE matchf_id = " + match.getId());
			
		match = this.find(match.getId());
		
		} catch (SQLException e) {
            e.printStackTrace();
		}
		return match; 
	}
	
	public int findMaxJournee() {
		
		int i = 0;
		
		try {
			ResultSet result = this.connect.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE).executeQuery(
					"SELECT MAX(matchf_journee) FROM matchf");
			
			if(result.next()) i = result.getInt(1); 
				
		} catch (SQLException e) {
            e.printStackTrace();
		}
		return i;
	}

}
