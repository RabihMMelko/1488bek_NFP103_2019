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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Thread lecteur du serveur
 */
public class ReadThread extends Thread {

    private BufferedReader reader;
    private Socket socket;
    private Client client;

    public ReadThread(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;

        try {
            InputStream input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
        } catch (IOException ex) {
            System.out.println("Erreur obtention flux entrant: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void run() {

        while (true) {
            try {
                if (!socket.isClosed()) {
                    String response = reader.readLine();
                    if (response.equals("null")) {
                        try {
                            this.socket.close();
                        } catch (Exception e) {
                            System.out.println("Crash");
                        }
                        System.exit(0);
                    } else {
                        System.out.println("\n" + response);

                        // prints the username after displaying the server's message
                        if (client.getUserName() != null) {
                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            Date date = new Date();
                            System.out.println("[" + dateFormat.format(date) + "][" + client.getUserName() + "]: ");
                        }
                    }
                }
            } catch (Exception ex) {

                System.exit(0);
            }

        }
    }
}
