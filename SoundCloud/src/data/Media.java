/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import com.jakewharton.fliptables.FlipTableConverters;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author anacesár
 * @author coutinho
 */
public class Media {
    private int id;
    private String title;
    private String artist;
    private int yearRelease;
    private List<String> genres;
    private int numberDownloads;
    private String path;
    private ReentrantLock lock;
    
    public Media(){
        this.id = 0;
        this.title = "";
        this.artist = "";
        this.yearRelease = 0;
        this.genres = new ArrayList<>();
        this.numberDownloads = 0;
        this.path = "";
        this.lock = new ReentrantLock();
    }
    
    public Media(String title, String artist, int yearRelease, List<String> genres){
        this.id = 0;
        this.title = title;
        this.artist = artist;
        this.yearRelease = yearRelease;
        setGenres(genres);
        this.numberDownloads = 0;
        this.lock = new ReentrantLock();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public int getYearRelease() {
        return yearRelease;
    }

    public List<String> getGenres() {
        return genres;
    }

    public int getNumberDownloads() {
        return numberDownloads;
    }

    public String getPath() {
        return path;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setYearRelease(int yearRelease) {
        this.yearRelease = yearRelease;
    }
    
    public void setGenres(List<String> genres){
        this.genres = new ArrayList<>();
        for(String s : genres){
            this.genres.add(s);
        }
    }

    public void setNumberDownloads(int numberDownloads) {
        this.numberDownloads = numberDownloads;
    }

    public void setPath(String path) {
        this.path = path;
    }
    
    public void lock(){
        this.lock.lock();
    }
    
    public void unlock(){
        this.lock.unlock();
    }
    
    public List<Object> convertToObjectList(){
        List<Object> result = new ArrayList<>();
        result.add(this.id);
        result.add(this.title);
        result.add(this.artist);
        result.add(this.yearRelease);
        result.add(this.genres);
        result.add(this.numberDownloads);          
        
        return result;
    }
    
    public static void printMedia(List<Media> media){
        String[] headers = {"Identifier", "Title", "Artist", "Year Release", "Genre", "Number of Downloads"};
        int i = 0;
        
        Object[][] data = new Object[media.size()][];
        for (Media m : media) {
            List<Object> row = m.convertToObjectList();
            data[i] = row.toArray();
            i++;
        }

        System.out.println(FlipTableConverters.fromObjects(headers, data));
    }
    
    public static String printMediaStrings(Media media){
        String res = media.getId() + " " + media.getTitle() + " " + media.getArtist() + " " + media.getYearRelease() + " " + media.getNumberDownloads() + " " + media.getGenres().size();
        for(String s : media.getGenres()){
            res += " ";
            res += s;
        }
        return res;
    }

}
