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
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;



public class TestBackup extends JFrame implements ActionListener, KeyEventDispatcher{
	JFrame frame;
	public Panel rootPanel = new Panel(); //previously m_RootPane
	public Panel mazePanel = new Panel(); //previously m_MazePane
	public Dimension windowDimension = new Dimension(); //previously m_dmWindow
	public JLabel mazeLabel = new JLabel(); //previously m_lblMaze
	public Timer timer = null; //previously m_Timer
	public TestRobot robot = null;				//initRobot() //previously m_Robot
	
	public Vector vetorObjects = new Vector();	//addObjectMark() //previously m_vecObjects
	public JButton exitButton = new JButton();	//initStatus() //previously m_btnExit
	String imString = "src/img/Maze.png";
	BufferedImage bImage;
	
	int p = 100;
	private List<Point2D> wallPositions = new ArrayList<Point2D>(); 
	private List<Point2D> wallOfset = new ArrayList<Point2D>(); 

	Color myColor = new Color(0, 0, 255);
	int rgb = myColor.getRGB();
	
	int bImageWidth; int bImageHeight;
	 
	 int a;
	 int averageColor;
	 int [] neighbors = new int [8];
	 ArrayList<ArrayList<Integer>> bounds;
	
	public static void main(String args[]) throws IOException{
		TestBackup vor = new TestBackup();
	}
	
	private TestBackup() throws IOException{
		bImage = ImageIO.read(new File(imString));
		bImageWidth = bImage.getWidth(); bImageHeight = bImage.getHeight();
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
		
		putInWallsArray();
		createWallOfset();
		
		try {
			File output = new File("new.png");
			ImageIO.write(bImage, "png", output);
		} catch (Exception e) {
			// TODO: handle exception
		}
		//createWallOfset(wallPositions);
	}
	
	
	
	private int getRgb(int x,int y){
		p = bImage.getRGB(x, y);
		int a = (p>>24)&0xff;
		int r = (p>>16)&0xff;
		int g = (p>>8)&0xff;
		int b = p&0xff;
		averageColor = (r + g + b)/3;
		/*System.out.println("x: "+ x + " y: " + y + " Alpha is: " + a + 
				" red is " + r + " green is " + g + " blue is " + b +
				" average color is: " + averageColor);*/
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
		for(int x = 0; x < bImageWidth; x++){
			for(int y = 0; y <bImageHeight; y++){
				Point p = new Point(x, y);
				int rgb = getRgb(x, y);
				if(rgb==0){
					wallPositions.add(p);
				}
			}
		}
		return wallPositions;
	}
	
	private List<Point2D> createWallOfset(){
		for(int i = 0; i<wallPositions.size();i++){
			int newRGB = 230;
			Point2D p = wallPositions.get(i);
			Point2D neigh = this.getLocation();
			int x = (int) p.getX(); int y = (int)p.getY();
			int isXzero = 0; int isYzero = 0; int isXborder = bImageWidth; int isYborder = bImageHeight;
			//this part is for the borders
			if(x==isXzero){
				if(y==isYzero || y==isYborder)break;
				neigh.setLocation(x + 1, y);
				if(!wallOfset.contains(neigh) && !wallPositions.contains(neigh)){
					wallOfset.add(neigh);
					int nX = (int) neigh.getX(); int nY = (int)neigh.getY(); 
					bImage.setRGB(nX, nY, newRGB);
				}
			}
			
			if(x == isXborder){
				if(y==isYzero || y==isYborder)break;
				neigh.setLocation(x - 1, y);
				if(!wallOfset.contains(neigh) && !wallPositions.contains(neigh)){
					wallOfset.add(neigh);
					int nX = (int) neigh.getX(); int nY = (int)neigh.getY(); 
					bImage.setRGB(nX, nY, newRGB);
				}
			}
			
			if(y == isYzero){
				if(x==isYzero || x==isYborder)break;
				neigh.setLocation(x, y + 1);
				if(!wallOfset.contains(neigh) && !wallPositions.contains(neigh)){
					wallOfset.add(neigh);
					int nX = (int) neigh.getX(); int nY = (int)neigh.getY(); 
					bImage.setRGB(nX, nY, newRGB);
				}
			}
			
			if(y == isYborder){
				if(x==isYzero || x==isYborder)break;
				neigh.setLocation(x, y - 1);
				if(!wallOfset.contains(neigh) && !wallPositions.contains(neigh)){
					wallOfset.add(neigh);
					int nX = (int) neigh.getX(); int nY = (int)neigh.getY(); 
					bImage.setRGB(nX, nY, newRGB);
				}
			}
			//this part is the end of the borders
			
			int blackMaybe = 240;
			Point2D neigh1 = this.getLocation();
			neigh1.setLocation(x, y - 1);
			int n1RGB = getRgb(x, y - 1);
			if(!wallOfset.contains(neigh1) && !wallPositions.contains(neigh1) && n1RGB > blackMaybe){
				wallOfset.add(neigh1);
				bImage.setRGB(x, y - 1, newRGB);
				System.out.println("added " + neigh1); 
			}
			Point2D neigh2 = this.getLocation();
			neigh2.setLocation(x + 1, y - 1);
			int n2RGB = getRgb(x + 1, y - 1);
			if(!wallOfset.contains(neigh2) && !wallPositions.contains(neigh2) && n2RGB > blackMaybe){
				wallOfset.add(neigh2);
				bImage.setRGB(x + 1, y - 1, newRGB);
				System.out.println("added " + neigh2);
			}
			Point2D neigh3 = this.getLocation();
			neigh3.setLocation(x + 1, y);
			int n3RGB = getRgb(x + 1, y);
			if(!wallOfset.contains(neigh3) && !wallPositions.contains(neigh3) && n3RGB > blackMaybe){
				wallOfset.add(neigh3);
				bImage.setRGB(x + 1, y, newRGB);
				System.out.println("added " + neigh3);
			}
			Point2D neigh4 = this.getLocation();
			neigh4.setLocation(x + 1, y + 1);
			int n4RGB = getRgb(x + 1, y + 1);
			if(!wallOfset.contains(neigh4) && !wallPositions.contains(neigh4) && n4RGB > blackMaybe){
				wallOfset.add(neigh4);
				bImage.setRGB(x + 1, y + 1, newRGB);
				System.out.println("added " + neigh4);
			}
			Point2D neigh5 = this.getLocation();
			neigh5.setLocation(x, y + 1);
			int n5RGB = getRgb(x, y + 1);
			if(!wallOfset.contains(neigh5) && !wallPositions.contains(neigh5) && n5RGB > blackMaybe){
				wallOfset.add(neigh5);
				bImage.setRGB(x, y + 1, newRGB);
				System.out.println("added " + neigh5);
			}
			Point2D neigh6 = this.getLocation();
			neigh6.setLocation(x - 1, y + 1);
			int n6RGB = getRgb(x - 1, y + 1);
			if(!wallOfset.contains(neigh6) && !wallPositions.contains(neigh6) && n6RGB > blackMaybe){
				wallOfset.add(neigh6);
				bImage.setRGB(x - 1, y + 1, newRGB);
				System.out.println("added " + neigh6);
			}
			Point2D neigh7 = this.getLocation();
			neigh7.setLocation(x - 1, y);
			int n7RGB = getRgb(x - 1, y);
			if(!wallOfset.contains(neigh7) && !wallPositions.contains(neigh7) && n7RGB > blackMaybe){
				wallOfset.add(neigh7);
				bImage.setRGB(x - 1, y, newRGB);
				System.out.println("added " + neigh7);
			}
			Point2D neigh8 = this.getLocation();
			neigh8.setLocation(x - 1, y - 1);
			int n8RGB = getRgb(x - 1, y - 1);
			if(!wallOfset.contains(neigh8) && !wallPositions.contains(neigh8) && n8RGB > blackMaybe){
				wallOfset.add(neigh8);
				bImage.setRGB(x - 1, y - 1, newRGB);
				System.out.println("added " + neigh8);
			}
			
			//System.out.println("its " + i + " " + p + "size of array " + wallPositions.size()); 
		}
		System.out.println("size " + wallPositions.size());
		System.out.println("size " + wallOfset.size());
		return wallOfset;		
	}

	
	//this method adds the black points to the list of walls
		private List<Point2D> createWallOfset2(){
			int id = 1;
			for(int x = 0; x<bImageWidth; x++){
				for(int y = 0; y<bImageHeight; y++){
					Point p = new Point(x, y);
					Point2D neigh = this.getLocation();
					int newRGB = 200;
					int imageHeight = bImage.getHeight() - 1;
					int imageWidth = bImage.getWidth() - 1;
					int pRGB = getRgb(x, y);
					if (y != 0 && y != imageHeight){
						if(x == 0){
							wallPositions.add(p);
							neigh.setLocation(x + 1, y);
							if(!wallOfset.contains(neigh) && !wallPositions.contains(neigh)){
								wallOfset.add(neigh);
								int nX = (int) neigh.getX(); int nY = (int)neigh.getY(); 
								bImage.setRGB(nX, nY, newRGB);
							}
							//neighbors [0] = n;
							//check the neighbors
						}
						if(x == imageWidth){
							wallPositions.add(p);
							neigh.setLocation(x - 1, y);
							if(!wallOfset.contains(neigh) && !wallPositions.contains(neigh)){
								wallOfset.add(neigh);
								int nX = (int) neigh.getX(); int nY = (int)neigh.getY(); 
								bImage.setRGB(nX, nY, newRGB);
							}

							//check the neighbors
						}
						//createWallOfset(p);
						//setRgb(x, y, rgb);
					}
					if (x!=0 && x!=imageWidth){
						if(y==0){
							wallPositions.add(p);
							neigh.setLocation(x, y + 1);
							if(!wallOfset.contains(neigh) && !wallPositions.contains(neigh)){
								wallOfset.add(neigh);
								int nX = (int) neigh.getX(); int nY = (int)neigh.getY(); 
								bImage.setRGB(nX, nY, newRGB);
							}

							//check the neighbors
						}
						if(y==imageHeight){
							wallPositions.add(p);
							neigh.setLocation(x, y - 1);
							if(!wallOfset.contains(neigh) && !wallPositions.contains(neigh)){
								wallOfset.add(neigh);
								int nX = (int) neigh.getX(); int nY = (int)neigh.getY(); 
								bImage.setRGB(nX, nY, newRGB);
							}

							//check the neighbors
						}
						//System.out.println("Bound: " + x + ";" + y);
						/*
						bImage.setRGB(x, y, 43);
						createWallOfset(p);*/
						//setRgb(x, y, rgb);
					}
					if(pRGB == 0 && x!=0 && x!=imageWidth && y!=0 && y!=imageHeight){
						wallPositions.add(p);
						int blackMaybe = 240;
						Point2D neigh1 = this.getLocation();
						neigh1.setLocation(x, y - 1);
						int n1RGB = getRgb(x, y - 1);
						if(!wallOfset.contains(neigh1) && !wallPositions.contains(neigh1) && n1RGB > blackMaybe)
							wallOfset.add(neigh1);
							bImage.setRGB(x, y - 1, newRGB);
							System.out.println("added " + neigh1); 
						Point2D neigh2 = this.getLocation();
						neigh2.setLocation(x + 1, y - 1);
						int n2RGB = getRgb(x + 1, y - 1);
						if(!wallOfset.contains(neigh2) && !wallPositions.contains(neigh2) && n2RGB > blackMaybe)
							wallOfset.add(neigh2);
							bImage.setRGB(x + 1, y - 1, newRGB);
							System.out.println("added " + neigh2);
						Point2D neigh3 = this.getLocation();
						neigh3.setLocation(x + 1, y);
						int n3RGB = getRgb(x + 1, y);
						if(!wallOfset.contains(neigh3) && !wallPositions.contains(neigh3) && n3RGB > blackMaybe)
							wallOfset.add(neigh3);
							bImage.setRGB(x + 1, y, newRGB);
							System.out.println("added " + neigh3);
						Point2D neigh4 = this.getLocation();
						neigh4.setLocation(x + 1, y + 1);
						int n4RGB = getRgb(x + 1, y + 1);
						if(!wallOfset.contains(neigh4) && !wallPositions.contains(neigh4) && n4RGB > blackMaybe)
							wallOfset.add(neigh4);
							bImage.setRGB(x + 1, y + 1, newRGB);
							System.out.println("added " + neigh4);
						Point2D neigh5 = this.getLocation();
						neigh5.setLocation(x, y + 1);
						int n5RGB = getRgb(x, y + 1);
						if(!wallOfset.contains(neigh5) && !wallPositions.contains(neigh5) && n5RGB > blackMaybe)
							wallOfset.add(neigh5);
							bImage.setRGB(x, y + 1, newRGB);
							System.out.println("added " + neigh5);
						Point2D neigh6 = this.getLocation();
						neigh6.setLocation(x - 1, y + 1);
						int n6RGB = getRgb(x - 1, y + 1);
						if(!wallOfset.contains(neigh6) && !wallPositions.contains(neigh6) && n6RGB > blackMaybe)
							wallOfset.add(neigh6);
							bImage.setRGB(x - 1, y + 1, newRGB);
							System.out.println("added " + neigh6);
						Point2D neigh7 = this.getLocation();
						neigh7.setLocation(x - 1, y);
						int n7RGB = getRgb(x - 1, y);
						if(!wallOfset.contains(neigh7) && !wallPositions.contains(neigh7) && n7RGB > blackMaybe)
							wallOfset.add(neigh7);
							bImage.setRGB(x - 1, y, newRGB);
							System.out.println("added " + neigh7);
						Point2D neigh8 = this.getLocation();
						neigh8.setLocation(x - 1, y - 1);
						int n8RGB = getRgb(x - 1, y - 1);
						if(!wallOfset.contains(neigh8) && !wallPositions.contains(neigh8) && n8RGB > blackMaybe)
							wallOfset.add(neigh8);
							bImage.setRGB(x - 1, y - 1, newRGB);
							System.out.println("added " + neigh8);
						
/*						neigh.setLocation(x + 1, y);
						if(!wallOfset.contains(neigh) && !wallPositions.contains(neigh)){
							wallOfset.add(neigh);
							int nX = (int) neigh.getX(); int nY = (int)neigh.getY(); 
							bImage.setRGB(nX, nY, newRGB);
						}

						createWallOfset(p);
						//check the neighbors
						System.out.println("Point" + id + " "+ wallPositions.get(wallPositions.lastIndexOf(p)));*/
						id++;
					}
				}
			}
			return wallPositions;
		}
		
		@SuppressWarnings("null")
		private List<Point2D> createWallOfset(Point2D p){
			Point2D neigh = this.getLocation();
			int newRGB = 43;
			int imageHeight = bImage.getHeight() - 1;
			int imageWidth = bImage.getWidth() - 1;
			int x = (int) p.getX(); int y = (int) p.getY();
				if (x == 0 && y!=0 && y != imageHeight){
					int n1 = getRgb(x - 1, y - 1);
					neighbors [0] = n1;
					int n2 = getRgb(x, y - 1);
					neighbors [1] = n2; 
					int n3 = getRgb(x + 1, y - 1);
					neighbors [2] = n3; 
					int n4 = getRgb(x - 1, y);
					neighbors [3] = n4;
					int n5 = getRgb(x + 1, y);
					neighbors [4] = n5;
					int n6 = getRgb(x - 1, y + 1);
					neighbors [5] = n6;
					int n7 = getRgb(x, y + 1);
					neighbors [6] = n7;
					int n8 = getRgb(x + 1, y + 1);
					neighbors [7] = n8;
					/*if(y == 0){
						neigh.setLocation(x + 1, y + 1);
						if(!wallOfset.contains(neigh)){
							wallOfset.add(neigh);
							int nX = (int) neigh.getX(); int nY = (int)neigh.getY(); 
							bImage.setRGB(nX, nY, newRGB);
							System.out.println("Contains");
						}
					}
					if(y == imageWidth){
						neigh.setLocation(x + 1, y - 1);
						if(!wallOfset.contains(neigh)){
							wallOfset.add(neigh);
							int nX = (int) neigh.getX(); int nY = (int)neigh.getY(); 
							bImage.setRGB(nX, nY, newRGB);
							System.out.println("Contains");
						}
					}
					if(x == 0 && y!=0 && y != imageWidth){
						Point2D neigh1 = this.getLocation();
						neigh1.setLocation(x + 1, y + 1);
						if(!wallOfset.contains(neigh1) && !wallPositions.contains(neigh1)
								&& neigh1.getX()!=0 && neigh1.getY()!=0 && neigh1.getX()!= imageWidth && neigh1.getY()!=imageHeight){
							wallOfset.add(neigh1);
							int nX = (int) neigh1.getX(); int nY = (int)neigh1.getY(); 
							bImage.setRGB(nX, nY, newRGB);
							System.out.println("Contains");
						}
						Point2D neigh2 = this.getLocation();
						neigh2.setLocation(x + 1, y);
						if(!wallOfset.contains(neigh2) && !wallPositions.contains(neigh2)
								&& neigh2.getX()!=0 && neigh2.getY()!=0 && neigh2.getX()!= imageWidth && neigh2.getY()!=imageHeight){
							wallOfset.add(neigh2);
							int nX = (int) neigh2.getX(); int nY = (int)neigh2.getY(); 
							bImage.setRGB(nX, nY, newRGB);
							System.out.println("Contains");
						}
						Point2D neigh3 = this.getLocation();
						neigh3.setLocation(x + 1, y - 1);
						if(!wallOfset.contains(neigh3) && !wallPositions.contains(neigh3)
								&& neigh3.getX()!=0 && neigh3.getY()!=0 && neigh3.getX()!= imageWidth && neigh3.getY()!=imageHeight){
							wallOfset.add(neigh3);
							int nX = (int) neigh3.getX(); int nY = (int)neigh3.getY(); 
							bImage.setRGB(nX, nY, newRGB);
							System.out.println("Contains");
						}
					}					
					System.out.println("Bound");
					//wallPositions.add(p);
					//setRgb(x, y, rgb);
*/				
					
				}
				else if(y == 0 && x!=0 && x!=imageWidth){
					
				}
				else if(x == imageWidth && y != 0 && y != imageHeight){
					
				}
				else if(y == imageHeight && x!=0 && x!=imageWidth){
					
				}
				else if(x!=0 && x!=imageWidth && y!=0 && y!=imageHeight){
					
				}
				/*else if (x == imageWidth || y == imageHeight){
					System.out.println("Bound");
					//wallPositions.add(p);
					//setRgb(x, y, rgb);
				} 
				else{
						int n1 = getRgb(x - 1, y - 1);
						neighbors [0] = n1;
						int n2 = getRgb(x, y - 1);
						neighbors [1] = n2; 
						int n3 = getRgb(x + 1, y - 1);
						neighbors [2] = n3; 
						int n4 = getRgb(x - 1, y);
						neighbors [3] = n4;
						int n5 = getRgb(x + 1, y);
						neighbors [4] = n5;
						int n6 = getRgb(x - 1, y + 1);
						neighbors [5] = n6;
						int n7 = getRgb(x, y + 1);
						neighbors [6] = n7;
						int n8 = getRgb(x + 1, y + 1);
						neighbors [7] = n8;
						System.out.println(Arrays.toString(neighbors));
				}*/
					//System.out.println("wallpos(i)"+wallPositions.get(i).getX());
			return wallOfset;
		}
		
		private int[] checkEightNeighbors(int x, int y){
			int n1 = getRgb(x - 1, y - 1);
			neighbors [0] = n1;
			int n2 = getRgb(x, y - 1);
			neighbors [1] = n2; 
			int n3 = getRgb(x + 1, y - 1);
			neighbors [2] = n3; 
			int n4 = getRgb(x - 1, y);
			neighbors [3] = n4;
			int n5 = getRgb(x + 1, y);
			neighbors [4] = n5;
			int n6 = getRgb(x - 1, y + 1);
			neighbors [5] = n6;
			int n7 = getRgb(x, y + 1);
			neighbors [6] = n7;
			int n8 = getRgb(x + 1, y + 1);
			neighbors [7] = n8;
			return neighbors;
			//System.out.println(Arrays.toString(neighbors));
		}
		
		private void checkNeighbors(int x, int y){
			int imageHeight = bImage.getHeight() - 1;
			int imageWidth = bImage.getWidth() - 1;
			if (x == 0 || y ==0){
				System.out.println("Bound");
				//wallPositions.add(p);
				//setRgb(x, y, rgb);
			}else if (x == imageWidth || y == imageHeight){
				System.out.println("Bound");
				//wallPositions.add(p);
				//setRgb(x, y, rgb);
			} else{
					int n1 = getRgb(x - 1, y - 1);
					neighbors [0] = n1;
					int n2 = getRgb(x, y - 1);
					neighbors [1] = n2; 
					int n3 = getRgb(x + 1, y - 1);
					neighbors [2] = n3; 
					int n4 = getRgb(x - 1, y);
					neighbors [3] = n4;
					int n5 = getRgb(x + 1, y);
					neighbors [4] = n5;
					int n6 = getRgb(x - 1, y + 1);
					neighbors [5] = n6;
					int n7 = getRgb(x, y + 1);
					neighbors [6] = n7;
					int n8 = getRgb(x + 1, y + 1);
					neighbors [7] = n8;
					System.out.println(Arrays.toString(neighbors));
			}
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
			this.robot = new TestRobot();
			this.mazePanel.add(this.robot);
		}
	}
	//end of Init of robot

/*	@Override
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
		
	}*/
	
	
	@Override
	public void actionPerformed(ActionEvent e) 	{
		if (e.getSource() == this.timer)
		{
			if (this.robot.isStopped()) {
				return;
			}
			this.robot.move();
			

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
			System.out.println("p1 " + p1 + " p2 " + p2 + " p3 " + p3 + " p4 " + p4);
			System.out.println("p12 " + p12 + " p24 " + p24 + " p34 " + p34 + " p13 " + p13);
			
			try {
					if(wallPositions.contains(p1)){
						System.out.println("Contains p1");
						bCrashed = true;
					}
					if(wallPositions.contains(p2)){
						System.out.println("Contains p2");
						bCrashed = true;
					}
					if(wallPositions.contains(p3)){
						System.out.println("Contains p3");
						bCrashed = true;
					}
					if(wallPositions.contains(p4)){
						System.out.println("Contains p4");
						bCrashed = true;
					}
					if(wallPositions.contains(p12)){
						System.out.println("Contains p4");
						bCrashed = true;
					}
					if(wallPositions.contains(p24)){
						System.out.println("Contains p4");
						bCrashed = true;
					}
					if(wallPositions.contains(p34)){
						System.out.println("Contains p4");
						bCrashed = true;
					}
					if(wallPositions.contains(p13)){
						System.out.println("Contains p4");
						bCrashed = true;
					}
				
			} catch (Exception ex) {
				// TODO: handle exception
				ex.printStackTrace();
			}
			//END OF MY WAY OF WALKING
						

/*			int[] aryX = new int[4];
			int[] aryY = new int[4];
			aryX[0] = this.robot.getX();
			aryY[0] = this.robot.getY();
			System.out.println("0: " + aryX[0] +  " " + aryY[0] );
			aryX[1] = this.robot.getX();
			aryY[1] = (this.robot.getY() + this.robot.getHeight());
			System.out.println("1: " +aryX[1] + " " + aryY[1] );
			aryX[2] = (this.robot.getX() + this.robot.getWidth());
			aryY[2] = this.robot.getY();
			System.out.println("2: " +aryX[2] + " " + aryY[2] );
			aryX[3] = (this.robot.getX() + this.robot.getWidth());
			aryY[3] = (this.robot.getY() + this.robot.getHeight());
			System.out.println("3: " +aryX[3] + " " + aryY[3] );
			
			int i = 0;int j = 0;
			
			try {
				for (i = 0; i < 4; i++){
					if ((aryX[i] >= 0) && (aryY[i] >= 0) && (aryX[i] < this.bImage.getWidth()) && (aryY[i] < this.bImage.getHeight())){
						System.out.println("aryX " + aryX[i] + " aryY " + aryY[i]);
						
						if(wallPositions.contains(aryX[i]) && wallPositions.contains(aryY[i])){
							wallPositions.get(p);
							System.out.println("wall!");
						}
						
						if(wallPositions.contains(p1)){
							//wallPositions.get(p);
							System.out.println("WAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAL!"
									+ "WAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAL!"
									+ "WAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAL!"
									+ "WAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAL!"
									+ "WAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAL!");
						}
						
					}
				}
				
			}catch (Exception ex)
			{
				ex.printStackTrace();
			}*/
			

			
						
			/*int rgb = this.bImage.getRGB(5, 5);
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
			}*/
			
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


}
