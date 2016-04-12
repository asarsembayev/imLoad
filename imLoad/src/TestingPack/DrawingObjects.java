package TestingPack;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class DrawingObjects extends JPanel{
	
	private static int x, y;
	
	public void drawing(int xx, int yy){
		System.out.println("drawing");
		x = xx; y =yy;
		repaint();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);       
		System.out.println("paint");
        g.drawString("This is my custom Panel!",10,20);
        g.setColor(Color.RED);
		g.fillOval(x, y, 10, 10);	
	}

}
