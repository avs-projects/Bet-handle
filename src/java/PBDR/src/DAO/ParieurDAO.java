package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import data.Parieur;

public class ParieurDAO{
	
    public Connection connect = ConnexionPostgreSQL.getInstance();

	public Parieur create(Parieur obj) {
		
		try {
			
			ResultSet result = this.connect.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE
					).executeQuery("SELECT NEXTVAL('parieur_id_seq') as id");
			
			if(result.first()) {
				int id = result.getInt("id");
				PreparedStatement prepare = this.connect.prepareStatement(
						"INSERT INTO joueur (id_parieur, email, mdp, nom, prenom, pseudo, capital, date_naissance, pret, journee) "
						+ "VALUES(?,?,?,?,?,?,?,?,?,?)");
				
				prepare.setLong(1, id);
				prepare.setString(2, obj.getEmail());
				prepare.setString(3, obj.getMdp());
				prepare.setString(4, obj.getNom());
				prepare.setString(5, obj.getPrenom());
				prepare.setString(6, obj.getPseudo());
				prepare.setFloat(7, obj.getCapital());
				prepare.setDate(8, obj.getNaissance());
				prepare.setBoolean(9,  obj.getPret());
				prepare.setInt(10, obj.getJournee());
				prepare.executeUpdate();
				obj = this.find(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return obj;
		
	}
	
	public Parieur find(long id) {
		
		Parieur pr = new Parieur();
		
		try {
			ResultSet result = this.connect.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE).executeQuery(
					"SELECT * FROM parieur WHERE id_parieur = " + id);
			
			if(result.first()) 
				pr = new Parieur (id,
						result.getString("email"),
						result.getString("mdp"),
						result.getString("prenom"),
						result.getString("nom"),
						result.getString("pseudo"),
						result.getInt("capital"),
						result.getDate("date_naissance"),
						result.getBoolean("pret"),
						result.getInt("journee"));
		} catch (SQLException e) {
            e.printStackTrace();
		}
		return pr;
	}
	
	public int count(String pseudo) {
		
		int i = 0;
		try {
			ResultSet result = this.connect.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE).executeQuery(
					"SELECT COUNT(*) FROM parieur WHERE pseudo = " + pseudo);
			if(result.next())
			i = result.getInt(1);

		} catch (SQLException e) {
            e.printStackTrace();
		}
		return i;
	}
	
	public Parieur find(String pseudo) {
		Parieur pr = new Parieur();

		try {
			ResultSet result = this.connect.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE).executeQuery(
					"SELECT * FROM parieur WHERE pseudo = '" + pseudo+"'");
			
			if(result.first()) 
				pr = new Parieur (result.getLong("id_parieur"),
						result.getString("email"),
						result.getString("mdp"),
						result.getString("prenom"),
						result.getString("nom"),
						result.getString("pseudo"),
						result.getInt("capital"),
						result.getDate("date_naissance"),
						result.getBoolean("pret"),
						result.getInt("journee"));
		} catch (SQLException e) {
            e.printStackTrace();
		}
		return pr;
	}
	
	public Parieur updateReady(Parieur obj) {
		try {
            
            this .connect    
                 .createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE, 
                    ResultSet.CONCUR_UPDATABLE
                 ).executeUpdate(
                    "UPDATE parieur SET pret = 'true'");
                
            obj = this.find(obj.getId());

		} catch (SQLException e) {
            e.printStackTrace();
		}
	    return obj;

	}
	
	public void incrementJournee() {
		try {
            
            this .connect    
                 .createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE, 
                    ResultSet.CONCUR_UPDATABLE
                 ).executeUpdate(
                    "UPDATE parieur SET journee = ((SELECT journee FROM parieur WHERE id_parieur = '1')+1)");
                
		} catch (SQLException e) {
            e.printStackTrace();
		}
	}
	
	public int getJournee() {
		int i = 0;
		
		try {
			ResultSet result = this.connect.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE).executeQuery(
					"SELECT journee FROM parieur WHERE id_parieur = '1'");
			
			if(result.first()) 
				i = result.getInt("journee");
		} catch (SQLException e) {
            e.printStackTrace();
		}
		return i;
	}
	
	public void updateCapital(int somme, long id) {
		try {
            
            this .connect    
                 .createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE, 
                    ResultSet.CONCUR_UPDATABLE
                 ).executeUpdate(
                    "UPDATE parieur SET capital = (capital-"+somme+ ") WHERE id_parieur = " + id);
        
    } catch (SQLException e) {
            e.printStackTrace();
    }
    
	}
	
}
