/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ASE;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics; 
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke; 
import java.util.Timer;
import java.util.TimerTask;

import java.io.*;
import java.net.*;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author john doe
 */



public class OLD_WCU  extends JPanel {
    ArrayList Cart = new ArrayList();
    int Swidth;
    int Sheight;
    int state=1;
    int substate=0;
    int lastsubstate;
    int lastState;
    int fontScale;
    int clickableElements=62; //total elements +1. ie 26 elements = 27
    int[] killMeX1 = new int[clickableElements];
    int[] killMeY1 = new int[clickableElements];
    int[] killMeX2 = new int[clickableElements];
    int[] killMeY2 = new int[clickableElements];
    int[] killMeZ = new int[clickableElements];
       Connection conn = null; 
       
       //standards
       String stdcontainer="";
       String stddiscard;
       String stdusr;
       String stdpsw;
       
       int usrId;
       String usr;
 
       public void connect(){
        try {
            // connect way #1
            String url1 = "jdbc:mysql://46.183.136.66:3306/larpgdk_dtu";
            String user = "larpgdk_dtuusr";
            String password = "q1w2e3r4t5y6";
 
            conn = DriverManager.getConnection(url1, user, password);
            if (conn != null) {
                System.out.println("Connected to the database test1");
            }
       
                      
    //      System.out.println("Committed " + counts.length + " updates");
         } catch (SQLException ex) {
            System.out.println("An error occurred. Maybe user/password is invalid");
            ex.printStackTrace();
        }
}
       
       JTextField inputField_login_user = new JTextField(15);
       JTextField inputField_login_psw = new JTextField(15);
       
       JTextField inputField_config_user = new JTextField(15);
       JTextField inputField_config_psw = new JTextField(15);
       JTextField inputField_config_container = new JTextField(15);
       JTextField inputField_config_discard = new JTextField(15);
       
       JTextField inputField_service_item = new JTextField(15);
       JTextField inputField_service_container = new JTextField(15);
       
       JTextField inputField_item_item = new JTextField(15);
       JTextField inputField_item_stdprice = new JTextField(15);
       JTextField inputField_item_stdcontainer = new JTextField(15);
       JTextField inputField_item_name = new JTextField(15);
       JTextField inputField_item_stddiscard = new JTextField(15);
       JTextField inputField_item_lifespan = new JTextField(15);
       
       JTextField inputField_container_item = new JTextField(15);
       JTextField inputField_container_stdprice = new JTextField(15); 
       JTextField inputField_container_name = new JTextField(15); 
       
       JTextField inputField_refill_item = new JTextField(15);
       JTextField inputField_refill_barcode = new JTextField(15);
       
       //part 
       JTextField inputField_refill_stdcontainer = new JTextField(15);
       JTextField inputField_refill_name = new JTextField(15);
       
       // only whole
       JTextField inputField_refill_packingdate = new JTextField(15);
       JTextField inputField_refill_lifespan = new JTextField(15);
       JTextField inputField_refill_amount = new JTextField(15);
       
      String stdFont ="HelveticaNeue-Light";
      String stdFont2="HelveticaNeue";
      
      String[] r_itemname=new String[2];
      int[] r_itemid=new int[2];
      String[] r_barcode=new String[2];
      int r_parent;
      int r_weight;
      String r_itemc;
      
      int f_itemid; 
      String  f_barcode;
       String f_itemname; 
       int f_parent;
      String  f_itemc;
       int f_weight;
       int f_subtype;
       int f_state;
       int f_substate;
       
       //Do note, that any given item, that have subtype 1, cannot have a parent.
       //Also note, that subtype 1 == "is a container"
       
      
      public void refill_item(){
          String itemidto;
             itemidto = (inputField_refill_item.getText());
              if(itemidto==null||itemidto.equals(null)||itemidto.length()<0||itemidto.isEmpty()||itemidto=="null"||itemidto.equals("")||itemidto=="0"||itemidto.equals("null")||itemidto.equals("0")){
          }else{
                 try{
            //      System.err.println(itemidto+" .d. ");
            String query=null;
            ResultSet rs=null;
            Statement st = conn.createStatement(); 
            query="SELECT * FROM items WHERE barcode='"+itemidto+"' LIMIT 0,1";         
            rs = st.executeQuery(query);
            while (rs.next())
      {  
          //System.err.println(itemidto+" .e. ");
        f_itemid = rs.getInt("id"); 
        f_barcode = rs.getString("barcode");
        f_itemname = rs.getString("name"); 
        f_parent = rs.getInt("parent");
        f_itemc = rs.getString("stdcontainer");
        f_weight=rs.getInt("weight");
        f_subtype=rs.getInt("subtype");
        
        f_substate=2;
        if(f_parent>0){
            //It's a part
            f_state=11232;
                        
        }else{
            //It's a whole
            f_state=11231;
            inputField_refill_packingdate.setVisible(false);
       inputField_refill_lifespan.setVisible(false);
       inputField_refill_amount.setVisible(false);
       inputField_refill_packingdate.setVisible(true);
       inputField_refill_lifespan.setVisible(true);
       inputField_refill_amount.setVisible(true);
            if(f_subtype==1){
                //It's a container
                f_substate=1;
                inputField_refill_packingdate.setVisible(false);
       inputField_refill_lifespan.setVisible(false);
       inputField_refill_amount.setVisible(false); 
       inputField_refill_amount.setVisible(true);
            }
        }
        
        
              repaint();
      }
             
            
            } catch (SQLException ex) {
            System.out.println("Something went wrong / nothing found");
            ex.printStackTrace(); 
        }
             
      }
      }
      
      
      public void service_item(){
          /*r_itemid[0]='\n';
          r_itemname[0]="";
          r_barcode[0]="";
          r_parent[0]='\n';*/
          String itemidto;
             itemidto = (inputField_service_item.getText());
             
          System.err.println(itemidto+" .. ");
          if(itemidto==null||itemidto.equals(null)||itemidto.length()<0||itemidto.isEmpty()||itemidto=="null"||itemidto.equals("")||itemidto=="0"||itemidto.equals("null")||itemidto.equals("0")){
          }else{
              try{
            //      System.err.println(itemidto+" .d. ");
            String query=null;
            ResultSet rs=null;
            Statement st = conn.createStatement(); 
            query="SELECT * FROM items WHERE barcode='"+itemidto+"' LIMIT 0,1";         
            rs = st.executeQuery(query);
            while (rs.next())
      { 
          
          r_itemid[1]=0;
          r_itemname[1]="";
          r_barcode[1]=""; 
          //System.err.println(itemidto+" .e. ");
        r_itemid[0] = rs.getInt("id"); 
        r_barcode[0] = rs.getString("barcode");
        r_itemname[0] = rs.getString("name"); 
        r_parent = rs.getInt("parent");
        r_itemc = rs.getString("stdcontainer");
        r_weight=rs.getInt("weight");
        int done=0;
        if(r_parent=='\n' || r_parent==0){
            //doesn't have parent
            r_itemid[1]=r_itemid[0];
            r_barcode[1]=r_barcode[0];
            r_itemname[1]=r_itemname[0];
            
        }else{
            //has parent
            inputField_service_container.setVisible(true);
            ResultSet rs2=null;
            Statement st2 = conn.createStatement();
            query="SELECT * FROM items WHERE id='"+r_parent+"' LIMIT 0,1";         
            rs2 = st2.executeQuery(query);
            while (rs2.next())
      { 
          //System.err.println(itemidto+" .f. ");
        r_itemid[1] = rs2.getInt("id"); 
        r_barcode[1] = rs2.getString("barcode");
        r_itemname[1] = rs2.getString("name"); 
        if(r_itemc==null||r_itemc.equals(null)||r_itemc.length()<0||r_itemc.isEmpty()||r_itemc=="null"||r_itemc.equals("")||r_itemc=="0"||r_itemc.equals("null")||r_itemc.equals("0")){
            r_itemc=rs2.getString("stdcontainer");
        }
      }
            if(r_itemc==null||r_itemc.equals(null)||r_itemc.length()<0||r_itemc.isEmpty()||r_itemc=="null"||r_itemc.equals("")||r_itemc=="0"||r_itemc.equals("null")||r_itemc.equals("0")){
                //System.err.print("null detected.tyvm");
                inputField_service_container.setText(" ");
                inputField_service_container.setText("");
                done=1;
            }else{
            done=1;
            inputField_service_container.setText(r_itemc);
            } 
            
            }
        if(done==0){
            inputField_service_container.setText(" ");
                inputField_service_container.setText("");
        }
        
        //Date dateC = rs.getDate("parent");
        // print the results
        System.out.format("%s, %s, %s, %s\n", r_itemid[1], r_itemname[1], r_barcode[1], r_itemc);
        //state=11;
        
        repaint();
      }
             
            
            } catch (SQLException ex) {
            System.out.println("Something went wrong / nothing found");
            ex.printStackTrace(); 
        }
          }
      }
      
      String t_itemname;
      int t_itemid;
      int t_weight;
      String t_barcode;
      
      Timer timer = new Timer();
      
       public void service_container(){
          /*r_itemid[0]='\n';
          r_itemname[0]="";
          r_barcode[0]="";
          r_parent[0]='\n';*/
           
          String itemidto;
             itemidto = (inputField_service_container.getText());
          //System.err.println(itemidto+" .. ");
          if(itemidto==null||itemidto.equals(null)||itemidto.length()<0||itemidto.isEmpty()||itemidto=="null"||itemidto.equals("")||itemidto=="0"||itemidto.equals("null")||itemidto.equals("0")){
          }else{
              try{
            //      System.err.println(itemidto+" .d. ");
            String query=null;
            ResultSet rs=null;
            Statement st = conn.createStatement(); 
            query="SELECT * FROM items WHERE barcode='"+itemidto+"' AND subtype='1' LIMIT 0,1";         
            rs = st.executeQuery(query);
            while (rs.next())
      { 
             
          //System.err.println(itemidto+" .e. ");
        t_itemid = rs.getInt("id"); 
        t_weight = rs.getInt("weight"); 
        t_barcode = rs.getString("barcode");
        t_itemname = rs.getString("name"); 
        
 timer.schedule (new SayHello(), 0, 1000);
                        substate=1;
      }  
            if(r_barcode[0]==null||r_barcode[0].equals(null)||r_barcode[0].length()<0||r_barcode[0].isEmpty()||r_barcode[0]=="null"||r_barcode[0].equals("")||r_barcode[0]=="0"||r_barcode[0].equals("null")||r_barcode[0].equals("0")){
                //System.err.print("null detected.tyvm");
                inputField_service_item.setText(" ");
                inputField_service_item.setText("");
            }else{
              
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
inputField_service_item.setText(r_barcode[0]);                        
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                });
            }  
        
        //Date dateC = rs.getDate("parent");
        // print the results
        System.out.format("%s, %s, %s\n", t_itemid, t_itemname, t_barcode);
        
        repaint();
      
             
            
            } catch (SQLException ex) {
            System.out.println("Something went wrong / nothing found");
            ex.printStackTrace(); 
        }
          }
      }
      
       int startup=0;
        String fuser=null;
        int portid=4567;
        String ip="localhost";
        
       
      public String vejning(String Command) throws IOException {
          Socket s = new Socket(); 
    PrintWriter s_out = null;
    BufferedReader s_in = null;
         boolean doordie;
        try
        {
        s.connect(new InetSocketAddress(ip , portid));
        System.out.println("Connected");
             
        //writer for socket
            s_out = new PrintWriter( s.getOutputStream(), true);
            //reader for socket
            s_in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        }
         
        //Host not found
        catch (UnknownHostException e)
        {
            System.err.println("No weight responding. Terminated");
            //System.exit(1);
        } 
         //BufferedReader inFromUser = new BufferedReader( new InputStreamReader(System.in));
         String message;
         String response="fail"; 
         //sentence = inFromUser.readLine();
    message = Command+"\r\n";
    //System.out.println("Sending to server: "+sentence);
    s_out.println( message );
    doordie=false;         
    //System.out.println("Message send");
         
    //Get response from server
    
    
    while (doordie==false&&((response = s_in.readLine()) != null))
    {
        System.out.println( response );
        doordie=true;
    }
    //System.out.println("done");
    s.close();
    s = new Socket();
    s.connect(new InetSocketAddress(ip , portid));
            //writer for socket
            s_out = new PrintWriter( s.getOutputStream(), true);
            //reader for socket
            s_in = new BufferedReader(new InputStreamReader(s.getInputStream()));
    if(Command.equals("S")){
            String[] parts = response.split(" ");
            int theI=0;
            boolean b;
            for(int i=0;i<parts.length;i++){
                b=Pattern.matches("^[0-9\\,.]*$",parts[i]);
                if(b){
                    theI=i;
                }
            }
     //System.out.println("5:"+parts[5]+"6:"+parts[6]+"7:"+parts[7]+"3:"+parts[3]+"4:"+parts[4]);
            String goshjava;
           // String[] parts2= new String[20];
            //parts2[0]="0";
            goshjava="0";
             
            if(parts.length < theI){
                
            }else{
       
                 System.err.println(parts[theI]);
            String [] parts2 = parts[theI].split("\\.");
              
            if(parts2.length < 2){
            
                goshjava="0";
            }else{
                
                 System.err.println(parts2[0]);
                goshjava=parts2[0]+""+parts2[1];
            }
                    }
            goshjava=Integer.toString(Integer.parseInt(goshjava)-t_weight);
    response=goshjava;
    }
    /*
    
    int temp;
        temp = Integer.parseInt(response);
        response=Integer.toString(temp);*/
    s.close();
    return response;
    }
      
      String gotWeight="";
      
class SayHello extends TimerTask {
    public void run() {
        if(substate==1 && (state==111||state==1121||state==1122||state==11232)){
        try {
           gotWeight= vejning("s");
        } catch (IOException ex) {
            Logger.getLogger(OLD_WCU.class.getName()).log(Level.SEVERE, null, ex);
        }
 timer.schedule (new SayHello(), 1000);
       repaint();
       SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
inputField_service_item.setText(r_barcode[0]);  
inputField_service_container.setText(t_barcode);  
String buffer;
buffer=inputField_refill_lifespan.getText();
inputField_refill_lifespan.setText(" ");
inputField_refill_lifespan.setText(buffer);
buffer=inputField_refill_packingdate.getText();
inputField_refill_packingdate.setText(" ");
inputField_refill_packingdate.setText(buffer);
buffer=inputField_refill_stdcontainer.getText();
inputField_refill_stdcontainer.setText(" ");
inputField_refill_stdcontainer.setText(buffer);  
buffer=inputField_refill_name.getText();
inputField_refill_name.setText(" ");
inputField_refill_name.setText(buffer);   
buffer=inputField_refill_barcode.getText();
inputField_refill_barcode.setText(" ");
inputField_refill_barcode.setText(buffer);   

//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                });
        }
    }
 }
 
    int submenu=0;
       
    public void paintComponent(Graphics g)
	{
            grabDim();
            
            Color bgcolor = new Color(77,255,155);
            //Define fontsize for scaling
            //fontScale=Sheight/30;
            //fontScale=Swidth/50;
            fontScale=((Sheight/30)+(Swidth/50))/2;
            System.err.println(fontScale);
            
            
         //Drawing engine. state er udgangspunkt..
            
            super.paintComponent(g);     // tegn baggrunden p� panelet
            	
            //Draw background
            g.setColor(bgcolor);
            g.fillRect(0,0,Swidth,Sheight);
            g.setColor(Color.WHITE);
            g.setFont(new Font(stdFont, Font.PLAIN, fontScale)); 
            System.out.println(+r_itemid[0]);
            
            if(state!=lastState){
                inputField_login_user.setVisible(false);
                inputField_login_psw.setVisible(false);
                inputField_config_user.setVisible(false);
                inputField_config_psw.setVisible(false);
                inputField_config_container.setVisible(false);
                inputField_config_discard.setVisible(false);
                inputField_service_item.setVisible(false);
                inputField_service_container.setVisible(false);
                inputField_item_item.setVisible(false);
       inputField_item_stdprice.setVisible(false);
       inputField_item_stdcontainer.setVisible(false);
       inputField_item_name.setVisible(false);
       inputField_item_stddiscard.setVisible(false);
       inputField_item_lifespan.setVisible(false);
       
       inputField_container_item.setVisible(false);
       inputField_container_stdprice.setVisible(false); 
       inputField_container_name.setVisible(false); 
       
       inputField_refill_item.setVisible(false);
       
       //part 
       inputField_refill_stdcontainer.setVisible(false);
       inputField_refill_name.setVisible(false);
       
       // only whole
       inputField_refill_packingdate.setVisible(false);
       inputField_refill_lifespan.setVisible(false);
       inputField_refill_amount.setVisible(false);
             inputField_item_item.setVisible(false);
       inputField_item_stdprice.setVisible(false);
       inputField_item_stdcontainer.setVisible(false);
       inputField_item_name.setVisible(false);
       inputField_item_stddiscard.setVisible(false);
       inputField_item_lifespan.setVisible(false);
       
       inputField_container_item.setVisible(false);
       inputField_container_stdprice.setVisible(false); 
       inputField_container_name.setVisible(false); 
       
       inputField_refill_item.setVisible(false);
       
       //part 
       inputField_refill_stdcontainer.setVisible(false);
       inputField_refill_name.setVisible(false);
       
       // only whole
       inputField_refill_packingdate.setVisible(false);
       inputField_refill_lifespan.setVisible(false);
       inputField_refill_amount.setVisible(false);
       inputField_refill_barcode.setVisible(false); 
             
                
                    if(state==1){
                        //login screen
                        inputField_login_user.setVisible(true);
                        inputField_login_psw.setVisible(true);
                        inputField_login_user.setText(stdusr);
                        inputField_login_psw.setText(stdpsw);
                        inputField_login_user.requestFocus();
                    }else if(state==12){
                        inputField_config_user.setVisible(true);
                        inputField_config_psw.setVisible(true);
                        inputField_config_container.setVisible(true);
                        inputField_config_discard.setVisible(true);
                         
                        inputField_config_user.setText(stdusr);
                        inputField_config_psw.setText(stdpsw);
                        inputField_config_container.setText(stdcontainer);
                        inputField_config_discard.setText(stddiscard);
                        
                        
                        inputField_config_user.requestFocus();
                    }else if(state==111){
                        inputField_service_item.setVisible(true);
                        inputField_service_container.setVisible(true);
                        if(stdcontainer.equals("")||stdcontainer==null){
                            inputField_service_container.setText(" ");
                        inputField_service_container.setText("");
                        }else{
                        inputField_service_container.setText(stdcontainer);   
                        }
                        inputField_service_item.requestFocus();
                     }else if(state==1121){
                         substate=0;
                         inputField_item_item.setVisible(true);
       inputField_item_stdprice.setVisible(true);
       inputField_item_stdcontainer.setVisible(true);
       inputField_item_name.setVisible(true);
       inputField_item_stddiscard.setVisible(true);
       inputField_item_lifespan.setVisible(true);
       inputField_item_item.requestFocus();
                     }else if(state==1122){
                         substate=0;
       inputField_container_item.setVisible(true);
       inputField_container_stdprice.setVisible(true); 
       inputField_container_name.setVisible(true); 
       inputField_container_item.requestFocus();
                     }else if(state==1123 ||state==11231||state==11232){
                         inputField_refill_item.setVisible(true);
                         inputField_refill_item.setText(f_barcode);
                         if(state==11231){
                             if(substate!=1){
                              inputField_refill_packingdate.setVisible(true);
       inputField_refill_lifespan.setVisible(true);
                             }
       inputField_refill_amount.setVisible(true); 
                         }else if(state==11232){
                             
                substate=0;
                             inputField_refill_stdcontainer.setVisible(true);
       inputField_refill_name.setVisible(true);
        inputField_refill_packingdate.setVisible(true);
       inputField_refill_lifespan.setVisible(true);
       inputField_refill_amount.setVisible(false); 
       inputField_refill_barcode.setVisible(true); 
       inputField_refill_item.requestFocus();
                         }
                     }
                lastState=state;    
            }
            
            //StateBasedLayout
            if(state==1){
                //Welcome screen (1)
            g.drawString("Login",Swidth/2-(5*fontScale),4*fontScale);
            g.drawString("User",Swidth/2-(5*fontScale),6*fontScale);
            g.drawString("Password",Swidth/2-(5*fontScale),8*fontScale);
            g.drawString("< Login >",Swidth/2,10*fontScale);
            
            //0 - loginknap
            killMeX1[0]=Swidth/2;
            killMeY1[0]=10*fontScale;
            killMeX2[0]=Swidth/2+(fontScale*5);
            killMeY2[0]=10*fontScale+(fontScale+fontScale/2);
            killMeZ[0]=1;
            
            inputField_login_user.setBounds(Swidth/2,5*fontScale,5*fontScale,fontScale+4);
            inputField_login_psw.setBounds(Swidth/2,7*fontScale,5*fontScale,fontScale+4);
            g.drawString("< Terminal Config >",Swidth/2,Sheight-(4*fontScale));
            //1 - terminalknap
            killMeX1[1]=Swidth/2;
            killMeY1[1]=Sheight-(4*fontScale);
            killMeX2[1]=Swidth/2+(fontScale*9);
            killMeY2[1]=Sheight-(4*fontScale)+(fontScale+fontScale/2);
            killMeZ[1]=1;
            }
            
            if(state==12){
                //configscreen
                g.drawString("Standards for terminal (in session)",Swidth/2-(5*fontScale),4*fontScale);
                g.drawString("User",Swidth/2-(5*fontScale),6*fontScale);
                g.drawString("Password",Swidth/2-(5*fontScale),8*fontScale);
                inputField_config_user.setBounds(Swidth/2,5*fontScale,5*fontScale,fontScale+4);
                inputField_config_psw.setBounds(Swidth/2,7*fontScale,5*fontScale,fontScale+4);
                g.drawString("Container",Swidth/2-(5*fontScale),12*fontScale);
                g.drawString("Discarding",Swidth/2-(5*fontScale),14*fontScale);
                inputField_config_container.setBounds(Swidth/2,11*fontScale,5*fontScale,fontScale+4);
                inputField_config_discard.setBounds(Swidth/2,13*fontScale,5*fontScale,fontScale+4);
                g.drawString("< Submit >",Swidth/2,16*fontScale);
                
                killMeX1[26]=Swidth/2;
                killMeY1[26]=(16*fontScale);
                killMeX2[26]=Swidth/2+(fontScale*6);
                killMeY2[26]=(16*fontScale)+(fontScale+fontScale/2);
                killMeZ[26]=12;
            }
            
            if(state==11){
                //standard menu screen
                state=111;
                repaint();
            }
            
            if(state==111){
                for (int i = 0; i < Cart.size(); i++) {
			g.drawString((Cart.get(i).toString()),0,fontScale*(15+i));
		} 
                g.drawString("< Tjek varer ud >",0,fontScale*14);
                    killMeX1[61]=0;
                    killMeY1[61]=fontScale*13;
                    killMeX2[61]=Swidth/2-fontScale*4;
                    killMeY2[61]=fontScale*15;
                     killMeZ[61]=111;
                        
                if(substate!=lastsubstate){
                    inputField_service_item.setVisible(false);
                        inputField_service_container.setVisible(false);
                       if(substate==0||substate==1||substate==2){
                           inputField_service_item.setVisible(true);
                           inputField_service_container.setVisible(true);
                           if(substate==2){ 
                               if(r_parent==0){
                           inputField_service_container.setVisible(false);
                               }
                           inputField_service_item.setText(" ");
                           inputField_service_container.setText(" ");
                           inputField_service_item.setText("");
                           inputField_service_container.setText("");
                           
                        inputField_service_item.requestFocus();
                       
                           }
                       
                       }
                       
                        try {
                        vejning("Z"); //tare the weigth
                    } catch (IOException ex) {
                        Logger.getLogger(OLD_WCU.class.getName()).log(Level.SEVERE, null, ex);
                    }
                        lastsubstate=substate;
                        repaint();
                }
                //service
                paintMen(g);
                g.drawString("Service",Swidth/2-fontScale*6,fontScale*4);
                g.drawString("VareID:",Swidth/2-fontScale*6,fontScale*5);
                inputField_service_item.setBounds(Swidth/2,4*fontScale,5*fontScale,fontScale+4);
                if(r_itemid[0]!=r_itemid[1]){
                g.drawString("#"+r_barcode[0]+" "+r_itemname[1]+"["+r_itemid[1]+"] - "+r_itemname[0]+"["+r_itemid[0]+"]",Swidth/2-fontScale*3,fontScale*6);
                }else{
                 g.drawString("#"+r_barcode[0]+" "+r_itemname[0]+"["+r_itemid[0]+"]",Swidth/2-fontScale*3,fontScale*6); 
                }
                 if(r_parent=='\n'||r_parent==0){
                     g.drawString("< V >",Swidth/2,fontScale*15);
                    killMeX1[45]=Swidth/2;
                    killMeY1[45]=fontScale*14;
                    killMeX2[45]=Swidth/2+fontScale*4;
                    killMeY2[45]=fontScale*16;
                     killMeZ[45]=111;
                 }else{
                g.drawString("BeholderID:",Swidth/2-fontScale*6,fontScale*7);
                inputField_service_container.setBounds(Swidth/2,6*fontScale,5*fontScale,fontScale+4);
                g.drawString("#"+t_barcode+" "+t_itemname+"["+t_itemid+"]",Swidth/2-fontScale*3,fontScale*9); 
                
                     if(substate==0){
                    
                    if(t_itemid=='\n'||t_itemid==0){
                        g.drawString("V�lg venligst en beholder",Swidth/2,fontScale*11);
                    }else{
                        //Vejning
                        substate=1;
                    }
                
                    
                    }else if(substate==1){
                        //vejning
                        g.drawString("S�t venligst varer p� v�gt",Swidth/2,fontScale*11);
                       //String weight="fail";
                        try {
                        gotWeight=vejning("S");
                    } catch (IOException ex) {
                        Logger.getLogger(OLD_WCU.class.getName()).log(Level.SEVERE, null, ex);
                    } 
                    g.drawString(gotWeight+" g",Swidth/2,fontScale*13);
                    g.drawString("< V >",Swidth/2,fontScale*15);
                    killMeX1[45]=Swidth/2;
                    killMeY1[45]=fontScale*14;
                    killMeX2[45]=Swidth/2+fontScale*4;
                    killMeY2[45]=fontScale*16;
                     killMeZ[45]=111;
                        
                    }else if(substate==2){
                        for (int i = 0; i < Cart.size(); i++) {
			g.drawString((Cart.get(i).toString()),0,fontScale*(15+i));
		}
                    }
                 }
           
            }
            
            if(state==112){
                //submenu
                state=1123;
                repaint();
                //paintMenSub(g);
            }
            
            if(state==1121){
                //Ny varegruppe 
                if(substate!=lastsubstate){

       inputField_item_item.setVisible(false);
       inputField_item_stdprice.setVisible(false);
       inputField_item_stdcontainer.setVisible(false);
       inputField_item_name.setVisible(false);
       inputField_item_stddiscard.setVisible(false);
       inputField_item_lifespan.setVisible(false);
if(substate!=1){
       inputField_item_item.setVisible(true);
       inputField_item_stdprice.setVisible(true);
       inputField_item_stdcontainer.setVisible(true);
       inputField_item_name.setVisible(true);
       inputField_item_stddiscard.setVisible(true);
       inputField_item_lifespan.setVisible(true);
       inputField_item_item.requestFocus();
}
   try {
                        vejning("Z"); //tare the weigth
                    } catch (IOException ex) {
                        Logger.getLogger(OLD_WCU.class.getName()).log(Level.SEVERE, null, ex);
                    }
lastsubstate=substate;
/*if(substate!=0){
 timer.schedule (new SayHello(), 1000);
}*/
repaint();
}
                paintMenSub(g);
                g.drawString("New Item",Swidth/2-fontScale*6,fontScale*4);
                if(substate==0){
                g.drawString("Give ID:",Swidth/2-fontScale*6,fontScale*5);
                inputField_item_item.setBounds(Swidth/2,4*fontScale,5*fontScale,fontScale+4);
                //g.drawString("< Auto >",Swidth/2+fontScale*5,fontScale*5);
                    killMeX1[46]=Swidth/2+fontScale*4;
                    killMeY1[46]=fontScale*4;
                    killMeX2[46]=Swidth/2+fontScale*8;
                    killMeY2[46]=fontScale*6;
                     killMeZ[46]=1121;
                     if(submenu==0){
                     g.setFont(new Font(stdFont2, Font.BOLD, fontScale));
        }else{
            g.setFont(new Font(stdFont, Font.PLAIN, fontScale));
        }
                g.drawString("< Whole >",Swidth/2-fontScale*2,fontScale*7-(fontScale/2));
                    killMeX1[47]=Swidth/2-fontScale*2;
                    killMeY1[47]=fontScale*6-(fontScale/2);
                    killMeX2[47]=Swidth/2+fontScale*2;
                    killMeY2[47]=fontScale*8-(fontScale/2);
                     killMeZ[47]=1121;
                     if(submenu==1){
                     g.setFont(new Font(stdFont2, Font.BOLD, fontScale));
        }else{
            g.setFont(new Font(stdFont, Font.PLAIN, fontScale));
        }
                g.drawString("< Part >",Swidth/2+fontScale*2,fontScale*7-(fontScale/2));
                    killMeX1[48]=Swidth/2+fontScale*2;
                    killMeY1[48]=fontScale*6-(fontScale/2);
                    killMeX2[48]=Swidth/2+fontScale*6;
                    killMeY2[48]=fontScale*8-(fontScale/2);
                     killMeZ[48]=1121;
            g.setFont(new Font(stdFont, Font.PLAIN, fontScale));
                g.drawString("Name:",Swidth/2-fontScale*6,fontScale*8);
                inputField_item_name.setBounds(Swidth/2,7*fontScale,5*fontScale,fontScale+4);
                g.drawString("stdPrice:",Swidth/2-fontScale*6,fontScale*9);
                inputField_item_stdprice.setBounds(Swidth/2,8*fontScale,5*fontScale,fontScale+4);
                g.drawString("stdContainer:",Swidth/2-fontScale*6,fontScale*10);
                inputField_item_stdcontainer.setBounds(Swidth/2,9*fontScale,5*fontScale,fontScale+4);
                g.drawString("stdDiscard:",Swidth/2-fontScale*6,fontScale*11);
                inputField_item_stddiscard.setBounds(Swidth/2,10*fontScale,5*fontScale,fontScale+4);
                g.drawString("stdLifespan:",Swidth/2-fontScale*6,fontScale*12);
                inputField_item_lifespan.setBounds(Swidth/2,11*fontScale,5*fontScale,fontScale+4);
                }else{
                    //vejning
                     g.drawString("Place item on weight",Swidth/2,fontScale*13);
                       //String weight="fail";
                        try {
                        gotWeight=vejning("S");
                    } catch (IOException ex) {
                        Logger.getLogger(OLD_WCU.class.getName()).log(Level.SEVERE, null, ex);
                    } 
                        
                    g.drawString(gotWeight+" g",Swidth/2,fontScale*14);
                }
                if(substate==0){
                g.drawString("< -> >",Swidth/2,fontScale*15);
                    killMeX1[49]=Swidth/2;
                    killMeY1[49]=fontScale*14;
                    killMeX2[49]=Swidth/2+fontScale*4;
                    killMeY2[49]=fontScale*16;
                     killMeZ[49]=1121;
                }else{
                g.drawString("< V >",Swidth/2,fontScale*15);
                    killMeX1[50]=Swidth/2;
                    killMeY1[50]=fontScale*14;
                    killMeX2[50]=Swidth/2+fontScale*4;
                    killMeY2[50]=fontScale*16;
                     killMeZ[50]=1121;
                }
                
            }
            
            if(state==1122){
                //ny beholder 
                if(substate!=lastsubstate){

 inputField_container_item.setVisible(false);
       inputField_container_stdprice.setVisible(false); 
       inputField_container_name.setVisible(false); 
if(substate!=1){
 inputField_container_item.setVisible(true);
       inputField_container_stdprice.setVisible(true); 
       inputField_container_name.setVisible(true); 
       inputField_container_item.requestFocus();
}
    try {
                        vejning("Z"); //tare the weigth
                    } catch (IOException ex) {
                        Logger.getLogger(OLD_WCU.class.getName()).log(Level.SEVERE, null, ex);
                    }
lastsubstate=substate;
if(substate!=0){
 timer.schedule (new SayHello(), 1000);
}
repaint();

}

                paintMenSub(g);
                        g.drawString("New Container",Swidth/2-fontScale*6,fontScale*4);
                        if(substate==0){
                g.drawString("Give ID:",Swidth/2-fontScale*6,fontScale*5);
                inputField_container_item.setBounds(Swidth/2,4*fontScale,5*fontScale,fontScale+4);
                //g.drawString("< Auto >",Swidth/2+fontScale*5,fontScale*5);
                    killMeX1[51]=Swidth/2+fontScale*4;
                    killMeY1[51]=fontScale*4;
                    killMeX2[51]=Swidth/2+fontScale*8;
                    killMeY2[51]=fontScale*6;
                     killMeZ[51]=1122;
                     g.drawString("Uses:",Swidth/2-fontScale*6,fontScale*7-(fontScale/2));
                if(submenu==0){
                     g.setFont(new Font(stdFont2, Font.BOLD, fontScale));
        }else{
            g.setFont(new Font(stdFont, Font.PLAIN, fontScale));
        }
                g.drawString("< Single >",Swidth/2-fontScale*2,fontScale*7-(fontScale/2));
                    killMeX1[52]=Swidth/2-fontScale*2;
                    killMeY1[52]=fontScale*6-(fontScale/2);
                    killMeX2[52]=Swidth/2+fontScale*2;
                    killMeY2[52]=fontScale*8-(fontScale/2);
                     killMeZ[52]=1122;
                     if(submenu==1){
                     g.setFont(new Font(stdFont2, Font.BOLD, fontScale));
        }else{
            g.setFont(new Font(stdFont, Font.PLAIN, fontScale));
        }
                g.drawString("< Infinite >",Swidth/2+fontScale*2,fontScale*7-(fontScale/2));
                    killMeX1[53]=Swidth/2+fontScale*2;
                    killMeY1[53]=fontScale*6-(fontScale/2);
                    killMeX2[53]=Swidth/2+fontScale*6;
                    killMeY2[53]=fontScale*8-(fontScale/2);
                     killMeZ[53]=1122;
            g.setFont(new Font(stdFont, Font.PLAIN, fontScale));
                g.drawString("Name:",Swidth/2-fontScale*6,fontScale*8);
                inputField_container_name.setBounds(Swidth/2,7*fontScale,5*fontScale,fontScale+4);
                g.drawString("stdPrice:",Swidth/2-fontScale*6,fontScale*9);
                inputField_container_stdprice.setBounds(Swidth/2,8*fontScale,5*fontScale,fontScale+4);
                        }else{
                            //vejning
                             g.drawString("Place container on weight",Swidth/2,fontScale*13);
                       //String weight="fail";
                        try {
                        gotWeight=vejning("S");
                    } catch (IOException ex) {
                        Logger.getLogger(OLD_WCU.class.getName()).log(Level.SEVERE, null, ex);
                    } 
                        
                    g.drawString(gotWeight+" g",Swidth/2,fontScale*14);
                        }
                if(substate==0){
                g.drawString("< -> >",Swidth/2,fontScale*15);
                    killMeX1[54]=Swidth/2;
                    killMeY1[54]=fontScale*14;
                    killMeX2[54]=Swidth/2+fontScale*4;
                    killMeY2[54]=fontScale*16;
                     killMeZ[54]=1122;
                }else{
                     g.drawString("< V >",Swidth/2,fontScale*15);
                    killMeX1[55]=Swidth/2;
                    killMeY1[55]=fontScale*14;
                    killMeX2[55]=Swidth/2+fontScale*4;
                    killMeY2[55]=fontScale*16;
                     killMeZ[55]=1122;
                }
            }
            
            if(state==1123){
                //Opfyld
                //-v�lg whole/part 
                paintMenSub(g);
                        g.drawString("Refill",Swidth/2-fontScale*6,fontScale*4);
                g.drawString("ItemID:",Swidth/2-fontScale*6,fontScale*5);
                inputField_refill_item.setBounds(Swidth/2,4*fontScale,5*fontScale,fontScale+4);
                 g.drawString("#"+f_barcode+" "+f_itemname+"["+f_itemid+"]",Swidth/2-fontScale*3,fontScale*6); 
                
               g.drawString("< -> >",Swidth/2,fontScale*15);
                    killMeX1[60]=Swidth/2;
                    killMeY1[60]=fontScale*14;
                    killMeX2[60]=Swidth/2+fontScale*4;
                    killMeY2[60]=fontScale*16;
                     killMeZ[60]=1123;
            }
            
            if(state==11231){
                //whole
                //-OBS: dependent hvis beholder/vare
                
                        g.drawString("Refill - whole",Swidth/2-fontScale*6,fontScale*4);
                g.drawString("ItemID:",Swidth/2-fontScale*6,fontScale*5);
                g.drawString("#"+f_barcode+" "+f_itemname+"["+f_itemid+"]",Swidth/2-fontScale*3,fontScale*6); 
                
                inputField_refill_item.setBounds(Swidth/2,4*fontScale,5*fontScale,fontScale+4);
                paintMenSub(g);
                g.drawString("Amount:",Swidth/2-fontScale*6,fontScale*8);
                inputField_refill_amount.setBounds(Swidth/2,7*fontScale,5*fontScale,fontScale+4);
                if(substate!=1){
                g.drawString("packingdate:",Swidth/2-fontScale*6,fontScale*9);
                inputField_refill_packingdate.setBounds(Swidth/2,8*fontScale,5*fontScale,fontScale+4);
                g.drawString("stdLifespan:",Swidth/2-fontScale*6,fontScale*10);
                inputField_refill_lifespan.setBounds(Swidth/2,9*fontScale,5*fontScale,fontScale+4);
                }
                /*
                g.drawString("< -> >",Swidth/2,fontScale*15);
                    killMeX1[58]=Swidth/2;
                    killMeY1[58]=fontScale*14;
                    killMeX2[58]=Swidth/2+fontScale*4;
                    killMeY2[58]=fontScale*16;
                     killMeZ[58]=11231;
                  */   
                g.drawString("< V >",Swidth/2,fontScale*15);
                    killMeX1[59]=Swidth/2;
                    killMeY1[59]=fontScale*14;
                    killMeX2[59]=Swidth/2+fontScale*4;
                    killMeY2[59]=fontScale*16;
                     killMeZ[59]=11231;
            }
            
            if(state==11232){
               
                //part
                g.drawString("Refill - part",Swidth/2-fontScale*6,fontScale*4);
                g.drawString("ItemID:",Swidth/2-fontScale*6,fontScale*5);
                g.drawString("#"+f_barcode+" "+f_itemname+"["+f_itemid+"]",Swidth/2-fontScale*3,fontScale*6); 
                
                inputField_refill_item.setBounds(Swidth/2,4*fontScale,5*fontScale,fontScale+4);
                paintMenSub(g);
                g.drawString("Name:",Swidth/2-fontScale*6,fontScale*8);
                inputField_refill_name.setBounds(Swidth/2,7*fontScale,5*fontScale,fontScale+4);
                g.drawString("packingdate:",Swidth/2-fontScale*6,fontScale*9);
                inputField_refill_packingdate.setBounds(Swidth/2,8*fontScale,5*fontScale,fontScale+4);
                g.drawString("stdContainer:",Swidth/2-fontScale*6,fontScale*10);
                inputField_refill_stdcontainer.setBounds(Swidth/2,9*fontScale,5*fontScale,fontScale+4);
                g.drawString("stdLifespan:",Swidth/2-fontScale*6,fontScale*11);
                inputField_refill_lifespan.setBounds(Swidth/2,10*fontScale,5*fontScale,fontScale+4);
                g.drawString("Barcode:",Swidth/2-fontScale*6,fontScale*12);
                inputField_refill_barcode.setBounds(Swidth/2,11*fontScale,5*fontScale,fontScale+4);
                
                if(substate==0){
                g.drawString("< -> >",Swidth/2,fontScale*15);
                    killMeX1[56]=Swidth/2;
                    killMeY1[56]=fontScale*14;
                    killMeX2[56]=Swidth/2+fontScale*4;
                    killMeY2[56]=fontScale*16;
                     killMeZ[56]=11232;
                }else{ 
                     if(substate!=lastsubstate){
try {
                        vejning("Z"); //tare the weigth
                    } catch (IOException ex) {
                        Logger.getLogger(OLD_WCU.class.getName()).log(Level.SEVERE, null, ex);
                    }
lastsubstate=substate;
if(substate!=0){
 timer.schedule (new SayHello(), 1000);
}
repaint();
}
                        //vejning
                        g.drawString("Place item on weight",Swidth/2,fontScale*13);
                       //String weight="fail";
                        try {
                        gotWeight=vejning("S");
                    } catch (IOException ex) {
                        Logger.getLogger(OLD_WCU.class.getName()).log(Level.SEVERE, null, ex);
                    } 
                        
                    g.drawString(gotWeight+" g",Swidth/2,fontScale*14);
                     
                g.drawString("< V >",Swidth/2,fontScale*15);
                    killMeX1[57]=Swidth/2;
                    killMeY1[57]=fontScale*14;
                    killMeX2[57]=Swidth/2+fontScale*4;
                    killMeY2[57]=fontScale*16;
                     killMeZ[57]=11232;
                }
                
            }
            
        }
    
    public void paintMenSub(Graphics g){
        //g.fillRect((Swidth/4)*1,fontScale*2,(Swidth/4)*1,fontScale*1);
        
        if(state==1121){
            g.setFont(new Font(stdFont2, Font.BOLD, fontScale));
        }else{
            g.setFont(new Font(stdFont, Font.PLAIN, fontScale));
        }
           g.drawString("Ny Varetype",Swidth/4*1,fontScale*2);
           //System.out.println(state);
        if(state==1122){
            g.setFont(new Font(stdFont2, Font.BOLD, fontScale));
            //System.err.println("sigh");
        }else{
            g.setFont(new Font(stdFont, Font.PLAIN, fontScale));
        }
           g.drawString("Ny Beholder",Swidth/4*2,fontScale*2);
        if(state==1123||state==11231||state==11232){
            g.setFont(new Font(stdFont2, Font.BOLD, fontScale));
        }else{
            g.setFont(new Font(stdFont, Font.PLAIN, fontScale));
        }
           g.drawString("Opfyldning",Swidth/4*3,fontScale*2);
            
           g.setFont(new Font(stdFont, Font.PLAIN, fontScale));
            
           //112
            killMeX1[27]=Swidth/4*1;
            killMeY1[27]=fontScale*2;
            killMeX2[27]=Swidth/4*2;
            killMeY2[27]=fontScale*3;
             killMeZ[27]=112;
            
            killMeX1[28]=Swidth/4*2;
            killMeY1[28]=fontScale*2;
            killMeX2[28]=Swidth/4*3;
            killMeY2[28]=fontScale*3;
             killMeZ[28]=112;
            
            killMeX1[29]=Swidth/4*3;
            killMeY1[29]=fontScale*2;
            killMeX2[29]=Swidth/4*4;
            killMeY2[29]=fontScale*3;
             killMeZ[29]=112;
             
             //1121
            killMeX1[30]=Swidth/4*1;
            killMeY1[30]=fontScale*2;
            killMeX2[30]=Swidth/4*2;
            killMeY2[30]=fontScale*3;
             killMeZ[30]=1121;
            
            killMeX1[31]=Swidth/4*2;
            killMeY1[31]=fontScale*2;
            killMeX2[31]=Swidth/4*3;
            killMeY2[31]=fontScale*3;
             killMeZ[31]=1121;
            
            killMeX1[32]=Swidth/4*3;
            killMeY1[32]=fontScale*2;
            killMeX2[32]=Swidth/4*4;
            killMeY2[32]=fontScale*3;
             killMeZ[32]=1121;
             
             //1122
            killMeX1[33]=Swidth/4*1;
            killMeY1[33]=fontScale*2;
            killMeX2[33]=Swidth/4*2;
            killMeY2[33]=fontScale*3;
             killMeZ[33]=1122;
            
            killMeX1[34]=Swidth/4*2;
            killMeY1[34]=fontScale*2;
            killMeX2[34]=Swidth/4*3;
            killMeY2[34]=fontScale*3;
             killMeZ[34]=1122;
            
            killMeX1[35]=Swidth/4*3;
            killMeY1[35]=fontScale*2;
            killMeX2[35]=Swidth/4*4;
            killMeY2[35]=fontScale*3;
             killMeZ[35]=1122;
             
             //1123
            killMeX1[36]=Swidth/4*1;
            killMeY1[36]=fontScale*2;
            killMeX2[36]=Swidth/4*2;
            killMeY2[36]=fontScale*3;
             killMeZ[36]=1123;
            
            killMeX1[37]=Swidth/4*2;
            killMeY1[37]=fontScale*2;
            killMeX2[37]=Swidth/4*3;
            killMeY2[37]=fontScale*3;
             killMeZ[37]=1123;
            
            killMeX1[38]=Swidth/4*3;
            killMeY1[38]=fontScale*2;
            killMeX2[38]=Swidth/4*4;
            killMeY2[38]=fontScale*3;
             killMeZ[38]=1123;
             
             //11231
            killMeX1[39]=Swidth/4*1;
            killMeY1[39]=fontScale*2;
            killMeX2[39]=Swidth/4*2;
            killMeY2[39]=fontScale*3;
             killMeZ[39]=11231;
            
            killMeX1[40]=Swidth/4*2;
            killMeY1[40]=fontScale*2;
            killMeX2[40]=Swidth/4*3;
            killMeY2[40]=fontScale*3;
             killMeZ[40]=11231;
            
            killMeX1[41]=Swidth/4*3;
            killMeY1[41]=fontScale*2;
            killMeX2[41]=Swidth/4*4;
            killMeY2[41]=fontScale*3;
             killMeZ[41]=11231;
             
             //11232
            killMeX1[42]=Swidth/4*1;
            killMeY1[42]=fontScale*2;
            killMeX2[42]=Swidth/4*2;
            killMeY2[42]=fontScale*3;
             killMeZ[42]=11232;
            
            killMeX1[43]=Swidth/4*2;
            killMeY1[43]=fontScale*2;
            killMeX2[43]=Swidth/4*3;
            killMeY2[43]=fontScale*3;
             killMeZ[43]=11232;
            
            killMeX1[44]=Swidth/4*3;
            killMeY1[44]=fontScale*2;
            killMeX2[44]=Swidth/4*4;
            killMeY2[44]=fontScale*3;
             killMeZ[44]=11232;
           
        paintMen(g);
       }
    
     public void paintMen(Graphics g){
           g.drawString(usr+" ["+usrId+"]",Swidth/5*1,fontScale);
        if(state==111||state==11){
            g.setFont(new Font(stdFont2, Font.BOLD, fontScale));
        }else{
            g.setFont(new Font(stdFont, Font.PLAIN, fontScale));
        }
           g.drawString("Service",Swidth/5*2,fontScale);
        if(state==112||state==1121||state==1122||state==1123||state==11231||state==11232){
            g.setFont(new Font(stdFont2, Font.BOLD, fontScale));
        }else{
            g.setFont(new Font(stdFont, Font.PLAIN, fontScale));
        }
           g.drawString("Adminsitration",Swidth/5*3,fontScale);
            g.setFont(new Font(stdFont, Font.PLAIN, fontScale));
           g.drawString("Logud",Swidth/5*4,fontScale);
           
            g.setFont(new Font(stdFont, Font.PLAIN, fontScale));
           
           //11
           killMeX1[2]=Swidth/5*2;
            killMeY1[2]=fontScale*1;
            killMeX2[2]=Swidth/5*3;
            killMeY2[2]=fontScale*2;
            killMeZ[2]=11;
            
           killMeX1[3]=Swidth/5*3;
            killMeY1[3]=fontScale*1;
            killMeX2[3]=Swidth/5*4;
            killMeY2[3]=fontScale*2;
            killMeZ[3]=11;
            
           killMeX1[4]=Swidth/5*4;
            killMeY1[4]=fontScale*1;
            killMeX2[4]=Swidth/5*5;
            killMeY2[4]=fontScale*2;
            killMeZ[4]=11;
           
            //111
            killMeX1[5]=Swidth/5*2;
            killMeY1[5]=fontScale*1;
            killMeX2[5]=Swidth/5*3;
            killMeY2[5]=fontScale*2;
            killMeZ[5]=111;
            
           killMeX1[6]=Swidth/5*3;
            killMeY1[6]=fontScale*1;
            killMeX2[6]=Swidth/5*4;
            killMeY2[6]=fontScale*2;
            killMeZ[6]=111;
            
           killMeX1[7]=Swidth/5*4;
            killMeY1[7]=fontScale*1;
            killMeX2[7]=Swidth/5*5;
            killMeY2[7]=fontScale*2;
            killMeZ[7]=111;
           
            //112
            
            killMeX1[8]=Swidth/5*2;
            killMeY1[8]=fontScale*1;
            killMeX2[8]=Swidth/5*3;
            killMeY2[8]=fontScale*2;
            killMeZ[8]=112;
            
           killMeX1[9]=Swidth/5*3;
            killMeY1[9]=fontScale*1;
            killMeX2[9]=Swidth/5*4;
            killMeY2[9]=fontScale*2;
            killMeZ[9]=112;
            
           killMeX1[10]=Swidth/5*4;
            killMeY1[10]=fontScale*1;
            killMeX2[10]=Swidth/5*5;
            killMeY2[10]=fontScale*2;
            killMeZ[10]=112;
            
            //1121
            killMeX1[11]=Swidth/5*2;
            killMeY1[11]=fontScale*1;
            killMeX2[11]=Swidth/5*3;
            killMeY2[11]=fontScale*2;
             killMeZ[11]=1121;
            
            killMeX1[12]=Swidth/5*3;
            killMeY1[12]=fontScale*1;
            killMeX2[12]=Swidth/5*4;
            killMeY2[12]=fontScale*2;
             killMeZ[12]=1121;
            
            killMeX1[13]=Swidth/5*4;
            killMeY1[13]=fontScale*1;
            killMeX2[13]=Swidth/5*5;
            killMeY2[13]=fontScale*2;
             killMeZ[13]=1121;
             
             //1122
            killMeX1[14]=Swidth/5*2;
            killMeY1[14]=fontScale*1;
            killMeX2[14]=Swidth/5*3;
            killMeY2[14]=fontScale*2;
             killMeZ[14]=1122;
            
            killMeX1[15]=Swidth/5*3;
            killMeY1[15]=fontScale*1;
            killMeX2[15]=Swidth/5*4;
            killMeY2[15]=fontScale*2;
             killMeZ[15]=1122;
            
            killMeX1[16]=Swidth/5*4;
            killMeY1[16]=fontScale*1;
            killMeX2[16]=Swidth/5*5;
            killMeY2[16]=fontScale*2;
             killMeZ[16]=1122;
           

            //1123
             
            killMeX1[17]=Swidth/5*2;
            killMeY1[17]=fontScale*1;
            killMeX2[17]=Swidth/5*3;
            killMeY2[17]=fontScale*2;
             killMeZ[17]=1123;
            
            killMeX1[18]=Swidth/5*3;
            killMeY1[18]=fontScale*1;
            killMeX2[18]=Swidth/5*4;
            killMeY2[18]=fontScale*2;
             killMeZ[18]=1123;
            
            killMeX1[19]=Swidth/5*4;
            killMeY1[19]=fontScale*1;
            killMeX2[19]=Swidth/5*5;
            killMeY2[19]=fontScale*2;
             killMeZ[19]=1123;
             
             //11231
             
            killMeX1[20]=Swidth/5*2;
            killMeY1[20]=fontScale*1;
            killMeX2[20]=Swidth/5*3;
            killMeY2[20]=fontScale*2;
             killMeZ[20]=11231;
            
            killMeX1[21]=Swidth/5*3;
            killMeY1[21]=fontScale*1;
            killMeX2[21]=Swidth/5*4;
            killMeY2[21]=fontScale*2;
             killMeZ[21]=11231;
            
            killMeX1[22]=Swidth/5*4;
            killMeY1[22]=fontScale*1;
            killMeX2[22]=Swidth/5*5;
            killMeY2[22]=fontScale*2;
             killMeZ[22]=11231;
             
             //11231
             
            killMeX1[23]=Swidth/5*2;
            killMeY1[23]=fontScale*1;
            killMeX2[23]=Swidth/5*3;
            killMeY2[23]=fontScale*2;
             killMeZ[23]=11232;
            
            killMeX1[24]=Swidth/5*3;
            killMeY1[24]=fontScale*1;
            killMeX2[24]=Swidth/5*4;
            killMeY2[24]=fontScale*2;
             killMeZ[24]=11232;
            
            killMeX1[25]=Swidth/5*4;
            killMeY1[25]=fontScale*1;
            killMeX2[25]=Swidth/5*5;
            killMeY2[25]=fontScale*2;
             killMeZ[25]=11232;
           
       }
    
    public void grabDim(){
            //GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
            Swidth = refer.getWidth();
            Sheight = refer.getHeight();
        }
    
    JFrame refer;
        
        public OLD_WCU(JFrame vindue) {
		try {  
			jbInit(vindue); 
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
        
        //function:insert (table,fields,data)
        //insert; insert("comments", "MYUSER, EMAIL, WEBPAGE, SUMMARY, COMMENTS, DATUM", "'fuck', 'you', 'very',"+ Sheight+", 'cunt', '1000-10-10'");
        public void insert(String table, String fields, String data){
            try{
                
                Statement st = conn.createStatement();
                st.addBatch("INSERT INTO "+table+"("+fields+") VALUES("+data+")");
                st.executeBatch();
            }catch (SQLException ex) {
                System.out.println("An error occurred inserting (bad syntax)");
                ex.printStackTrace();
            }
        }
        
        //function:update
        //update("querry");
        //update("UPDATE comments SET COMMENTS='Java'");
        public void update(String Querry){
            try{
                Statement st = conn.createStatement();
                st.addBatch(Querry);
                st.executeBatch();
            }catch (SQLException ex) {
                System.out.println("An error occurred inserting (bad syntax)");
                ex.printStackTrace();
            }
        }

      public void sqltest(){
          update("UPDATE comments SET COMMENTS='Java'"); //can also be used to run insert/delete..
          insert("comments", "MYUSER, EMAIL, WEBPAGE, SUMMARY, COMMENTS, DATUM", "'fuck', 'you', 'very',"+ Sheight+", 'cunt', '1000-10-10'");
            
          try{
            String query=null;
            ResultSet rs=null;
            Statement st = conn.createStatement();
             
            query="SELECT * FROM comments";         
            rs = st.executeQuery(query);
            while (rs.next())
      {
        int id = rs.getInt("id");
        String firstName = rs.getString("MYUSER");
        String lastName = rs.getString("EMAIL");
        String lastName2 = rs.getString("COMMENTS");
        Date dateCreated = rs.getDate("DATUM");
       //boolean isAdmin = rs.getBoolean("is_admin");
        //int numPoints = rs.getInt("num_points");
         
        // print the results
        System.out.format("%s, %s, %s, %s, %s\n", id, firstName, lastName, lastName2, dateCreated);
      }           
            
            } catch (SQLException ex) {
            System.out.println("An error occurred. Maybe user/password is invalid");
            ex.printStackTrace();
            
        }
        
      }
      
      public void verifyUser(){
          int gotone=0;
          try{
            String query=null;
            ResultSet rs=null;
            Statement st = conn.createStatement();
            String username=inputField_login_user.getText();
            String password=inputField_login_psw.getText();
            query="SELECT * FROM users WHERE name='"+username+"' AND psw='"+password+"' LIMIT 0,1";         
            rs = st.executeQuery(query);
            while (rs.next())
      {
          gotone=1;
        usrId = rs.getInt("id");
        usr = rs.getString("name");
        String psw = rs.getString("psw"); 
        Date dateC = rs.getDate("date");
        // print the results
        System.out.format("%s, %s, %s, %s\n", usrId, usr, psw, dateC);
        state=11;
        repaint();
      }
            if(gotone==0){
                
            inputField_login_user.setText("");
            inputField_login_psw.setText("");
            inputField_login_user.requestFocus();
            }
            
            } catch (SQLException ex) {
            System.out.println("Something went wrong / nothing found");
            ex.printStackTrace();
            inputField_login_user.setText("");
            inputField_login_psw.setText("");
            inputField_login_user.requestFocus();
        }
      }
      
        
         private void jbInit(JFrame vindue) throws Exception {
             connect();
             vindue.add(inputField_login_user);
             vindue.add(inputField_login_psw);
             vindue.add(inputField_config_user);
             vindue.add(inputField_config_psw);
             vindue.add(inputField_config_container);
             vindue.add(inputField_config_discard);
             vindue.add(inputField_service_item);
             vindue.add(inputField_service_container); 
             vindue.add(inputField_item_item);
       vindue.add(inputField_item_stdprice);
       vindue.add(inputField_item_stdcontainer);
       vindue.add(inputField_item_name);
       vindue.add(inputField_item_stddiscard);
       vindue.add(inputField_item_lifespan);
       
       vindue.add(inputField_container_item);
       vindue.add(inputField_container_stdprice); 
       vindue.add(inputField_container_name); 
       
       vindue.add(inputField_refill_item);
       vindue.add(inputField_refill_barcode);
       
       //part 
       vindue.add(inputField_refill_stdcontainer);
       vindue.add(inputField_refill_name);
       
       // only whole
       vindue.add(inputField_refill_packingdate);
       vindue.add(inputField_refill_lifespan);
       vindue.add(inputField_refill_amount);
             
             vindue.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    int thisX=e.getX();
                    int thisY=e.getY();
                    for (int i=0; i<clickableElements;i++){
                        //System.err.println(killMeX1[i]+">"+thisX+"<"+killMeX2[i]+","+killMeY1[i]+">"+thisY+"<"+killMeY2[i]);
                        if(state==killMeZ[i]&&thisX>killMeX1[i]&&thisX<killMeX2[i]&&thisY>killMeY1[i]+(fontScale/2)&&thisY<killMeY2[i]+(fontScale/2)){
                            System.out.println("hit .. "+i+" .. (and in same screen)");
                            if(i==0){
                                //loginknap
                                verifyUser();
                            }else if(i==61){
                                state=111;
                                String query1;
                                String query2;
                                String query3;
                                int temp_itemid=0;
                                int temp_itemid2=0;
                                int temp_weight=0;
                                int temp_weight2=0;
                                int newWeight=0; 
                                int mweight=0;
                                String temp0[];
                                String temp[];
                                String temp2[];
                                String temp3[];
                                String temp4[];
                                String temp5[];
                                String temp6[];
                                for (int iz = 0; iz < Cart.size(); iz++) {
                                    temp0=(Cart.get(iz).toString()).split("I:");
                                    System.out.println(temp0[1]+" -0");
                                    temp=temp0[1].split(".B:");
                                    System.out.println(temp[0]+" -1");
                                    temp2=temp[0].split("-W:");
                                    temp3=temp2;
                                    System.out.println(temp3[1]+" -3");
                                    System.out.println(temp3[0]+" -4");
                                    temp_weight=Integer.parseInt(temp3[1]);
                                    System.out.println(temp_weight+" -5");
                                    temp4=temp3[0].split("\\[");
                                      temp5=temp4[1].split("\\]");
                                      temp_itemid=Integer.parseInt(temp5[0]);
                                      
                                    System.out.println(temp_itemid+" -6");
                                    if(temp.length > 1){
                                         //the thingy haz a container
                                    temp4=temp[1].split("-W:");
                                      temp_weight2=Integer.parseInt(temp4[1]);
                                    System.out.println(temp_weight2+" -5");
                                      temp5=temp4[0].split("\\[");
                                      temp6=temp5[1].split("\\]");
                                      temp_itemid2=Integer.parseInt(temp6[0]);
                                    System.out.println(temp_itemid2+" -7");
                                     } 
			//g.drawString((Cart.get(i).toString()),0,fontScale*(15+i));
                                    query1="date,user,itemid,weight,price"; 
                                    query2="(select now()),'"+usrId+"','"+temp_itemid+"','"+temp_weight+"','"+0+"'";
                                    System.out.println(query1);
                                    System.out.println(query2);
                                        insert("log",query1,query2);
                                        if(temp.length > 1){
            ResultSet rs=null;
            Statement st; 
                                    try {
                                        st = conn.createStatement();
                                    
            query3="SELECT * FROM items WHERE id='"+temp_itemid+"' LIMIT 0,1";         
            rs = st.executeQuery(query3);
            while (rs.next())
      {  
          //System.err.println(itemidto+" .e. ");
        mweight = rs.getInt("weight"); 
      }
            newWeight=mweight-temp_weight;
                                        update("UPDATE items SET weight='"+newWeight+"' WHERE id='"+temp_itemid+"'");
                                    
                                         update("UPDATE items SET deletedDate=(select now()) WHERE deletedDate IS NULL AND parent='"+temp_itemid2+"' ORDER BY id LIMIT 1");       
                                  
                                    query1="date,user,itemid,weight,price"; 
                                    query2="(select now()),'"+usrId+"','"+temp_itemid2+"','"+temp_weight+"','"+0+"'";
                                    System.out.println(query1);
                                    System.out.println(query2);
                                        insert("log",query1,query2);
                                      
                                    } catch (SQLException ex) {
                                        Logger.getLogger(OLD_WCU.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }else{
                                         update("UPDATE items SET deletedDate=(select now()) WHERE deletedDate IS NULL AND parent='"+temp_itemid+"' ORDER BY id LIMIT 1");
                                    
                                        }
                                }
                                        Cart.clear();
                                
                                repaint();
                            }
                            else if(i==1){
                                //configknap
                                state=12;
                                repaint();
                            }else if(i==2||i==5||i==8||i==11||i==14||i==17||i==20||i==23){
                                //Status
                                state=111;
                                repaint();
                            }else if(i==3||i==6||i==9||i==12||i==15||i==18||i==21||i==24){
                                //adminsitration
                                state=112;
                                repaint();
                            }else if(i==4||i==7||i==10||i==13||i==16||i==19||i==22||i==25){
                                //logud
                                state=1;
                                usr=null;
                                usrId=0;
                                repaint();
                            }else if(i==26){
                                state=1;
                                stdusr=inputField_config_user.getText();
                                stdpsw=inputField_config_psw.getText();
                                stdcontainer=inputField_config_container.getText();
                                stddiscard=inputField_config_discard.getText();
                                repaint();
                            }else if(i==27||i==30||i==33||i==36||i==39||i==42){
                                //Ny varetype
                                state=1121;
                                repaint();
                            }else if(i==28||i==31||i==34||i==37||i==40||i==43){
                                //Ny beholder
                                state=1122;
                                repaint();
                            }else if(i==29||i==32||i==35||i==38||i==41||i==44){
                                //Opfyldning
                                state=1123;
                                repaint();
                            }else if(i==45){
                                if(r_itemid[0]!='\n'&&r_itemid[0]!=0&&((r_parent==0||r_parent=='\n')||(t_itemid!='\n'&&t_itemid!=0))){
                        String toCart="I:"+r_barcode[0]+","+r_itemname[0]+"["+r_itemid[0]+"]";
                                if((r_parent=='\n' || r_parent==0)){
                                    
                                    toCart=toCart+"-W:"+r_weight;
                                }else{
                                    toCart=toCart+"-W:"+gotWeight;
                                    toCart=toCart+".B:"+t_barcode+","+t_itemname+"["+t_itemid+"]"+"-W:"+t_weight;
                                    
                                }
                                Cart.add(toCart);
                                substate=2;
                                r_itemid[0]=0;
                           r_itemname[0]=null;
                           r_barcode[0]=null;
                           r_weight=0;
                           r_parent=0;
                                  
                           r_itemid[1]=0;
                           r_itemname[1]=null;
                           r_barcode[1]=null;
                            r_itemc=null;
                           t_itemid=0;
                           t_itemname=null;
                           t_barcode=null;
                           t_weight=0;
                           r_parent=0;
                                repaint();
                                }
                            }else if(i==60){
                            if(f_state>0&&f_substate>0){
                                state=f_state;
                                substate=f_substate; 
                                repaint();
                                 }
                            }else if(i==47||i==52){
                                submenu=0;
                                if(i==47){
                                 String tempbuffer="";

tempbuffer=inputField_item_name.getText();
inputField_item_name.setText(" ");
inputField_item_name.setText(tempbuffer);
tempbuffer=inputField_item_stdprice.getText();
inputField_item_stdprice.setText(" ");
inputField_item_stdprice.setText(tempbuffer);
tempbuffer=inputField_item_stdcontainer.getText();
inputField_item_stdcontainer.setText(" ");
inputField_item_stdcontainer.setText(tempbuffer);
tempbuffer=inputField_item_stddiscard.getText();
inputField_item_stddiscard.setText(" ");
inputField_item_stddiscard.setText(tempbuffer);
tempbuffer=inputField_item_lifespan.getText();
inputField_item_lifespan.setText(" ");
inputField_item_lifespan.setText(tempbuffer);
tempbuffer=inputField_item_item.getText();
inputField_item_item.setText(" ");
inputField_item_item.setText(tempbuffer);   
                                }else{
                                   String tempbuffer="";

tempbuffer=inputField_container_item.getText();
inputField_container_item.setText(" ");
inputField_container_item.setText(tempbuffer);
tempbuffer=inputField_container_name.getText();
inputField_container_name.setText(" ");
inputField_container_name.setText(tempbuffer);
tempbuffer=inputField_container_stdprice.getText();
inputField_container_stdprice.setText(" ");
inputField_container_stdprice.setText(tempbuffer); 
                                }
                                repaint();
                            }else if(i==48||i==53){
                                submenu=1;
                                if(i==48){
                                    String tempbuffer="";

tempbuffer=inputField_item_name.getText();
inputField_item_name.setText(" ");
inputField_item_name.setText(tempbuffer);
tempbuffer=inputField_item_stdprice.getText();
inputField_item_stdprice.setText(" ");
inputField_item_stdprice.setText(tempbuffer);
tempbuffer=inputField_item_stdcontainer.getText();
inputField_item_stdcontainer.setText(" ");
inputField_item_stdcontainer.setText(tempbuffer);
tempbuffer=inputField_item_stddiscard.getText();
inputField_item_stddiscard.setText(" ");
inputField_item_stddiscard.setText(tempbuffer);
tempbuffer=inputField_item_lifespan.getText();
inputField_item_lifespan.setText(" ");
inputField_item_lifespan.setText(tempbuffer);
tempbuffer=inputField_item_item.getText();
inputField_item_item.setText(" ");
inputField_item_item.setText(tempbuffer);
                                }else{
                                    String tempbuffer="";

tempbuffer=inputField_container_item.getText();
inputField_container_item.setText(" ");
inputField_container_item.setText(tempbuffer);
tempbuffer=inputField_container_name.getText();
inputField_container_name.setText(" ");
inputField_container_name.setText(tempbuffer);
tempbuffer=inputField_container_stdprice.getText();
inputField_container_stdprice.setText(" ");
inputField_container_stdprice.setText(tempbuffer);
                                }
                                repaint();
                            }
                            else if(i==56){ 
                                if(substate==0){
                                state=11232;
                                substate=1;
                                f_state=11232;
                                f_substate=1;
                                lastsubstate=0;
                                repaint(); 
                                }
                            }else if(i==59){
                                //submit, 11231, whole
                                //Verify data
                                String query1="";
                                String query2="";
                                boolean f_error=false;
                                System.out.println("insert started");
                                if(f_itemid!=0){
                                if(f_subtype!=1){
                                    System.out.println("Hasn't subtype");
                                    if(isNumeric(inputField_refill_lifespan.getText())){
                                        
                                    }else{
                                        System.err.println("subtype != int");
                                        f_error=true;
                                    }
                                    //yyyy-mm-dd
                                    if(isDate(inputField_refill_packingdate.getText())){
                                    
                                }else{
                                        System.err.println("date != date");
                                    f_error=true;
                                }
                                    query1="originDate, lifespan, ";
                                    query2="'"+inputField_refill_packingdate.getText()+"', '"+inputField_refill_lifespan.getText()+"', ";
                                }
                                if(isNumeric(inputField_refill_amount.getText())){
                                    
                                }else{
                                    System.err.println("amount != numeric");
                                    f_error=true;
                                }
                                
                                }else{
                                    System.err.println("no id..");
                                    f_error=true;
                                }
                                if(f_error==false){
                                    //insert; insert("comments", "MYUSER, EMAIL, WEBPAGE, SUMMARY, COMMENTS, DATUM", "'fuck', 'you', 'very',"+ Sheight+", 'cunt', '1000-10-10'");
                                    
                                    //At this point, it possibly is blank, or contains originDate, lifespan..
                                    query1=query1+"date,name,parent,weight"; 
                                    query2=query2+"(select now()),'"+f_itemname+" unit','"+f_itemid+"','"+f_weight+"'";
                                    System.out.println(query1);
                                    System.out.println(query2);
                                    for(int ii=0;ii<Integer.parseInt(inputField_refill_amount.getText());ii++){
                                        insert("items",query1,query2);
                                    }
                                    
                                 state=112;
                                 f_state=0;
                                 repaint();
                                } else{
                                    System.out.println("some error appeared");
                                }
                            }else if(i==49&&substate==0){
                                //page2, new item
                                substate=1;
                                repaint();
                            }else if(i==54&&substate==0){
                                //page2, new container
                                substate=1;
                                repaint();
                            }else if(i==50&&substate==1){
                                //submit, new item
                                //Verify data
                                String query1="";
                                String query2="";
                                boolean f_error=false;
                                System.out.println("insert started");
                                 
                                    if(isNumeric(inputField_container_stdprice.getText())){
                                        
                                    }else{
                                        System.err.println("stdprice != int");
                                        f_error=true;
                                    } 
                                     if(isNumeric(inputField_item_stddiscard.getText())){
                                        
                                    }else{
                                        System.err.println("stddiscard != int");
                                        f_error=true;
                                    } 
                                      if(isNumeric(inputField_item_lifespan.getText())){
                                        
                                    }else{
                                        System.err.println("stdlifespan != int");
                                        f_error=true;
                                    } 
                                       if(isNumeric(inputField_item_stdcontainer.getText())){
                                        
                                    }else{
                                        System.err.println("stdcontainer != int");
                                        f_error=true;
                                    } 
                                    if(inputField_item_name.getText()==null||inputField_item_name.getText().equals(null)||inputField_item_name.getText().length()<0||inputField_item_name.getText().isEmpty()||inputField_item_name.getText()=="null"||inputField_item_name.getText().equals("")||inputField_item_name.getText()=="0"||inputField_item_name.getText().equals("null")||inputField_item_name.getText().equals("0")){
                                    System.err.println("naming failed");
                                        f_error=true;
                                    }
                                    if(inputField_item_item.getText()==null||inputField_item_item.getText().equals(null)||inputField_item_item.getText().length()<0||inputField_item_item.getText().isEmpty()||inputField_item_item.getText()=="null"||inputField_item_item.getText().equals("")||inputField_item_item.getText()=="0"||inputField_item_item.getText().equals("null")||inputField_item_item.getText().equals("0")){
                                    System.err.println("barcoding failed");
                                        f_error=true;
                                    }
                                    
                                  
                                if(f_error==false){
                                    //insert; insert("comments", "MYUSER, EMAIL, WEBPAGE, SUMMARY, COMMENTS, DATUM", "'fuck', 'you', 'very',"+ Sheight+", 'cunt', '1000-10-10'");
                                  int memtemp=2;
                                  if(submenu==1){
                                      memtemp=3;
                                  }
                                  //2=whole,3=part
                                     
                                    //At this point, it possibly is blank, or contains originDate, lifespan..
                                    query1="date,name,barcode,weight,subtype, stddiscard, stdcontainer, lifespan"; 
                                    query2="(select now()),'"+inputField_item_name.getText()+"','"+inputField_item_item.getText()+"','"+gotWeight+"','"+memtemp+"','"+inputField_item_stddiscard.getText()+"','"+inputField_item_stdcontainer.getText()+"','"+inputField_item_lifespan.getText()+"'";
                                    System.out.println(query1);
                                    System.out.println(query2);
                                         insert("items",query1,query2);
                                    
                                    
                                 state=112;
                                 f_state=0;
                                 substate=0;
                                 repaint();
                                } else{
                                    System.out.println("some error appeared");
                                }
                                substate=0;
                                repaint();
                            }else if(i==55&&substate==1){
                                //submit, new container
                                //Verify data
                                String query1="";
                                String query2="";
                                boolean f_error=false;
                                System.out.println("insert started");
                                 
                                    if(isNumeric(inputField_container_stdprice.getText())){
                                        
                                    }else{
                                        System.err.println("stdprice != int");
                                        f_error=true;
                                    } 
                                    if(inputField_container_name.getText()==null||inputField_container_name.getText().equals(null)||inputField_container_name.getText().length()<0||inputField_container_name.getText().isEmpty()||inputField_container_name.getText()=="null"||inputField_container_name.getText().equals("")||inputField_container_name.getText()=="0"||inputField_container_name.getText().equals("null")||inputField_container_name.getText().equals("0")){
                                    System.err.println("naming failed");
                                        f_error=true;
                                    }
                                    if(inputField_container_item.getText()==null||inputField_container_item.getText().equals(null)||inputField_container_item.getText().length()<0||inputField_container_item.getText().isEmpty()||inputField_container_item.getText()=="null"||inputField_container_item.getText().equals("")||inputField_container_item.getText()=="0"||inputField_container_item.getText().equals("null")||inputField_container_item.getText().equals("0")){
                                    System.err.println("barcoding failed");
                                        f_error=true;
                                    }
                                    
                                  
                                if(f_error==false){
                                    //insert; insert("comments", "MYUSER, EMAIL, WEBPAGE, SUMMARY, COMMENTS, DATUM", "'fuck', 'you', 'very',"+ Sheight+", 'cunt', '1000-10-10'");
                                  int memtemp=0;
                                  if(submenu==0){
                                      memtemp=1;
                                  }
                                     
                                    //At this point, it possibly is blank, or contains originDate, lifespan..
                                    query1="date,name,barcode,weight,subtype,lifespan"; 
                                    query2="(select now()),'"+inputField_container_name.getText()+"','"+inputField_container_item.getText()+"','"+gotWeight+"','1','"+memtemp+"'";
                                    System.out.println(query1);
                                    System.out.println(query2);
                                         insert("items",query1,query2);
                                    
                                    
                                 state=112;
                                 f_state=0;
                                 substate=0;
                                 repaint();
                                } else{
                                    System.out.println("some error appeared");
                                }
                                substate=0;
                                repaint();
                                
                            }else if(i==57){
                                if(substate==1&&lastsubstate==1){
                                //submit, 11232, part
                                //Verify data
                                String query1="";
                                String query2="";
                                boolean f_error=false;
                                System.out.println("insert started");
                                if(f_itemid!=0){ 
                                    if(isNumeric(inputField_refill_lifespan.getText())){
                                        
                                    }else{
                                        System.err.println("lifespan != int");
                                        f_error=true;
                                    }
                                    //yyyy-mm-dd
                                    if(isDate(inputField_refill_packingdate.getText())){
                                    
                                }else{
                                        System.err.println("date != date");
                                    f_error=true;
                                }
                                    query1="originDate, lifespan, stdcontainer, ";
                                    query2="'"+inputField_refill_packingdate.getText()+"', '"+inputField_refill_lifespan.getText()+"', '"+inputField_refill_stdcontainer.getText()+"', ";
                                
                                 
                                
                                }else{
                                    System.err.println("no id..");
                                    f_error=true;
                                }
                                if(f_error==false){
                                    //insert; insert("comments", "MYUSER, EMAIL, WEBPAGE, SUMMARY, COMMENTS, DATUM", "'fuck', 'you', 'very',"+ Sheight+", 'cunt', '1000-10-10'");
                                    String temp_name=f_itemname+" container";
                                    if(inputField_refill_name.getText()==null||inputField_refill_name.getText().equals(null)||inputField_refill_name.getText().length()<0||inputField_refill_name.getText().isEmpty()||inputField_refill_name.getText()=="null"||inputField_refill_name.getText().equals("")||inputField_refill_name.getText()=="0"||inputField_refill_name.getText().equals("null")||inputField_refill_name.getText().equals("0")){
                                    }else{
                                         temp_name=inputField_refill_name.getText();
                                     }
                                    String temp_bar=f_itemid+"-";
                                    if(inputField_refill_barcode.getText()==null||inputField_refill_barcode.getText().equals(null)||inputField_refill_barcode.getText().length()<0||inputField_refill_barcode.getText().isEmpty()||inputField_refill_barcode.getText()=="null"||inputField_refill_barcode.getText().equals("")||inputField_refill_barcode.getText()=="0"||inputField_refill_barcode.getText().equals("null")||inputField_refill_barcode.getText().equals("0")){
                                    }else{
                                         temp_bar=inputField_refill_barcode.getText();
                                     }
                                     
                                    //At this point, it possibly is blank, or contains originDate, lifespan..
                                    query1=query1+"date,name,parent,barcode,weight"; 
                                    query2=query2+"(select now()),'"+temp_name+"','"+f_itemid+"','"+temp_bar+"','"+gotWeight+"'";
                                    System.out.println(query1);
                                    System.out.println(query2);
                                         insert("items",query1,query2);
                                    
                                    
                                 state=112;
                                 f_state=0;
                                 substate=0;
                                 repaint();
                                } else{
                                    System.out.println("some error appeared");
                                }
                            }
                            }    
                            
                        }
                    }
                    
                System.out.println("Clicked!"+e.getX()+" : "+e.getY()+" .. "+state);
                }
             });
             
            InputMap inputMap; 
            ActionMap actionMap;
            AbstractAction action;
            inputMap  = this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            actionMap = this.getActionMap();
            
            action    = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent ae) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            };
            
            
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter");
            inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "quit");
            
            actionMap.put("quit", new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                       System.exit(0);
                    

                }
            });
            
            actionMap.put("enter", new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    //firstrun=0;
                    if(state==1){
                        verifyUser();
                    }
                    if(state==12){
                        state=1;
                                stdusr=inputField_config_user.getText();
                                stdpsw=inputField_config_psw.getText();
                                stdcontainer=inputField_config_container.getText();
                                stddiscard=inputField_config_discard.getText();
                                repaint();
                    }
                    if(state==111){
                        
                        if(r_itemid[0]!='\n'&&r_itemid[0]!=0&&((r_parent==0||r_parent=='\n')||(t_itemid!='\n'&&t_itemid!=0))){
                        String toCart="I:"+r_barcode[0]+","+r_itemname[0]+"["+r_itemid[0]+"]";
                                if((r_parent=='\n' || r_parent==0)){
                                    
                                    toCart=toCart+"-W:"+r_weight;
                                }else{
                                    toCart=toCart+"-W:"+gotWeight;
                                    toCart=toCart+".B:"+t_barcode+","+t_itemname+"["+t_itemid+"]"+"-W:"+t_weight;
                                    
                                }
                                Cart.add(toCart);
                                substate=2;
                           r_itemid[0]=0;
                           r_itemname[0]=null;
                           r_barcode[0]=null;
                           r_weight=0;
                           r_parent=0;
                           r_itemid[0]=0;
                                  
                           r_itemid[1]=0;
                           r_itemid[1]=0;
                           r_itemname[1]=null;
                           r_barcode[1]=null;
                           
                           r_itemc=null;
                           
                           t_itemid=0;
                           t_itemid=0;
                           t_itemname=null;
                           t_barcode=null;
                           t_weight=0;
                           r_parent=0;
                                
                                repaint();
                        }
                    }else if(state==11231||state==11232||state==1123){
                        
                        if(state==11231&&inputField_refill_item.isFocusOwner()||state==11232||state==1123){
                            state=f_state;
                        substate=f_substate;
                        }else{
                        //submit, 11231, whole
                                //Verify data
                                String query1="";
                                String query2="";
                                boolean f_error=false;
                                System.out.println("insert started");
                                if(f_itemid!=0){
                                if(f_subtype!=1){
                                    System.out.println("Hasn't subtype");
                                    if(isNumeric(inputField_refill_lifespan.getText())){
                                        
                                    }else{
                                        System.err.println("subtype != int");
                                        f_error=true;
                                    }
                                    //yyyy-mm-dd
                                    if(isDate(inputField_refill_packingdate.getText())){
                                    
                                }else{
                                        System.err.println("date != date");
                                    f_error=true;
                                }
                                    query1="originDate, lifespan, ";
                                    query2="'"+inputField_refill_packingdate.getText()+"', '"+inputField_refill_lifespan.getText()+"', ";
                                }
                                if(isNumeric(inputField_refill_amount.getText())){
                                    
                                }else{
                                    System.err.println("amount != numeric");
                                    f_error=true;
                                }
                                
                                }else{
                                    System.err.println("no id..");
                                    f_error=true;
                                }
                                if(f_error==false){
                                    //insert; insert("comments", "MYUSER, EMAIL, WEBPAGE, SUMMARY, COMMENTS, DATUM", "'fuck', 'you', 'very',"+ Sheight+", 'cunt', '1000-10-10'");
                                    
                                    //At this point, it possibly is blank, or contains originDate, lifespan..
                                    query1=query1+"date,name,parent,weight"; 
                                    query2=query2+"(select now()),'"+f_itemname+" unit','"+f_itemid+"','"+f_weight+"'";
                                    System.out.println(query1);
                                    System.out.println(query2);
                                    for(int ii=0;ii<Integer.parseInt(inputField_refill_amount.getText());ii++){
                                        insert("items",query1,query2);
                                    }
                                    
                        state=112;
                        f_state=0;
                        repaint();
                                } else{
                                    System.out.println("some error appeared");
                                }
                            
                        repaint();
                        }
                    }
                }
            });
            inputField_service_item.getDocument().addDocumentListener(new DocumentListener() {
  public void changedUpdate(DocumentEvent e) {
      if(inputField_service_item.getText().equals(r_barcode[0])&&!inputField_service_item.equals(" ")&&!inputField_service_item.equals("")){
          
      }else{
    service_item();
      }
  }
  public void removeUpdate(DocumentEvent e) {
    if(inputField_service_item.getText().equals(r_barcode[0])&&!inputField_service_item.equals(" ")&&!inputField_service_item.equals("")){
          
      }else{
    service_item();
      }
  }
  public void insertUpdate(DocumentEvent e) {
    if(inputField_service_item.getText().equals(r_barcode[0])&&!inputField_service_item.equals(" ")&&!inputField_service_item.equals("")){
          
      }else{
    service_item();
      }
  }
                
  });
            
            
                        inputField_service_container.getDocument().addDocumentListener(new DocumentListener() {
  public void changedUpdate(DocumentEvent e) {
      if(inputField_service_container.getText().equals(t_barcode)&&!inputField_service_container.equals(" ")&&!inputField_service_container.equals("")){
          
      }else{
    service_container();
      }
  }
  public void removeUpdate(DocumentEvent e) {
    if(inputField_service_container.getText().equals(t_barcode)&&!inputField_service_container.equals(" ")&&!inputField_service_container.equals("")){
          
      }else{
    service_container();
      }
  }
  public void insertUpdate(DocumentEvent e) {
    if(inputField_service_container.getText().equals(t_barcode)&&!inputField_service_container.equals(" ")&&!inputField_service_container.equals("")){
          
      }else{
    service_container();
      }
  }
                
  });
                        
                        inputField_refill_item.getDocument().addDocumentListener(new DocumentListener() {
  public void changedUpdate(DocumentEvent e) {
      if(inputField_refill_item.getText().equals(f_itemid)&&!inputField_refill_item.equals(" ")&&!inputField_refill_item.equals("")){
          
      }else{
    refill_item();
      }
  }
  public void removeUpdate(DocumentEvent e) {
       if(inputField_refill_item.getText().equals(f_itemid)&&!inputField_refill_item.equals(" ")&&!inputField_refill_item.equals("")){
         
      }else{
    refill_item();
      }
  }
  public void insertUpdate(DocumentEvent e) {
       if(inputField_refill_item.getText().equals(f_itemid)&&!inputField_refill_item.equals(" ")&&!inputField_refill_item.equals("")){
          
      }else{
    refill_item();
      }
  }
                
  });
            
            this.setLayout(null);
            repaint();
            //sqltest();
         refer=vindue;
         }
         
         public static boolean isNumeric(String str)
{
  return str.matches("\\d+?");  //match a number  
}
         
         public static boolean isDate(String str){
             return str.matches("\\d{4}-\\d{2}-\\d{2}");
         }
    
}
