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
import java.util.logging.Level;
import java.util.logging.Logger;

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
                echoDetails(incoming.getAddress().getCanonicalHostName() + ":" + incoming.getAddress().getHostName() + ":" + incoming.getPort() + "->" + s, true);

                /*
                Check the incoming String if is something of protocol
                 */
                //adding the client to the chat Room
                //Also Advertise new Client to Others
                if (s.startsWith("welcome")) {

                    try {
                        insertElementsToList(connected, incoming, s);
                    } catch (Exception ex) {
                        Logger.getLogger(LChatServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //Removes a client from the list
                } else if (s.startsWith("USER_02")) {

                    try {
                        removeElementsFromList(connected, incoming, s);
                    } catch (Exception ex) {
                        Logger.getLogger(LChatServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    //Send the list of clients to the connected pc's.
                } else if (s.equals("GET")) {
                    for (ConHosts ips3 : connected) {
                        String hosts = ips3.Name;
                        hosts = "USER_01 " + hosts;
                        DatagramPacket dp = new DatagramPacket(hosts.getBytes(), hosts.getBytes().length, incoming.getAddress(), port_send);
                        sock_client.send(dp);

                    }
                }

                System.out.println("");

                for (ConHosts ips2 : connected) {
                    if (incoming.getAddress().getHostAddress() == ips2.getHost().getHostAddress() || incoming.getAddress().getHostAddress().equals(ips2.getHost().getHostAddress())) {
                        s = ips2.Name + ": " + s;
                    }

                }

                for (ConHosts ips1 : connected) {
//                    System.out.println(ips1.getHost().getHostAddress());
                    echoDetails(ips1 + ":" + ips1.getHost().getHostName() + ":" + port_send + "->" + s, false);
                    //System.out.println("=================");
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

    public static void echoDetails(String msg, boolean in) {
        Date tNow = new Date();
        SimpleDateFormat ft1 = new SimpleDateFormat("EEE dd MMM YYYY");
        SimpleDateFormat ft2 = new SimpleDateFormat("hh:mm:ss");
        String inOut = null;

        if (in) {
            inOut = "IN";
        } else {
            inOut = "OUT";
        }
        //echo(ft1.format(tNow));
        System.out.println(ft1.format(tNow) + "\t" + inOut + "\t" + ft2.format(tNow) + "\t" + msg);
    }

    public static void insertElementsToList(ArrayList<ConHosts> e, DatagramPacket pack, String s) throws Exception {
        int j = 0;
        String nMessage = null;
        ConHosts n = new ConHosts();

        for (ConHosts el : e) {
            //if (!e.contains(pack.getAddress()));
            if (el.host.getAddress().equals(pack.getAddress())) {
                j = 1;
            }
        }
        if (j != 1) {
            String[] nHost = s.split(" ");
            n.Name = nHost[1];
            n.host = pack.getAddress();
            sendData(e, "USER_01 " + n.Name);
            e.add(n);

        }
        System.out.printf("The List Size is : %d", e.size());

    }

    public static void removeElementsFromList(ArrayList<ConHosts> e, DatagramPacket pack, String s) throws Exception {
        int j = 0;
        String nMessage = null;
        ConHosts n = new ConHosts();
        String[] rm = s.split(" ");
        for (ConHosts el : e) {
            //if (!e.contains(pack.getAddress()));
            n.Name = el.Name;
            if (n.getName().equals(rm[1])) {
                j = e.indexOf(el);

            }
        }
        e.remove(j);
        sendData(e, "USER_02 " + rm[1]);
        System.out.printf("The List Size is : %d", e.size());
    }

    public boolean checkList(ArrayList<ConHosts> e, DatagramPacket in) {
        boolean check = true;
        for (ConHosts test : e) {
            if (test.host.getHostAddress().equals(in.getAddress().getHostAddress())) {
                check = true;
            } else {
                check = false;
            }
        }
        return check;
    }

    public static void sendData(ArrayList<ConHosts> ec, String msg) {
        int port_send = 27985;
        Date tNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("EEE dd MMM - dd-MM-yy - hh:mm:ss ");
        try {

            DatagramSocket opr = new DatagramSocket();

            for (ConHosts el : ec) {
                System.out.println("");
                echo(ft.format(tNow) + "Sending Message TO : ->->");
                for (int i = 0; i < 25; i++) {
                    System.out.print("=");
                }
                System.out.println("");
                System.out.println(el.host.getHostName() + ":" + el.Name + " -> " + msg);
                System.out.println("===============================");
                DatagramPacket inform = new DatagramPacket(msg.getBytes(), msg.length(), el.host, port_send);
                opr.send(inform);

            }
            //opr.close();

        } catch (SocketException ex) {
            Logger.getLogger(LChatServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
