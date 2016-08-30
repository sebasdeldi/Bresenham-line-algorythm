/*
 * Based on https://github.com/donkike/Computer-Graphics/blob/master/LineClipping/LineClippingPanel.java
 */
package lineclippingpanel;

/**
 *
 * @author sdelgad4
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class LineClippingPanel extends JPanel {

    public static final int INSIDE = 0;
    public static final int LEFT   = 1;
    public static final int RIGHT  = 2;
    public static final int BOTTOM = 4;
    public static final int TOP    = 8;




    private int xMin;
    private int xMax;
    private int yMin;
    private int yMax;

    private LineClipper clipper;
    
    private class LineSegment {
        public int x0;
        public int y0;
        public int x1;
        public int y1;

        public LineSegment(int x0, int y0, int x1, int y1) {
            this.x0 = x0;
            this.y0 = y0;
            this.x1 = x1;
            this.y1 = y1;
        }
    }

    public interface LineClipper {
        public LineSegment clip(LineSegment clip);
    }

        public class LiangBarsky implements LineClipper {

 
        public LineSegment clip(LineSegment line) {
            
            double u1 = 0, u2 = 1;
            int x0 = line.x0, y0 = line.y0, x1 = line.x1, y1 = line.y1;
            int dx = x1 - x0, dy = y1 - y0;
            int p[] = {-dx, dx, -dy, dy};
            int q[] = {x0 - xMin, xMax - x0, y0 - yMin, yMax - y0};
            for (int i = 0; i < 4; i++) {
                if (p[i] == 0) {
                    if (q[i] < 0) {
                        return null;
                    }
                } else {
                    double u = (double) q[i] / p[i];
                    if (p[i] < 0) {
                        u1 = Math.max(u, u1);
                    } else {
                        u2 = Math.min(u, u2);
                    }
                }
            }
            System.out.println("u1: " + u1 + ", u2: " + u2);
            if (u1 > u2) {
                return null;
            }
            int nx0, ny0, nx1, ny1;
            nx0 = (int) (x0 + u1 * dx);
            ny0 = (int) (y0 + u1 * dy);
            nx1 = (int) (x0 + u2 * dx);
            ny1 = (int) (y0 + u2 * dy);
            return new LineSegment(nx0, ny0, nx1, ny1);
        }
    }

   

    public LineClippingPanel(int xMin, int yMin, int xMax, int yMax) {
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;
        this.setBackground(Color.white);
     
        clipper = new LiangBarsky();
       
        
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Scanner input;
        Dimension size = getSize();
        Insets insets = getInsets();
        
        
        
        try {
            input = new Scanner(new File("datos.txt"));
            int n = input.nextInt();
            
            int w = size.width - insets.left - insets.right;
            int h = size.height - insets.top - insets.bottom;
            
            int [][] coords = new int [n][2];
            
            g2d.setColor(Color.black);
            drawLine(g2d, xMin + w/2, yMin + h/2, xMin + w/2, yMax + h/2);
            drawLine(g2d, xMin + w/2, yMax + h/2, xMax + w/2, yMax + h/2);
            drawLine(g2d, xMin + w/2, yMin + h/2, xMax + w/2, yMin + h/2);
            drawLine(g2d, xMax + w/2, yMin + h/2, xMax + w/2, yMax + h/2);

            int x0, y0, x1, y1;
            LineSegment line, clippedLine;
            
            
            for (int i = 0; i < n; i++) {
                
                coords [i][0] = input.nextInt();
                coords [i][1] = input.nextInt();
            }
            
            int m = input.nextInt();
            
            for (int j = 0; j < m; j++){
                
                int ini = input.nextInt();
                int fin = input.nextInt();
        
                x0 = coords[ini][0];
                x1 = coords[fin][0];
                y0 = coords[ini][1];
                y1 = coords[fin][1];
               
             
   
                line = new LineSegment(x0, y0, x1, y1);
                clippedLine = clipper.clip(line);

                System.out.println("Original: " + line);
                System.out.println("Clipped: " + clippedLine);

                if (clippedLine == null) {
                    g2d.setColor(Color.red);
                    drawLine(g2d, line.x0 + w/2, line.y0 + h/2, line.x1+ w/2, line.y1 + h/2);
                } else {
                    g2d.setColor(Color.red);
                    drawLine(g2d, line.x0 + w/2, line.y0 + h/2, clippedLine.x0 + w/2, clippedLine.y0 + h/2);
                    drawLine(g2d, clippedLine.x1 + w/2, clippedLine.y1 + h/2, line.x1 + w/2, line.y1 + h/2);
                    g2d.setColor(Color.blue);
                    drawLine(g2d, clippedLine.x0 + w/2, clippedLine.y0 + h/2, clippedLine.x1 + w/2, clippedLine.y1 + h/2);
                }
            }
            
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LineClippingPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void drawLine(Graphics g, int x1, int y1, int x2, int y2) {
        g.drawLine(x1, getHeight() - y1, x2, getHeight() - y2);
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner input;
        input = new Scanner(new File("datos.txt"));


        

        JFrame mainFrame = new JFrame("Liang-Barsky Implementation");
        mainFrame.setSize(800, 600);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int x0, y0, x1, y1;
      

        int n = input.nextInt();
        
        for(int i = 0; i < n; i++){
            input.nextInt();
            input.nextInt();
        }
        int m = input.nextInt();
        
        for (int j = 0; j < m; j++) {
            input.nextInt();
            input.nextInt();
        }
        
        x0 = input.nextInt();
        y0 = input.nextInt();
        x1 = input.nextInt();
        y1 = input.nextInt();
        


      

        mainFrame.add(new LineClippingPanel(x0, y0, x1, y1));
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

}