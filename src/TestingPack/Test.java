package TestingPack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;


public class Test extends JFrame implements ActionListener, KeyEventDispatcher, MouseListener{
	String imString = "src/img/Maze2.png";
	
	JFrame frame;
	public Panel rootPanel = new Panel(); //previously m_RootPane
	public Panel mazePanel = new Panel(); //previously m_MazePane
	public Dimension windowDimension = new Dimension(); //previously m_dmWindow
	public JLabel mazeLabel = new JLabel(); //previously m_lblMaze
	public Timer timer = null; //previously m_Timer
	public TestRobot robot = null;				//initRobot() //previously m_Robot
	public Dimension robSize;
	
	public Vector vetorObjects = new Vector();	//addObjectMark() //previously m_vecObjects
	public JButton exitButton = new JButton();	//initStatus() //previously m_btnExit
	BufferedImage bImage;
	
	int p = 100;
	private List<Point2D> wallPositions = new ArrayList<Point2D>(); 
	private List<Point2D> wallOfset = new ArrayList<Point2D>();
	private List<Point2D> openList = new ArrayList<Point2D>();
	
	private List<Point2D> wallOfset2 = new ArrayList<Point2D>();	
	
	public List<Point2D>pathListMain = new ArrayList<Point2D>();
	
	private int offsetDist = 3;
	
	//private static DrawingObjects object = new DrawingObjects();
	private int xFinish;
	private int yFinish;
	Point finishPoint;
	Point robotLocation;
	Color myColor = new Color(0, 0, 255);
	int rgb = myColor.getRGB();
	
	int robotsOnTheScreen; int robotPosX; int robotPosY;
	int numFinalPoints;
	
	int bImageWidth; int bImageHeight;
	 
	 int a;
	 int averageColor;
	 int [] neighbors = new int [8];
	 ArrayList<ArrayList<Integer>> bounds;
	
	public static void main(String args[]) throws IOException{
		Test vor = new Test();
	}
	
	private Test() throws IOException{
		bImage = ImageIO.read(new File(imString));
		bImageWidth = bImage.getWidth(); bImageHeight = bImage.getHeight();
		putInWallsArray();
		createWallOfset();
		frame = new JFrame();
		
		//Here Im creating the JFrame
		frame.setTitle("My Prog");
		frame.setExtendedState(frame.getExtendedState()|MAXIMIZED_BOTH);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//end of JFrame
	

		

		initUI();
		exportImage(); //delete after
		makeTheOpenList();
		//countDist();	//FOR ASTAR
		//addMouseListener(this);
		

	}
	
	private void countDist(){
		robotPosX = robot.getX(); robotPosY = robot.getY();
		double dist = Math.sqrt(  
				(robotPosX-xFinish)*(robotPosX-xFinish) + (robotPosY - yFinish)*(robotPosY-yFinish)
				);
		System.out.println("Distance is " + dist);
	}
	
	private int getRgb(int x,int y){
		p = bImage.getRGB(x, y);
		int a = (p>>24)&0xff;
		int r = (p>>16)&0xff;
		int g = (p>>8)&0xff;
		int b = p&0xff;
		averageColor = (r + g + b)/3;
		return averageColor;
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
		//drawGrid();
		
		
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int w = (int)screenSize.getWidth();
		int h = (int)screenSize.getHeight();
		setBounds((w - this.windowDimension.width) / 2, (h - this.windowDimension.height) / 2, this.windowDimension.width, this.windowDimension.height);

		JPanel centerPanel = new JPanel(new FlowLayout());
		centerPanel.add(mazePanel);
		mazePanel.setSize(new Dimension(bImage.getWidth(), bImage.getHeight()));
		rootPanel.add(centerPanel, BorderLayout.CENTER);
		
		rootPanel.setBackground(Color.DARK_GRAY);
		mazePanel.setBackground(Color.YELLOW);
		centerPanel.setBackground(Color.BLUE);
		

	}
	
	private List<Point2D> putInWallsArray(){
		int white = 0; int black = 0;		
		for(int x = 0; x < bImageWidth; x++){
			for(int y = 0; y <bImageHeight; y++){
				Point p = new Point(x, y);
				int rgb = getRgb(x, y);
				if(rgb==0){
					wallPositions.add(p);
					black++;
				}
			}
		}
		return wallPositions;
	}
	


	private List<Point2D> createWallOfset(){
		System.out.println("started ");
		long currentTime = System.currentTimeMillis();
		
		List<Point2D> temporaryList = wallPositions;
		int newRGB = 241;
		int offsetDistance = 0;
		for(int j = 0; j<offsetDist; j++){
			offsetDistance++; //increment offsetDist each time until it reaches final
			// loop through temporary list
			for(int i = 0; i<temporaryList.size(); i++){
				Point2D p = temporaryList.get(i);
				Point2D neigh = this.getLocation();
				int blackColor = 255;
				int x = (int) p.getX(); int y = (int)p.getY();
				int isXzero = 0; int isYzero = 0; int isXborder = bImageWidth - 1; int isYborder = bImageHeight - 1;
				//this part is for the borders
				if(x==isXzero){
					if(y!=isYzero && y!=isYborder){
						neigh.setLocation(x + 1, y);
						int neighColor = getRgb((int)neigh.getX(), (int)neigh.getY());
						if(!wallOfset.contains(neigh) && !temporaryList.contains(neigh) && neighColor == blackColor){
							wallOfset2.add(neigh);
							int nX = (int) neigh.getX(); int nY = (int)neigh.getY(); 
							bImage.setRGB(nX, nY, newRGB);
						}
						
					}
				}
				
				if(x == isXborder){
					if(y!=isYzero && y!=isYborder){
						neigh.setLocation(x - 1, y);
						int neighColor = getRgb((int)neigh.getX(), (int)neigh.getY());
						if(!wallOfset.contains(neigh) && !temporaryList.contains(neigh) && neighColor == blackColor){
							wallOfset2.add(neigh);
							int nX = (int) neigh.getX(); int nY = (int)neigh.getY(); 
							bImage.setRGB(nX, nY, newRGB);
						}	
					}					
				}
				
				if(y == isYzero){
					if(x!=isYzero && x!=isYborder){
						neigh.setLocation(x, y + 1);
						int neighColor = getRgb((int)neigh.getX(), (int)neigh.getY());
						if(!wallOfset.contains(neigh) && !temporaryList.contains(neigh) && neighColor == blackColor){
							wallOfset2.add(neigh);
							int nX = (int) neigh.getX(); int nY = (int)neigh.getY(); 
							bImage.setRGB(nX, nY, newRGB);
						}	
					}					
				}
				
				if(y == isYborder){
					if(x!=isYzero && x!=isYborder){
						neigh.setLocation(x, y - 1);
						int neighColor = getRgb((int)neigh.getX(), (int)neigh.getY());
						if(!wallOfset.contains(neigh) && !temporaryList.contains(neigh) && neighColor == blackColor){
							wallOfset2.add(neigh);
							int nX = (int) neigh.getX(); int nY = (int)neigh.getY(); 
							bImage.setRGB(nX, nY, newRGB);
						}	
					}					
				}
				//this part is the end of the borders
				
				//if x and y are not zeros or borders
				if(x!=isXzero && x!=isXborder && y!=isYzero && y!=isYborder
						/*&& (bImageWidth - x)>offsetDistance && (bImageHeight - y)>offsetDistance*/ )
				{
					int blackMaybe = 240;
					Point2D neigh1 = this.getLocation();
					neigh1.setLocation(x, y - 1);
					int n1RGB = getRgb(x, y - 1);
					if(!wallOfset.contains(neigh1) && !wallPositions.contains(neigh1) && n1RGB > blackMaybe){
						wallOfset2.add(neigh1);
						bImage.setRGB(x, y - 1, newRGB);
					}
					Point2D neigh2 = this.getLocation();
					neigh2.setLocation(x + 1, y - 1);
					int n2RGB = getRgb(x + 1, y - 1);
					if(!wallOfset.contains(neigh2) && !wallPositions.contains(neigh2) && n2RGB > blackMaybe){
						wallOfset2.add(neigh2);
						bImage.setRGB(x + 1, y - 1, newRGB);
					}
					Point2D neigh3 = this.getLocation();
					neigh3.setLocation(x + 1, y);
					int n3RGB = getRgb(x + 1, y);
					if(!wallOfset.contains(neigh3) && !wallPositions.contains(neigh3) && n3RGB > blackMaybe){
						wallOfset2.add(neigh3);
						bImage.setRGB(x + 1, y, newRGB);
					}
					Point2D neigh4 = this.getLocation();
					neigh4.setLocation(x + 1, y + 1);
					int n4RGB = getRgb(x + 1, y + 1);
					if(!wallOfset.contains(neigh4) && !wallPositions.contains(neigh4) && n4RGB > blackMaybe){
						wallOfset2.add(neigh4);
						bImage.setRGB(x + 1, y + 1, newRGB);
					}
					Point2D neigh5 = this.getLocation();
					neigh5.setLocation(x, y + 1);
					int n5RGB = getRgb(x, y + 1);
					if(!wallOfset.contains(neigh5) && !wallPositions.contains(neigh5) && n5RGB > blackMaybe){
						wallOfset2.add(neigh5);
						bImage.setRGB(x, y + 1, newRGB);
					}
					Point2D neigh6 = this.getLocation();
					neigh6.setLocation(x - 1, y + 1);
					int n6RGB = getRgb(x - 1, y + 1);
					if(!wallOfset.contains(neigh6) && !wallPositions.contains(neigh6) && n6RGB > blackMaybe){
						wallOfset2.add(neigh6);
						bImage.setRGB(x - 1, y + 1, newRGB);
					}
					Point2D neigh7 = this.getLocation();
					neigh7.setLocation(x - 1, y);
					int n7RGB = getRgb(x - 1, y);
					if(!wallOfset.contains(neigh7) && !wallPositions.contains(neigh7) && n7RGB > blackMaybe){
						wallOfset2.add(neigh7);
						bImage.setRGB(x - 1, y, newRGB);
					}
					Point2D neigh8 = this.getLocation();
					neigh8.setLocation(x - 1, y - 1);
					int n8RGB = getRgb(x - 1, y - 1);
					if(!wallOfset.contains(neigh8) && !wallPositions.contains(neigh8) && n8RGB > blackMaybe){
						wallOfset2.add(neigh8);
						bImage.setRGB(x - 1, y - 1, newRGB);
					}
				}

			}// end if loop through temporary list
			wallOfset.addAll(wallOfset2);
			temporaryList.clear();
			temporaryList.addAll(wallOfset2);
			wallOfset2.clear();
		}
		System.out.println("finished ");
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - currentTime;
		System.out.println("time spent for exec wallOfset " + totalTime);
/*		for(int i = 0; i<wallPositions.size();i++){
			int newRGB = 240;
			Point2D p = wallPositions.get(i);
			Point2D neigh = this.getLocation();
			int x = (int) p.getX(); int y = (int)p.getY();
			int isXzero = 0; int isYzero = 0; int isXborder = bImageWidth; int isYborder = bImageHeight;
			//this part is for the borders
			if(x==isXzero){
				if(y==isYzero || y==isYborder)break;
				neigh.setLocation(x + offsetDist, y);
				if(!wallOfset.contains(neigh) && !wallPositions.contains(neigh)){
					wallOfset.add(neigh);
					int nX = (int) neigh.getX(); int nY = (int)neigh.getY(); 
					bImage.setRGB(nX, nY, newRGB);
				}
			}
			
			if(x == isXborder){
				if(y==isYzero || y==isYborder)break;
				neigh.setLocation(x - offsetDist, y);
				if(!wallOfset.contains(neigh) && !wallPositions.contains(neigh)){
					wallOfset.add(neigh);
					int nX = (int) neigh.getX(); int nY = (int)neigh.getY(); 
					bImage.setRGB(nX, nY, newRGB);
				}
			}
			
			if(y == isYzero){
				if(x==isYzero || x==isYborder)break;
				neigh.setLocation(x, y + offsetDist);
				if(!wallOfset.contains(neigh) && !wallPositions.contains(neigh)){
					wallOfset.add(neigh);
					int nX = (int) neigh.getX(); int nY = (int)neigh.getY(); 
					bImage.setRGB(nX, nY, newRGB);
				}
			}
			
			if(y == isYborder){
				if(x==isYzero || x==isYborder)break;
				neigh.setLocation(x, y - offsetDist);
				if(!wallOfset.contains(neigh) && !wallPositions.contains(neigh)){
					wallOfset.add(neigh);
					int nX = (int) neigh.getX(); int nY = (int)neigh.getY(); 
					bImage.setRGB(nX, nY, newRGB);
				}
			}
			//this part is the end of the borders
			
			int blackMaybe = 240;
			Point2D neigh1 = this.getLocation();
			neigh1.setLocation(x, y - offsetDist);
			int n1RGB = getRgb(x, y - offsetDist);
			if(!wallOfset.contains(neigh1) && !wallPositions.contains(neigh1) && n1RGB > blackMaybe){
				wallOfset.add(neigh1);
				bImage.setRGB(x, y - offsetDist, newRGB);
			}
			Point2D neigh2 = this.getLocation();
			neigh2.setLocation(x + offsetDist, y - offsetDist);
			int n2RGB = getRgb(x + offsetDist, y - offsetDist);
			if(!wallOfset.contains(neigh2) && !wallPositions.contains(neigh2) && n2RGB > blackMaybe){
				wallOfset.add(neigh2);
				bImage.setRGB(x + offsetDist, y - offsetDist, newRGB);
			}
			Point2D neigh3 = this.getLocation();
			neigh3.setLocation(x + offsetDist, y);
			int n3RGB = getRgb(x + offsetDist, y);
			if(!wallOfset.contains(neigh3) && !wallPositions.contains(neigh3) && n3RGB > blackMaybe){
				wallOfset.add(neigh3);
				bImage.setRGB(x + offsetDist, y, newRGB);
			}
			Point2D neigh4 = this.getLocation();
			neigh4.setLocation(x + offsetDist, y + offsetDist);
			int n4RGB = getRgb(x + offsetDist, y + offsetDist);
			if(!wallOfset.contains(neigh4) && !wallPositions.contains(neigh4) && n4RGB > blackMaybe){
				wallOfset.add(neigh4);
				bImage.setRGB(x + offsetDist, y + offsetDist, newRGB);
			}
			Point2D neigh5 = this.getLocation();
			neigh5.setLocation(x, y + offsetDist);
			int n5RGB = getRgb(x, y + offsetDist);
			if(!wallOfset.contains(neigh5) && !wallPositions.contains(neigh5) && n5RGB > blackMaybe){
				wallOfset.add(neigh5);
				bImage.setRGB(x, y + offsetDist, newRGB);
			}
			Point2D neigh6 = this.getLocation();
			neigh6.setLocation(x - offsetDist, y + offsetDist);
			int n6RGB = getRgb(x - offsetDist, y + offsetDist);
			if(!wallOfset.contains(neigh6) && !wallPositions.contains(neigh6) && n6RGB > blackMaybe){
				wallOfset.add(neigh6);
				bImage.setRGB(x - offsetDist, y + offsetDist, newRGB);
			}
			Point2D neigh7 = this.getLocation();
			neigh7.setLocation(x - offsetDist, y);
			int n7RGB = getRgb(x - offsetDist, y);
			if(!wallOfset.contains(neigh7) && !wallPositions.contains(neigh7) && n7RGB > blackMaybe){
				wallOfset.add(neigh7);
				bImage.setRGB(x - offsetDist, y, newRGB);
			}
			Point2D neigh8 = this.getLocation();
			neigh8.setLocation(x - offsetDist, y - offsetDist);
			int n8RGB = getRgb(x - offsetDist, y - offsetDist);
			if(!wallOfset.contains(neigh8) && !wallPositions.contains(neigh8) && n8RGB > blackMaybe){
				wallOfset.add(neigh8);
				bImage.setRGB(x - offsetDist, y - offsetDist, newRGB);
			}
		}
*/		//System.out.println("wallpoitions size " + wallPositions.size());
		//System.out.println("walloffset size " + wallOfset.size());
		//System.out.println("wallOfset "  + wallOfset);
		return wallOfset;	
		
	}
	
	private List<Point2D> makeTheOpenList(){
		for(int x = 0; x < bImageWidth; x++){
			for(int y = 0; y <bImageHeight; y++){
				Point p = new Point(x, y);
				if(!wallPositions.contains(p) && !wallOfset.contains(p)){
					openList.add(p);
					//System.out.println(openList);
				}
			}
		}
		return openList;
	}
	
	//makes matrix of booleans based on RGB values, if true there is an obstacle
	private void drawGrid(){
		//Graphics2D g2 = bImage.createGraphics();
		//g2.setColor(Color.RED);
		//g2.setStroke(new BasicStroke(20));
		//g2.drawLine(i, j, i+bImage.getWidth(), j+bImage.getHeight());
		boolean[][]obstacles = new boolean[bImageWidth][bImageHeight];
		for(int i = 0; i<bImageWidth; i++){
			for(int j = 0; j < bImageHeight; j++){				
				p = bImage.getRGB(i, j);
				int a = (p>>24)&0xff;
				int r = (p>>16)&0xff;
				int g = (p>>8)&0xff;
				int b = p&0xff;
				int grayScale = (r + g + b)/3;
				if (grayScale < 10)
				{
					obstacles[i][j] = true; //if true there is an obstacle
				} else{
					obstacles[i][j] = false; //if false there is no any obstacle
				}
				
			}
		}
		for(int i = 0; i < bImageWidth; i++){
			for(int j = 0; j < bImageHeight; j++){
				if(obstacles[i][j] == true){
				System.out.print("i: " + i + "; j: " + j + " is " + obstacles[i][j]+ "\t");
				System.out.println();
				}
			}
		}
	}

	private void drawObjects(int xOval, int yOval){
		Graphics2D g2 = bImage.createGraphics();
		g2.setColor(Color.RED);
		g2.fillOval(xOval, yOval, 10, 10);	
	}
		
	public void initMaze()
	{
		this.mazeLabel.setIcon(new ImageIcon(this.bImage));		
		this.mazeLabel.setBounds(0, 0, this.bImage.getWidth(), this.bImage.getHeight());
		this.mazePanel.add(this.mazeLabel);
		this.windowDimension.setSize(this.bImage.getWidth() + 50, this.bImage.getHeight() + 50);
		
		this.mazePanel.addMouseListener(this);
		
		this.timer = new Timer(200, this);
		this.timer.start();
	}
	//end of init maze
	
	//Init of robot
	public void initRobot()
	{
		if (this.robot == null)
		{
			this.robot = new TestRobot();
			this.mazePanel.add(this.robot);
			this.mazePanel.setComponentZOrder(robot, 0);
			//robotPosX = robot.getX(); robotPosY = robot.getY();
			robotLocation = new Point(robot.getX(), robot.getY());
			robSize = this.robot.getSize();
		}
	}
	//end of Init of robot

	@Override
	public void actionPerformed(ActionEvent e) 	{
		
		if (e.getSource() == this.timer){		
			if (this.robot.isStopped()) {
				return;
			}//for moving freely uncomment the code bellow
			this.robot.move();
			
			
			
			/*//if you want to move by the AStar you should use the code bellow
			for(int i=0; i<pathListMain.size(); i++){
				this.robot.move((int) pathListMain.get(i).getX(), (int) pathListMain.get(i).getY());
				System.out.println("X " + pathListMain.get(i).getX() + " Y " + pathListMain.get(i).getY());
				if(i == pathListMain.size())break;
			}this.robot.stop();
*/
			boolean bCrashed = false;			
			
			//BEGINNING OF MY WAY OF WALKING
			Point2D p1 = this.robot.getLocation();
			Point2D p2 = this.robot.getLocation();
			Point2D p3 = this.robot.getLocation();
			Point2D p4 = this.robot.getLocation();
			Point2D p12 = this.robot.getLocation();
			Point2D p24 = this.robot.getLocation();
			Point2D p34 = this.robot.getLocation();
			Point2D p13 = this.robot.getLocation();
			p2.setLocation(this.robot.getX(), this.robot.getY()+this.robot.getHeight());
			p3.setLocation(this.robot.getX() + this.robot.getWidth(), this.robot.getY());
			p4.setLocation(this.robot.getX() + this.robot.getWidth(), this.robot.getY()+this.robot.getHeight());
			p12.setLocation(this.robot.getX(), this.robot.getY()+this.robot.getHeight()/2);
			p24.setLocation(this.robot.getX() + this.robot.getWidth()/2, this.robot.getY()+this.robot.getHeight());
			p34.setLocation(this.robot.getX() + this.robot.getWidth(), this.robot.getY()+this.robot.getHeight()/2);
			p13.setLocation(this.robot.getX() + this.robot.getWidth()/2, this.robot.getY());
			//System.out.println("p1 " + p1 + " p2 " + p2 + " p3 " + p3 + " p4 " + p4);
			//System.out.println("p12 " + p12 + " p24 " + p24 + " p34 " + p34 + " p13 " + p13);
			
			try {
					if(wallOfset.contains(p1)){
						System.out.println("Contains p1");
						bCrashed = true;
					}
					if(wallOfset.contains(p2)){
						System.out.println("Contains p2");
						bCrashed = true;
					}
					if(wallOfset.contains(p3)){
						System.out.println("Contains p3");
						bCrashed = true;
					}
					if(wallOfset.contains(p4)){
						System.out.println("Contains p4");
						bCrashed = true;
					}
					if(wallOfset.contains(p12)){
						System.out.println("Contains p4");
						bCrashed = true;
					}
					if(wallOfset.contains(p24)){
						System.out.println("Contains p4");
						bCrashed = true;
					}
					if(wallOfset.contains(p34)){
						System.out.println("Contains p4");
						bCrashed = true;
					}
					if(wallOfset.contains(p13)){
						System.out.println("Contains p4");
						bCrashed = true;
					}
				
			} catch (Exception ex) {
				// TODO: handle exception
				ex.printStackTrace();
			}
			//END OF MY WAY OF WALKING
			
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
		TestObjectMark obj = new TestObjectMark(strType, this.robot.getX(), this.robot.getY());
		Point location = new Point(this.robot.getX(), this.robot.getY());
		String locToString = "[" + location.x + "]" + ";" + "[" + location.y + "]";
		System.out.println(locToString); //print it as the String
		
/*		checkTheObject(strType, location);
		System.out.println("its" + map);*/
		
		this.mazePanel.add(obj.m_lblImage);
		this.mazePanel.setComponentZOrder(obj.m_lblImage, 0);
		this.mazePanel.setComponentZOrder(this.robot, 1);
		this.mazePanel.repaint();
		
		this.vetorObjects.add(obj);
		
	}
	
	private void drawLineFromStartToFinish(){
		Graphics2D g2 = bImage.createGraphics();
		g2.setColor(Color.RED);
		g2.drawLine(robotLocation.x, robotLocation.y, xFinish, yFinish);
	}
	
	private void addFinal(){
		TestDrawFinish objF = new TestDrawFinish(xFinish, yFinish);
		this.mazePanel.add(objF.m_lblImage);
		this.mazePanel.setComponentZOrder(objF.m_lblImage, 0);
		//this.mazePanel.setComponentZOrder(this.robot, 2);
		this.mazePanel.repaint();
		
		finishPoint = new Point(xFinish, yFinish);
	}
	
	public void aStar(Point robotLocation, Point finishPoint){
		TestAstar ta = new TestAstar(robotLocation, finishPoint, wallOfset);
		pathListMain = ta.pathList;
		drawPath(ta.pathList);
	}
	
	public void quadrification(){
		TestQuadrification tq = new TestQuadrification(bImage, openList, wallPositions, wallOfset, robSize, robotLocation, finishPoint);
		drawPath2(tq.quadPathList);
	}
	
	public void drawPath2(List<Point2D> quadPathList){
		Graphics2D g2 = bImage.createGraphics();
		g2.setColor(Color.RED);
		g2.drawLine(robotLocation.x, robotLocation.y, (int)quadPathList.get(0).getX(), (int)quadPathList.get(0).getY());
		int sizeofList = quadPathList.size();
		for(int i=0; i<quadPathList.size(); i++){
			int x = (int) quadPathList.get(i).getX(); int y = (int) quadPathList.get(i).getY();
			if(i == sizeofList -1) break;
			int xNext = (int) quadPathList.get(i+1).getX(); int yNext = (int) quadPathList.get(i+1).getY();
			bImage.setRGB(x, y, 180);
			g2.drawLine(x, y, xNext, yNext);
		}
		int xLast = (int) quadPathList.get(sizeofList-1).getX(); int yLast = (int) quadPathList.get(sizeofList-1).getY();
		g2.drawLine(xLast, yLast, finishPoint.x, finishPoint.y);
		exportImage2();
	}
	
	private void exportImage2(){
		try {
			File output = new File("new33.png");
			ImageIO.write(bImage, "png", output);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	
	public void drawPath(List<Point2D> pathList){
		for(int i=0; i<pathList.size(); i++){
			int x = (int) pathList.get(i).getX(); int y = (int) pathList.get(i).getY();
			bImage.setRGB(x, y, 30);
		}
		exportImage();
	}
	
	private void exportImage(){
		try {
			File output = new File("new22.png");
			ImageIO.write(bImage, "png", output);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	//	I EITHER USE CLICKED OR RELEASED
	@SuppressWarnings("deprecation")
	@Override
	public void mousePressed(MouseEvent paramMouseEvent) {
		//THIS IS A METHOD BODY TODO Auto-generated method stub
		/*int clickNum = paramMouseEvent.getClickCount();
		if(clickNum == 1 && numFinalPoints == 0){
			xFinish = paramMouseEvent.getX(); yFinish = paramMouseEvent.getY();
			addFinal();
			drawLineFromStartToFinish();
			numFinalPoints ++;
			clickNum++;
			aStar(robotLocation, finishPoint);
			quadrification();
		}*/
	}

	@Override
	public void mouseReleased(MouseEvent paramMouseEvent) {
		//THIS IS A METHOD BODY TODO Auto-generated method stub
		int clickNum = paramMouseEvent.getClickCount();
		if(clickNum == 1 && numFinalPoints == 0){
		xFinish = paramMouseEvent.getX(); yFinish = paramMouseEvent.getY();
		addFinal();	
		numFinalPoints ++;
		clickNum++;
		aStar(robotLocation, finishPoint);
		quadrification();
		}
	}

	@Override
	public void mouseEntered(MouseEvent paramMouseEvent) {
		//THIS IS A METHOD BODY TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent paramMouseEvent) {
		//THIS IS A METHOD BODY TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent paramMouseEvent) {
		//THIS IS A METHOD BODY TODO Auto-generated method stub

	}
	


}
