/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author anacesar
 * @author joaocoutinho
 */
public class ClientConnection {
    private Socket socket;
    private Socket socketBytes;
    private PrintWriter out;
    private BufferedReader in;
    private DataOutputStream outByte;
    private DataInputStream inByte;
    private final int MAXSIZE = 8192;
    private String path = "/Users/joanacruz/Desktop/Downloads";
    private Reader reader;
    //private String path = "E:\\test\\down";

    public ClientConnection() throws IOException {
        this.socket = new Socket("localhost", 12345);
        this.socketBytes = new Socket("localhost", 12345);
        this.out = new PrintWriter(this.socket.getOutputStream());
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.outByte = new DataOutputStream(this.socketBytes.getOutputStream());
        this.inByte = new DataInputStream(this.socketBytes.getInputStream());
        this.reader = new Reader(this.in);
        Thread clientReader = new Thread(this.reader);
        clientReader.start();
    }
    
    public void setUsername(String username){
        this.path += username;
        File file = new File(this.path);
        if (!file.exists())
            file.mkdir();
    }
    
    public void sendToServer(String s) {
        this.out.println(s);
        this.out.flush();
    }
    
    public String readFromServer() throws IOException, InterruptedException{
        return this.reader.readNext();
    }
    
    public void sendBytes(String path) throws IOException{
        File file = new File(path);
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[MAXSIZE];
        int content, offset = 0;
        while ((content = fis.read(buffer)) > 0) {
            this.outByte.write(buffer, offset, content);
            this.outByte.flush();
        }
        fis.close();
    }
    
    public void readBytes(String fileName) throws IOException{
        File download = new File(this.path + "/" + fileName);
        download.createNewFile();
        FileOutputStream fos = new FileOutputStream(download, false); 
        byte[] buffer = new byte[MAXSIZE];
        int count = 0;
        while ((count = inByte.read(buffer)) > 0) {
            fos.write(buffer, 0, count);
            if(count != MAXSIZE) break;
        }
        fos.flush();
        fos.close();
    }

    public void closeSocket() throws IOException {
        this.out.close();
        this.in.close();
        this.outByte.close();
        this.inByte.close();
        this.socket.shutdownInput();
        this.socket.shutdownOutput();
        this.socket.close();
        this.socketBytes.shutdownInput();
        this.socketBytes.shutdownOutput();
        this.socketBytes.close();
    }
}
