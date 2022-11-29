package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import data.Match;
import data.Pari;
import data.PariMatch;

public class PariDAO{
	
    public Connection connect = ConnexionPostgreSQL.getInstance();

    public PariMatch create(PariMatch obj) {
		try {
			
			ResultSet result = this.connect.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE
					).executeQuery("SELECT NEXTVAL('pari_id_seq') as id");
			
			if(result.first()) {
				int id = result.getInt("id");
				PreparedStatement prepare = this.connect.prepareStatement(
						"INSERT INTO parimatch (pari_id, pari_cote, pari_somme, pari_somme_gg"
						+ ",pari_joue, id_parieur, matchf_id, pari_victoire, pari_gagnant) "
						+ "VALUES(?,?,?,?,?,?,?,?,?)");
				
				prepare.setLong(1,id);
				prepare.setFloat(2,obj.getCote());
				prepare.setFloat(3,obj.getSomme());
				prepare.setFloat(4,obj.getSomme_gg());
				prepare.setBoolean(5, obj.isJoue());
				prepare.setLong(6, obj.getId_parieur());
				prepare.setLong(7,obj.getMatchf_id());
				prepare.setString(8, obj.getVictoire());
				prepare.setString(9, obj.getGagnant());
				prepare.executeUpdate();
				obj = this.find(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return obj;
		
	}
	
	public PariMatch find(long id) {
		
		PariMatch pr = new PariMatch();
		
		try {
			ResultSet result = this.connect.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE).executeQuery(
					"SELECT * FROM parimatch WHERE pari_id = " + id);
			
			if(result.first()) 
				pr = new PariMatch (id,
						result.getFloat("pari_cote"),
						result.getFloat("pari_somme"),
						result.getFloat("pari_somme_gg"),
						result.getBoolean("pari_joue"),
						result.getLong("id_parieur"),
						result.getLong("matchF_id"),
						result.getString("pari_victoire"),
						result.getString("pari_gagnant"));
		} catch (SQLException e) {
            e.printStackTrace();
		}
		return pr;
	}
	
	public PariMatch update(PariMatch obj) {
		try {
            
            this .connect    
                 .createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE, 
                    ResultSet.CONCUR_UPDATABLE
                 ).executeUpdate(
                    "UPDATE parimatch SET pari_cote = '" + obj.getCote() + "', pari_somme ='" + obj.getSomme()+
                    "', pari_victoire = '" + obj.getVictoire() + "', pari_gagnant = '" + obj.getGagnant() + 
                    "'pari_somme_gg ='"+obj.getSomme_gg() + "', pari_joue = '"+ obj.isJoue() + "', id_parieur ='" + obj.getId_parieur()+
                 	"', matchf_id ='" + obj.getMatchf_id() + " WHERE pari_id = " + obj.getId());
        
        obj = this.find(obj.getId());
        
    } catch (SQLException e) {
            e.printStackTrace();
    }
    
    return obj ;
	}
	
	public void delete(Pari obj) {
		
	}
	
	public ArrayList<PariMatch> getParis(long id) {
		
		ArrayList<PariMatch> paris = new ArrayList<PariMatch>();
		PariMatch pr = new PariMatch();
		
		try {
			ResultSet result = this.connect.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE).executeQuery(
					"SELECT * FROM parimatch WHERE id_parieur = " + id);
			
			while (result.next()) {
				pr = new PariMatch (id,
						result.getFloat("pari_cote"),
						result.getFloat("pari_somme"),
						result.getFloat("pari_somme_gg"),
						result.getBoolean("pari_joue"),
						result.getLong("id_parieur"),
						result.getLong("matchF_id"),
						result.getString("pari_victoire"),
						result.getString("pari_gagnant"));
			paris.add(pr);
			}
		} catch (SQLException e) {
            e.printStackTrace();
		}
		return paris;
	}
	
	public void pariFini (Match match) {
		
		String gagnant;
		
		try {
			
			if(match.getScoreD()>match.getScoreE()) {
				gagnant = match.getEquipeD();
			}
			else if (match.getScoreD()<match.getScoreE()){
				gagnant = match.getEquipeE();
			}
			else {
				gagnant = "Nul";
			}
			
			this .connect    
            .createStatement(
               ResultSet.TYPE_SCROLL_INSENSITIVE, 
               ResultSet.CONCUR_UPDATABLE
            ).executeUpdate(
               "UPDATE parimatch SET pari_gagnant = '"+gagnant+"', pari_joue = 'true' WHERE matchf_id = " + match.getId());
			
			ResultSet result = this.connect.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE).executeQuery(
					"SELECT pari_gagnant, pari_victoire FROM parimatch WHERE matchf_id = " + match.getId() + "ORDER BY pari_id");
				
			while (result.next()) 
					this .connect    
			           .createStatement(
			              ResultSet.TYPE_SCROLL_INSENSITIVE, 
			              ResultSet.CONCUR_UPDATABLE
			           ).executeUpdate(
			              "UPDATE parimatch SET pari_somme_gg = (pari_cote*pari_somme) WHERE pari_victoire = pari_gagnant");
				
			} catch (SQLException e) {
            e.printStackTrace();
		}
	}
	
	public void distriGain(Match match) {
		try {
			ResultSet result = this.connect.createStatement(
				ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_UPDATABLE).executeQuery(
				"SELECT pari_somme_gg, id_parieur FROM parimatch WHERE matchf_id = " + match.getId());
			
			while (result.next()) 
					this .connect    
			           .createStatement(
		              ResultSet.TYPE_SCROLL_INSENSITIVE, 
		              ResultSet.CONCUR_UPDATABLE
			        		   ).executeUpdate(
		              "UPDATE parieur SET capital = (capital+"+ result.getFloat("pari_somme_gg")+") WHERE id_parieur = '" +result.getInt("id_parieur")+"'");
			
		} catch (SQLException e) {
        e.printStackTrace();
	}
	}
}
