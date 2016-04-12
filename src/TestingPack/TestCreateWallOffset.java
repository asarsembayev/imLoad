package TestingPack;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.awt.Point;

public class TestCreateWallOffset {
	private List<Point2D> wallOfset = new ArrayList<Point2D>();
	private int offsetDist = 5;
	int averageColor;
	int bImageWidth; int bImageHeight;
	int p = 100;
	
	public TestCreateWallOffset(List<Point2D> wallPositions, BufferedImage bImage) {
		//THIS IS CONSTRUCTOR TODO Auto-generated constructor stub
		createWallOfset(wallPositions, bImage);
		
	}
	
	
/*	private Point2D getLocation() {
		//THIS IS A METHOD BODY TODO Auto-generated method stub
		Point2D location = this.getLocation();
		return location;
	}*/


	private int getRgb(int x,int y, BufferedImage bImage){
		p = bImage.getRGB(x, y);
		int a = (p>>24)&0xff;
		int r = (p>>16)&0xff;
		int g = (p>>8)&0xff;
		int b = p&0xff;
		averageColor = (r + g + b)/3;
		return averageColor;
	}

	public void createWallOfset(List<Point2D> wallPositions, BufferedImage bImage) {
		//THIS IS A METHOD BODY TODO Auto-generated method stub
		for(int i = 0; i<wallPositions.size();i++){
			int newRGB = 230;
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
			int n1RGB = getRgb(x, y - offsetDist, bImage);
			if(!wallOfset.contains(neigh1) && !wallPositions.contains(neigh1) && n1RGB > blackMaybe){
				wallOfset.add(neigh1);
				bImage.setRGB(x, y - offsetDist, newRGB);
			}
			Point2D neigh2 = this.getLocation();
			neigh2.setLocation(x + offsetDist, y - offsetDist);
			int n2RGB = getRgb(x + offsetDist, y - offsetDist, bImage);
			if(!wallOfset.contains(neigh2) && !wallPositions.contains(neigh2) && n2RGB > blackMaybe){
				wallOfset.add(neigh2);
				bImage.setRGB(x + offsetDist, y - offsetDist, newRGB);
			}
			Point2D neigh3 = this.getLocation();
			neigh3.setLocation(x + offsetDist, y);
			int n3RGB = getRgb(x + offsetDist, y, bImage);
			if(!wallOfset.contains(neigh3) && !wallPositions.contains(neigh3) && n3RGB > blackMaybe){
				wallOfset.add(neigh3);
				bImage.setRGB(x + offsetDist, y, newRGB);
			}
			Point2D neigh4 = this.getLocation();
			neigh4.setLocation(x + offsetDist, y + offsetDist);
			int n4RGB = getRgb(x + offsetDist, y + offsetDist, bImage);
			if(!wallOfset.contains(neigh4) && !wallPositions.contains(neigh4) && n4RGB > blackMaybe){
				wallOfset.add(neigh4);
				bImage.setRGB(x + offsetDist, y + offsetDist, newRGB);
			}
			Point2D neigh5 = this.getLocation();
			neigh5.setLocation(x, y + offsetDist);
			int n5RGB = getRgb(x, y + offsetDist, bImage);
			if(!wallOfset.contains(neigh5) && !wallPositions.contains(neigh5) && n5RGB > blackMaybe){
				wallOfset.add(neigh5);
				bImage.setRGB(x, y + offsetDist, newRGB);
			}
			Point2D neigh6 = this.getLocation();
			neigh6.setLocation(x - offsetDist, y + offsetDist);
			int n6RGB = getRgb(x - offsetDist, y + offsetDist, bImage);
			if(!wallOfset.contains(neigh6) && !wallPositions.contains(neigh6) && n6RGB > blackMaybe){
				wallOfset.add(neigh6);
				bImage.setRGB(x - offsetDist, y + offsetDist, newRGB);
			}
			Point2D neigh7 = this.getLocation();
			neigh7.setLocation(x - offsetDist, y);
			int n7RGB = getRgb(x - offsetDist, y, bImage);
			if(!wallOfset.contains(neigh7) && !wallPositions.contains(neigh7) && n7RGB > blackMaybe){
				wallOfset.add(neigh7);
				bImage.setRGB(x - offsetDist, y, newRGB);
			}
			Point2D neigh8 = this.getLocation();
			neigh8.setLocation(x - offsetDist, y - offsetDist);
			int n8RGB = getRgb(x - offsetDist, y - offsetDist, bImage);
			if(!wallOfset.contains(neigh8) && !wallPositions.contains(neigh8) && n8RGB > blackMaybe){
				wallOfset.add(neigh8);
				bImage.setRGB(x - offsetDist, y - offsetDist, newRGB);
			}
		}
		System.out.println("size " + wallPositions.size());
		System.out.println("size " + wallOfset.size());
		//return wallOfset;		
	}




	
	
}
