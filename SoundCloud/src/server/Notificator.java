/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import data.Data;
import data.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

/**
 *
 * @author anacesar
 * @author joaocoutinho
 */

public class Notificator implements Runnable{
    private PrintWriter out;
    private User user;
    
    Notificator(PrintWriter pw, User u){
        this.out = pw;
        this.user = u;
    }
    
    public void run(){
        try{
            user.lock();
            try {
                while(true){
                    while(this.user.getNotifications().isEmpty()){
                        System.out.println("Im waiting " + Thread.currentThread().getId());
                        this.user.getHasNotifications().await();
                    }
                    System.out.println("MANDEI");
                    List<String> notifications = this.user.getNotifications();
                    String notification = notifications.remove(0);
                    this.user.setNotifications(notifications);
                    out.println(notification);
                    out.flush();
                }
            } finally {
                user.unlock();
            }
        } catch (InterruptedException ex) {    
            ex.printStackTrace();
        }    
    }
}