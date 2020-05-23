/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import exceptions.NameAlreadyInUse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author anacesár
 * @author coutinho
 */
public class Data implements SoundCloudInterface{
    private Map<String,User> users;
    private Map<Integer,Media> media;
    private int idMusic;
    private List<String> notifications;
    private ReentrantLock lockUsers;
    private ReentrantLock lockMedia;
    private ReentrantLock lockId;
    private Condition hasNotifications;
    
    public Data(){
        this.users = new HashMap<>();
        this.media = new HashMap<>();
        this.idMusic = 4;
        this.notifications = new ArrayList<>();
        this.lockUsers = new ReentrantLock();
        this.lockMedia = new ReentrantLock();
        this.lockId = new ReentrantLock();
        this.hasNotifications = lockMedia.newCondition();
        User u1 = new User("joaocoutinho", "queroentrar");
        User u2 = new User("ana", "queroentrar");
        User u3 = new User("1", "1");
        User u4 = new User("2", "2");
        User u5 = new User("3", "3");
        this.users.put("joaocoutinho", u1);
        this.users.put("ana", u2);
        this.users.put("1", u3);
        this.users.put("2", u4);
        this.users.put("3", u5);
        List<String> genres = new ArrayList<>();
        genres.add("hiphop");
        List<String> genres1 = new ArrayList<>();
        genres1.add("rock");
        Media m1 = new Media("kanye1.mp4", "kanye", 2010, genres);
        genres.add("kanyehop");
        Media m2 = new Media("kanye2.mp4", "kanye", 2010, genres);
        genres.add("samba");
        Media m3 = new Media("kanye3.mp4", "kanye", 2010, genres);
        Media m4 = new Media("oasis.mp4", "oasis", 2010, genres1);
        m1.setId(1);
        m2.setId(2);
        m3.setId(3);
        m4.setId(4);
        this.media.put(1,m1);
        this.media.put(2,m2);
        this.media.put(3,m3);
        this.media.put(4,m4);
    }
    
    public Map<String,User> getUsers(){
        return this.users;
    }

    public Map<Integer,Media> getMedia(){
        return this.media;
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
    
    public void lockUsers(){
        this.lockUsers.lock();
    }
    
    public void unlockUsers(){
        this.lockUsers.unlock();
    }
    
    public void lockMedia(){
        this.lockMedia.lock();
    }
    
    public void unlockMedia(){
        this.lockMedia.unlock();
    }
    
    public void lockId(){
        this.lockId.lock();
    }
    
    public void unlockId(){
        this.lockId.unlock();
    }

    @Override
    public void registerUser(String username, String password) throws NameAlreadyInUse{
        lockUsers();
        if(!this.users.containsKey(username)){
            this.users.put(username, new User(username, password));
            unlockUsers();
        }
        else{
            unlockUsers();
            throw new NameAlreadyInUse();
        }
    }

    @Override
    public boolean loginUser(String username, String password){
        User user = this.users.get(username);

        if(password.equals(user.getPassword())) return true;
        else return false;
    }
    
    
    @Override
    public int uploadMedia(Media media){
        incId();
        this.media.put(idMusic, media);
        return idMusic;
    }
    
    @Override
    public List<Media> searchMedia(String tag){
        List<Media> mediaTag = new ArrayList<>();
        for(Media m : media.values()){
            if(m.getGenres().contains(tag) || m.getTitle().contains(tag) || m.getArtist().contains(tag)){
                mediaTag.add(m);
            }
        }
        return mediaTag;
    }
    
    @Override
    public String downloadMedia(int identifier){
        Media media = this.media.get(identifier);
        media.lock();
        int nDownloads = media.getNumberDownloads();
        media.setNumberDownloads(++nDownloads);
        String nameMedia = media.getTitle();
        media.unlock();
        return nameMedia;
    }
    
    @Override
    public List<Media> getMusic(){
        List<Media> allMedia = new ArrayList<>();
        for(Media m : this.media.values()){
            allMedia.add(m);
        }
        return allMedia;
    }
    
    public int getId(){
        lockId();
        int idMedia = idMusic;
        unlockId();
        return idMedia;
    }

    public void incId(){
        lockId();
        idMusic++;
        unlockId();
    }
}
