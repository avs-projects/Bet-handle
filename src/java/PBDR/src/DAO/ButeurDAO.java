package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import data.Buteur;
import data.JoueurEquipe;
import data.Match;

public class ButeurDAO {
	
    public Connection connect = ConnexionPostgreSQL.getInstance();
    
    public Buteur create(Buteur obj) {
			
		try {
			
			ResultSet result = this.connect.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE
					).executeQuery("SELECT NEXTVAL('buteur_id_seq') as id");
			
			if(result.first()) {
				int id = result.getInt("id");
				PreparedStatement prepare = this.connect.prepareStatement(
						"INSERT INTO buteur (buteur_id, buteur_nom, buteur_nbbut, matchf_id, joueureq_id) "
						+ "VALUES(?,?,?,?,?)");
				
				prepare.setLong(1,id);
				prepare.setString(2,obj.getNom());
				prepare.setInt(3,obj.getNbButs());
				prepare.setLong(4,obj.getMatchf_id());
				prepare.setLong(5,obj.getJoueureq_id());
				prepare.executeUpdate();
				
				obj = this.find(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return obj;
		
	}
	
	public Buteur find(long id) {
		
		Buteur bu = null;
		
		try {
			ResultSet result = this.connect.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE).executeQuery(
					"SELECT * FROM buteur WHERE joueureq_id = " + id);
			
			if(result.first()) 
				bu = new Buteur (id,
						result.getString("buteur_nom"),
						result.getInt("buteur_nbbut"),
						result.getLong("matchf_id"),
						result.getLong("joueureq_id"));
		} catch (SQLException e) {
            e.printStackTrace();
		}
		return bu;
	}
	
	public void update(JoueurEquipe obj) {
		try {
            
            this .connect    
                 .createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE, 
                    ResultSet.CONCUR_UPDATABLE
                 ).executeUpdate(
                    "UPDATE buteur SET buteur_nbbut = (buteur_nbbut+1)" + "WHERE joueureq_id = " + obj.getId());
        
		} catch (SQLException e) {
            e.printStackTrace();
		} 
	}
	
	public int count(JoueurEquipe joueur, Match match) {
		
		int i=0;
		try {
			ResultSet result = this.connect.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE).executeQuery(
					"SELECT COUNT(*) FROM buteur WHERE joueureq_id = " + joueur.getId() +" AND matchf_id =" + match.getId());
		
		result.next();
		i = result.getInt(1);
		
		} catch (SQLException e) {
            e.printStackTrace();
		}
		return i;
	}

}
