package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import data.Joueur;

public class JoueurDAO{
	
    public Connection connect = ConnexionPostgreSQL.getInstance();

	public Joueur create(Joueur obj) {
		
		try {
			
			ResultSet result = this.connect.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE
					).executeQuery("SELECT NEXTVAL('joueur_id_seq') as id");
			
			if(result.first()) {
				int id = result.getInt("id");
				PreparedStatement prepare = this.connect.prepareStatement(
						"INSERT INTO joueur (joueur_id, joueur_pseudo, joueur_mdp, joueur_capital, joueur_pret, joueur_journee) "
						+ "VALUES(?,?,?,?,?,?)");
				
				prepare.setLong(1, id);
				prepare.setString(2, obj.getPseudo());
				prepare.setString(3, obj.getMdp());
				prepare.setFloat(4, obj.getCapital());
				prepare.setBoolean(5, obj.isPret());
				prepare.setInt(6,obj.getJournee());
				prepare.executeUpdate();
				obj = this.find(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return obj;
		
	}
	
	public Joueur find(long id) {
		
		Joueur jr = new Joueur();
		
		try {
			ResultSet result = this.connect.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE).executeQuery(
					"SELECT * FROM joueur WHERE joueur_id = " + id);
			
			if(result.first()) 
				jr = new Joueur (id,
						result.getString("joueur_pseudo"),
						result.getString("joueur_mdp"),
						result.getFloat("joueur_capital"),
						result.getBoolean("joueur_pret"),
						result.getInt("joueur_journee"));
		} catch (SQLException e) {
            e.printStackTrace();
		}
		return jr;
	}
	
	public int count(String pseudo) {
		
		int i = 0;
		try {
			ResultSet result = this.connect.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE).executeQuery(
					"SELECT COUNT(*) FROM joueur WHERE joueur_pseudo = " + pseudo);
			if(result.next())
			i = result.getInt(1);

		} catch (SQLException e) {
            e.printStackTrace();
		}
		return i;
	}
	
	public Joueur find(String pseudo) {
		Joueur jr = new Joueur();

		try {
			ResultSet result = this.connect.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE).executeQuery(
					"SELECT * FROM joueur WHERE joueur_pseudo = '" + pseudo+"'");
			
			if(result.first()) 
				jr = new Joueur (result.getLong("joueur_id"),
						result.getString("joueur_pseudo"),
						result.getString("joueur_mdp"),
						result.getFloat("joueur_capital"),
						result.getBoolean("joueur_pret"),
						result.getInt("joueur_journee"));
		} catch (SQLException e) {
            e.printStackTrace();
		}
		return jr;
	}
	
	public Joueur update(Joueur obj) {
		try {
            
            this .connect    
                 .createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE, 
                    ResultSet.CONCUR_UPDATABLE
                 ).executeUpdate(
                    "UPDATE joueur SET joueur_pseudo = '" + obj.getPseudo() + "', joueur_mdp" + obj.getMdp() +
                    "', joueur_capital = '" + obj.getCapital() + "', joueur_pret = '" + obj.isPret() + "', joueur_journee ='" + obj.getJournee() +
                    " WHERE joueur_id = " + obj.getId()
                 );
        
        obj = this.find(obj.getId());
        
		} catch (SQLException e) {
            e.printStackTrace();
		}
    
		return obj;
	}
	
	public Joueur updateReady(Joueur obj) {
		try {
            
            this .connect    
                 .createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE, 
                    ResultSet.CONCUR_UPDATABLE
                 ).executeUpdate(
                    "UPDATE joueur SET joueur_pret = 'true'");
                
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
                    "UPDATE joueur SET joueur_journee = ((SELECT joueur_journee FROM joueur WHERE joueur_id = '1')+1)");
                
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
					"SELECT joueur_journee FROM joueur WHERE joueur_id = '1'");
			
			if(result.first()) 
				i = result.getInt("joueur_journee");
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
                    "UPDATE joueur SET joueur_capital = (joueur_capital-"+somme+ ") WHERE joueur_id = " + id);
        
    } catch (SQLException e) {
            e.printStackTrace();
    }
    
	}	
}
