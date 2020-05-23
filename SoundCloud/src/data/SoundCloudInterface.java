/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import exceptions.NameAlreadyInUse;
import java.util.List;

/**
 *
 * @author anacesár
 * @author coutinho
 */
public interface SoundCloudInterface {
 
    public void registerUser(String username, String password) throws NameAlreadyInUse;
    public boolean loginUser(String email, String password);
    public int uploadMedia(Media media);
    public List<Media> searchMedia(String tag);
    public String downloadMedia(int identifier);
    public List<Media> getMusic();
}
