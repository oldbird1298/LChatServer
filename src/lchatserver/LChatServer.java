/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lchatserver;

import java.net.DatagramSocket;

/**
 *
 * @author dgerontop
 *
 * This is a LChat Server which is Responsible for the Chating between the
 * Ussers in a Local Network.
 *
 */
import java.io.*;
import java.net.*;

public class LChatServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        DatagramSocket sock = null;

        try {
            // Creating a socket to receive msgs, parameter is port
            sock = new DatagramSocket(28988);

            //buffer to receive incoming messages
            byte[] buffer = new byte[65536];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
            echo("Server socket created. Waiting for incoming chat messages. . .");
            
            //Communication loop
            while (true) {
                sock.receive(incoming);
                byte[] data = incoming.getData();
                String s = new String(data, 0 , incoming.getLength());
                echo (incoming.getAddress().getHostAddress() + ":" + incoming.getAddress().getHostName() + " : " + incoming.getPort() + "->" + s  );
                s = "OK : " + s ;
                DatagramPacket dp = new DatagramPacket(s.getBytes(), s.getBytes().length, incoming.getAddress(), incoming.getPort());
                sock.send(dp);
                
            
            }
            

        } catch (IOException e) {
            System.out.println("IOException : " + e);

        }

    }

    public static void echo(String msg) {
        System.out.println(msg);
    }

}