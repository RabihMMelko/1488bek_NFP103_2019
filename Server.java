/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ACCOV2019;

/**
 *
 * @author Rabih
 * @date $(date)
 */

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
 

public class Server {
    private final int port;
    private Set<String> userNames = new HashSet<>();
    private HashMap<String, UserThread> userThreads = new HashMap<>();
    private final Scanner in = new Scanner(System.in);
 
    public Server(int port) {
        this.port = port;
    }
    
    public Scanner getScanner(){
      return this.in;
    }
    
    
    
    public HashMap<String, UserThread> getUserThreads(){
      return this.userThreads;
    }
    
    public UserThread getClientThread(String s){
      return this.userThreads.get(s);
    }
 
    public void execute() throws InterruptedException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
 
            System.out.println("Serveur operationnel sur le port " + port);
            while (true) {
                
                synchronized(userNames){
                  if (userNames.size() >= 10){
                    System.out.println("Desolé, pas de place disponible");
                  }
                }
                
                Socket socket = serverSocket.accept();
                
                UserThread newUser = new UserThread(socket, this);
                newUser.start();
                newUser.wait();
                System.out.println("[ANNONCE]"+newUser.getName()+" est connecté");
                broadcast("[ANNONCE]"+newUser.getName()+" est connecté", null);
 
            }
 
        } catch (IOException ex) {
            System.out.println("Erreur sur le serveur: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
 
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Syntaxe: java -cp .. ACCOV2019.Server <port-number>");
            System.exit(0);
        }
 
        int port = Integer.parseInt(args[0]);
 
        Server server = new Server(port);
        try{
        server.execute();
        }
        catch(InterruptedException e){e.printStackTrace();}
    }
 
    /**
     * Fonction de broadcast
     */
    void broadcast(String message, UserThread excludeUser) {
        for (Map.Entry<String, UserThread> entry : userThreads.entrySet()) {
            String usN = entry.getKey();
            UserThread aUser = entry.getValue();
            
            if (aUser != excludeUser) {
                aUser.sendMessage(message);
            }
        }
    }
 
    /**
     * Ajout de l'utilisateur.
     */
    void addUserName(String userName) {
        userNames.add(userName);
    }
 
    /**
     * Retrait de l'utilisateur et thread associé lors de la deconnexion
     */
    void removeUser(String userName, UserThread aUser) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            userThreads.remove(aUser);
            System.out.println("L'utilisateur " + userName + " a quitté");
            broadcast("L'utilisateur " + userName + " a quitté", null);
        }
    }
 
    public Set<String> getUserNames() {
        return this.userNames;
    }
 
    /**
     * Retourne vrai s'il existe des utilisateurs connectés (utilisateur courant non compté)
     */
    boolean hasUsers() {
        return !this.userNames.isEmpty();
    }
}
