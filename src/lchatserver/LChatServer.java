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
import java.util.ArrayList;
import org.omg.PortableInterceptor.ORBInitInfoPackage.DuplicateName;

public class LChatServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        DatagramSocket sock = null;
        DatagramSocket sock_client = null;
        ArrayList<InetAddress> ips = new ArrayList<>();
        int j = 0;

        try {
            // Creating a socket to receive msgs, parameter is port
            sock = new DatagramSocket(28988);
            sock_client = new DatagramSocket();
            int port_send = 27985;

            //buffer to receive incoming messages
            byte[] buffer = new byte[65536];
            DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
            echo("Server socket created. Waiting for incoming chat messages. . .");

            //Communication loop
            while (true) {
                sock.receive(incoming);
                byte[] data = incoming.getData();
                String s = new String(data, 0, incoming.getLength());

                //adding the client to the chat Room
                if (s.equals("welcome")) {
                    j = 0;
                    for (InetAddress dupclicate : ips) {
                        System.out.println(incoming.getAddress().getHostAddress() + "::" + dupclicate.getHostAddress());
                        if (incoming.getAddress().getHostAddress() == dupclicate.getHostAddress() || incoming.getAddress().getHostAddress().equals(dupclicate.getHostAddress())) {
                            j = 1;
                        }
                    }
                    if (j != 1) {
                        ips.add(incoming.getAddress());
                    }
                }else if (s.equals("bye")) {
                    if (ips.contains(incoming.getAddress())) {
                        ips.remove(ips.indexOf(incoming.getAddress()));
                    }   
                }

                echo(incoming.getAddress().getHostAddress() + ":" + incoming.getAddress().getHostName() + " : " + incoming.getPort() + "->" + s);
                s = "OK : " + s;
                for (InetAddress ips1 : ips) {
                    System.out.println(ips1.getHostName());
                    echo (ips1.getAddress() + ":" + ips1.getHostName() + ":" + port_send + "->" + s);
                    DatagramPacket dp = new DatagramPacket(s.getBytes(), s.getBytes().length, ips1, port_send);
                    sock_client.send(dp);

                }

            }

        } catch (IOException e) {
            System.out.println("IOException : " + e);

        }

    }

    public static void echo(String msg) {
        System.out.println(msg);
    }

}
