package TestingPack;

import java.awt.Color;
import java.io.PrintStream;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class TestRobot
	extends JLabel
{
	public final int ROBOT_STEP = 1;
	public int m_nXDir = 1;
	public int m_nYDir = 0;
	public int m_nStep = 0;
	
	public TestRobot()
	{
		init();
	}
	
	public void init()
	{
		ImageIcon icon = new ImageIcon(TestRobot.class.getResource("Robot.png"));
		if (icon == null) {}
		setIcon(icon);
		setBounds(40, 40, icon.getIconWidth(), icon.getIconHeight());
	}
	
	public void turnLeft()
	{
		System.out.println("Robot - Turn Left!");
		
		this.m_nXDir = -1;
		this.m_nYDir = 0;
	}
	
	public void turnRight()
	{
		System.out.println("Robot - Turn Right!");
		
		this.m_nXDir = 1;
		this.m_nYDir = 0;
	}
	
	public void turnUp()
	{
		System.out.println("Robot - Turn UP!");
		
		this.m_nXDir = 0;
		this.m_nYDir = -1;
	}
	
	public void turnDown()
	{
		System.out.println("Robot - Turn Down!");
		
		this.m_nXDir = 0;
		this.m_nYDir = 1;
	}
	
	public void turnBack()
	{
		System.out.println("Robot - Turn Back!");
		
		this.m_nXDir = (-this.m_nXDir);
		this.m_nYDir = (-this.m_nYDir);
	}
	
	public void go()
	{
		this.m_nStep = 1;
		
		System.out.println("Robot - Go!");
	}
	
	public void stop()
	{
		this.m_nStep = 0;
		
		System.out.println("Robot - Stop!");
	}
	
	public boolean isStopped()
	{
		if (this.m_nStep == 0) {
			return true;
		}
		return false;
	}
	
	public void undoMove()
	{
		int x = getX() - this.m_nStep * this.m_nXDir;
		int y = getY() - this.m_nStep * this.m_nYDir;
		int w = getWidth();
		int h = getHeight();
		
		setBounds(x, y, w, h);
	}
	//for moving from start to finish directly
	public void move(int d, int e)
	{
		//from start to finish
		int x = d + this.m_nStep * this.m_nXDir;
		int y = e + this.m_nStep * this.m_nYDir;
		
		int w = getWidth();
		int h = getHeight();
		
		setBounds(x, y, w, h);
	}
	
	//for moving free
	public void move()
	{
		int x = getX() + this.m_nStep * this.m_nXDir;
		int y = getY() + this.m_nStep * this.m_nYDir;
		int w = getWidth();
		int h = getHeight();
		
		setBounds(x, y, w, h);
	}
	
}
