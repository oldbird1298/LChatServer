/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lchatserver;

import java.net.InetAddress;

/**
 *
 * @author 1AKEDEEPROG2
 */
public class ConHosts {

    String Name;
    InetAddress host;

    public String getName() {
        return Name;
    }

    public InetAddress getHost() {
        return host;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public void setHost(InetAddress host) {
        this.host = host;
    }

}
