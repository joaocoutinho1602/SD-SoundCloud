/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author anacesár
 * @author coutinho
 */
public class Menu {
    private List<String> options;
    private String name;
    
    public Menu(List<String> options, String name){
        setOptions(options);
        this.name = name;
    }
    
    public void setOptions(List<String> ops){
        this.options = new ArrayList<>();
        ops.forEach(o -> {this.options.add(o);});
    }
    
    
    public void show(){
        System.out.println("--------------------------------------------------------------------");
        System.out.println("|                          " + this.name + "                          |");
        System.out.println("--------------------------------------------------------------------");
        for(String op: this.options){
            System.out.println(op.toString());
        }
        System.out.println("--------------------------------------------------------------------");
        System.out.println("Select one available option");
    }
}
