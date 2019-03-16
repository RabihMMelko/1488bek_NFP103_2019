/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ACCOV;

/**
 *
 * @author Rabih
 * @date $(date)
 */

import java.io.*; //Pour la gestion des E/S
import java.util.*; 
import java.net.*; //Pour la gestion des reseaux


public class Server {
   static ArrayList<Client> activeUsers = new ArrayList<Client>();
   
   public static void main(String [] args) throws IOException{
        ServerSocket ss; 
       
     
        ss = new ServerSocket(1234); //Initialization de la Socket Serveur: Ecoute sur le port 1234
        Socket s; 
          
        //Boucle infinie pour les requêtes client
        while (true)  
        { 
            //Accepter la requête client
            s = ss.accept(); 
            
            //Obtention des flux de données IN/OUT
            
            DataInputStream cdis = new DataInputStream(s.getInputStream());
            DataOutputStream cdos = new DataOutputStream(s.getOutputStream());
            
            //Creation d'une instance client, ainsi que son Thread dedié
            Client cli = new Client (s,"Client"+activeUsers.size(),cdis,cdos);
            
            Thread session = new Thread(cli);
            
            //Ajout du client à la liste des utilisateurs actifs
            
            activeUsers.add(cli);
            
            //Demarrage de session
            session.start();
                    }
}
}
