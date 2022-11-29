package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import data.Match;
import data.Pari;
import data.PariJoueur;

public class PariJoueurDAO {
	
	public Connection connect = ConnexionPostgreSQL.getInstance();

    public PariJoueur create(PariJoueur obj) {
		try {
			
			ResultSet result = this.connect.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE
					).executeQuery("SELECT NEXTVAL('pari_id_seq') as id");
			
			if(result.first()) {
				int id = result.getInt("id");
				PreparedStatement prepare = this.connect.prepareStatement(
						"INSERT INTO parijoueur (pari_id, pari_cote, pari_somme, pari_somme_gg"
						+ ",pari_joue, id_parieur, matchf_id, pari_buteur, pari_resultat) "
						+ "VALUES(?,?,?,?,?,?,?,?,?)");
				
				prepare.setLong(1,id);
				prepare.setFloat(2,obj.getCote());
				prepare.setFloat(3,obj.getSomme());
				prepare.setFloat(4,obj.getSomme_gg());
				prepare.setBoolean(5, obj.isJoue());
				prepare.setLong(6, obj.getId_parieur());
				prepare.setLong(7,obj.getMatchf_id());
				prepare.setString(8, obj.getButeur());
				prepare.setInt(9, obj.getResultat());


				prepare.executeUpdate();
				obj = this.find(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return obj;
		
	}
	
	public PariJoueur find(long id) {
		
		PariJoueur pr = new PariJoueur();
		
		try {
			ResultSet result = this.connect.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE).executeQuery(
					"SELECT * FROM parijoueur WHERE pari_id = " + id);
			
			if(result.first()) 
				pr = new PariJoueur (id,
						result.getFloat("pari_cote"),
						result.getFloat("pari_somme"),
						result.getFloat("pari_somme_gg"),
						result.getBoolean("pari_joue"),
						result.getLong("id_parieur"),
						result.getLong("matchf_id"),
						result.getString("pari_buteur"),
						result.getInt("pari_resultat"));

		} catch (SQLException e) {
            e.printStackTrace();
		}
		return pr;
	}
	
	public PariJoueur update(PariJoueur obj) {
		try {
            
            this .connect    
                 .createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE, 
                    ResultSet.CONCUR_UPDATABLE
                 ).executeUpdate(
                    "UPDATE parimatch SET pari_cote = '" + obj.getCote() + "', pari_somme ='" + obj.getSomme()+
                    "', pari_buteur = '" + obj.getButeur() + "', pari_resultat = '" + obj.getResultat()+ 
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
	
	public ArrayList<PariJoueur> getParis(long id) {
		
		ArrayList<PariJoueur> paris = new ArrayList<PariJoueur>();
		PariJoueur pr = new PariJoueur();
		 
		try {
			ResultSet result = this.connect.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE).executeQuery(
					"SELECT * FROM parijoueur WHERE id_parieur = " + id);
			
			while (result.next()) {
				pr = new PariJoueur (id,
						result.getFloat("pari_cote"),
						result.getFloat("pari_somme"),
						result.getFloat("pari_somme_gg"),
						result.getBoolean("pari_joue"),
						result.getLong("id_parieur"),
						result.getLong("matchf_id"),
						result.getString("pari_buteur"),
						result.getInt("pari_resultat"));
			paris.add(pr);
			}
		} catch (SQLException e) {
            e.printStackTrace();
		}
		return paris;
	}
	
	public void pariFini (Match m) {
		try {
			
				this .connect    
		           .createStatement(
		              ResultSet.TYPE_SCROLL_INSENSITIVE, 
		              ResultSet.CONCUR_UPDATABLE
		           ).executeUpdate(
		              "UPDATE parijoueur SET pari_joue = 'true', pari_resultat = (SELECT COUNT(*) FROM buteur WHERE buteur_nom = pari_buteur AND matchf_id = " + m.getId() +") WHERE matchf_id = "+m.getId());
				
				this .connect    
		           .createStatement(
		              ResultSet.TYPE_SCROLL_INSENSITIVE, 
		              ResultSet.CONCUR_UPDATABLE
		           ).executeUpdate(
		              "UPDATE parijoueur SET pari_somme_gg = (pari_cote*pari_somme) WHERE pari_resultat>0 AND matchf_id = "+m.getId());
				
			} catch (SQLException e) {
            e.printStackTrace();
		}
	}
	
	public void distriGain(Match match) {
		try {
			ResultSet result = this.connect.createStatement(
				ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_UPDATABLE).executeQuery(
				"SELECT * FROM parijoueur WHERE matchf_id = " + match.getId());
			
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