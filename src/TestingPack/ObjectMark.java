package TestingPack;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ObjectMark {
	//public String m_strItemName = "Door";
	//public String m_strColor = "Black";
	//public String m_strNumber = "000";
	public int m_nX = 0;
	public int m_nY = 0;
	public JLabel m_lblImage = new JLabel();
	//public Point p;
	
	public int doorNum = 1;
	public int windowNum = 1;
	public int corNum = 1;

	//Point p = new Point(x, y);
	//Map<String, Point> map = new HashMap<>();
	
	public ObjectMark(String strType, int x, int y){
		
		
		//p = new Point(x, y);
		
		ImageIcon icon = null;
		
		if (strType.equals("Door"))
		{
			icon = new ImageIcon(ObjectMark.class.getResource("Door.png"));
			//String s = strType + doorNum;
				//map.put(s, p);
			//coords.add(x, y);      
			System.out.println("Here is a door!");
			//System.out.println(map);
			doorNum++;
			setDoorNum(doorNum);
		}
		else if (strType.equals("Window"))
		{
			icon = new ImageIcon(ObjectMark.class.getResource("Window.png"));
			String s = strType + windowNum;
			//map.put(s, p);
      
			System.out.println("Here is a window!");
			//System.out.println(map);
			 windowNum++;
		}
		else if (strType.equals("Corridor"))
		{
			icon = new ImageIcon(ObjectMark.class.getResource("Corridor.png"));
			String s = strType + corNum;
			//map.put(s, p);
      
			System.out.println("Here is a corridor!");
			//System.out.println(map);
			 corNum++;
		}
		
		
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

	public int getDoorNum() {
		return doorNum;
	}

	public void setDoorNum(int doorNum) {
		this.doorNum = doorNum;
	}

	public int getWindowNum() {
		return windowNum;
	}

	public void setWindowNum(int windowNum) {
		this.windowNum = windowNum;
	}

	public int getCorNum() {
		return corNum;
	}

	public void setCorNum(int corNum) {
		this.corNum = corNum;
	}
	

	
}

