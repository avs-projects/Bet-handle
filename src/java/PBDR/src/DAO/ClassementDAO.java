package DAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import data.Classement;
import data.Match;

public class ClassementDAO{
	
    public Connection connect = ConnexionPostgreSQL.getInstance();
	public Classement create(Classement obj) {
		
		try {
			
			ResultSet result = this.connect.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE
					).executeQuery("SELECT NEXTVAL('classement_id_seq') as id");
			
			if(result.first()) {
				int id = result.getInt("id");
				PreparedStatement prepare = this.connect.prepareStatement( 
						"INSERT INTO classement (classement_id, classement_victoire, classement_defaite, classement_rang) "
						+ "VALUES(?,?,?,?)");
				
				prepare.setLong(1, id);
				prepare.setInt(2, obj.getVictoire());
				prepare.setInt(3, obj.getDefaite());
				prepare.executeUpdate();
				obj = this.find(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return obj;
		
	}
	
	public Classement find(long id) {
		
		Classement cl = new Classement();
		
		try {
			ResultSet result = this.connect.createStatement(
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_UPDATABLE).executeQuery(
					"SELECT * FROM classement WHERE classement_id = " + id);
			
			if(result.first()) 
				cl = new Classement (id,
						result.getInt("classement_victoire"),
						result.getInt("classement_defaite"),
						result.getInt("classement_rang"));
		} catch (SQLException e) {
            e.printStackTrace();
		}
		return cl;
	}
	
	public Classement update(Classement obj) {
		try {
            
            this .connect    
                 .createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE, 
                    ResultSet.CONCUR_UPDATABLE
                 ).executeUpdate(
                    "UPDATE classement SET classement_victoire = '" + obj.getVictoire() + "', classement_defaite ='" + obj.getDefaite()
                    + "', classement_rang ='" +  obj.getRang() + "'" +
                    " WHERE classement_id = " + obj.getId()
                 );
        
        obj = this.find(obj.getId());
        
		} catch (SQLException e) {
            e.printStackTrace();
		}
    
		return obj;
	}
	
	public void updateC(Match obj) {
		
		int vd=0,dd=0,ve=0,de=0,pt_d=0,pt_e= 0;
		
		if(obj.getScoreD()>obj.getScoreE()) {
			pt_d = 3;
			vd = 1;
			de = 1;
		}
		else if(obj.getScoreD()<obj.getScoreE()) {
			pt_e=3;
			dd = 1;
			ve = 1;
		}
		else {
			pt_d=1;
			pt_e=1;

		}

		try {
            
            this .connect    
                 .createStatement(
                    ResultSet.TYPE_SCROLL_INSENSITIVE, 
                    ResultSet.CONCUR_UPDATABLE
                 ).executeUpdate(
                    "UPDATE classement SET classement_victoire = (classement_victoire+"+ vd +"), classement_defaite =(classement_defaite+"+ dd+"), classement_point = (classement_point+"
                 + pt_d + ") WHERE classement_id = '" +obj.getEquipeD_id()+"'");
            
            this .connect    
            .createStatement(
               ResultSet.TYPE_SCROLL_INSENSITIVE, 
               ResultSet.CONCUR_UPDATABLE
            ).executeUpdate(
            		"UPDATE classement SET classement_victoire = (classement_victoire+"+ ve +"), classement_defaite =(classement_defaite+"+ de+ "), classement_point = (classement_point+"
                            + pt_e + ") WHERE classement_id = '" +obj.getEquipeE_id()+"'");
                
		} catch (SQLException e) {
            e.printStackTrace();
    	}
	}
}

