/**
* Made by Linus Sylven 4 anyone to enjoy
* linus.sylven@gmail.com
*/
package dmt.typeotech;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainFrame extends JFrame{
  public static final long serialVersionUID = 202407042124L;
  Rubik2x2Cube rc;
  int h = 777, w = 888;

  public static void main(String[] p){ 
    JFrame.setDefaultLookAndFeelDecorated(true); 
    MainFrame me = new MainFrame();
    me.setVisible(true);
  }

  MainFrame(){
    rc = new Rubik2x2Cube(new Color(40,40,40));
    setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
          try{try {
            rc.buttLiznrThread.join();
            rc.thread.join();
          } catch (InterruptedException e1) {}} catch (Exception e1) {}
          System.exit(0);
        }
    });
    setLayout(new BorderLayout());
    setTitle("Rubik's 2x2x2 - puzzle");
    add("Center", rc.jp);
    JPanel jpanel = new JPanel();
    jpanel.add(rc.jbHint);
    jpanel.add(rc.jbRanCol);
    int i=0,b=2;
    for(;b<15;b++){
      JButton temp = rc.ranButts[i++];
      jpanel.add(temp);
    }
    jpanel.add(rc.jbSolve);
    add("South", jpanel);
    add("North", rc.jpb);
    setSize(w, h);
    
  }

}//**class RubCube


