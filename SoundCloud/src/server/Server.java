/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import data.Data;
import data.Media;
import data.User;
import exceptions.NameAlreadyInUse;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author anacesár
 * @author coutinho
 */
public class Server implements Runnable {

    private Socket socket;
    private Socket socketBytes;
    private final int MAXSIZE = 8192;
    private Data data;
    private ThreadPool pool;
    private final String uploadsDirectory = "/Users/joanacruz/Desktop/Uploads";
    private BufferedReader in;
    private PrintWriter out;
    private DataInputStream inBytes;
    private DataOutputStream outBytes;
    private static List<String> loggedUsers = new ArrayList<>(); 


    public Server(Socket s, Socket sb, Data data, ThreadPool pool) throws IOException {
        this.socket = s;
        this.socketBytes = sb;
        this.data = data;
        this.pool = pool;
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.out = new PrintWriter(this.socket.getOutputStream());
        this.inBytes = new DataInputStream(this.socketBytes.getInputStream());
        this.outBytes = new DataOutputStream(this.socketBytes.getOutputStream());
    }

    @Override
    public void run() {

        try {
            String s;
            //String uploadsDirectory = "E:\\test\\up";
            File file = new File(uploadsDirectory);
            file.mkdir();
            

            do {
                s = in.readLine();
                System.out.println(s);
                if (s.matches("register [A-Za-z0-9]+ [A-Za-z0-9]+")) {
                    String[] tokens = s.split(" ");
                    try {
                        this.data.registerUser(tokens[1], tokens[2]);
                        out.println("Success! User registered successfully");
                    } catch (NameAlreadyInUse e) {
                        out.println("Error register");                        
                    }
                         
                } else if (s.matches("login [A-Za-z0-9]+ [A-Za-z0-9]+")) {
                    
                    String[] tokens = s.split(" ");
                    boolean logged = this.data.loginUser(tokens[1], tokens[2]);
                    if (logged) {
                        out.println("Success! User logged in successfully");
                        User user = this.data.getUsers().get(tokens[1]);
                        Notificator not = new Notificator(this.out, user);
                        Thread notificatorUser = new Thread(not);
                        notificatorUser.start();
                        loggedUsers.add(tokens[1]);
                    }
                    else out.println("Error with login");
                    
                } else if (s.matches("uploadM [A-Za-z0-9.-]+ [A-Za-z0-9.-]+ [0-9]+ [0-9]+ [A-Za-z ]+")) {
                    
                    upload(s);
                    
                } else if (s.matches("download [0-9]+")) {                   
                    Download request = new Download(s, this.data, out, outBytes);     
                    this.pool.execute(request);
                    ThreadPool.nDownloads++;       
                } else if (s.matches("search [A-Z0-9a-z]+")) {
                    String[] tokens = s.split(" ");
                    List<Media> mediaTag = this.data.searchMedia(tokens[1]);
                    out.println(mediaTag.size());
                    for (Media m : mediaTag) {
                        out.println(Media.printMediaStrings(m));
                        out.flush();
                    }                                
                } else if (s.equals("getMusic")) {
                    List<Media> media = this.data.getMusic();
                    out.println(media.size());
                    for (Media m : media) {
                        out.println(Media.printMediaStrings(m));
                        out.flush();
                    }
                }
                out.flush();
            } while (!s.equals("exit"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                this.socket.shutdownInput();
                this.socket.shutdownOutput();
                this.socketBytes.shutdownInput();
                this.socketBytes.shutdownOutput();
                this.socket.close();
                this.socketBytes.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void upload(String s) throws IOException{
        String[] tokens = s.split(" ");
        int nGenres = Integer.parseInt(tokens[4]);
        List<String> genres = new ArrayList<>(nGenres);
        for (int i = 0; i < nGenres; i++) {
            genres.add(tokens[i + 5]);
        }
        File upload = new File(uploadsDirectory + "/" + tokens[1]);
        upload.createNewFile();
        FileOutputStream fos = new FileOutputStream(upload, false); 
        int count;
        byte[] buffer = new byte[MAXSIZE];
        while ((count = inBytes.read(buffer)) > 0) {
            fos.write(buffer, 0, count);
            if(count != MAXSIZE) break;
        }
        fos.flush();
        fos.close();
        Media m = new Media(tokens[1], tokens[2], Integer.parseInt(tokens[3]), genres);
        int id = this.data.uploadMedia(m);
        m.setId(id);
        out.println(id);

        /*List<String> notifications = this.data.getNotifications();
        notifications.add(": New music was uploaded with title " + tokens[1] + " and artist " + tokens[2]);
        notifications.add(": New music was uploaded with title " + tokens[1] + " and artist " + tokens[2]);
        notifications.add(": New music was uploaded with title " + tokens[1] + " and artist " + tokens[2]);
        this.data.setNotifications(notifications);
        this.data.lockMedia();
        this.data.getHasNotifications().signalAll();
        this.data.unlockMedia();*/
        
        for(String user : loggedUsers){
            User u = this.data.getUsers().get(user);
            List<String> notifications = u.getNotifications();
            notifications.add(": New music was uploaded with title " + tokens[1] + " and artist " + tokens[2]);
            u.setNotifications(notifications);
            u.lock();
            u.getHasNotifications().signal();
            u.unlock();
        }
        
    }

}
