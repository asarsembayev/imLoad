package TestingPack;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class TestDrawFinish {
	public int m_nX = 0;
	public int m_nY = 0;
	public JLabel m_lblImage = new JLabel();
	int radius = 10;
	
	public TestDrawFinish(int x, int y){

		ImageIcon icon = null;
		icon = new ImageIcon(TestDrawFinish.class.getResource("Circle_Red_small.png"));
		int w;
		int h;
		if (icon == null)
		{
			w = h = 20;
		}
		else
		{
			this.m_lblImage.setIcon(icon);
			w = icon.getIconWidth();
			h = icon.getIconHeight();
		}
		this.m_lblImage.setBounds(x, y, w, h);

		
	}
	
}

