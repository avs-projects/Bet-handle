package DAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionPostgreSQL {
	
	private static String url = "jdbc:postgresql://localhost:5432/postgres";
	
	private static String user = "postgres";
	
	private static String pass = "fredsql";
	
	private static Connection connect;
	
	public static Connection getInstance(){
        if(connect == null){
            try {
                connect = DriverManager.getConnection(url, user, pass);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }        
        return connect;    
    }    

}
