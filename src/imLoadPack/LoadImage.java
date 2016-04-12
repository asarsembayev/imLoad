package imLoadPack;
/**
 * @author Aidos Sarsembayev 
 * This class reads the image as BufferedImage
 * you can get the height, width and RGB values of the image 
 * */

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class LoadImage{
	public BufferedImage image;
	public int width;
	public int height;
	
	public LoadImage(String imString){
		String im = imString;
		loadIm(im);
	}
	
	/** loadIm method reads the image and reads its width and height
	 * in order to use it, you need to add the parameter String */
	public void loadIm(String im){
	      try {
	          //File input = new File(im);
	          image = ImageIO.read(new File(im));
	          width = image.getWidth();
	          height = image.getHeight();
	          

	       } catch (Exception e) {
	    	   e.printStackTrace();
	       }	      
	}
	
	/** getColorss method uses BufferedImage as an input and returns RGB values of the image
	 * to use this method you need to call: im.getColorss(im.image); 
	 * don't forget to write the LoadImage variable (im) first, then 'dot', then image*/
	@SuppressWarnings("unused")
	public void getColorss(BufferedImage imag) {
		//THIS IS A METHOD BODY TODO Auto-generated method stub
		int count = 0;
        for(int i=0; i<height; i++){
        
           for(int j=0; j<width; j++){
           
              count++;
              Color c = new Color(image.getRGB(j, i));
              System.out.println("S.No: " + count + " Red: " + c.getRed() +"  Green: " + c.getGreen() + " Blue: " + c.getBlue());
           }
        }
	}
	

}
