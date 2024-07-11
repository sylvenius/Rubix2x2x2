/**
* Made by Linus Sylven 4 anyone to enjoy
* linus.sylven@gmail.com
*/
package dmt.typeotech;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

class MyJPanel extends JPanel{
  public static final long serialVersionUID = 200511011158L;
  Camera cam;
  
  MyJPanel(Camera cam, Color color){
    this.cam = cam;
    setBackground(color);
  }
  
  @Override
  public void paintComponent(Graphics g){ 
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D)g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 
    cam.paint(g2d);
  }
  
}