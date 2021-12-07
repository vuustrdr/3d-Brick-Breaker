/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

/**
 *
 * @author Dursun Satiroglu
 */

import com.jme3.font.BitmapText;


public class Text {
    
    BitmapText text;
    
    //constructor for text
    public Text(BitmapText x){
        
        this.text = x;
        
    }
    
    public BitmapText getText(){
        
        return text;
        
    }
    
    //parameter getters and setters
    public void setTextSettings(float size, float x, float y, float z){
        text.setSize(size);
        text.setLocalTranslation(x, y, z);
    }
    
    public void setText(String s){
        text.setText(s);
    }
    
}
