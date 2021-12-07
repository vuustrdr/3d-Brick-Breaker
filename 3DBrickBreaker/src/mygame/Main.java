/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;


import com.jme3.system.AppSettings;



/**
 *
 * @author Dursun Satiroglu
 * STUDENT ID: 201458316
 */
public class Main {

    /**
     * @param args the command line arguments
     */
   public static void main(String[] args) {
        
        //set settings
        AppSettings setting = new AppSettings(true);
        
        Game game = new Game();
        
        setting.setResolution(1280,720);
        
        setting.setTitle("Arkanoid");
        
        game.setSettings(setting);
        
        game.setShowSettings(true);
      
        //start
        game.start();
    }
    
}
