package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import data.Equipe;

public class EquipeDAO{
	
    public Connection connect = ConnexionPostgreSQL.getInstance();

	public Equipe create(Equipe obj) {
		
		try {
			
			ResultSet result = this.connect.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE
					).executeQuery("SELECT NEXTVAL('equipe_id_seq') as id");
			
			if(result.first()) {
				int id = result.getInt("id");
				PreparedStatement prepare = this.connect.prepareStatement(
						"INSERT INTO equipe (equipe_id, equipe_nom, equipe_note, classement_id) "
						+ "VALUES(?,?,?,?)");
				
				prepare.setLong(1, id);
				prepare.setString(2, obj.getNom());
				prepare.setInt(3, obj.getNote());
				prepare.executeUpdate();
				obj = this.find(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return obj;
		
	}
	
	public Equipe find(long id) {
		
		Equipe eq = new Equipe();
		
		try {
			ResultSet result = this.connect.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE).executeQuery(
					"SELECT * FROM equipe WHERE equipe_id = " + id);
			
			if(result.first()) 
				eq = new Equipe (id,
						result.getString("equipe_nom"),
						result.getInt("equipe_note"),
						result.getInt("classement_id"));
		} catch (SQLException e) {
            e.printStackTrace();
		}
		return eq;
	}
	
	public Equipe find(String name) {
		
		Equipe eq = new Equipe();
		
		try {
			ResultSet result = this.connect.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE).executeQuery(
					"SELECT * FROM equipe WHERE equipe_nom ='" + name+"'");
			
			if(result.first()) 
				eq = new Equipe (result.getInt("equipe_id"),
						result.getString("equipe_nom"),
						result.getInt("equipe_note"),
						result.getInt("classement_id"));
		} catch (SQLException e) {
            e.printStackTrace();
		}
		return eq;
	}
	
	public Equipe update(Equipe obj) {
		try {
            
            this .connect    
                 .createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE, 
                    ResultSet.CONCUR_UPDATABLE
                 ).executeUpdate(
                    "UPDATE equipe SET equipe_nom = '" + obj.getNom() + "', equipe_note ='" + obj.getNote() 
                    + "', classement_id ='" +  obj.getClassement_id() + "'" +
                    " WHERE equipe_id = " + obj.getId()
                 );
        
        obj = this.find(obj.getId());
        
		} catch (SQLException e) {
            e.printStackTrace();
		}
    
		return obj;
	}
}
