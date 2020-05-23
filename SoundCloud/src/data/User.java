/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author anacesár
 * @author coutinho
 */

public class User {
    private String username;
    private String password;
    private ReentrantLock lockUser;
    private Condition hasNotifications;
    private List<String> notifications;
    
    public User(String username, String password){
        this.username = username;
        this.password = password;
        this.lockUser = new ReentrantLock();
        this.hasNotifications = this.lockUser.newCondition();
        this.notifications = new ArrayList<>();
    }

    public String getPassword() {
        return password;
    }
    
    public void lock(){
        this.lockUser.lock();
    }
    
    public void unlock(){
        this.lockUser.unlock();
    }

    public List<String> getNotifications(){
        List<String> not = new ArrayList<>();
        for(String n : this.notifications) not.add(n);
        return not;
    }
    
            
    public void setNotifications(List<String> not){
        this.notifications = new ArrayList<>();
        for(String n : not) this.notifications.add(n);
    }
    
    public Condition getHasNotifications() {
        return hasNotifications;
    }
}
