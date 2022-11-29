package DAO;
import java.sql.Connection;

public abstract class DAO<T> {

    public Connection connect = ConnexionPostgreSQL.getInstance();
    
    /**
     * Allows you to retrieve an object via its ID
     * @param id
     * @return
     */
    public abstract T find(long id);
    
    /**
     * Allows you to create an entry in the database
     * relative to an object
     * @param obj
     */
    public abstract T create(T obj);
    
    /**
     * Allows you to update the data of an entry in the database
     * @param obj
     */
    public abstract T update(T obj);
    
    /**
     * Allows the deletion of an entry from the database
     * @param obj
     */
    public abstract void delete(T obj);
    
    public abstract void search(T obj);
}
