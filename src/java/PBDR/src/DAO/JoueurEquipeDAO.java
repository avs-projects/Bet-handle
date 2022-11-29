package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import data.JoueurEquipe;

public class JoueurEquipeDAO {

	 public Connection connect = ConnexionPostgreSQL.getInstance();
	    
	    public JoueurEquipe create(JoueurEquipe obj) {
			
			try {
				
				ResultSet result = this.connect.createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE
						).executeQuery("SELECT NEXTVAL('joueureq_id_seq') as id");
				
				if(result.first()) {
					int id = result.getInt("id");
					PreparedStatement prepare = this.connect.prepareStatement(
							"INSERT INTO joueur_equipe (joueureq_id, joueureq_nom, joueureq_poste, equipe_id) "
							+ "VALUES(?,?,?,?)");
					
					prepare.setLong(1,id);
					prepare.setString(2,obj.getNom());
					prepare.setString(3,obj.getPoste());
					prepare.setLong(4,obj.getEquipe_id());
					prepare.executeUpdate();
					
					obj = this.find(id);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			return obj;
			
		}
		
		public JoueurEquipe find(long id) {
			
			JoueurEquipe jr = null;
			
			try {
				ResultSet result = this.connect.createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE).executeQuery(
						"SELECT * FROM joueur_equipe WHERE joueureq_id = " + id);
				
				if(result.first()) 
					jr = new JoueurEquipe (id,
							result.getString("joueureq_nom"),
							result.getString("joueureq_poste"),
							result.getLong("equipe_id"));
			} catch (SQLException e) {
	            e.printStackTrace();
			}
			return jr;
		}
		
		public JoueurEquipe findPlayer(String nom) {
			
			JoueurEquipe jr = null;
			
			try {
				ResultSet result = this.connect.createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE).executeQuery(
						"SELECT * FROM joueur_equipe WHERE joueureq_nom = '" +nom+"'");
				
				if(result.first()) 
					jr = new JoueurEquipe (result.getLong("joueureq_id"),
							nom,
							result.getString("joueureq_poste"),
							result.getLong("equipe_id"));
			} catch (SQLException e) {
	            e.printStackTrace();
			}
			return jr;
		}
		
		public JoueurEquipe update(JoueurEquipe obj) {
			try {
	            
	            this .connect    
	                 .createStatement(
	                    ResultSet.TYPE_SCROLL_INSENSITIVE, 
	                    ResultSet.CONCUR_UPDATABLE
	                 ).executeUpdate(
	                    "UPDATE joueur_equipe SET joueureq = '" + obj.getNom()+ "', joueureq_poste ='" + obj.getPoste()+
	                    "', equipe_id = '" + obj.getEquipe_id() +"' WHERE joueureq_id = " + obj.getId());
	        
	        obj = this.find(obj.getId());
	        
	    } catch (SQLException e) {
	            e.printStackTrace();
	    }
	    
	    return obj ;
		}
		
		public JoueurEquipe findViaEq(long id, String poste) {
			
			JoueurEquipe jr = new JoueurEquipe();
				
			try {
				ResultSet result = this.connect.createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE).executeQuery(
						"SELECT * FROM joueur_equipe WHERE joueureq_poste ='" + poste +"' AND equipe_id =" + id + " ORDER BY RANDOM()");
				
				if(result.first()) 
					jr = new JoueurEquipe (result.getLong("joueureq_id"),
							result.getString("joueureq_nom"),
							result.getString("joueureq_poste"),
							result.getLong("equipe_id"));
			} catch (SQLException e) {
	            e.printStackTrace();
			}
			return jr;
		}
		
		public ArrayList<JoueurEquipe> find(String name) {
			
			ArrayList<JoueurEquipe> list = new ArrayList<JoueurEquipe>();
			JoueurEquipe jr = null;
			try {
				ResultSet result = this.connect.createStatement(
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_UPDATABLE).executeQuery(
						"SELECT * FROM joueur_equipe WHERE equipe_id = (SELECT equipe_id FROM equipe WHERE equipe_nom = '" + name +"')");
				
				while (result.next()) {
					jr = new JoueurEquipe (result.getLong("joueureq_id"),
							result.getString("joueureq_nom"),
							result.getString("joueureq_poste"),
							result.getLong("equipe_id"));
					list.add(jr);
				}
			} catch (SQLException e) {
	            e.printStackTrace();
			}
			return list;
		}
}
