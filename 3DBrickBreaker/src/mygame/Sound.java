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


import com.jme3.audio.AudioNode;
import com.jme3.bounding.BoundingVolume;

public class Sound {
    
    
    private AudioNode collide, collide2, bgm;
    
    
    //construct sound with passed in parameters
    public Sound(AudioNode c, AudioNode c2, AudioNode back){
        
        collide = c;
        collide2 = c2;
        bgm = back;
        
       
         
    }
    //setters and getters.
    public AudioNode getBgm(){
        return bgm;
    }
    
    public AudioNode getCollide2(){
        return collide2;
    }
    
    public AudioNode getCollide1(){
        return collide;
    }
    
    public void setCollide1(boolean positional, boolean loop, float vol){
        collide.setPositional(positional);
        collide.setLooping(loop);
        collide.setVolume(vol);  
    }
    
    public void setCollide2(boolean positional, boolean loop, float vol){
        collide2.setPositional(positional);
        collide2.setLooping(loop);
        collide2.setVolume(vol);  
    }
    
    public void setBgm(boolean positional, boolean loop, float vol){
        bgm.setPositional(positional);
        bgm.setLooping(loop);
        bgm.setVolume(vol);
    }
    
    
    
}
