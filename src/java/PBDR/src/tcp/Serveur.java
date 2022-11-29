package tcp;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import data.Parieur;
	
public class Serveur {
	
	public ArrayList<Socket> listClients=new ArrayList<Socket>();
	public ArrayList<Parieur> readyPlayers = new ArrayList<Parieur>();
	public int nbClients;
	public int nbRPlayers;
	
	private static final int PORT = 5000;
	private static ServerSocket serverSocket;
	
	public static void main(String[] args) {
		
		try {
			Serveur serveur = new Serveur();
			serverSocket = new ServerSocket(PORT);
			System.out.println("[Server] Running...");
		
		while(true) {
			
			ServeurThread thread = new ServeurThread(serverSocket.accept(),serveur);
			thread.start();
			
			}
		} catch(BindException eb) {
			System.err.println("PORT " + PORT + " deja utilisé ou inutilisable");
		} catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	synchronized public int addClient(Socket socket) {
    	nbClients++;
    	listClients.add(socket);
		return listClients.size();    	
    }
	
	synchronized public int removeClient(Socket socket) {
    	nbClients--;
    	listClients.remove(socket);
		return listClients.size();    	
    }
	
	synchronized public int addRPlayers(Parieur joueur) {
		nbRPlayers++;
    	readyPlayers.add(joueur);
		return readyPlayers.size(); 	
    }
	
	synchronized public int removeRPlayers(Parieur joueur) {
		nbRPlayers--;
		readyPlayers.remove(joueur);
		return readyPlayers.size();    	
    }
	
	synchronized public int removeAll() {
		nbRPlayers = 0;
		readyPlayers = new ArrayList<Parieur>();
		return readyPlayers.size();  	
    }	
}
