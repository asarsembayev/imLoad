package TestingPack;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class TestQuadrification {
	int rgb = 0;

	public TestQuadrification(BufferedImage bImage, List<Point2D> openList, List<Point2D> wallPositions, List<Point2D> wallOfset) {
		//THIS IS CONSTRUCTOR TODO Auto-generated constructor stub
		for(int i = 0; i < bImage.getWidth(); i++){
			for(int j = 0; j < bImage.getHeight(); j++){
				if(i%10==0){
					if(j%10==0){
						//System.out.println("yeeeah" + i + " " + j);
						bImage.setRGB(i, j, rgb);
					}
				}
			}
		}
		//return bImage; 
	}

}
