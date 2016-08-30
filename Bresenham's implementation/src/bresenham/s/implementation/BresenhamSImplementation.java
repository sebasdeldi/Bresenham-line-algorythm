/*
 * 
 * Bresenham's straight line algorythm for Computer Graphics course.
 * Eafit - Medellín.
 * Based on http://members.chello.at/easyfilter/bresenham.html implementation.
 */

package bresenham.s.implementation;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JFrame;



/**
 *
 * @author Sebastián Delgado.
 * 
 */
public class BresenhamSImplementation extends JPanel {


  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g;
    Dimension size = getSize();
    Insets insets = getInsets();


    Scanner input;
    try {
        input = new Scanner(new File("datos.txt")); //datos.txt should be on root folder, otherwise modify file path.     
        int w = size.width - insets.left - insets.right;
        int h = size.height - insets.top - insets.bottom;
        
        int n = input.nextInt();
        //reading from file and setting corresponding color.
        for (int i = 0; i < n; i++) {
            
            int x0 = input.nextInt();
            int y0 = input.nextInt();
            int x1 = input.nextInt();
            int y1 = input.nextInt();
            String color = input.next();
            
            switch (color) {
                case "r":
                    g2d.setColor(Color.RED);
                    break;
                case "b":
                    g2d.setColor(Color.BLUE);
                    break;
                case "y":
                    g2d.setColor(Color.YELLOW);
                    break;
            }
            
            //distance in x and y, setting x, corresponding step (depends on direction.)
	    int dx =  changeSign(x1-x0),sx = x0<x1 ? 1 : -1; 
            int dy = -changeSign(y1-y0), sy = y0<y1 ? 1 : -1;
            int d = dx+dy;
            int e2;

            for(;;){ 
               //drawing each line
               g2d.drawLine(x0 + w/2, -y0 + h/2, x0 + w/2, -y0 + h/2);
               if (x0==x1 && y0==y1) break; //if there is no slope, there is nothing to do.
               e2 = 2*d;
               
               //increment on y and x (depending on the direction) and on d in order to find line end.
               if (e2 >= dy) { 
                   d += dy; 
                   x0 += sx; 
               }
               if (e2 <= dx) { 
                   d += dx; 
                   y0 += sy; 
               } 
            }
            
            
        }
    } catch (FileNotFoundException e) {
        //Log or do something with the filre reading error.
    }
    
  }
  
   
    static int changeSign(int a) {
        if (a < 0) {
            return -a;
        } else {
            return a;
        }
    }

  public static void main(String[] args) {
    //frame settings
    JFrame frame = new JFrame("Bresenham's Imp");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(new BresenhamSImplementation());
    frame.setSize(250, 200);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  }
}
