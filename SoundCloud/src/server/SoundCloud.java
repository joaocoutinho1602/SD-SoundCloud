/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import data.Data;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author anacesár
 * @author coutinho
 */
public class SoundCloud {

    /**
     * @param args the command line arguments
     */
    
    
    public static void main(String[] args) throws Exception {
        ServerSocket ss = new ServerSocket(12345);
        Data data = new Data();
        ThreadPool pool = new ThreadPool(10);
        System.out.println("SoundCloud server is now up and listening for connections");
        while (true) {
            Socket socket = ss.accept();
            Socket socketBytes = ss.accept(); 
            System.out.println("New connection established");
            Thread thread = new Thread(new Server(socket, socketBytes, data, pool));
            thread.start();
        }
    }
    
}
