/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ASE;


import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.*;
 

/**
 *
 * @author john doe
 */
public class OLD_Main_WCU {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        int Swidth;
        int Sheight;
         
        
        JFrame vindue = new JFrame( "gui.java" );
        vindue.add( new OLD_WCU(vindue) );
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        Swidth = gd.getDisplayMode().getWidth();
        Sheight = gd.getDisplayMode().getHeight();
	vindue.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
	vindue.setSize(Swidth,Sheight-37);
        vindue.setVisible(true);
        
    }
    
}
