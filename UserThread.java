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
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
 

public class UserThread extends Thread {
    private final Socket socket;
    private final Server server;
    private PrintWriter writer;
 
    public UserThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }
 
    @Override
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
 
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
 
            String userName = new String(reader.readLine());
            this.setName(userName);
            server.addUserName(userName);
            server.getUserThreads().put(userName, this);
            
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String serverMessage = "["+dateFormat.format(date)+"]Un nouveau utilisateur s'est connecté : " + userName;
            server.broadcast(serverMessage, this);
            
            String clientMessage;
 
            do {
                
                clientMessage = reader.readLine();
                if (clientMessage.equals("_who")){
                  printUsers();
                }
                dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                date = new Date();
                serverMessage = "["+dateFormat.format(date)+"][" + userName + "]: " + clientMessage;
                server.broadcast(serverMessage, this);
 
            } while (!clientMessage.equals("_quit"));
 
            server.removeUser(userName, this);
            socket.close();
            
            
            serverMessage = "["+dateFormat.format(date)+"]"+userName + " a quitté.";
            server.broadcast(serverMessage, this);
 
        } catch (IOException ex) {
            System.out.println("Erreur dans UserThread: " + ex.getMessage());
        }
    }
 
    /**
     * Liste des utilisateurs connectés (_who)
     */
    void printUsers() {
        if (server.hasUsers()) {
            writer.println("Utilisateurs connectés: " + server.getUserNames());
        } else {
            writer.println("Aucun utilisateur connecté");
        }
    }
 
    /**
     * Sends a message to the client.
     */
    void sendMessage(String message) {
        writer.println(message);
    }
}
