/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import data.Media;
import exceptions.NameAlreadyInUse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author anacesár
 * @author coutinho
 */
public class ClientMain {

    public static void main(String[] args) {
        try {
            ClientConnection connection = new ClientConnection();
            ClientStub stub = new ClientStub(connection);

            System.out.println("Connection established to the server");
            String input = null;
            List<String> userInput;
            boolean logged = false;
            
            do {
                if(!logged){
                input = UserInterface.showWelcomeMenu();
                switch (input) {
                    case "login":
                        userInput = UserInterface.showLoginMenu();
                        logged = stub.loginUser(userInput.get(0), userInput.get(1));
                        if(logged) connection.setUsername(userInput.get(0));
                        break;
                    case "register":
                        userInput = UserInterface.showRegisterMenu();
                        try{
                            stub.registerUser(userInput.get(0), userInput.get(1));
                        } catch(NameAlreadyInUse e){
                            System.out.println("The provided name is already in use");
                        }
                        break;
                }
              } else ClientMain.enterSoundCloudMenu(stub);
            } while (!input.equals("exit"));
            connection.sendToServer("exit");
            connection.closeSocket();
        } catch (java.net.ConnectException e) {
            System.out.println("Could not establish a connection with the server");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void enterSoundCloudMenu(ClientStub stub) throws IOException{
        String input;
        int id = 0;
        List<String> userInput;
        Media media = new Media();
        do {
            input = UserInterface.showSoundCloudMenu();
            switch (input) {
                case "music":
                    List<Media> medias = stub.getMusic();
                    Media.printMedia(medias);
                    ClientMain.enterMusicMenu(stub);
                    break;
                case "search":
                    input = UserInterface.showSearchMenu();
                    List<Media> mediaTag = stub.searchMedia(input);
                    Media.printMedia(mediaTag);
                    break;
                case "uploadM":
                    userInput = UserInterface.showUploadMMusic();
                    media.setPath(userInput.get(0));
                    media.setTitle(userInput.get(1));
                    media.setArtist(userInput.get(2));
                    media.setYearRelease(Integer.parseInt(userInput.get(3)));
                    List<String> genres = new ArrayList<>();
                    int nGenres = Integer.parseInt(userInput.get(4));
                    for(int i = 0; i < nGenres; i++){
                        genres.add(userInput.get(i+5));
                    }
                    media.setGenres(genres);
                    id = stub.uploadMedia(media);
                    System.out.println("REPLY : Your uploaded music ID is " + id);
                    break;
                case "download":
                    id = UserInterface.showDownloadMusic();
                    String nameMedia = stub.downloadMedia(id);
                    String[] tokens = nameMedia.split("\\.");
                    System.out.println("REPLY : Your downloaded music title is " + tokens[0] + " and the format is " + tokens[1]);           
                    break;
            }
        } while (!input.equals("goBack"));
    }
    
    public static void enterMusicMenu(ClientStub stub) throws IOException{
        String input;
        do {
            input = UserInterface.showMusicMenu();
            switch (input) {
                case "refresh":
                    List<Media> medias = stub.getMusic();
                    Media.printMedia(medias);
                    break;
            }
        } while (!input.equals("goBack"));
        ClientMain.enterSoundCloudMenu(stub);
    }


}
