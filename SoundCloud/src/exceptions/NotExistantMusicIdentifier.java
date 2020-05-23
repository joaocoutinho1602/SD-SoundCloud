/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author anacesar
 * @author joaocoutinho
 */

public class NotExistantMusicIdentifier extends Exception{
    
    public NotExistantMusicIdentifier(){
        super();
    }
    
    public NotExistantMusicIdentifier(String s){
        super(s);
    }
    
}
