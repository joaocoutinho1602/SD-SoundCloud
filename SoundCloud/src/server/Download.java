/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import data.Data;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author anacesar
 * @author joaocoutinho
 */

public class Download{
    private String received;
    private Data data;
    private PrintWriter out;
    private DataOutputStream outBytes;
    private final String uploadsDirectory = "/Users/joanacruz/Desktop/Uploads";
    private final int MAXSIZE = 8192;
    
    public Download(String s, Data data, PrintWriter pw, DataOutputStream ds){
        this.received = s;
        this.data = data;
        this.out = pw;
        this.outBytes = ds;
    }
    
    public void downloadMedia() throws InterruptedException{
            FileInputStream fis = null;
            String[] tokens = this.received.split(" ");
            String nameMedia = this.data.downloadMedia(Integer.parseInt(tokens[1]));
            this.out.println(nameMedia);
            this.out.flush();
        try {
            File download = new File(uploadsDirectory + "/" + nameMedia);
            fis = new FileInputStream(download);
            byte[] buffer = new byte[MAXSIZE];
            int content, offset = 0;
            while ((content = fis.read(buffer)) > 0) {
                this.outBytes.write(buffer, offset, content);
                this.outBytes.flush();
                Thread.sleep(100);
            }
            ThreadPool.nDownloads--;
            fis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
       
}
