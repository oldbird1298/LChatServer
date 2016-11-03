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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LChatServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        DatagramSocket sock = null;
        DatagramSocket sock_client = null;
        ArrayList<InetAddress> ips = new ArrayList<>();
        ArrayList<ConHosts> connected = new ArrayList<>();
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
                Date tNow = new Date();
                SimpleDateFormat ft = new SimpleDateFormat("dd-MM-yy hh:mm:ss");
                sock.receive(incoming);
                byte[] data = incoming.getData();
                String s = new String(data, 0, incoming.getLength());

                //adding the client to the chat Room
                if (s.startsWith("welcome")) {
                    ConHosts newClient = new ConHosts();
                    j = 0;
                    for (ConHosts dupclicate : connected) {
                        System.out.println(incoming.getAddress().getHostAddress() + "::" + dupclicate.getHost().getHostAddress());
                        if (incoming.getAddress().getHostAddress() == dupclicate.getHost().getHostAddress() || incoming.getAddress().getHostAddress().equals(dupclicate.getHost().getHostAddress())) {
                            s = dupclicate.Name + ": " + s;
                            j = 1;
                        }
                    }
                    if (j != 1) {
                        String[] nickname = s.split(" ");
                        newClient.Name = nickname[1];
                        newClient.host = incoming.getAddress();
                        connected.add(newClient);
                    }
                } else if (s.equals("bye")) {
                    if (connected.contains(incoming.getAddress())) {
                        connected.remove(connected.indexOf(incoming.getAddress()));
                    }
                } else if (s.equals("GET")) {
                    for (ConHosts ips3 : connected) {
                        String hosts = ips3.Name;
                        hosts = "USER " + hosts;
                        DatagramPacket dp = new DatagramPacket(hosts.getBytes(), hosts.getBytes().length, incoming.getAddress(), port_send);
                        sock_client.send(dp);

                    }
                }
                echo(ft.format(tNow));
                for (int i = 0; i < 10; i++) {
                    System.out.print("=");
                }
                System.out.println("");
                echo(" " + incoming.getAddress().getHostAddress() + ":" + incoming.getAddress().getHostName() + " : " + incoming.getPort() + "->" + s);

                for (int i = 0; i < 10; i++) {
                    System.out.print("=");
                }
                System.out.println("");
                //s = "OK : " + s;
                for (ConHosts ips2 : connected) {
                    if (incoming.getAddress().getHostAddress() == ips2.getHost().getHostAddress() || incoming.getAddress().getHostAddress().equals(ips2.getHost().getHostAddress())) {
                        s = ips2.Name + ": " + s;
                    }

                }

                for (ConHosts ips1 : connected) {
                    echo(ft.format(tNow));
                    for (int i = 0; i < 10; i++) {
                        System.out.print("=");
                    }
                    System.out.println("");
                    System.out.println(ips1.getHost().getHostAddress());
                    echo(ft.format(tNow) + " " + ips1 + ":" + ips1.getHost().getHostName() + ":" + port_send + "->" + s);

                    DatagramPacket dp = new DatagramPacket(s.getBytes(), s.getBytes().length, ips1.getHost(), port_send);
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
