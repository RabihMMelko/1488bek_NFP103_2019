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
import java.net.*;
import java.io.*;
import java.util.Scanner;
 

public class Client {
    private String hostname;
    private int port;
    private String userName;
 
    public Client(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }
 
    public void execute() {
        try {
            Socket socket = new Socket(hostname, port);
 
            System.out.println("Connecté au serveur");
 
            ReadThread rt = new ReadThread(socket, this);
            WriteThread wt = new WriteThread(socket, this);
            
            rt.setName(userName); wt.setName(userName);
            rt.start(); wt.start();
 
        } catch (UnknownHostException ex) {
            System.out.println("Serveur inexistant: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Erreur E/S: " + ex.getMessage());
        }
 
    }
 
    void setUserName(String userName) {
        this.userName = userName;
    }
 
    String getUserName() {
        return this.userName;
    }
    
    public void kick(){
        System.exit(0);
    }
 
 
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in); 
     System.out.println("Bienvenue. Pour commencer, veuillez vous connecter en utilisant la commande ci-dessous\n _connect <surnom> <machine> <port>");
     String init = in.nextLine();
     while(!init.startsWith("_connect")){
       System.out.println("Commande invalide. Usage: _connect <surnom> <machine> <port>");
       init = in.nextLine();
     }
     String[] connection = init.split(" ");
     
     while(connection.length < 4){
       System.out.println("Commande invalide. Usage: _connect <surnom> <machine> <port>");
       connection = in.nextLine().split(" ");
     }
     
     while (Integer.parseInt(connection[3]) <= 1023){
         System.out.println("Veuillez ne pas utiliser un port reservé. Pour cela, utiliser un numero de port supérieur à 1024");
         connection[3] = Integer.toString(in.nextInt());
     }
     
     int port =  Integer.parseInt(connection[3]);
     String hostname = connection[2];
     
 
        Client client = new Client(hostname, port);
        client.setUserName(connection[1]);
        client.execute();
    }
}
