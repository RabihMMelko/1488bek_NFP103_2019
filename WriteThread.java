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
 
/**
 * Thread de lecture utilisateur
 * Boucle infinie jusqu'à _quit
 */
public class WriteThread extends Thread {
    private PrintWriter writer;
    private Socket socket;
    private Client client;
 
    public WriteThread(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
 
        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
 
    public void run() {
 
        Console console = System.console();
 
        String userName = console.readLine("\nEnter your name: ");
        client.setUserName(userName);
        writer.println(userName);
 
        String text;
 
        do {
            text = console.readLine("[" + userName + "]: ");
            writer.println(text);
 
        } while (!text.equals("_quit"));
 
        try {
            socket.close();
        } catch (IOException ex) {
 
            System.out.println("Error writing to server: " + ex.getMessage());
        }
    }
}
