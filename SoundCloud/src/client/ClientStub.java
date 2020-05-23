/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import data.Media;
import data.SoundCloudInterface;
import exceptions.NameAlreadyInUse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author anacesár
 * @author coutinho
 */
public class ClientStub implements SoundCloudInterface{
    private ClientConnection connection;

    public ClientStub(ClientConnection c){
        this.connection = c;
    }

    @Override
    public void registerUser(String username, String password) throws NameAlreadyInUse{
        String line = "register " + username + " " + password;
        this.connection.sendToServer(line);
        try {
            line = this.connection.readFromServer();
            if(line.startsWith("Success")){
                System.out.println("REPLY : " + line);
            }
            else if(line.startsWith("Error")) throw new NameAlreadyInUse();
        } catch (IOException | InterruptedException ex) {
            //TODO create an exception foe the case that the client couldnt read the message
            ex.printStackTrace();
        }
    }

    @Override
    public boolean loginUser(String username, String password){
        String line = "login " + username + " " + password;
        this.connection.sendToServer(line);
        try {
            line = this.connection.readFromServer();
            if(line.startsWith("Success")){
                System.out.println(line);
                return true;
            }
            else if (line.startsWith("Error")){
                System.out.println("...Wrong Password or not exist username");
                return false;
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public int uploadMedia(Media media){
        String line = "uploadM " + media.getTitle() + " " + media.getArtist() + " " + media.getYearRelease() +
                " " + media.getGenres().size() + " ";
        for(String genre : media.getGenres()){
            line += genre;
            line += " ";
        }
        this.connection.sendToServer(line);
        try{
            this.connection.sendBytes(media.getPath() + "/" + media.getTitle());
            line = this.connection.readFromServer();
        }catch(IOException | InterruptedException ex){
            ex.printStackTrace();
        }
        
        return Integer.parseInt(line);

    }

    @Override
    public List<Media> searchMedia(String tag){
        String line = "search " + tag;
        this.connection.sendToServer(line);
        List<Media> list = new ArrayList<>();
        try {
            line = this.connection.readFromServer();
            int mediaNumber = Integer.parseInt(line);
            for(int i = 0; i < mediaNumber; i++){
                Media media = new Media();
                line = this.connection.readFromServer();
                String[] array = line.split(" ");
                media.setId(Integer.parseInt(array[0]));
                media.setTitle(array[1]);
                media.setArtist(array[2]);
                media.setYearRelease(Integer.parseInt(array[3]));
                media.setNumberDownloads(Integer.parseInt(array[4]));
                int nGenres = Integer.parseInt(array[5]);
                List<String> genres = new ArrayList<>();
                for(int j = 0; j < nGenres; j++){
                    genres.add(array[j + 6]);
                }
                media.setGenres(genres);
                list.add(media);
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        
        return list;
    }

    @Override
    public String downloadMedia(int identifier){
        String line = "download " + identifier;
        this.connection.sendToServer(line);
        try { 
            line = this.connection.readFromServer();
            this.connection.readBytes(line);
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        
        return line;
    }

    @Override
    public List<Media> getMusic(){
        this.connection.sendToServer("getMusic");
        List<Media> list = new ArrayList<>();
        try {
            String line1 = this.connection.readFromServer();
            int n = Integer.parseInt(line1);
            for(int i = 0; i < n; i++){
                Media media = new Media();
                String line = this.connection.readFromServer();
                String[] array = line.split(" ");
                media.setId(Integer.parseInt(array[0]));
                media.setTitle(array[1]);
                media.setArtist(array[2]);
                media.setYearRelease(Integer.parseInt(array[3]));
                media.setNumberDownloads(Integer.parseInt(array[4]));
                int nGenres = Integer.parseInt(array[5]);
                List<String> genres = new ArrayList<>();
                for(int j = 0; j < nGenres; j++){
                    genres.add(array[j + 6]);
                }
                media.setGenres(genres);
                list.add(media);
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        
        return list;
    }

}
