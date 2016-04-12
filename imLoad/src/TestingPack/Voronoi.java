package TestingPack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;



public class Voronoi extends JFrame implements ActionListener, KeyEventDispatcher{
	JFrame frame;
	public Panel rootPanel = new Panel(); //previously m_RootPane
	public Panel mazePanel = new Panel(); //previously m_MazePane
	public Dimension windowDimension = new Dimension(); //previously m_dmWindow
	public JLabel mazeLabel = new JLabel(); //previously m_lblMaze
	public Timer timer = null; //previously m_Timer
	public Robot robot = null;				//initRobot() //previously m_Robot
	
	public Vector vetorObjects = new Vector();	//addObjectMark() //previously m_vecObjects
	public JButton exitButton = new JButton();	//initStatus() //previously m_btnExit
	String imString = "src/img/3.png";
	BufferedImage bImage;
	int p = 100;

	Color myColor = new Color(0, 0, 255);
	int rgb = myColor.getRGB();
	 
	 int a;
	 int averageColor;
	 int [] neighbors = new int [4];
	 ArrayList<ArrayList<Integer>> bounds;
	
	public static void main(String args[]) throws IOException{
		Voronoi vor = new Voronoi();
		
		//setRgb(10, 10, p);
	}
	
	private Voronoi() throws IOException{
		bImage = ImageIO.read(new File(imString));
		frame = new JFrame();
		//Here Im creating the JFrame
		frame.setTitle("My Prog");
		frame.setExtendedState(frame.getExtendedState()|MAXIMIZED_BOTH);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//end of JFrame
		
		//uncomment later
/*		for(int x = 0; x < bImage.getWidth(); x++){
			for(int y = 0; y < bImage.getHeight(); y++){
				int currentPixel = getRgb(x, y);
				if(currentPixel >= 200){
					System.out.println("White pixel");
					checkNeighbors(x, y);
				}
			}
		}*/
		
		initUI();
		
		//writeImage(bImage);
	}
	
	private int getRgb(int x,int y){
		p = bImage.getRGB(x, y);
		int a = (p>>24)&0xff;
		int r = (p>>16)&0xff;
		int g = (p>>8)&0xff;
		int b = p&0xff;
		averageColor = (r + g + b)/3;
		System.out.println("x: "+ x + " y: " + y + " Alpha is: " + a + 
				" red is " + r + " green is " + g + " blue is " + b +
				" average color is: " + averageColor);
		return averageColor;
	}
	
	private void checkNeighbors(int x, int y){
		int imageHeight = bImage.getHeight() - 1;
		int imageWidth = bImage.getWidth() - 1;
		if (x == 0 || y ==0){
			System.out.println("Bound");
			setRgb(x, y, rgb);
		}else if (x == imageWidth || y == imageHeight){
			System.out.println("Bound");
			setRgb(x, y, rgb);
		} else{
		

				int eastNei = getRgb(x - 1, y);
				neighbors [0] = eastNei;
				int northNei = getRgb(x, y - 1);
				neighbors [1] = northNei; 
				int westNei = getRgb(x + 1, y);
				neighbors [2] = westNei; 
				int southNei = getRgb(x, y + 1);
				neighbors [3] = southNei;
				System.out.println(Arrays.toString(neighbors));
		}
	}
	
	private void setRgb(int x, int y, int p){
		bImage.setRGB(x, y, rgb);
		/*startX - the starting X coordinate
		startY - the starting Y coordinate
		w - width of the region
		h - height of the region;
		rgbArray - the rgb pixels
		offset - offset into the rgbArray
		scansize - scanline stride for the rgbArray*/
		System.out.println("setRGB complete");
	}
	

	
	public void initUI(){
		KeyboardFocusManager keymgr = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		keymgr.addKeyEventDispatcher(this);
		frame.add(rootPanel, "Center");
		setLayout(new BorderLayout());
		//add(this.rootPanel, "Center");
		
		this.rootPanel.setLayout(new BorderLayout());
		this.mazePanel.setLayout(null);
		
		
		initMaze();
		initRobot();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int w = (int)screenSize.getWidth();
		int h = (int)screenSize.getHeight();
		setBounds((w - this.windowDimension.width) / 2, (h - this.windowDimension.height) / 2, this.windowDimension.width, this.windowDimension.height);

		JPanel centerPanel = new JPanel(new FlowLayout());
		centerPanel.add(mazePanel);
		mazePanel.setSize(new Dimension(bImage.getWidth(), bImage.getHeight()));
		rootPanel.add(centerPanel, BorderLayout.CENTER);
		
		rootPanel.setBackground(Color.DARK_GRAY);
		mazePanel.setBackground(Color.DARK_GRAY);
		centerPanel.setBackground(Color.DARK_GRAY);
		

	}

	private void drawGrid(){
		Graphics2D g2 = bImage.createGraphics();
		g2.setColor(Color.RED);
		//g2.setStroke(new BasicStroke(20));
/*		for(int i = 0; i<20; i++){
			for(int j = 0; j < 20; j++){
				g2.drawLine(i, j, i+bImage.getWidth(), j+bImage.getHeight());
			}
		}*/
		
	}
	
	private void drawObjects(){
		Graphics2D g2 = bImage.createGraphics();
		g2.setColor(Color.RED);
	}
	
	public void initMaze()
	{
		this.mazeLabel.setIcon(new ImageIcon(this.bImage));		
		this.mazeLabel.setBounds(0, 0, this.bImage.getWidth(), this.bImage.getHeight());
		this.mazePanel.add(this.mazeLabel);
		this.windowDimension.setSize(this.bImage.getWidth() + 50, this.bImage.getHeight() + 50);
		
		this.timer = new Timer(100, this);
		this.timer.start();
	}
	//end of init maze
	
	//Init of robot
	public void initRobot()
	{
		if (this.robot == null)
		{
			this.robot = new Robot();
			this.mazePanel.add(this.robot);
		}
	}
	//end of Init of robot

	
	public void writeImage(BufferedImage imag){
		try {
			File f = new File("img/out2.png");
			ImageIO.write(imag, "png", f);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) 	{
		if (e.getSource() == this.timer)
		{
			if (this.robot.isStopped()) {
				return;
			}
			this.robot.move();
			

			boolean bCrashed = false;
			

			int[] aryX = new int[4];
			int[] aryY = new int[4];
			aryX[0] = this.robot.getX();aryY[0] = this.robot.getY();
			aryX[1] = this.robot.getX();aryY[1] = (this.robot.getY() + this.robot.getHeight());
			aryX[2] = (this.robot.getX() + this.robot.getWidth());aryY[2] = this.robot.getY();
			aryX[3] = (this.robot.getX() + this.robot.getWidth());aryY[3] = (this.robot.getY() + this.robot.getHeight());
			

			int i = 0;int j = 0;
						
			int rgb = this.bImage.getRGB(5, 5);
			int r = rgb >> 16 & 0xFF;
			int g = rgb >> 8 & 0xFF;
			int b = rgb & 0xFF;
			int gray0 = (r + g + b) / 3;
			try
			{
				for (i = 0; i < 4; i++) {
					if ((aryX[i] >= 0) && (aryY[i] >= 0) && (aryX[i] < this.bImage.getWidth()) && (aryY[i] < this.bImage.getHeight()))
					{
						rgb = this.bImage.getRGB(aryX[i], aryY[i]);
						

						r = rgb >> 16 & 0xFF;
						g = rgb >> 8 & 0xFF;
						b = rgb & 0xFF;
						int gray = (r + g + b) / 3;
						if (Math.abs(gray - gray0) > 30)
						{
							bCrashed = true;
							break;
						}
					}
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
			if (bCrashed) {
				this.robot.undoMove();
				System.out.println("Crashed!");
			}
			if ((this.robot.getX() < 0) || (this.robot.getY() < 0) || 
				(this.robot.getX() + this.robot.getWidth() > this.windowDimension.getWidth()) || 
				(this.robot.getX() + this.robot.getWidth() > this.windowDimension.getWidth()))
			{
				this.robot.undoMove();
				System.out.println("undo move");
				this.robot.stop();
			}
		}
		else if (e.getSource() == this.exitButton)
		{
			System.exit(0);
		}
		
	}
	
	public boolean dispatchKeyEvent(KeyEvent e) {
		//THIS IS A METHOD BODY TODO Auto-generated method stub
		int id = e.getID();
		if (id != 401) {
			return true;
		}
		if (e.getKeyCode() == 83) {
			this.robot.stop();
		} else if (e.getKeyCode() == 71) {
			this.robot.go();
		} else if (e.getKeyCode() == 76) {
			this.robot.turnLeft();
		} else if (e.getKeyCode() == 82) {
			this.robot.turnRight();
		} else if (e.getKeyCode() == 85) {
			this.robot.turnUp();
		} else if (e.getKeyCode() == 68) {
			this.robot.turnDown();
		} else if (e.getKeyCode() == 66) {
			this.robot.turnBack();
		} else if (e.getKeyCode() == KeyEvent.VK_O) {
			addObjectMark(STRINGS.STR_MSG_DOOR);
		} else if (e.getKeyCode() == KeyEvent.VK_W) {
			addObjectMark(STRINGS.STR_MSG_WINDOW);
		} else if (e.getKeyCode() == KeyEvent.VK_C) {
			addObjectMark(STRINGS.STR_MSG_CORRIDOR);
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public void addObjectMark(String strType)
	{
		ObjectMark obj = new ObjectMark(strType, this.robot.getX(), this.robot.getY());
		Point location = new Point(this.robot.getX(), this.robot.getY());
		String locToString = "[" + location.x + "]" + ";" + "[" + location.y + "]";
		System.out.println(locToString); //print it as the String
		
/*		checkTheObject(strType, location);
		System.out.println("its" + map);*/
		
		this.mazePanel.add(obj.m_lblImage);
		this.mazePanel.setComponentZOrder(obj.m_lblImage, 0);
		this.mazePanel.setComponentZOrder(this.robot, 0);
		this.mazePanel.repaint();
		
		this.vetorObjects.add(obj);
		
	}


}
